package com.morse.basemoduel.media;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 视频编码
 */
public class CameraRecorderRunable implements Runnable {
    private static final String TAG = "CameraEncoder";
    private MediaCodec mEnc;
    private MediaCodec.BufferInfo mInfo;
    private String mime = "video/avc"; //编码的MIME
    private int rate = 256000; //波特率，256kb
    private int frameRate = 24; //帧率，24帧
    private int frameInterval = 1; //关键帧一秒一关键帧

    private int fpsTime;

    private Thread mThread;
    private boolean mStartFlag = false;
    private int width;
    private int height;
    private byte[] mHeadInfo = null;

    private byte[] nowFeedData;
    private long nowTimeStep;
    private boolean hasNewData = false;

    private FileOutputStream fos;
    private String mSavePath;

    @SuppressLint("NewApi")
    public CameraRecorderRunable() {
        mInfo = new MediaCodec.BufferInfo();
        fpsTime = 1000 / frameRate;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public void setFrameInterval(int frameInterval) {
        this.frameInterval = frameInterval;
    }

    public void setSavePath(String path) {
        this.mSavePath = path;
    }


    /**
     * 准备录制
     *
     * @param width  视频宽度
     * @param height 视频高度
     * @throws IOException
     */
    @SuppressLint("NewApi")
    public void prepare(int width, int height) throws IOException {
        mHeadInfo = null;
        this.width = width;
        this.height = height;
        File file = new File(mSavePath);
        File folder = file.getParentFile();
        if (!folder.exists()) {
            boolean b = folder.mkdirs();
            Log.e("wuwang", "create " + folder.getAbsolutePath() + " " + b);
        }
        if (file.exists()) {
            boolean b = file.delete();
        }
        fos = new FileOutputStream(mSavePath);
        //和音频编码一样，设置编码格式，获取编码器实例
        MediaFormat format = MediaFormat.createVideoFormat(mime, width, height);
        format.setInteger(MediaFormat.KEY_BIT_RATE, rate);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, frameRate);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, frameInterval);
        //这里需要注意，为了简单这里是写了个固定的ColorFormat
        //实际上，并不是所有的手机都支持COLOR_FormatYUV420Planar颜色空间
        //所以正确的做法应该是，获取当前设备支持的颜色空间，并从中选取
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities
                .COLOR_FormatYUV420Planar);
        mEnc = MediaCodec.createEncoderByType(mime);
        mEnc.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
    }

    /**
     * 开始录制
     * 准备好了后，开始编码器
     * @throws InterruptedException
     */
    @SuppressLint("NewApi")
    public void start() throws InterruptedException {
        if (mThread != null && mThread.isAlive()) {
            mStartFlag = false;
            mThread.join();
        }
        mEnc.start();
        mStartFlag = true;
        mThread = new Thread(this);
        mThread.start();
    }

    /**
     * 停止录制
     */
    @SuppressLint("NewApi")
    public void stop() {
        try {
            mStartFlag = false;
            mThread.join();
            mEnc.stop();
            mEnc.release();
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 由外部喂入一帧数据
     *
     * @param data     RGBA数据
     * @param timeStep camera附带时间戳
     */
    public void feedData(final byte[] data, final long timeStep) {
        hasNewData = true;
        nowFeedData = data;
        nowTimeStep = timeStep;
    }

    private ByteBuffer getInputBuffer(int index) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return mEnc.getInputBuffer(index);
        } else {
            return mEnc.getInputBuffers()[index];
        }
    }

    private ByteBuffer getOutputBuffer(int index) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return mEnc.getOutputBuffer(index);
        } else {
            return mEnc.getOutputBuffers()[index];
        }
    }

    @SuppressLint("NewApi")
    //TODO 定时调用，如果没有新数据，就用上一个数据
    private void readOutputData(byte[] data, long timeStep) throws IOException {
        //编码器正确开始后，在子线程中循环编码，固定码率的话，就是一个循环加上线程休眠的时间固定
        //流程和音频编码一样，取出空盒子，往空盒子里面加原料，放回盒子到原处,
        //盒子中原料被加工，取出盒子，从盒子里面取出成品，放回盒子到原处
        int index = mEnc.dequeueInputBuffer(-1);
        if (index >= 0) {
            if (hasNewData) {
                if (yuv == null) {
                    yuv = new byte[width * height * 3 / 2];
                }
                //把传入的rgba数据转成yuv的数据，转换在网上也是一大堆，不够下面还是一起贴上吧
                rgbaToYuv(data, width, height, yuv);
            }
            ByteBuffer buffer = getInputBuffer(index);
            buffer.clear();
            buffer.put(yuv);
            //把盒子和原料一起放回到传送带上原来的位置
            mEnc.queueInputBuffer(index, 0, yuv.length, timeStep, 0);
        }
        MediaCodec.BufferInfo mInfo = new MediaCodec.BufferInfo();
        //尝试取出加工好的数据，和音频编码一样，do while和while都行，觉得怎么好怎么写
        int outIndex = mEnc.dequeueOutputBuffer(mInfo, 0);
        while (outIndex >= 0) {
            ByteBuffer outBuf = getOutputBuffer(outIndex);
            byte[] temp = new byte[mInfo.size];
            outBuf.get(temp);
            if (mInfo.flags == MediaCodec.BUFFER_FLAG_CODEC_CONFIG) {
                Log.e(TAG, "start frame");
                //把编码信息保存下来，关键帧上要用
                mHeadInfo = new byte[temp.length];
                mHeadInfo = temp;
            } else if (mInfo.flags % 8 == MediaCodec.BUFFER_FLAG_KEY_FRAME) {
                Log.e(TAG, "key frame");
                //关键帧比普通帧是多了个帧头的，保存了编码的信息
                byte[] keyframe = new byte[temp.length + mHeadInfo.length];
                System.arraycopy(mHeadInfo, 0, keyframe, 0, mHeadInfo.length);
                System.arraycopy(temp, 0, keyframe, mHeadInfo.length, temp.length);
                Log.e(TAG, "other->" + mInfo.flags);
                //写入文件
                fos.write(keyframe, 0, keyframe.length);
            } else if (mInfo.flags == MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                //结束的时候应该发送结束信号，在这里处理
                Log.e(TAG, "end frame");
            } else {
                //写入文件
                fos.write(temp, 0, temp.length);
            }
            mEnc.releaseOutputBuffer(outIndex, false);
            outIndex = mEnc.dequeueOutputBuffer(mInfo, 0);
            Log.e("wuwang", "outIndex-->" + outIndex);
        }

    }

    byte[] yuv;

    /**
     * RGBA转YUV的方法，这是最简单粗暴的方式，在使用的时候，一般不会选择在Java层，用这种方式做转换
     * @param rgba
     * @param width
     * @param height
     * @param yuv
     */
    private void rgbaToYuv(byte[] rgba, int width, int height, byte[] yuv) {
        final int frameSize = width * height;

        int yIndex = 0;
        int uIndex = frameSize;
        int vIndex = frameSize + frameSize / 4;

        int R, G, B, Y, U, V;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                index = j * width + i;
                if (rgba[index * 4] > 127 || rgba[index * 4] < -128) {
                    Log.e("color", "-->" + rgba[index * 4]);
                }
                R = rgba[index * 4] & 0xFF;
                G = rgba[index * 4 + 1] & 0xFF;
                B = rgba[index * 4 + 2] & 0xFF;

                Y = ((66 * R + 129 * G + 25 * B + 128) >> 8) + 16;
                U = ((-38 * R - 74 * G + 112 * B + 128) >> 8) + 128;
                V = ((112 * R - 94 * G - 18 * B + 128) >> 8) + 128;

                yuv[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
                if (j % 2 == 0 && index % 2 == 0) {
                    yuv[uIndex++] = (byte) ((U < 0) ? 0 : ((U > 255) ? 255 : U));
                    yuv[vIndex++] = (byte) ((V < 0) ? 0 : ((V > 255) ? 255 : V));
                }
            }
        }
    }

    @Override
    public void run() {
        while (mStartFlag) {
            long time = System.currentTimeMillis();
            if (nowFeedData != null) {
                try {
                    readOutputData(nowFeedData, nowTimeStep);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            long lt = System.currentTimeMillis() - time;
            if (fpsTime > lt) {
                try {
                    Thread.sleep(fpsTime - lt);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
