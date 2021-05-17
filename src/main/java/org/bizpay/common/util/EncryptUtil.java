package org.bizpay.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

// 단방향 암호화 
@Component
public class EncryptUtil {
    public String encryptSHA256(String s) {
        return encrypt(s, "SHA-256");
    }

    public String encryptMD5(String s) {
        return encrypt(s, "MD5");
    }
    
	public String encrypt(String str , String messageDigest) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(messageDigest);
			byte[] passBytes = str.getBytes();
	        md.reset();
	        byte[] digested = md.digest(passBytes);
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < digested.length; i++) sb.append(Integer.toString((digested[i]&0xff) + 0x100, 16).substring(1));
	        return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return str;
		}
		
	}
}
