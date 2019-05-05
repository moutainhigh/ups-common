package com.pgy.ups.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author 墨凉 RSA加密，解密，生成秘钥，加签，验签工具类，统一采用base64编码
 *
 */
public class RSAUtils {

	private static Logger logger = LoggerFactory.getLogger(RSAUtils.class);
	/**
	 * 生成公私秘钥对
	 */
	public static void genKeyPair() {
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			logger.error("无此加密算法异常：{}", ExceptionUtils.getStackTrace(e));
			return;
		}
		// 初始化密钥对生成器，密钥大小为96-1024位
		keyPairGen.initialize(1024, new SecureRandom());
		// 生成一个密钥对，保存在keyPair中
		KeyPair keyPair = keyPairGen.generateKeyPair();
		// 得到私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// 得到公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

		// 得到公钥字符串
		String publicKeyString = Base64.encodeBase64String(publicKey.getEncoded());
		// 得到私钥字符串
		String privateKeyString = Base64.encodeBase64String(privateKey.getEncoded());
		logger.info("公钥：{}", publicKeyString);
		logger.info("私钥：{}", privateKeyString);
	}

	/**
	 * 通过baase64公钥获取公钥对象
	 * 
	 * @param publicKeyString
	 * @return
	 */
	public static RSAPublicKey getRSAPublicKey(String publicKeyString) {
		try {
			byte[] buffer = Base64.decodeBase64(publicKeyString);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (InvalidKeySpecException e) {
			logger.error("无效的key指定：{}", ExceptionUtils.getStackTrace(e));
		} catch (NoSuchAlgorithmException e) {
			logger.error("无此加密算法异常：{}", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * 通过baase64私钥获取私钥对象
	 * 
	 * @param publicKeyString
	 * @return
	 */
	public static RSAPrivateKey getRSAPrivateKey(String privateKey) {
		byte[] buffer = Base64.decodeBase64(privateKey);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			logger.error("无此加密算法异常：{}", ExceptionUtils.getStackTrace(e));
		} catch (InvalidKeySpecException e) {
			logger.error("无效的key指定：{}", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * 公钥加密
	 * 
	 * @param publicKey
	 * @param plainTextData
	 * @return
	 */
	public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) {
		if (publicKey == null) {
			logger.error("公钥不能为null");
			return null;
		}
		try {
			// 使用默认RSA
			Cipher cipher = Cipher.getInstance("RSA");
			// cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			logger.error("无此加密算法：{}", ExceptionUtils.getStackTrace(e));
		} catch (NoSuchPaddingException e) {
			logger.error("加密公钥非法,请检查：{}", ExceptionUtils.getStackTrace(e));
		} catch (InvalidKeyException e) {
			logger.error("NoSuchPaddingException异常，请检查：{}", ExceptionUtils.getStackTrace(e));
		} catch (IllegalBlockSizeException e) {
			logger.error("明文长度非法：{}", ExceptionUtils.getStackTrace(e));
		} catch (BadPaddingException e) {
			logger.error("明文数据已损坏：{}", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * 私钥加密
	 * 
	 * @param publicKey
	 * @param plainTextData
	 * @return
	 */
	public static byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData) {
		if (privateKey == null) {
			logger.error("私钥不能为null");
			return null;
		}

		try {
			// 使用默认RSA
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(plainTextData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			logger.error("无此加密算法：{}", ExceptionUtils.getStackTrace(e));
		} catch (NoSuchPaddingException e) {
			logger.error("加密公钥非法,请检查：{}", ExceptionUtils.getStackTrace(e));
		} catch (InvalidKeyException e) {
			logger.error("NoSuchPaddingException异常，请检查：{}", ExceptionUtils.getStackTrace(e));
		} catch (IllegalBlockSizeException e) {
			logger.error("明文长度非法：{}", ExceptionUtils.getStackTrace(e));
		} catch (BadPaddingException e) {
			logger.error("明文数据已损坏：{}", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * 私钥解密过程
	 * 
	 * @param privateKey 私钥
	 * @param cipherData 密文数据
	 * @return 明文
	 */
	public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) {
		if (privateKey == null) {
			logger.error("私钥不能为null");
			return null;
		}
		try {
			// 使用默认RSA
			Cipher cipher = Cipher.getInstance("RSA");
			
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] output = cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			logger.error("无此加密算法：{}", ExceptionUtils.getStackTrace(e));
		} catch (NoSuchPaddingException e) {
			logger.error("加密公钥非法,请检查：{}", ExceptionUtils.getStackTrace(e));
		} catch (InvalidKeyException e) {
			logger.error("NoSuchPaddingException异常，请检查：{}", ExceptionUtils.getStackTrace(e));
		} catch (IllegalBlockSizeException e) {
			logger.error("明文长度非法：{}", ExceptionUtils.getStackTrace(e));
		} catch (BadPaddingException e) {
			logger.error("明文数据已损坏：{}", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * 公钥解密过程
	 * 
	 * @param publicKey  公钥
	 * @param cipherData 密文数据
	 * @return 明文
	 */
	public static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData) {
		if (publicKey == null) {
			logger.error("公钥不能为null");
			return null;
		}
		try {
			// 使用默认RSA
			Cipher cipher = Cipher.getInstance("RSA");
			// cipher= Cipher.getInstance("RSA", new BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			byte[] output = cipher.doFinal(cipherData);
			return output;
		} catch (NoSuchAlgorithmException e) {
			logger.error("无此加密算法：{}", ExceptionUtils.getStackTrace(e));
		} catch (NoSuchPaddingException e) {
			logger.error("加密公钥非法,请检查：{}", ExceptionUtils.getStackTrace(e));
		} catch (InvalidKeyException e) {
			logger.error("NoSuchPaddingException异常，请检查：{}", ExceptionUtils.getStackTrace(e));
		} catch (IllegalBlockSizeException e) {
			logger.error("明文长度非法：{}", ExceptionUtils.getStackTrace(e));
		} catch (BadPaddingException e) {
			logger.error("明文数据已损坏：{}", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * 加签
	 * 
	 * @param privateKeyStr 私钥
	 * @param content       需要签名的内容
	 * @return 明文
	 */
	public static String Sign(String privateKeyStr, String content) {

		try {
			RSAPrivateKey privateKey = RSAUtils.getRSAPrivateKey(privateKeyStr);
			Signature signature;
			signature = Signature.getInstance("Sha1WithRSA");
			signature.initSign(privateKey);
			signature.update(content.getBytes("UTF-8"));
			byte[] signed = signature.sign();
			return Base64.encodeBase64String(signed);

		} catch (NoSuchAlgorithmException e) {
			logger.error("无此加密算法：{}", ExceptionUtils.getStackTrace(e));
		} catch (SignatureException e) {
			logger.error("签名异常：{}", ExceptionUtils.getStackTrace(e));
		} catch (UnsupportedEncodingException e) {
			logger.error("不支持的编码异常：{}", ExceptionUtils.getStackTrace(e));
		} catch (InvalidKeyException e) {
			logger.error("无效的key异常：{}", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * 验证签名
	 * 
	 * @param publicKeyStr 验签
	 * @param content      需要签名的内容
	 * @param signed       签名内容
	 * @return 明文
	 */
	public static boolean verfySign(String publicKeyStr, String content, String signed) {

		try {
			RSAPublicKey publicKey = RSAUtils.getRSAPublicKey(publicKeyStr);
			Signature signature = Signature.getInstance("Sha1WithRSA");
			signature.initVerify(publicKey);
			signature.update(content.getBytes("UTF-8"));
			byte[] bytes = Base64.decodeBase64(signed);
			return signature.verify(bytes);

		} catch (NoSuchAlgorithmException e) {
			logger.error("无此加密算法：{}", ExceptionUtils.getStackTrace(e));
		} catch (SignatureException e) {
			logger.error("签名异常：{}", ExceptionUtils.getStackTrace(e));
		} catch (UnsupportedEncodingException e) {
			logger.error("不支持的编码异常：{}", ExceptionUtils.getStackTrace(e));
		} catch (InvalidKeyException e) {
			logger.error("无效的key异常：{}", ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

}
