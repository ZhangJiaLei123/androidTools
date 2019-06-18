package com.bigbai.mnetwork;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @brief：对文件的简述
 * @author: Zhang
 * @date: 2019/6/16 - 21:28
 * @note Created by com.bigbai.mnetwork.
 */
public class MNetTool {
    /**
     * 获取ip地址
     * @return
     */
    public static String getHostIP() {

        String hostIp = "未接入网络";
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return hostIp;
    }
}
