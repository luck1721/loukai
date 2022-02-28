package com.example.demo.bll.utils;

import cn.com.citycloud.hcs.common.domain.BaseBean;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

/**
 * 密码加密器
 * 
 * @created 2017年10月19日
 * @author  huanglj
 */
public class PasswordEncipher extends BaseBean {
	private static final long serialVersionUID = -7299012565232805158L;

	/** 加密编码 */
	public static final String ENCODING = "UTF-8";

	/** 文本 */
	private String text;
	/** 明文 */
	private String plain;
	/** 密文 */
	private String cipher;

	public PasswordEncipher(String text) {
		this.text = text;
		try {
			if(isCipher(text)) {
				cipher = text;
				plain = decrypt(cipher);
			} else {
				plain = text;
				cipher = encrypt(plain);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getText() {
		return text;
	}

	public String getPlain() {
		return plain;
	}

	public String getCipher() {
		return cipher;
	}

	private static boolean isCipher(String text) {
		if(text == null || !text.matches("^[\\da-f]{7,}$")) {
			return false;
		}
		int p = new BigInteger(text.substring(0, 1), 16).intValue();
		int s = (p & 2) / 2 + 1;
		if(text.length() <= s + 5) {
			return false;
		}
		int m = (p & 8) / 2 + (p & 4) / 2 + (p & 1);
		int n = new BigInteger(text.substring(1, 1 + s), 16).intValue();
		int r = new BigInteger(text.substring(text.length() - 3), 16).intValue() & 2047;
		if(r != n * 8 + m) {
			return false;
		}
		int q = new BigInteger(text.substring(text.length() - 4, text.length() - 2), 16).intValue() >> 3;
		BigInteger c = new BigInteger(text.substring(1 + s, text.length() - 4), 16);
		BigInteger t = c.multiply(BigInteger.valueOf(n%16+16)).subtract(BigInteger.valueOf(m - q));
		return t.compareTo(BigInteger.valueOf(0)) >= 0 && t.toString(16).length() % 2 == 0;
	}

	private String encrypt(String plain) throws UnsupportedEncodingException {
		if(StringUtils.isEmpty(plain)) {
			return plain;
		}
		int r = RandomUtils.nextInt(0, 2048);
		int n = r / 8;
		int m = r % 8;
		BigInteger t = new BigInteger(Hex.encodeHexString(plain.getBytes(ENCODING)), 16);
		BigInteger c = t.add(BigInteger.valueOf(m)).divide(BigInteger.valueOf(n%16+16));
		int q = t.add(BigInteger.valueOf(m)).mod(BigInteger.valueOf(n%16+16)).intValue();
		String ns = BigInteger.valueOf(n).toString(16);
		String rs = BigInteger.valueOf((q & 1) * 2048 + r).toString(16);
		int p = (m & 4) * 2 + (m & 2) * 2 + (ns.length() - 1) * 2 + (m & 1);
		StringBuilder sb = new StringBuilder(BigInteger.valueOf(p).toString(16)).append(ns).append(c.toString(16)).append(BigInteger.valueOf(q >> 1).toString(16));
		for (int i = 0; i < 3 - rs.length(); i++) {
			sb.append(0);
		}
		return sb.append(rs).toString();
	}

	private String decrypt(String cipher) throws UnsupportedEncodingException, DecoderException {
		int p = new BigInteger(cipher.substring(0, 1), 16).intValue();
		int s = (p & 2) / 2 + 1;
		int m = (p & 8) / 2 + (p & 4) / 2 + (p & 1);
		int n = new BigInteger(cipher.substring(1, 1 + s), 16).intValue();
		int q = new BigInteger(cipher.substring(cipher.length() - 4, cipher.length() - 2), 16).intValue() >> 3;
		BigInteger c = new BigInteger(cipher.substring(1 + s, cipher.length() - 4), 16);
		BigInteger t = c.multiply(BigInteger.valueOf(n%16+16)).subtract(BigInteger.valueOf(m - q));
		return new String(Hex.decodeHex(t.toString(16).toCharArray()), ENCODING);
	}

}
