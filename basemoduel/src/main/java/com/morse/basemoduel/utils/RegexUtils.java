package com.morse.basemoduel.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.StrictMode;
import android.text.TextUtils;

/**
 * 基本信息验证：
 * <p>
 * 日期验证
 * </p>
 * <p>
 * 用户名验证
 * </p>
 * <p>
 * 用户ID验证
 * </p>
 * <p>
 * Email验证
 * </p>
 * <p>
 * 身份证号码验证
 * </p>
 * <p>
 * 手机号码验证
 * </p>
 * <p>
 * 固定电话号码验证
 * </p>
 * <p>
 * 整数验证
 * </p>
 * <p>
 * 整数和浮点数验证
 * </p>
 * <p>
 * 空白字符验证
 * </p>
 * <p>
 * 中文验证
 * </p>
 * <p>
 * url验证
 * </p>
 * <p>
 * 中国邮政编码验证
 * </p>
 * <p>
 * IP地址验证
 * </p>
 * <p>
 * 所有email地址格式验证
 * </p>
 *
 * @author Administrator
 */
public class RegexUtils {
    /**
     * 验证日期
     *
     * @return 验证成功返回true 验证失败false;
     * @格式 yyyy-mm-dd
     * @考虑了二月.闰年等等
     */
    public static boolean checkDate(String date) {
        String regex = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29))$";
        return Pattern.matches(regex, date);
    }

    /**
     * 验证用户名
     */
    public static boolean checkUserName(String username) {
        String regex = "^([a-zA-Z]{1})\\w{5,15}$";
        return Pattern.matches(regex, username);
    }

    /**
     * 英文大小写加数字
     *
     * @param id
     * @return
     */
    public static boolean checkID(String id) {
        if (id == null) {
            return false;
        }

        String regex = "^[A-Za-z0-9-]+$";
        return Pattern.matches(regex, id);
    }

    /**
     * 验证ID列表
     *
     * @return 验证成功返回true 验证失败false;
     * @格式 yyyy-mm-dd
     * @考虑了二月.闰年等等
     */
    public static boolean checkUserIDList(String date) {
        String regex = "^([1-9]\\d+){1}(,{1}([1-9]\\d+))*$";
        return Pattern.matches(regex, date);
    }

    /**
     * 验证Email
     *
     * @param email email地址，格式：zhangsan@sina.com，zhangsan@xxx.com.cn，xxx代表邮件服务商
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEmail(String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }

    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }

    public static boolean checkIDCard(String IDCard) {
        String regex = "((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65)[0-9]{4})"
                + "(([1|2][0-9]{3}[0|1][0-9][0-3][0-9][0-9]{3}" + "[Xx0-9])|([0-9]{2}[0|1][0-9][0-3][0-9][0-9]{3}))";
        return Pattern.matches(regex, IDCard);
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               <p>
     *               移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）
     *               </p>
     *               <p>
     *               联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）
     *               </p>
     *               <p>
     *               电信的号段：133、153、180（未启用）、189
     *               </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMobile(String mobile) {
        String regex = "(\\+\\d+)?1[34587]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    /**
     * 验证固定电话号码
     *
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     *              <p>
     *              <b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9
     *              的一位或多位数字， 数字之后是空格分隔的国家（地区）代码。
     *              </p>
     *              <p>
     *              <b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     *              对不使用地区或城市代码的国家（地区），则省略该组件。
     *              </p>
     *              <p>
     *              <b>电话号码：</b>这包含从 0 到 9 的一个或多个数字
     *              </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPhone(String phone) {
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";
        return Pattern.matches(regex, phone);
    }

    /**
     * 验证整数（正整数）
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkInteger(String digit) {
        String regex = "^\\d+$";
        return Pattern.matches(regex, digit);
    }

    /**
     * 验证整数（正整数和负整数）
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDigit(String digit) {
        String regex = "\\-?[1-9]\\d+";
        return Pattern.matches(regex, digit);
    }

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     *
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDecimals(String decimals) {
        String regex = "^[0-9]+(\\.[0-9]+)?$";
        return Pattern.matches(regex, decimals);
    }

    /**
     * 验证空白字符
     *
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBlankSpace(String blankSpace) {
        String regex = "\\s*|\t|\r|\n";
        return Pattern.matches(regex, blankSpace);
    }

    /**
     * 验证中文
     *
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex, chinese);
    }

    /**
     * 验证日期（年月日）
     *
     * @param birthday 日期，格式：1992-09-03，
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBirthday(String birthday) {
        String regex = "[0-9]{4}([-/])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex, birthday);
    }

    /**
     * 验证URL地址
     *
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或
     *            http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkURL(String url) {
        if (!TextUtils.isEmpty(url)) {
            String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
            return Pattern.matches(regex, url);
        }
        return false;
    }

    public static synchronized boolean checkDomain(String url) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        if (!checkURL(url))
            return false;
        if (TextUtils.isEmpty(url))
            return false;
        try {
            InetAddress[] addresses = InetAddress.getAllByName(url);
            System.out.println("address length is:" + addresses.length);
            for (int i = 0; i < addresses.length; i++) {
                System.out.println(addresses[i]);
            }
            return true;
        } catch (UnknownHostException ex) {
            return false;
        }
    }

    /**
     * 匹配中国邮政编码
     *
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }

    /**
     * 获取页面中所有email地址
     *
     * @param surl
     * @throws Exception
     */
    public static void WebCrawlersEmail(String surl) throws Exception {
        URL url = new URL(surl);
        // 打开连接
        URLConnection conn = url.openConnection();
        // 设置连接网络超时时间
        conn.setConnectTimeout(1000 * 10);
        // 读取指定网络地址中的文件
        BufferedReader bufr = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = null;
        String regex = "[a-zA-Z0-9_-]+@\\w+\\.[a-z]+(\\.[a-z]+)?"; // 匹配email的正则
        Pattern p = Pattern.compile(regex);
        while ((line = bufr.readLine()) != null) {
            Matcher m = p.matcher(line);
            while (m.find()) {
                System.out.println(m.group());// 获得匹配的email
            }
        }
    }

    /**
     * 下面的例子演示如何利用正则表达式从一个URL中查找并输出所有类似下面的超链接： <a href="http://www.sina.com">
     * 首先我们从命令行输入URL地址，打开输入流， 读取URL的内容并转化为字符串存入htmlString中。
     * 然后以"(<a\\s*href=[^>]*>)" 构造正则表达式，最后在字符串htmlString中查找匹配的字符串。
     *
     * @param surl
     */
    public static void WebCrawlersA(String surl) {

        InputStream in = null;
        PrintWriter out = null;
        String htmlString = null;
        try {
            // Set up the streams
            URL url = new URL(surl); // Create the URL
            in = url.openStream(); // Open a stream to it
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = bin.readLine()) != null) {
                if (out != null)
                    out.println(line);
                sb = sb.append(line);
            }
            htmlString = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally { // Always close the streams, no matter what.
            try {
                in.close();
                // out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Pattern p = Pattern.compile("(<a\\s*href=[^>]*>)");
        Matcher m = p.matcher(htmlString);
        boolean result = m.find();
        while (result) {
            for (int i = 1; i <= m.groupCount(); i++) {
                System.out.println(m.group(i));
            }
            result = m.find();
        }
    }

    public static String stripHtml(String content) {
        // <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "/r/n");
        // <br><br/>替换为换行
        content = content.replaceAll("<br//s*/?>", "/r/n");
        // 去掉其它的<>之间的东西
        content = content.replaceAll("//<.*?>", "");
        // 还原HTML
        // content = HTMLDecoder.decode(content);
        return content;
    }

    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     *
     * @param ipAddress IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIpAddress(String ipAddress) {
        String regex = "^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$";
        return Pattern.matches(regex, ipAddress);
    }

    /**
     * 匹配MAC地址(简单匹配，格式，如：00:E0:20:1C:7C:0C，没有匹配MAC段的大小)
     *
     * @param mac MAC地址字符串
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkMac(String mac) {
        if (!TextUtils.isEmpty(mac)) {
            String regex = "^[a-fA-F0-9]{2}(:[a-fA-F0-9]{2}){5}$";
            return Pattern.matches(regex, mac);
        }
        return false;
    }

    public static boolean isIP(String string) {
        // TODO Auto-generated method stub
        if (!checkIpAddress(string) || TextUtils.isEmpty(string)) {
            return false;
        }
        return true;
    }

    public static boolean isUrl(String string) {
        if (!checkURL(string) || TextUtils.isEmpty(string)) {
            return false;
        }
        return true;
    }

    /**
     * 判断子网掩码是否合法
     *
     * @param ipValue
     * @return true 是 false 不是
     */
    public static boolean isNetMask(String ipValue) {
        String[] ips = ipValue.split("\\.");
        if (ips.length < 4) {
            return false;
        }
        String binaryVal = "";
        for (int i = 0; i < ips.length; i++) {
            String binaryStr = Integer.toBinaryString(Integer.parseInt(ips[i]));

            int times = 8 - binaryStr.length();

            for (int j = 0; j < times; j++) {
                binaryStr = "0" + binaryStr;
            }
            binaryVal += binaryStr;
        }
        String regx = "^[1]*[0]*$";
        if (isRegx(binaryVal, regx)) {
            return true;
        }
        return false;
    }

    public static boolean isRegx(String binaryVal, String regx) {
        return Pattern.matches(regx, binaryVal);
    }
}
