package com.inner.sdk.utils;

import org.springframework.util.DigestUtils;

public class Md5 {
	
	/**
	 * 执行密码加密
	 * @param password 原密码
	 * @param salt 盐值
	 * @return 加密后的密文
	 */
	public static String getMd5Password(String password, String salt) {
		// 【加密规则】
		// 1. 无视原始密码的强度；
		// 2. 使用UUID作为盐值，在原始密码的左右两侧拼接；
		// 3. 循环加密3次。
		for (int i = 0; i < 3; i++) {
			password = DigestUtils.md5DigestAsHex(
				(salt + password + salt).getBytes())
					.toUpperCase();
		}
		return password;
	}
	
}
