package com.lifegame.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utitls {
	public static String stringToMD5(String password){
		//把密码进行MD5加密
		byte[] secretBytes=null;
		//调用MessaheDigest工具实例化secreBytes
		try{
			secretBytes =MessageDigest.getInstance("md5").digest(password.getBytes());
		}catch(NoSuchAlgorithmException e){
			throw new RuntimeException("没有这个md5算法");
		}
		//加密到md5code并且返回
		String md5code =new BigInteger(1,secretBytes).toString(16);
		for(int i=0;i<32-md5code.length();i++){
			md5code="0"+md5code;
		}
		return md5code;
	}
}
