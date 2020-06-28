package com.jdc.app.util;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
	
	private static Properties prop;
	private static String url;
	private static String user;
	private static String password;
	
	static {
		try {
			
			prop = new Properties();
			prop.load(new FileInputStream("application.properties"));
			url = prop.getProperty("db.url");
			user = prop.getProperty("db.user");
			password = prop.getProperty("db.password");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url,user,password);
	}

}
