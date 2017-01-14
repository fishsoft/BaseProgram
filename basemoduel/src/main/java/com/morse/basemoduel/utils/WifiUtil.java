package com.morse.basemoduel.utils;

import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

/**
 * Wifi连接管理工具类<br>
 */
public class WifiUtil implements Comparator<ScanResult> {
    // 定义一个WifiManager对象
    private WifiManager mWifiManager;
    // 定义一个WifiInfo对象
    private WifiInfo mWifiInfo;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    // 网络连接列表
    private List<WifiConfiguration> mWifiConfigurations;
    private WifiLock mWifiLock;
    private Context mContext;

    /**
     * 定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
     *
     * @author Morse
     */
    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    public WifiUtil(Context context) {
        // 取得WifiManager对象
        this.mContext = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 取得WifiInfo对象
        mWifiInfo = mWifiManager.getConnectionInfo();

    }

    /**
     * Function:关闭wifi<br>
     * www.javaapk.com更改
     *
     * @author ZYT DateTime 2014-5-15 上午12:17:37<br>
     * @return<br>
     */
    public boolean closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            return mWifiManager.setWifiEnabled(false);
        }
        return false;
    }

    /**
     * Gets the Wi-Fi enabled state.检查当前wifi状态
     *
     * @return One of {@link WifiManager#WIFI_STATE_DISABLED},
     * {@link WifiManager#WIFI_STATE_DISABLING},
     * {@link WifiManager#WIFI_STATE_ENABLED},
     * {@link WifiManager#WIFI_STATE_ENABLING},
     * {@link WifiManager#WIFI_STATE_UNKNOWN}
     * @see #isWifiEnabled()
     */
    public int checkState() {
        return mWifiManager.getWifiState();
    }

    /**
     * 锁定wifiLock
     */
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    /**
     * 解锁wifiLock
     */
    public void releaseWifiLock() {
        // 判断是否锁定
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    /**
     * 创建一个wifiLock
     */
    public void createWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("test");
    }

    /**
     * 得到配置好的网络
     *
     * @return
     */
    public List<WifiConfiguration> getConfiguration() {
        return mWifiConfigurations;
    }

    /**
     * 指定配置好的网络进行连接
     *
     * @param index
     */
    public void connetionConfiguration(int index) {
        if (index > mWifiConfigurations.size()) {
            return;
        }
        // 连接配置好指定ID的网络
        mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, true);
    }

    /**
     * 扫描wifi列表
     */
    public void startScan() {
        // openWifi();
        mWifiManager.startScan();
        // 得到扫描结果
        mWifiList = mWifiManager.getScanResults();
        // 得到配置好的网络连接
        mWifiConfigurations = mWifiManager.getConfiguredNetworks();
    }

    /**
     * 得到网络列表
     *
     * @return
     */
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }

    /**
     * 查看扫描结果
     *
     * @return
     */
    public StringBuffer lookUpScan() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mWifiList.size(); i++) {
            sb.append("Index_" + String.valueOf(i + 1) + ":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level
            sb.append((mWifiList.get(i)).toString()).append("\n");
        }
        return sb;
    }

    /**
     * 获取手机mac地址
     *
     * @return
     */
    public String getMacAddress() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    /**
     * 获取路由器mac地址 Return the basic service set identifier (BSSID) of the current
     * access point. The BSSID may be {@code null} if there is no network
     * currently connected.
     *
     * @return the BSSID, in the form of a six-byte MAC address:
     * {@code XX:XX:XX:XX:XX:XX}
     */
    public String getBSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    /**
     * 获取手机IP
     *
     * @return
     */
    public int getIpAddress() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    /**
     * 获取wifi SSID
     *
     * @return
     */
    public String getSSID() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getSSID().replaceAll("\"", "");
    }

    /**
     * 获取连接速度
     *
     * @return
     */
    public int getLinkSpeed() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getLinkSpeed();
    }

    /**
     * 获取RSSI
     *
     * @return
     */
    public int getRssi() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getRssi();
    }

    /**
     * 获取网络ID Each configured network has a unique small integer ID, used to
     * identify the network when performing operations on the supplicant. This
     * method returns the ID for the currently connected network.
     *
     * @return the network ID, or -1 if there is no currently connected network
     */
    public int getNetWordId() {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    /**
     * Function: 得到wifiInfo的所有信息<br>
     *
     * @author ZYT DateTime 2014-5-14 上午11:03:32<br>
     * @return<br>
     */
    public String getWifiInfo() {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    /**
     * 添加一个网络并连接
     *
     * @param configuration
     */
    public void addNetWork(WifiConfiguration configuration) {
        int wcgId = mWifiManager.addNetwork(configuration);
        mWifiManager.enableNetwork(wcgId, true);
    }

    /**
     * 断开指定ID的网络
     *
     * @param netId
     */
    public void disConnectionWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        disConnect();
    }

    public void disConnect() {
        mWifiManager.disconnect();
    }

    /**
     * Function: 打开wifi功能<br>
     *
     * @return true:打开成功；false:打开失败<br>
     * @author ZYT DateTime 2014-5-14 上午11:01:11<br>
     */
    public boolean openWifi() {
        boolean bRet = true;
        if (!mWifiManager.isWifiEnabled()) {
            bRet = mWifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    /**
     * Function: 提供一个外部接口，传入要连接的无线网 <br>
     *
     * @param SSID
     *            SSID
     * @param Password
     * @param Type
     * <br>
     *            没密码：{@linkplain WifiCipherType#WIFICIPHER_NOPASS}<br>
     *            WEP加密： {@linkplain WifiCipherType#WIFICIPHER_WEP}<br>
     *            WPA加密： {@linkplain WifiCipherType#WIFICIPHER_WPA}
     * @return true:连接成功；false:连接失败<br>
     */
    // public boolean connect(String SSID, String Password, WifiCipherType Type)
    // {
    // if (!this.openWifi()) {
    // return false;
    // }
    // // 开启wifi功能需要一段时间(我在手机上测试一般需要1-3秒左右)，所以要等到wifi
    // // 状态变成WIFI_STATE_ENABLED的时候才能执行下面的语句
    // while (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLING) {
    // openWifi();
    // }
    // WifiConfiguration wifiConfig = createWifiInfo(SSID, Password, Type);
    // if (wifiConfig == null) {
    // return false;
    // }
    // WifiConfiguration tempConfig = this.isExsits(SSID);
    // int tempId = wifiConfig.networkId;
    // if (tempConfig != null) {
    // tempId = tempConfig.networkId;
    // mWifiManager.removeNetwork(tempConfig.networkId);
    // }
    // int netID = mWifiManager.addNetwork(wifiConfig);
    // // 断开连接
    // mWifiManager.disconnect();
    // // 重新连接
    // netID = wifiConfig.networkId;
    // // 设置为true,使其他的连接断开
    // boolean bRet = mWifiManager.enableNetwork(netID, true);
    // mWifiManager.reconnect();
    // return bRet;
    // }

    /**
     * 查看以前是否也配置过这个网络
     *
     * @param SSID
     * @return
     */
    public boolean isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建一个wifi连接
     *
     * @param SSID
     * @param Password
     * @param Type
     * @return
     */
    // private WifiConfiguration createWifiInfo(String SSID, String Password,
    // WifiCipherType Type) {
    // WifiConfiguration config = new WifiConfiguration();
    // config.allowedAuthAlgorithms.clear();
    // config.allowedGroupCiphers.clear();
    // config.allowedKeyManagement.clear();
    // config.allowedPairwiseCiphers.clear();
    // config.allowedProtocols.clear();
    // config.SSID = "\"" + SSID + "\"";
    // if (Type == WifiCipherType.WIFICIPHER_NOPASS) {
    // config.wepKeys[0] = "";
    // config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
    // config.wepTxKeyIndex = 0;
    // }
    // if (Type == WifiCipherType.WIFICIPHER_WEP) {
    // config.preSharedKey = "\"" + Password + "\"";
    // config.hiddenSSID = true;
    // config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
    // config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
    // config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
    // config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
    // config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
    // config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
    // config.wepTxKeyIndex = 0;
    // }
    // if (Type == WifiCipherType.WIFICIPHER_WPA) {
    // // 修改之后配置
    // config.preSharedKey = "\"" + Password + "\"";
    // config.hiddenSSID = true;
    // config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
    // config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
    // config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
    // config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
    // // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
    // config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
    // config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
    //
    // } else {
    // return null;
    // }
    // return config;
    // }

    /**
     * Function:判断扫描结果是否连接上<br>
     *
     * @param result
     * @author ZYT DateTime 2014-5-14 上午11:31:40<br>
     * @return<br>
     */
    public boolean isConnect(ScanResult result) {
        if (result == null) {
            return false;
        }
        mWifiInfo = mWifiManager.getConnectionInfo();
        String g2 = "\"" + result.SSID + "\"";
        if (mWifiInfo.getSSID() != null && mWifiInfo.getSSID().endsWith(g2)
                && result.BSSID.equals(mWifiInfo.getBSSID())) {
            return true;
        }
        return false;
    }

    /**
     * 获取已连接的wifi的网络ID
     *
     * @return
     */
    public int getConnNetId() {
        // result.SSID;
        mWifiInfo = mWifiManager.getConnectionInfo();
        return mWifiInfo.getNetworkId();
    }

    /**
     * Function:信号强度转换为字符串<br>
     *
     * @param level <br>
     * @author ZYT DateTime 2014-5-14 下午2:14:42<br>
     */
    public static String singlLevToStr(int level) {

        String resuString = "无信号";

        if (Math.abs(level) > 100) {
        } else if (Math.abs(level) > 80) {
            resuString = "弱";
        } else if (Math.abs(level) > 70) {
            resuString = "强";
        } else if (Math.abs(level) > 60) {
            resuString = "强";
        } else if (Math.abs(level) > 50) {
            resuString = "较强";
        } else {
            resuString = "极强";
        }
        return resuString;
    }

    /**
     * 添加到网络
     *
     * @param wcg
     */
    public boolean addNetwork(WifiConfiguration wcg) {
        if (wcg == null) {
            return false;
        }
        int wcgID = mWifiManager.addNetwork(wcg);
        boolean b = mWifiManager.enableNetwork(wcgID, true);
        mWifiManager.saveConfiguration();
        return b;
    }

    /**
     * 连接指定AP
     *
     * @param scan
     * @return
     */
    public boolean connectSpecificAP(ScanResult scan) {
        List<WifiConfiguration> list = mWifiManager.getConfiguredNetworks();
        boolean networkInSupplicant = false;
        boolean connectResult = false;
        disConnect();
        if (null != list) {
            for (WifiConfiguration w : list) {
                // 将指定AP 名字转化并连接网络
                if (w.BSSID != null && w.BSSID.equals(scan.BSSID)) {
                    connectResult = mWifiManager.enableNetwork(w.networkId, true);
                    networkInSupplicant = true;
                    break;
                }
            }

            if (!networkInSupplicant) {
                WifiConfiguration config = CreateWifiInfo(scan, "");
                connectResult = addNetwork(config);
            }
        }
        return connectResult;
    }

    /**
     * 然后是一个实际应用方法，只验证过没有密码的情况：
     *
     * @param scan
     * @param Password
     * @return
     */
    public WifiConfiguration CreateWifiInfo(ScanResult scan, String Password) {
        WifiConfiguration config = new WifiConfiguration();
        config.hiddenSSID = false;
        config.status = WifiConfiguration.Status.ENABLED;
        if (scan.capabilities.contains("WEP")) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.SSID = "\"" + scan.SSID + "\"";
            config.wepTxKeyIndex = 0;
            config.wepKeys[0] = Password;
        } else if (scan.capabilities.contains("PSK")) {
            config.SSID = "\"" + scan.SSID + "\"";
            config.preSharedKey = "\"" + Password + "\"";
        } else if (scan.capabilities.contains("EAP")) {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.SSID = "\"" + scan.SSID + "\"";
            config.preSharedKey = "\"" + Password + "\"";
        } else {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.SSID = "\"" + scan.SSID + "\"";
            config.preSharedKey = null;
            config.wepKeys[0] = "\"" + "\"";
            config.wepTxKeyIndex = 0;
        }
        return config;
    }

    /**
     * 连接WIFI
     *
     * @param ssid
     * @param pwd
     */
    public void connectWifi(String ssid, String pwd) {
        startScan();
        for (ScanResult scanResult : mWifiList) {
            if (!ssid.equals(scanResult.SSID))
                continue;
            if (getSecurity(scanResult)) {
                // 判断是否为同一个WIFI
                if (ssid.equals(getSSID()) && getBSSID().equals(scanResult.BSSID)) {
                    return;
                }
                // 判断当前WIFI是连接
                if (NetUtils.isWifiConnect(mContext)) {
                    disConnect();
                }
                addNetwork(CreateWifiInfo(scanResult, pwd));
            } else {
                if (!NetUtils.isWifiConnect(mContext)) {
                    connectSpecificAP(scanResult);
                }
            }
        }
    }

    /**
     * 判断wifi是否加密
     */
    public static boolean getSecurity(ScanResult result) {
        if (result.capabilities.contains("WEP") || result.capabilities.contains("PSK")
                || result.capabilities.contains("EAP")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断wifi是否连接
     *
     * @param context
     * @return
     */
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断wifi打开状态
     *
     * @return
     */
    public boolean checkNetCardState() {
        boolean flag = false;
        if (mWifiManager.getWifiState() == 0) {
            flag = false;
        } else if (mWifiManager.getWifiState() == 1) {
            flag = false;
        } else if (mWifiManager.getWifiState() == 2) {
        } else if (mWifiManager.getWifiState() == 3) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * wifi重连
     *
     * @return boolean
     */
    public boolean wifiReConnect() {
        return mWifiManager.reconnect();
    }

    /**
     * 按wifi信号排序
     */
    @Override
    public int compare(ScanResult lhs, ScanResult rhs) {
        // TODO Auto-generated method stub
        if (Math.abs(lhs.level) - Math.abs(rhs.level) <= 0)
            return 1;
        else
            return -1;
    }

    /**
     * 获取当前连接的地址
     *
     * @param context
     * @return
     */
    public static String getServiceIp(Context context) {
        WifiManager mManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return IPUtils.int2Ip(mManager.getDhcpInfo().serverAddress);
    }

}
