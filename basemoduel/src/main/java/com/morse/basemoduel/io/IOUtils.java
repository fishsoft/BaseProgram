package com.morse.basemoduel.io;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Administrator on 2017/1/14.
 */

public class IOUtils {

    /**
     * 文件写入
     *
     * @param context
     * @param fileName 文件名
     * @param str      写入的文件
     */
    public void ioFile(Context context, String fileName, String str) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND | Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
            fos.write(str.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件写入
     *
     * @param relativePath 文件相对路径
     * @param str          写入的数据
     */
    public void ioFile(String relativePath, String str) {
        try {
            File dir = Environment.getDataDirectory();
            File newFile = new File(dir, relativePath);
            FileWriter fw = new FileWriter(newFile, true);
            fw.write(str);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件写入
     *
     * @param relativePath 文件相对路径
     * @param str          写入的数据
     */
    public void ioWriteFile(String relativePath, String str) {
        try {
            File dir = Environment.getDataDirectory();
            File newFile = new File(dir, relativePath);
            FileWriter fw = new FileWriter(newFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(str);
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读数据
     *
     * @param relativePath 文件相对路径
     */
    public void ioReadFile(String relativePath) {
        String content = "";
        String temp = "";
        try {
            File dir = Environment.getDataDirectory();
            File newFile = new File(dir, relativePath);
            FileReader fr = new FileReader(newFile);
            BufferedReader br = new BufferedReader(fr);
            while ((content = br.readLine()) != null) {
                temp += content + "\r\n";
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
