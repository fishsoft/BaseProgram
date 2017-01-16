package com.morse.basemoduel.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

public class ImageUtils {

    private static final String TAG = "ImageUtil";

    public static final int ACTION_SET_AVATAR = 260;
    public static final int ACTION_SET_COVER = 261;
    public static final int ACTION_TAKE_PIC = 262;
    public static final int ACTION_TAKE_PIC_FOR_GRIDVIEW = 263;
    public static final int ACTION_PICK_PIC = 264;
    public static final int ACTION_ACTION_CROP = 265;
    /**
     * 图片压缩率
     */
    private static final int iMG_COMPRESS = 6;
    private static final int UPLOAD_IMG_SRC_LENGTH = 350;

    /**
     * 调用系统自带裁图工具
     *
     * @param activity
     * @param size
     * @param uri
     * @param action
     * @param cropFile
     */
    public static void cropPicture(Activity activity, int size, Uri uri, int action, File cropFile) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            // 返回格式
            intent.putExtra("outputFormat", CompressFormat.JPEG.toString());
            intent.putExtra("crop", true);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", size);
            intent.putExtra("outputY", size);
            intent.putExtra("scale", true);
            intent.putExtra("scaleUpIfNeeded", true);
            intent.putExtra("cropIfNeeded", true);
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropFile));
            activity.startActivityForResult(intent, action);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用系统自带裁图工具 outputX = 250 outputY = 250
     *
     * @param activity
     * @param uri
     * @param action
     * @param cropFile
     */
    public static void cropPicture(Activity activity, Uri uri, int action, File cropFile) {
        cropPicture(activity, 250, uri, action, cropFile);
    }

    /**
     * 获取圆角Bitmap
     *
     * @param srcBitmap
     * @param radius
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap srcBitmap, float radius) {
        Bitmap resultBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xBDBDBE);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(srcBitmap, rect, rect, paint);
        return resultBitmap;
    }

    /**
     * 图片解析
     *
     * @param path
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap decodeScaleImage(String path, int targetWidth, int targetHeight) {
        Options bitmapOptions = getBitmapOptions(path);
        bitmapOptions.inSampleSize = calculateInSampleSize(bitmapOptions, targetWidth, targetHeight);
        bitmapOptions.inJustDecodeBounds = false;
        Bitmap noRotatingBitmap = BitmapFactory.decodeFile(path, bitmapOptions);
        int degree = readPictureDegree(path);
        Bitmap rotatingBitmap;
        if (noRotatingBitmap != null && degree != 0) {
            rotatingBitmap = rotatingImageView(degree, noRotatingBitmap);
            noRotatingBitmap.recycle();
            return rotatingBitmap;
        } else {
            return noRotatingBitmap;
        }
    }

    /**
     * 获取缩略图
     *
     * @param path
     * @param targetWidth
     * @return
     */
    public static String getThumbnailImage(String path, int targetWidth) {
        Bitmap scaleImage = decodeScaleImage(path, targetWidth, targetWidth);
        try {
            File file = File.createTempFile("image", ".jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            scaleImage.compress(CompressFormat.JPEG, 60, fileOutputStream);
            fileOutputStream.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return path;
        }
    }

    /**
     * 图片解析
     *
     * @param context
     * @param resId
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static Bitmap decodeScaleImage(Context context, int resId, int targetWidth, int targetHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        return bitmap;
    }

    /**
     * 计算样本大小
     *
     * @param options
     * @param targetWidth
     * @param targetHeight
     * @return
     */
    public static int calculateInSampleSize(Options options, int targetWidth, int targetHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int scale = 1;
        if (height > targetHeight || width > targetWidth) {
            int heightScale = Math.round((float) height / (float) targetHeight);
            int widthScale = Math.round((float) width / (float) targetWidth);
            scale = heightScale > widthScale ? heightScale : widthScale;
        }
        return scale;
    }

    /**
     * 获取图片角度
     *
     * @param filename
     * @return
     */
    public static int readPictureDegree(String filename) {
        short degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filename);
            int anInt = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            switch (anInt) {
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                case ExifInterface.ORIENTATION_TRANSPOSE:
                case ExifInterface.ORIENTATION_TRANSVERSE:
                default:
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return degree;
    }

    /**
     * 旋转ImageView
     *
     * @param degree
     * @param source
     * @return
     */
    public static Bitmap rotatingImageView(int degree, Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) degree);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * 获取视频缩略图
     *
     * @param filePath
     * @param width
     * @param height
     * @param kind
     * @return
     */
    public static Bitmap getVideoThumbnail(String filePath, int width, int height, int kind) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * Image resource ID was converted into a byte [] data 图片资源ID 转换 为 图片 byte[]
     * 数据
     *
     * @param context
     * @param imageResourceId
     * @return
     */
    public static byte[] toByteArray(Context context, int imageResourceId) {
        Bitmap bitmap = ImageUtils.toBitmap(context, imageResourceId);
        if (bitmap != null) {
            return ImageUtils.toByteArray(bitmap);
        } else {
            return null;
        }
    }

    /**
     * ImageView getDrawable () into a byte [] data ImageView的getDrawable() 转换为
     * byte[] 数据
     *
     * @param imageView
     * @return
     */
    public static byte[] toByteArray(ImageView imageView) {
        Bitmap bitmap = ImageUtils.toBitmap(imageView);
        if (bitmap != null)
            return ImageUtils.toByteArray(bitmap);
        else {
            Log.w(ImageUtils.TAG, "the ImageView imageView content was invalid");
            return null;
        }
    }

    /**
     * byte [] data type conversion for Bitmap data types byte[]数据类型转换为
     * Bitmap数据类型
     *
     * @param imageData
     * @return
     */
    public static Bitmap toBitmap(byte[] imageData) {
        if ((imageData != null) && (imageData.length != 0)) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            return bitmap;
        } else {
            Log.w(ImageUtils.TAG, "the byte[] imageData content was invalid");
            return null;
        }
    }

    /**
     * Image resource ID is converted to Bitmap type data 资源ID 转换为 Bitmap类型数据
     *
     * @param context
     * @param imageResourceId
     * @return
     */
    public static Bitmap toBitmap(Context context, int imageResourceId) {
        // 将图片转化为位图
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageResourceId);
        if (bitmap != null) {
            return bitmap;
        } else {
            Log.w(ImageUtils.TAG, "the int imageResourceId content was invalid");
            return null;
        }
    }

    /**
     * ImageView types into a Bitmap ImageView类型转换为Bitmap
     *
     * @param imageView
     * @return
     */
    public static Bitmap toBitmap(ImageView imageView) {
        if (imageView.getDrawable() != null) {
            Bitmap bitmap = ImageUtils.toBitmap(imageView.getDrawable());
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * Bitmap type is converted into a image byte [] data Bitmap类型 转换 为图片 byte[]
     * 数据
     *
     * @param bitmap
     * @return
     */
    public static byte[] toByteArray(Bitmap bitmap) {
        if (bitmap != null) {
            int size = bitmap.getWidth() * bitmap.getHeight() * 4;
            // 创建一个字节数组输出流，流的大小为size
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(size);
            // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
            // 将字节数组输出流转化为字节数组byte[]
            byte[] imageData = byteArrayOutputStream.toByteArray();
            return imageData;
        } else {
            Log.w(ImageUtils.TAG, "the Bitmap bitmap content was invalid");
            return null;
        }

    }

    /**
     * Drawable type into a Bitmap Drawable 类型转换为 Bitmap类型
     *
     * @param drawable
     * @return
     */
    public static Bitmap toBitmap(Drawable drawable) {
        if (drawable != null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            return bitmap;
        } else {
            Log.w(ImageUtils.TAG, "the Drawable drawable content was invalid");
            return null;
        }
    }

    /**
     * Bitmap type into a Drawable Bitmap 类型转换为 Drawable类型
     *
     * @param bitmap
     * @return
     */
    public static Drawable toDrawable(Bitmap bitmap) {
        if (bitmap != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bitmap);
            return drawable;
        } else {
            Log.w(ImageUtils.TAG, "the Bitmap bitmap content was invalid");
            return null;
        }
    }

    // ***************** 根据Uri获取文件绝对路径 *****************//

    /**
     * 根据Uri获取文件绝对路径
     *
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getAbsoluteImagePath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    // ***************** 根据Uri获取文件绝对路径 *****************//

    public static String getBase64FromUri(Context context, Uri uri) {
        return bitmap2String(getBitmapFormUri(context, uri));
    }

    public static Bitmap getBitmapFormUri(Context context, Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String bitmap2String(Bitmap bitmap) {
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, iMG_COMPRESS, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    public Bitmap string2Bitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /************** 拓展助手上传图片压缩 *****************/

    /**
     * 得到指定路径图片的options，不加载内存
     *
     * @param srcPath 源图片路径
     * @return Options {@link BitmapFactory.Options}
     */
    public static BitmapFactory.Options getBitmapOptions(String srcPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, options);
        return options;
    }

    /**
     * 获取压缩bitmap和编码后的base64
     *
     * @param uploadPath
     * @param srcWidth
     * @param srcHeight
     * @param srcLength
     * @return
     */
    public static void getCompressImg(String uploadPath, int srcWidth, int srcHeight, int srcLength) {
        Options opts = new Options();// 解析图片的选项参数
        opts.inPreferredConfig = Config.RGB_565;

        // 2.得到图片的宽高属性。
        opts.inJustDecodeBounds = true;// 不真正的解析这个bitmap ，只是获取bitmap的宽高信息
        Bitmap bitmap = BitmapFactory.decodeFile(uploadPath, opts);

        int imageHeight = opts.outHeight;
        int imageWidth = opts.outWidth;
        // 3.计算缩放比例。
        int dx = imageWidth / srcWidth;
        int dy = imageHeight / srcHeight;

        // LogUtils.show("原图宽高： " + imageWidth + " * " + imageHeight);

        int scale = 1;
        if (dx >= dy && dx >= 1) {
            scale = dy;
        }
        if (dy >= dx && dx >= 1) {
            scale = dx;
        }

        opts.inJustDecodeBounds = false;// 真正的解析bitmap
        opts.inSampleSize = scale; // 指定图片缩放比例
        bitmap = BitmapFactory.decodeFile(uploadPath, opts);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (options > 0 && baos.toByteArray().length / 1024 > srcLength) { // 循环判断如果压缩后图片大于目标大小,继续压缩
            baos.reset();// 重置baos即清空baos
            bitmap.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }

        String base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

//        CompressImg compressImg = new CompressImg();
//        compressImg.bitmap = bitmap;
//        compressImg.base64 = base64;
//        return compressImg;
    }

    /**
     * 首次压缩图片
     *
     * @param uploadPath
     * @param showPath
     * @return
     */
    public static HashMap<Bitmap, String> getScaleBitmap(String uploadPath, String showPath) {
        Options opts = getBitmapOptions(uploadPath);
        opts.inPreferredConfig = Config.RGB_565;

        int srcWidth = 1000;
        int srcHeigth = 500;

        // 2.得到图片的宽高属性。
        int imageHeight = opts.outHeight;
        int imageWidth = opts.outWidth;
        // 3.计算缩放比例。
        int dx = imageWidth / srcWidth;
        int dy = imageHeight / srcHeigth;

        // LogUtils.show("原图宽高： " + imageWidth + " * " + imageHeight);

        int scale = 1;
        if (dx >= dy && dx >= 1) {
            scale = dy;
        }
        if (dy >= dx && dx >= 1) {
            scale = dx;
        }

        // LogUtils.show("压缩比例： " + scale);

        opts.inJustDecodeBounds = false;// 真正的解析bitmap
        opts.inSampleSize = scale; // 指定图片缩放比例
        Bitmap bitmap = BitmapFactory.decodeFile(uploadPath, opts);

        return compressImage(bitmap, showPath);
    }

    public static HashMap<Bitmap, String> compressImage(Bitmap bitmap, String showPath) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (options > 0 && baos.toByteArray().length / 1024 > UPLOAD_IMG_SRC_LENGTH) { // 循环判断如果压缩后图片大于400kb,继续压缩
            baos.reset();// 重置baos即清空baos
            bitmap.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }

        String base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        HashMap<Bitmap, String> map = new HashMap<Bitmap, String>();
        map.put(getShowBitmap(baos, showPath), base64);

        return map;
    }

    /**
     * 获取要显示的更小的bitmap
     *
     * @param baos
     * @return
     */
    private static Bitmap getShowBitmap(ByteArrayOutputStream baos, String showPath) {
        Options newOpts = new Options();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        newOpts.inSampleSize = iMG_COMPRESS;
        Bitmap showBitmap = BitmapFactory.decodeStream(bais, null, newOpts);
        saveBitmapFile(showBitmap, showPath);
        return showBitmap;
    }

    /**
     * 将显示的bitmap转化成文件存到本地
     *
     * @param bitmap
     * @param showPath
     */
    public static void saveBitmapFile(Bitmap bitmap, String showPath) {
        int end = showPath.lastIndexOf("/");
        String filePath = showPath.substring(0, end + 1);
        String fileName = showPath.substring(end + 1, showPath.length());

        File file = getFile(filePath, fileName);

        if (file == null) {
            return;
        }

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static File getFile(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 根据InputStream获取图片实际的宽度和高度
     *
     * @param imageStream
     * @return
     */
    public static ImageSize getImageSize(InputStream imageStream) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(imageStream, null, options);
        return new ImageSize(options.outWidth, options.outHeight);
    }

    public static class ImageSize {
        int width;
        int height;

        public ImageSize() {
        }

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return "ImageSize{" + "width=" + width + ", height=" + height + '}';
        }
    }

    public static int calculateInSampleSize(ImageSize srcSize, ImageSize targetSize) {
        // 源图片的宽度
        int width = srcSize.width;
        int height = srcSize.height;
        int inSampleSize = 1;

        int reqWidth = targetSize.width;
        int reqHeight = targetSize.height;

        if (width > reqWidth && height > reqHeight) {
            // 计算出实际宽度和目标宽度的比率
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) height / (float) reqHeight);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }
        return inSampleSize;
    }

    /**
     * 根据ImageView获适当的压缩的宽和高
     *
     * @param view
     * @return
     */
    public static ImageSize getImageViewSize(View view) {

        ImageSize imageSize = new ImageSize();

        imageSize.width = getExpectWidth(view);
        imageSize.height = getExpectHeight(view);

        return imageSize;
    }

    /**
     * 根据view获得期望的高度
     *
     * @param view
     * @return
     */
    private static int getExpectHeight(View view) {

        int height = 0;
        if (view == null)
            return 0;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        // 如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
        if (params != null && params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = view.getWidth(); // 获得实际的宽度
        }
        if (height <= 0 && params != null) {
            height = params.height; // 获得布局文件中的声明的宽度
        }

        if (height <= 0) {
            height = getImageViewFieldValue(view, "mMaxHeight");// 获得设置的最大的宽度
        }

        // 如果宽度还是没有获取到，憋大招，使用屏幕的宽度
        if (height <= 0) {
            DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
            height = displayMetrics.heightPixels;
        }

        return height;
    }

    /**
     * 根据view获得期望的宽度
     *
     * @param view
     * @return
     */
    private static int getExpectWidth(View view) {
        int width = 0;
        if (view == null)
            return 0;

        final ViewGroup.LayoutParams params = view.getLayoutParams();
        // 如果是WRAP_CONTENT，此时图片还没加载，getWidth根本无效
        if (params != null && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
            width = view.getWidth(); // 获得实际的宽度
        }
        if (width <= 0 && params != null) {
            width = params.width; // 获得布局文件中的声明的宽度
        }

        if (width <= 0)

        {
            width = getImageViewFieldValue(view, "mMaxWidth");// 获得设置的最大的宽度
        }
        // 如果宽度还是没有获取到，憋大招，使用屏幕的宽度
        if (width <= 0)

        {
            DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
            width = displayMetrics.widthPixels;
        }

        return width;
    }

    /**
     * 通过反射获取imageview的某个属性值
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
        }
        return value;

    }

}
