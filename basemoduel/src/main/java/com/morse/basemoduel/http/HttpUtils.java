package com.morse.basemoduel.http;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/14.
 */

public class HttpUtils {

    /**
     * http post
     *
     * @param url
     * @return
     */
    public static HttpURLConnection post(String url) {
        HttpURLConnection httpUrlConnection = null;
        try {
            httpUrlConnection = getConnection(url);
            //设置请求参数
            httpUrlConnection.setRequestMethod("POST");
            //传递参数时需要开启
            httpUrlConnection.setDoOutput(true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpUrlConnection;
    }

    @NonNull
    private static HttpURLConnection getConnection(String url) throws IOException {
        HttpURLConnection httpUrlConnection = null;
        URL mUrl = new URL(url);
        //打开http连接
        httpUrlConnection = (HttpURLConnection) mUrl.openConnection();
        //设置连接超时
        httpUrlConnection.setConnectTimeout(15000);
        //设置读取超时
        httpUrlConnection.setReadTimeout(15000);
        //接受输入流
        httpUrlConnection.setDoInput(true);
        //添加Header
        httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
        return httpUrlConnection;
    }

    /**
     * http get
     *
     * @param url
     * @return
     */
    public static HttpURLConnection get(String url) {
        HttpURLConnection httpUrlConnection = null;
        try {
            httpUrlConnection = getConnection(url);
            httpUrlConnection.setRequestMethod("GET");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpUrlConnection;
    }

    public static void postParams(OutputStream output, List<NameValuePair>paramsList) throws IOException{
        StringBuilder mStringBuilder=new StringBuilder();
        for (NameValuePair pair:paramsList){
            if(!TextUtils.isEmpty(mStringBuilder)){
                mStringBuilder.append("&");
            }
            mStringBuilder.append(URLEncoder.encode(pair.getName(),"UTF-8"));
            mStringBuilder.append("=");
            mStringBuilder.append(URLEncoder.encode(pair.getValue(),"UTF-8"));
        }
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(output,"UTF-8"));
        writer.write(mStringBuilder.toString());
        writer.flush();
        writer.close();
    }

    private void useHttpUrlConnectionPost(String url) {
        InputStream mInputStream = null;
        HttpURLConnection mHttpURLConnection = post(url);
        try {
            List<NameValuePair> postParams = new ArrayList<>();
            //要传递的参数
            postParams.add(new BasicNameValuePair("username", "moon"));
            postParams.add(new BasicNameValuePair("password", "123"));
            postParams(mHttpURLConnection.getOutputStream(), postParams);
            mHttpURLConnection.connect();
            mInputStream = mHttpURLConnection.getInputStream();
            int code = mHttpURLConnection.getResponseCode();
            String respose = converStreamToString(mInputStream);
            Log.i("wangshu", "请求状态码:" + code + "\n请求结果:\n" + respose);
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将请求结果装潢为String类型
     *
     * @param is InputStream
     * @return String
     * @throws IOException
     */
    public static String converStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        String respose = sb.toString();
        return respose;
    }

}
