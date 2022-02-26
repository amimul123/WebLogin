package com.ami.main;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebLogin {
	private HttpsURLConnection conn;
	
	public void sendPostRequest(String url, String postParams) throws Exception {

		URL obj = new URL(url);
		conn = (HttpsURLConnection) obj.openConnection();

		// header fields as a browser
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Origin", Header.origin);
		conn.setRequestProperty("User-Agent", Header.userAgent);
		conn.setRequestProperty("Accept", Header.accept);
		conn.setRequestProperty("Accept-Language", Header.acceptLanguage);
		for (String cookie : Header.getCookies()) {
			conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
		}
		conn.setRequestProperty("Connection", Header.connection);
		conn.setRequestProperty("Referer", Header.referer);
		conn.setRequestProperty("Content-Type", Header.contentType);
		conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

		conn.setDoOutput(true);
		conn.setDoInput(true);

		// post request
		DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
		outputStream.writeBytes(postParams);
		outputStream.flush();
		outputStream.close();

		// System.out.println("POST request URL : " + url);
		// System.out.println("Post parameters : " + postParams);
		// System.out.println("Response Code : " + conn.getResponseCode());

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		StringBuffer response = new StringBuffer();

		while ((line = in.readLine()) != null) {
			response.append(line);
			System.out.println(line);
		}
		in.close();
		//System.out.println(response.toString());

	}

	public String getContent(String url) throws Exception {

		URL obj = new URL(url);
		conn = (HttpsURLConnection) obj.openConnection();
		
		// header fields as a browser
		// default request is GET
		conn.setRequestMethod("GET");
		conn.setUseCaches(false);
		conn.setRequestProperty("User-Agent", Header.userAgent);
		conn.setRequestProperty("Accept", Header.accept);
		conn.setRequestProperty("Accept-Language", Header.acceptLanguage);
		if (Header.getCookies() != null) {
			for (String cookie : Header.getCookies()) {
				conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}
		}
		
		// System.out.println("GET request URL : " + url);
		// System.out.println("Response Code : " + conn.getResponseCode());

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		StringBuffer pageContent = new StringBuffer();

		while ((line = in.readLine()) != null) {
			pageContent.append(line);
		}
		in.close();

		// fetch the cookies
		Header.setCookies(conn.getHeaderFields().get("Set-Cookie"));

		return pageContent.toString();

	}

	public String getParameters(String html, String username, String password) throws UnsupportedEncodingException {

		List<String> paramList = new ArrayList<String>();

		Document doc = Jsoup.parse(html);

		// extracting HTML form's input fields like user, pass
		Element loginForm = doc.getElementById("login_form");
		Elements inputElements = loginForm.getElementsByTag("input");
		for (Element inputElement : inputElements) {
			String key = inputElement.attr("name");
			String value = inputElement.attr("value");
			if (key.equals("user"))
				value = username;
			else if (key.equals("pass"))
				value = password;
			paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
		}
		
		// manually fill up  the parameters
		// paramList.add("user" + "=" + URLEncoder.encode(username, "UTF-8"));
		// paramList.add("pass" + "=" + URLEncoder.encode(password, "UTF-8"));
		
		// generating parameter for GET and POST request 
		StringBuilder queryString = new StringBuilder();
		for (String param : paramList) {
			if (queryString.length() == 0) {
				queryString.append(param);
			} else {
				queryString.append("&" + param);
			}
		}
		return queryString.toString();
	}


}
