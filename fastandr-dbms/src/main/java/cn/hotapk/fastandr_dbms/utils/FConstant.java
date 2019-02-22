package cn.hotapk.fastandr_dbms.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author laijian
 * @version 2017/11/24
 * @Copyright (C)下午10:04 , www.hotapk.cn
 * 一些常量
 */
public class FConstant {
    public static final String SHAREDPREFS_XML = "SHAREDPREFS_XML";
    public static final String INTEGER = "integer";
    public static final String BOOLEAN = "boolean";
    public static final String FLOAT = "float";
    public static final String LONG = "long";
    public static final String TEXT = "text";
    public static final String STRING_SET = "set";
    public static final String BLOB = "blob";
    public static final String REAL = "real";
    public static final String dirName = "/dengdai";
    
    public static String getURL(){
        return "http://" +  getIPAddress(true) + ":8888";
    }

    public static String getIPAddress(final boolean useIPv4) {
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                // 防止小米手机返回10.0.2.15
                if (!ni.isUp()) continue;
                for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        boolean isIPv4 = hostAddress.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4) return hostAddress;
                        } else {
                            if (!isIPv4) {
                                int index = hostAddress.indexOf('%');
                                return index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, index).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

}
