package com.api.central.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDecrypt 
{
	private static SecretKeySpec secretKey ;
    private static byte[] key ;
    
    public static void setKey(String myKey){
        
   
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            secretKey = new SecretKeySpec(key, "AES");
            
            
        } catch (NoSuchAlgorithmException e) {
          
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
          
            e.printStackTrace();
        }
        
              
    
    }
    
   
    public static String encrypt(String strToEncrypt)
    {
        String s2=null;
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        
         
            String s=Base64.getEncoder().encodeToString((cipher.doFinal(strToEncrypt.getBytes("UTF-8"))));
            s2= Base64.getEncoder().encodeToString(s.getBytes("utf-8"));
        
        }
        catch (Exception e)
        {
           
            System.out.println("Error while encrypting: "+e.toString());
        }
        return s2;
    }
    public static String decrypt(String strToDecrypt)
    {
        String s=null;
        try
        {
            byte[] base64decodedBytes = Base64.getDecoder().decode(strToDecrypt);
            String aa= new String(base64decodedBytes, "utf-8");
            
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
           
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            s=new String(cipher.doFinal(Base64.getDecoder().decode((aa))));
            
        }
        catch (Exception e)
        {
         
            System.out.println("Error while decrypting: "+e.toString());
        }
        return s;
    }
    public static String EncryptCertiNoUtn(String Cno,String Utn)
    {
                final String strToEncrypt = Cno+"::"+Utn;
                final String strPssword = "BSOLIRI";
                EncryptDecrypt.setKey(strPssword);
                return EncryptDecrypt.encrypt(strToEncrypt.trim());
                               
        
    }
    public static String DecryptQid(String strToDecrypt)
    {
                final String strPssword = "BSOLIRI";
                EncryptDecrypt.setKey(strPssword);
                return EncryptDecrypt.decrypt(strToDecrypt.trim());    
    }


}
