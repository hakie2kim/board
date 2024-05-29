package com.portfolio.www.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordUtil {
	public static String encPassword(String password) {
		return BCrypt.withDefaults().hashToString(12, password.toCharArray());
	}

	public static boolean verifyPassword(String password, String encPassword) {
		BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), encPassword);
		return result.verified;
	}
}
