package com.sad.jetpack.v1.datamodel.api.utils;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Locale;

public class AddressUtils {
    /**
     * 获取本机内网IP地址
     */
    public static String getLocalIP(){
        try {
            for(Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
                NetworkInterface intf = en.nextElement();
                for(Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();){
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if(!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)){
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 获取路由器IP
     * @return
     */
    public static String getLocalRoutersIP(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
            int serverAddress = dhcpInfo.serverAddress;
            String routersIPAddress=String.format(Locale.US, "%d.%d.%d.%d", (serverAddress & 0xff), (serverAddress >> 8 & 0xff), (serverAddress >> 16 & 0xff), (serverAddress >> 24 & 0xff));
            return routersIPAddress;
        }
        return "";
    }
}
