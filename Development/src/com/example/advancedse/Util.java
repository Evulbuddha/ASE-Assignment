package com.example.advancedse;

public class Util {

	public static String encodeEmail(String email){
		return email = email.replaceAll("@", "&#64;");
	}
	
	public static String decodeEmail(String email){
		return email = email.replaceAll("&#64;", "@");
	}
}
