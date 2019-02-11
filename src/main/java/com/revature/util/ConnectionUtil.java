package com.revature.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionUtil {
	private static Connection connectionservice = null;
	private void ConnectionUtil() {}
	public static Connection getConnection() {
			if (ConnectionUtil.connectionservice != null) {
				return ConnectionUtil.connectionservice;
			}
				InputStream in = null;

			try {
				Properties property = new Properties();
				in = new FileInputStream(
						"C:\\Users\\icyro\\OneDrive\\Documents\\Revature\\Java\\Project_0\\src\\main\\resources\\connections.properties");
				property.load(in);

				Class.forName("oracle.jdbc.driver.OracleDriver");
				Connection connect = null;

				String endpoint = property.getProperty("jdbc.url");
				String username = property.getProperty("jdbc.username");
				String password = property.getProperty("jdbc.password");

				connect = DriverManager.getConnection(endpoint, username, password);
				connectionservice = connect;
				return connectionservice;
			} catch (Exception e) {
				System.out.println("Unable to obtain a connection");
			} finally {
				try {
					in.close();
				} catch (IOException e) {

				}
			}

			return null;
		} 
}
