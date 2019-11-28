import java.util.Scanner;

import ui.TextUI;

import database.DatabaseConnector;

public class Main {
	public static void main(String[] args) {
		try {
			DatabaseConnector tutorDB = new DatabaseConnector();
			tutorDB.close();
			Scanner scan = new Scanner(System.in);
			TextUI text = new TextUI(scan, tutorDB);
			text.driver();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("THINGS WENT WRONG");
		}
	
		
	}
}
