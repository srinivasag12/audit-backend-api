package com.api.central.security;


public class EncodeDecode 
{
	 public static String EncodeCertiNoUtn(String Cno, String Utn) throws Throwable {
		final String strToEncrypt = Utn + "::" + Cno;
		//String base64encodedString = Base64.getUrlEncoder().encodeToString(strToEncrypt.getBytes("utf-8"));
		String base64encodedString =AesUtil.encryptQid(strToEncrypt);
		return base64encodedString;
	}

	public static String DecodeQid(String strToDecrypt) throws Throwable {
		//byte[] base64decodedBytes = Base64.getDecoder().decode(strToDecrypt);
		//String base64decodeString = new String(base64decodedBytes, "utf-8");
		String base64decodeString = AesUtil.decryptQid(strToDecrypt);
		return base64decodeString;
	}


}
