package com.pgy.ups.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author IP工具类
 *
 */
public class IPUtils {

	private static Logger logger = LoggerFactory.getLogger(IPUtils.class);

	public static String getLocalIp() {
		String sIP = "";
		InetAddress ip = null;  
		try {
			// 如果是Windows操作系统
			if (isWindowsOS()) {
				ip = InetAddress.getLocalHost();
			}
			// 如果是Linux操作系统
			else {
				boolean bFindIP = false;
				Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
						.getNetworkInterfaces();
				while (netInterfaces.hasMoreElements()) {
					if (bFindIP) {
						break;
					}
					NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
					// ----------特定情况，可以考虑用ni.getName判断
					// 遍历所有ip
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						ip = (InetAddress) ips.nextElement();
						if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() // 127.开头的都是lookback地址
								&& ip.getHostAddress().indexOf(":") == -1) {
							bFindIP = true;
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取本地IP异常：{}", e);
		}

		if (null != ip) {
			sIP = ip.getHostAddress();
		}
		return sIP;
	}

	private static boolean isWindowsOS() {
		String osName = System.getProperties().getProperty("os.name");
		return osName.toLowerCase().contains("windows");
	}
}
