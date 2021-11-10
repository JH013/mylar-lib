package com.mylar.lib.rabbitmq.component.utils;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * 主机名工具类
 *
 * @author wangz
 * @date 2021/11/7 0007 22:11
 */
public class RabbitHostNameUtil {

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
