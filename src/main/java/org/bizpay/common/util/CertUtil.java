package org.bizpay.common.util;
/**
 * 이전소스에서 이동함
 * 
 * @author <a href="mailto:kangwoo@jarusoft.com">kangwoo</a>
 * @version 1.0
 * @since 1.0
 */
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class CertUtil {
	@Autowired
	ByteUtils byteUtils;
	
	/**
	 * 암호화처리를 한다.
	 * @param text
	 * @param password
	 * @throws Exception
	 */
	public String encrypt(String text) {
		try{
			if(text == null || text.trim() .length()<1) return "";
			
			Key key = generateKey("AES", byteUtils.toBytes("696d697373796f7568616e6765656e61", 16));
			String transformation = "AES/ECB/PKCS5Padding";
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			 
			byte[] hexText=text.getBytes();
			hexText = cipher.doFinal(hexText);
			 
			return byteUtils.toHexString(hexText);
		}catch(Exception e){
			return "";
		}
	}
	/**
	 * 암호화된데이터를 복호화처리한다.
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public String decrypt(String text){
		try{

			if(text== null || text.length() ==0) {

				return "";
			}
			Key key = generateKey("AES", byteUtils.toBytes("696d697373796f7568616e6765656e61", 16));
			String transformation = "AES/ECB/PKCS5Padding";
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.DECRYPT_MODE, key);
			 
			byte[] hexId=byteUtils.toBytesFromHexString(text);
			hexId = cipher.doFinal(hexId);
			String decodingData = new String(hexId, "euc-kr");
			

			if(decodingData==null || decodingData.length() <1 ) {
				return "";
			}
			else return decodingData;
		}catch(Exception e){
			return "";
		}
	}
	/**
	  * <p>해당 알고리즘에 사용할 비밀키(SecretKey)를 생성한다.</p>
	  *
	  * @return 비밀키(SecretKey)
	  */
	private Key generateKey(String algorithm) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
	    SecretKey key = keyGenerator.generateKey();
	    return key;
	    
	}
	/**
	* <p>주어진 데이터로, 해당 알고리즘에 사용할 비밀키(SecretKey)를 생성한다.</p>
	*
	* @param algorithm DES/DESede/TripleDES/AES
	* @param keyData
	* @return
	*/
	private Key generateKey(String algorithm, byte[] keyData) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
		String upper = algorithm.toUpperCase();
		
		if ("DES".equals(upper)) {
			KeySpec keySpec = new DESKeySpec(keyData);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
			SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
			return secretKey;
		} else if ("DESede".equals(upper) || "TripleDES".equals(upper)) {
			KeySpec keySpec = new DESedeKeySpec(keyData);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
			SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
			return secretKey;
		} else {
			SecretKeySpec keySpec = new SecretKeySpec(keyData, algorithm);
			return keySpec;
		}
	}
}
