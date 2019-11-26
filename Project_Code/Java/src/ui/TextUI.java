package ui;

import java.time.LocalDateTime;
import java.util.Scanner;
import database.DatabaseConnector;


public class TextUI {
	Scanner input;
	int ID;
	DatabaseConnector DB;
	private void addNewBlock(LocalDateTime start, LocalDateTime end) {
		String query = String.format("INSERT INTO appointments(startTime, endTime, offerdBy) VALUES (&s, %s, %d);", start.toString(), end.toString(), ID);
		DB.runQuery(query);
	}
	private void displayTakenAppointments() {
		
	}
	private void removeBlock(LocalDateTime start, LocalDateTime end) {
		
	}
	public TextUI(Scanner in, int ID, DatabaseConnector DB) {
		input = in;
		this.ID = ID;
		this.DB = DB;
	}
	public void driver() {
	
	}
	
	
}
