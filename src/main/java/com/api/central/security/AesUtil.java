package com.api.central.security;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class AesUtil {
	private final int keySize;
	private final int iterationCount;
	private final Cipher cipher;
	// Key url:http://onlinemd5.com/, SHA-256, bsoliriAESEncrypt
	private final static String Userkey = "45722925F23B89F6C772B5DD44C1B41A3A1054A9DD64B596120543FB9E2FAE92";

	private static String toHex(byte[] array) throws NoSuchAlgorithmException {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}

	private static byte[] getSalt() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt;
	}

	public AesUtil(int keySize, int iterationCount) {
		this.keySize = keySize;
		this.iterationCount = iterationCount;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw fail(e);
		}
	}

	private SecretKey generateKey(String salt, String passphrase) {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), iterationCount, keySize);
			SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
			return key;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			return null;
		}
	}

	private byte[] doFinal(int mode, SecretKey key, String iv, byte[] bytes) {
		try {
			cipher.init(mode, key, new IvParameterSpec(hex(iv)));
			return cipher.doFinal(bytes);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException
				| BadPaddingException e) {
			return null;
		}
	}

	public static byte[] base64(String str) {
		return Base64.decodeBase64(str);
	}

	public static byte[] hex(String str) {
		try {
			return Hex.decodeHex(str.toCharArray());
		} catch (DecoderException e) {
			throw new IllegalStateException(e);
		}
	}

	private IllegalStateException fail(Exception e) {
		return null;
	}

	public String encrypt(String strToEncrypt) {
		try {
			byte[] salt = getSalt();
			String finalSalt = toHex(salt);
			String finalIv = toHex(salt);
			SecretKey key = generateKey(finalSalt, Userkey);
			byte[] encryptedVal = doFinal(Cipher.ENCRYPT_MODE, key, finalIv, strToEncrypt.getBytes());
			String base64EncodedEncryptedData = new String(Base64.encodeBase64(encryptedVal), "UTF-8");
			String FinalVal = finalIv + "::" + finalSalt + "::" + base64EncodedEncryptedData;
			String base64encodedString = java.util.Base64.getUrlEncoder().encodeToString(FinalVal.getBytes("utf-8"));
			return base64encodedString;
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public String decrypt(String salt, String iv, String passphrase, String strToDecrypt) {
		try {
			SecretKey key = generateKey(salt, passphrase);
			byte[] decryptedVal = doFinal(Cipher.DECRYPT_MODE, key, iv, base64(strToDecrypt));
			return new String(decryptedVal, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static String Decrypt(String str) {
		AesUtil aesUtil = new AesUtil(128, 1000);
		String decrypted = new String(java.util.Base64.getDecoder().decode(str));
		String originalVal = aesUtil.decrypt(decrypted.split("::")[1], decrypted.split("::")[0], Userkey,
				decrypted.split("::")[2]);
		return originalVal;
	}

	public static String Encrypt(String str)  {
		AesUtil aesUtil = new AesUtil(128, 1000);
		return aesUtil.encrypt(str);
	} 
	
	   public static String encryptQid(String strToEncrypt) throws Throwable
		{		
				String userKey="bsol1234";
				String CIPHER = "DES/CBC/PKCS5Padding";
				byte[] key = userKey.getBytes();
				byte[] value =strToEncrypt.getBytes();
				String INIT_VECTOR ="TY54ABCX";
				SecretKeySpec secretKeySpec=new SecretKeySpec(key, "DES");
				IvParameterSpec ivParameterSpec=null;
				try {
					ivParameterSpec=new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				Cipher decryptCipher=null;
				try {
					decryptCipher= Cipher.getInstance(CIPHER);
					decryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec,ivParameterSpec);
			    
				} catch (Throwable  e) {
					e.printStackTrace();
				}
			  String result="";
			  try {
				  byte[] b=decryptCipher.doFinal(value);
			      result = new String(Base64.encodeBase64(b), "UTF-8");
			  } catch (IllegalBlockSizeException e) {
			    e.printStackTrace();
			  } catch (BadPaddingException e) {
			    e.printStackTrace();
			  }
			  return result;
		}
		
		 public static String decryptQid(String toDecrypt) throws Throwable {
			  String key="bsol1234";
		      SecureRandom sr = new SecureRandom(key.getBytes());
		      KeyGenerator kg = KeyGenerator.getInstance("DES");
		      kg.init(sr);
		      byte[] bb=key.getBytes();
		      String INIT_VECTOR ="TY54ABCX";
				SecretKeySpec secretKeySpec=new SecretKeySpec(bb, "DES");
				IvParameterSpec ivParameterSpec=null;
				try {
					ivParameterSpec=new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				
		      Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec,ivParameterSpec);
		      byte[] s=Base64.decodeBase64(toDecrypt);
		      byte[] decrypted = cipher.doFinal(s);
		  
		      return new String(decrypted);
		   }
//	   public static String encryptQid(String toEncrypt) throws Exception {
//
//			   byte[] key = DatatypeConverter.parseHexBinary("6d7973656372657470617373776f7264");
//
//			   SecureRandom sr = new SecureRandom(key);
//			   
//			  
//		      KeyGenerator kg = KeyGenerator.getInstance("DES");
//		      kg.init(sr);
//		      SecretKey sk = kg.generateKey();
//		  
//		      Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
//		  
//		      SecureRandom secureRandom = new SecureRandom();
//		      byte[] ivspec = new byte[cipher.getBlockSize()];
//		      secureRandom.nextBytes(ivspec);
//		      iv = new IvParameterSpec(ivspec);
//		  
//		      cipher.init(Cipher.ENCRYPT_MODE, sk);
//		      byte[] value =
//		 		     DatatypeConverter.parseHexBinary(toEncrypt);
//		      byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());
//		      System.out.println(toHex(cipher.doFinal(value)));
//		      String base64EncodedEncryptedData = new String(Base64.encodeBase64URLSafe(encrypted), "UTF-8");
//		      return base64EncodedEncryptedData;
//		   }
//	  
//	   public static String decryptQid(String toDecrypt) throws Exception {
//	      // create a binary key from the argument key (seed)
//		   String key="bsoliri";
//	      SecureRandom sr = new SecureRandom(key.getBytes());
//	      KeyGenerator kg = KeyGenerator.getInstance("DES");
//	      kg.init(sr);
//	      SecretKey sk = kg.generateKey();
//	  
//	      // do the decryption with that key
//	      Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
//	      cipher.init(Cipher.DECRYPT_MODE, sk, iv);
//	      byte[] s=Base64.decodeBase64(toDecrypt);
//	      byte[] decrypted = cipher.doFinal(s);
//	  
//	      return new String(decrypted);
//	   }
	  
	

}
