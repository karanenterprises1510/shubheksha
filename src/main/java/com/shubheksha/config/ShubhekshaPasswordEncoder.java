package com.shubheksha.config;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.shubheksha.service.common.EncDecAES;

public class ShubhekshaPasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		try {
			return EncDecAES.encrypt(rawPassword.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		try {
			return EncDecAES.encrypt(rawPassword.toString()).equals(encodedPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String decode(String encoded) throws Exception {
		return EncDecAES.decrypt(encoded);
	}
}
