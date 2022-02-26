package com.ami.main;

import java.net.CookieHandler;
import java.net.CookieManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HttpLoginApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(HttpLoginApplication.class, args);
		
		String formURL = "your login form url";
		String accountURL = "your login account url";
		String userName= "your username";
		String password = "your password";

		WebLogin webObj = new WebLogin();

		// turn on cookies
		CookieHandler.setDefault(new CookieManager());
		
		try {
			// send a GET request to get the Login form.
			String htmlLoginForm = webObj.getContent(formURL);
			// generating parameters based on the HTML login form
			String postReqParameters = webObj.getParameters(htmlLoginForm, userName, password);

			// send a post request to login to the application
			webObj.sendPostRequest(accountURL, postReqParameters);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
