package com.morse.basemoduel.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FileUtils {

    private String SDPATH = Environment.getExternalStorageDirectory() + "/";
    private static final double KB = 1024.0;
    private static final double MB = KB * KB;
    private static final double GB = KB * KB * KB;
    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 创建文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public File createSDFile(String fileName) {
        File file = new File(SDPATH + fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 创建SD卡文件目录
     *
     * @param dirName
     * @return
     */
    public File createSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 判断文件是否存在
     *
     * @return
     */
    public boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        return file.exists();
    }

    /**
     * 文件重命名
     *
     * @param path    文件目录
     * @param oldname 原来的文件名
     * @param newname 新文件名
     */
    public void reNameFile(String path, String oldname, String newname) {
        if (!oldname.equals(newname)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path + "/" + oldname);
            File newfile = new File(path + "/" + newname);
            if (!oldfile.exists()) {
                return;// 重命名文件不存在
            }
            if (newfile.exists()) // 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
            {
                Log.i(TAG, "已经存在！");
                return;
            } else {
                oldfile.renameTo(newfile);
            }
        } else {
            Log.i(TAG, "新文件名和旧文件名相同...");
            return;
        }
    }

    /**
     * 获取文件保存路径
     *
     * @return
     */
    public String getPath(String filename) {
        String path = "";
        if (FileUtils.isExistSDCard()) {
            path = Environment.getExternalStorageDirectory() + "/" + filename;
        } else {
            path = Environment.getRootDirectory() + "/" + filename;
        }
        return path;
    }

    /**
     * 判断sd卡是否存在
     *
     * @return boolean
     */
    public static boolean isExistSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     * 格式化大小
     *
     * @param size
     * @return
     */
    public static String formatSize(long size) {
        String fileSize;
        if (size < KB)
            fileSize = size + "B";
        else if (size < MB)
            fileSize = String.format("%.1f", size / KB) + " KB";
        else if (size < GB)
            fileSize = String.format("%.1f", size / MB) + " MB";
        else
            fileSize = String.format("%.1f", size / GB) + " GB";

        return fileSize;
    }

    /**
     * 文件如果不存在就创建
     *
     * @param path
     * @return
     */
    public static boolean createFile(String path) {
        File file = new File(path);
        boolean mk = false;
        if (!file.exists()) {
            try {
                mk = file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return mk;
    }

    public static boolean isFileExists(String path) {
        boolean flag = false;
        File f = new File(path);
        if (f.isFile())
            flag = true;
        Log.i("flag", flag + "");
        return flag;
    }

    /**
     * getFileSize
     *
     * @param path : can be path of file or directory.
     */
    public static long getFileSize(String path) {
        return getFileSize(new File(path));
    }

    /**
     * getFileSize
     *
     * @param file : can be file or directory.
     */
    public static long getFileSize(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                long size = 0;
                for (File subFile : file.listFiles()) {
                    size += getFileSize(subFile);
                }
                return size;
            } else {
                return file.length();
            }
        } else {
            throw new IllegalArgumentException("File does not exist!");
        }
    }

    /**
     * copyFile
     *
     * @param originalFilePath
     * @param destFilePath
     * @throws IOException
     */
    public static void copyFile(String originalFilePath, String destFilePath) throws IOException {
        copyFile(new File(originalFilePath), destFilePath);
    }

    /**
     * copyFile
     *
     * @param originalFilePath
     * @param destFile
     * @throws IOException
     */
    public static void copyFile(String originalFilePath, File destFile) throws IOException {
        copyFile(new File(originalFilePath), destFile);
    }

    /**
     * copyFile
     *
     * @param originalFile
     * @param destFilePath
     * @throws IOException
     */
    public static void copyFile(File originalFile, String destFilePath) throws IOException {
        copyFile(originalFile, new File(destFilePath));
    }

    /**
     * copyFile
     *
     * @param originalFile
     * @param destFile
     * @throws IOException
     */
    public static void copyFile(File originalFile, File destFile) throws IOException {
        copy(new FileInputStream(originalFile), new FileOutputStream(destFile));
    }

    /**
     * copy
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte buf[] = new byte[1024];
        int numRead = 0;

        while ((numRead = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, numRead);
        }

        close(outputStream, inputStream);
    }

    /**
     * deleteFile
     *
     * @param path : can be file's absolute path or directories' path.
     */
    public static void deleteFile(String path) {
        deleteFile(new File(path));
    }

    /**
     * deleteFile
     *
     * @param file : can be file or directory.
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
            LogUtils.e("The file to be deleted does not exist! File's path is: " + file.getPath());
        } else {
            deleteFileRecursively(file);
        }
    }

    /**
     * deleteFileRecursively Invoker must ensure that the file to be deleted
     * exists.
     */
    private static void deleteFileRecursively(File file) {
        if (file.isDirectory()) {
            for (String fileName : file.list()) {
                File item = new File(file, fileName);

                if (item.isDirectory()) {
                    deleteFileRecursively(item);
                } else {
                    if (!item.delete()) {
                        LogUtils.e("Failed in recursively deleting a file, file's path is: " + item.getPath());
                    }
                }
            }

            if (!file.delete()) {
                LogUtils.e("Failed in recursively deleting a directory, directories' path is: " + file.getPath());
            }
        } else {
            if (!file.delete()) {
                LogUtils.e("Failed in deleting this file, its path is: " + file.getPath());
            }
        }
    }

    /**
     * readToString
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String readToString(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        StringBuffer buffer = new StringBuffer();
        String readLine = null;

        while ((readLine = reader.readLine()) != null) {
            buffer.append(readLine);
            buffer.append("\n");
        }

        reader.close();

        return buffer.toString();
    }

    /**
     * readToByteArray
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] readToByteArray(File file) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        copy(new FileInputStream(file), outputStream);

        return outputStream.toByteArray();
    }

    /**
     * writeByteArray
     *
     * @param byteArray
     * @param filePath
     * @throws IOException
     */
    public static void writeByteArray(byte[] byteArray, String filePath) throws IOException {
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
        outputStream.write(byteArray);
        outputStream.close();
    }

    /**
     * saveBitmapToFile
     *
     * @param fileForSaving
     * @param bitmap
     */
    public static void saveBitmapToFile(String fileForSaving, Bitmap bitmap) {
        File targetFile = new File(
                Environment.getExternalStorageDirectory().getPath() + "/" + fileForSaving + ".png");

        if (!targetFile.exists()) {
            try {
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(targetFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * toCatenatedPath If sequentialPathStrs are "a","b","c", below method
     * return "a/b/c"
     */
    public static String toCatenatedPath(String... sequentialPathStrs) {
        String catenatedPath = "";
        for (int i = 0; i < sequentialPathStrs.length - 1; i++) {
            catenatedPath += sequentialPathStrs[i] + File.separator;
        }
        catenatedPath += sequentialPathStrs[sequentialPathStrs.length - 1];
        return catenatedPath;
    }

    /**
     * close
     *
     * @param closeables
     */
    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断内置SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取内置SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        /*File fstabFile=new File("/etc/vold.fstab");
		FileInputStream fis=null;
		try{
			new FileInputStream(fstabFile);
			byte[] buffer=new byte[(int) fstabFile.length()];
			fis.read(buffer);
			String[] toSearch = new String(buffer).split(" ");
	        ArrayList<String> out = new ArrayList<String>();
	        for (int i = 0; i < toSearch.length; i++) {
	            if (toSearch[i].contains("dev_mount")) {
	                if (new File(toSearch[i + 2]).exists()) {
	                    out.add(toSearch[i + 2]);
	                }
	            }
	        }
	        String path = null;

	        File sdCardFile = null;
	        for (String devMount : out) {
	            File file = new File(devMount);

	            if (file.isDirectory() && file.canWrite()) {
	                path = file.getAbsolutePath();

	                String timeStamp = "temp";
	                File testWritable = new File(path, "test_" + timeStamp);

	                if (testWritable.mkdirs()) {
	                    testWritable.delete();
	                } else {
	                    path = null;
	                }
	            }
	        }

	        if (path != null) {
	            sdCardFile = new File(path);
	            LogUtils.show("sdCardFile =" + sdCardFile);
	            return sdCardFile.getAbsolutePath();
	        }

	        return null;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;*/

        File file = UIUtils.getContext().getFilesDir();
        if (file == null || !file.exists()) {
            file = UIUtils.getContext().getCacheDir();
        }
        permissionPath(file.getAbsolutePath());
        return file.getAbsolutePath() + File.separator;//Environment.getRootDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * 获取内置SD卡或系统存储路径
     *
     * @return
     */
    public static String getSavePath() {
        if (isSDCardEnable()) {
            return getSDCardPath();
        } else {
            return getRootDirectoryPath();
        }
    }

    /**
     * 获取外部缓存目录
     *
     * @param context
     * @return
     */
    public static String getExternalCacheDir(Context context) {
        String path = context.getExternalCacheDir().getAbsolutePath();
        return path;
    }

    /**
     * 获取手机内置和外置SDcard存储路径
     *
     * @param context
     * @return
     */
    public static String[] getStorageList(Context context) {
        StorageManager manager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        Method methodGetPaths = null;
        try {
            methodGetPaths = manager.getClass().getMethod("getVolumePaths");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        String[] paths = null;
        try {
            paths = (String[]) methodGetPaths.invoke(manager);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return paths;
    }

    /**
     * 获取外置SDcard路径
     *
     * @param context
     * @return
     */
    public static String getExternalSDCartPath(Context context) {
        String[] paths = getStorageList(context);

        if (paths.length == 2) {
            for (String path : paths) {
                if (path != null && !path.equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                    return path + File.separator;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * 缓存主目录
     *
     * @return
     */
    public static String getExpandCache() {
        return getSavePath() + "100mshExpand/";
    }

    /**
     * 网络图片本地缓存目录
     *
     * @return
     */
    public static String getImgCache() {
        return getExpandCache() + "img/";
    }

    /**
     * 崩溃日志保存目录
     *
     * @return
     */
    public static String getCrashCache() {
        return getExpandCache() + "crash/";
    }

    /**
     * App更新包缓存目录
     *
     * @return
     */
    public static String getAppCache() {
        String path = getExpandCache() + "app/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        permissionPath(path);
        return path;
    }

    public static void permissionPath(String path) {
        if (!isSDCardEnable()) {
            try {
                Runtime runtime = Runtime.getRuntime();
                String command = "chmod -R 777 " + path;
                runtime.exec(command);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
