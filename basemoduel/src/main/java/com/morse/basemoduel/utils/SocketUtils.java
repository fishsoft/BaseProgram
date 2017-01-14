package com.morse.basemoduel.utils;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * socket工具类
 *
 * @author Administrator
 */
public class SocketUtils {

    private static final int SERVER_FILE_PORT = 0;
    private static final int SERVER_PORT = 0;
    private static final int TIMEOUT_SOCKET_CONNECT = 0;
    private static final int TIMEOUT_SOCKET_RESPONSE = 0;
    private static final String SOCKET_TIMEOUT = "";
    private static final int INTERVAL_SOCKET_READ = 0;
    private static final String SOCKET_ERROR1 = "";
    private static final String SOCKET_ERROR2 = "";

    /**
     * 客户端发送命令
     *
     * @param context
     * @param sendMsg
     * @param timeout
     * @return
     */
    public static String socketSend(Context context, String sendMsg, int timeout) {
        LogUtils.show(sendMsg + "");
        OutputStreamWriter mWriter = null;
        InputStream mRead = null;
        String responseInfo = null;
        Socket mSocket = null;
        int readTime = 0;
        try {
            mSocket = new Socket();
            String host = WifiUtil.getServiceIp(context);
            SocketAddress remoteAddr = new InetSocketAddress(host, SERVER_PORT);
            LogUtils.show("连接路由器");
            mSocket.connect(remoteAddr, TIMEOUT_SOCKET_CONNECT);
            LogUtils.show("连接路由器成功");
            mWriter = new OutputStreamWriter(mSocket.getOutputStream(), "utf-8");
            mWriter.write(sendMsg);
            mWriter.flush();
            boolean flag = true;
            while (flag) {
                if (true == mSocket.isConnected() && false == mSocket.isClosed()) {
                    mSocket.setSoTimeout(TIMEOUT_SOCKET_RESPONSE);
                    mRead = mSocket.getInputStream();
                    byte[] buffer = new byte[mRead.available()];
                    mRead.read(buffer);
                    responseInfo = new String(buffer);
                    if ("".equals(responseInfo)) {
                        if (readTime >= timeout) {
                            flag = false;
                            responseInfo = SOCKET_TIMEOUT;
                            break;
                        }
                        Thread.sleep(INTERVAL_SOCKET_READ);
                        readTime += INTERVAL_SOCKET_READ;
                        continue;
                    } else {
                        flag = false;
                        break;
                    }
                }
            }
            JSONObject object = JsonUtils.parseObject(responseInfo);
            if (null != object) {
                responseInfo = (object.toString().replace("\\n", ""));
            }
        } catch (IOException e) {
            if (e instanceof ConnectException) {
                responseInfo = SOCKET_ERROR1;
            }
            e.printStackTrace();
        } catch (InterruptedException e) {
            responseInfo = SOCKET_ERROR2;
            e.printStackTrace();
        } finally {
            closeConnect(mWriter, mRead, mSocket);
        }

        LogUtils.show("返回消息：" + responseInfo);

        return responseInfo;
    }

    private static void closeConnect(OutputStreamWriter mWriter, InputStream mRead, Socket mSocket) {
        try {
            if (mWriter != null) {
                mWriter.close();
                mWriter = null;
            }
            if (mRead != null) {
                mRead.close();
                mRead = null;
            }
            if (!mSocket.isClosed()) {
                mSocket.close();
                mSocket = null;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /**
     * 发送文件
     *
     * @param context
     * @param path
     * @return
     */
    public static boolean sendFile(Context context, String path) {
        boolean isSuccess = false;
        Socket client = null;
        InputStream is = null;
        OutputStream os = null;
        FileInputStream fis = null;
        try {
            client = new Socket();
            SocketAddress remoteAddr = new InetSocketAddress(WifiUtil.getServiceIp(context),
                    SERVER_FILE_PORT);
            client.connect(remoteAddr, TIMEOUT_SOCKET_CONNECT);
            client.setSoTimeout(TIMEOUT_SOCKET_RESPONSE);
            is = client.getInputStream();
            os = client.getOutputStream();
            File file = new File(path);
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length = 0;
            while ((length = fis.read(b)) != -1) {
                // 2、把文件写入socket输出流
                os.write(b, 0, length);
            }
            isSuccess = true;
            os.close();
            fis.close();
            is.close();
            client.close();
            client = null;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            isSuccess = false;
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }
}
