package database;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConnector {
	private Connection database;
	private Statement smt;
	private Properties config;
	public DatabaseConnector() {
		config = new Properties();
		try {
			FileInputStream file = new FileInputStream("config.properties");
			config.load(file);
		} catch (Exception e) {
			System.out.println("Failed to load config file!");
			e.printStackTrace();
		}
		String connectionUrl = "jdbc:mysql://prclab1.erau.edu:3306/" + config.getProperty("DB");
		try {
			database = DriverManager.getConnection(connectionUrl, config.getProperty("userName"), config.getProperty("password"));
			if(database != null) {
				System.out.println("Successfully connected to prclab");
			}
		} catch(Exception e) {
			System.out.println("COULD NOT CONNECT TO PRCLAB");
			e.printStackTrace();
		}
	}

	public DatabaseConnector(String host, String database, String user, String password) { 
		String connectionURL = "jdbc:mysql://" + host + "/" + database;
		System.out.println("Attempting to connect to database " + database + "at host " + host);
		try {
			this.database = DriverManager.getConnection(connectionURL, user, password);
		} catch (SQLException e) {
			System.out.println("Connectioned Failed");
			database = null;
			e.printStackTrace();
		}
	}

	public ResultSet runQuery(String query) {
		ResultSet rs = null;
		try {
			if(database != null && !database.isClosed()) {
				smt = database.createStatement();
				rs = smt.executeQuery(query);
			} 
		} catch (Exception e) {
			System.out.print("Failed to execute querey");
			e.printStackTrace();
			rs = null;
		}
		return rs;
	}	
	public void close() {
		if(smt != null) {
			try {
				smt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(database != null) {
			try {
			  database.close();
			}  catch (SQLException e) { 
				e.printStackTrace();
			}
		}
		
	}
	

}
