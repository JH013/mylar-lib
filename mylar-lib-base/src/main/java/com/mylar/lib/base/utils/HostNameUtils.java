package com.mylar.lib.base.utils;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * 主机名工具类
 *
 * @author wangz
 * @date 2023/3/6 0006 22:50
 */
public class HostNameUtils {

    public static final String HOSTNAME;

    static {
        String hostName;
        try {
            hostName = Inet4Address.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostName = null;
        }

        HOSTNAME = hostName;
    }
}
