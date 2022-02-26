package com.ami.main;

import java.util.List;

public final class Header {
	public static String userAgent = "\"Mozilla/5.0\""; 
	public static String accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"; 
	public static String acceptLanguage = "en-US,en;q=0.5"; 
	public static String connection = "keep-alive"; 
	public static String contentType = "application/x-www-form-urlencoded"; 
	public static String origin = "your request header origin";
	public static String referer = "your request header referer";
	private static List<String> cookies;
	
	public static List<String> getCookies() {
		return cookies;
	}
	public static void setCookies(List<String> cookies) {
		Header.cookies = cookies;
	}
	

}
