package cn.sdt.libniocommon.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

public class NetTools {
    private final static String TAG = "NetTools";

    public static String getClientIp(Context context) {
        String ip = null;
        ConnectivityManager conMann = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo ethernetNetworkInfo = conMann.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);

        if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected()) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
            System.out.println("wifi ip地址为:" + ip);
        } else if (mobileNetworkInfo != null && mobileNetworkInfo.isConnected()) {
            ip = getLocalIpAddress();
            System.out.println("本地ip:" + ip);
        } else if (ethernetNetworkInfo != null && ethernetNetworkInfo.isConnected()) {
            ip = getEthernetIpAddress();
        }
        return ip;
    }

    public static String getLocalIpAddress() {
        String ipv4 = null;
        try {
            ArrayList<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface ni : nilist) {
                ArrayList<InetAddress> ialist = Collections.list(ni.getInetAddresses());
                for (InetAddress address : ialist) {
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        ipv4=address.getHostAddress();
                        Log.e("localip", "ipv4:"+ipv4);

                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("localip", ex.toString());
        }
        return ipv4;
    }


    /**
     * 获取以太网ip
     *
     * @return
     */
    public static String getEthernetIpAddress() {
        String localIp = null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress() &&
                            inetAddress instanceof Inet4Address &&
                            !inetAddress.getHostAddress().toString().equals("0.0.0.0")) {
                        localIp = inetAddress.getHostAddress().toString();

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localIp;
    }

    public static String intToIp(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

}
