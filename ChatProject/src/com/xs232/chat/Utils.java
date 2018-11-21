package com.xs232.chat;

import java.io.Closeable;

/**
 * ������
 *
 *
 */
public class Utils {
	/**
	 * �ͷ���Դ
	 */
	public static void close(Closeable... targets) {
		for(Closeable target:targets) {
			try {
				if(null!=target)
					target.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
