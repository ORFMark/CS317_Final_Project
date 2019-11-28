package ui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Scanner;
import database.DatabaseConnector;

public class TextUI {
	Scanner input;
	int ID;
	DatabaseConnector DB;
	boolean isTutor;

	public TextUI(Scanner in, DatabaseConnector DB) {
		input = in;
		this.DB = DB;
	}
	
	private boolean auth() {
		String query = String.format("SELECT * from people WHERE ID = %d;", ID);
		ResultSet result = DB.runQuery(query);
		try {
			boolean empty = !result.next();
			if (result != null && !empty) {
				isTutor = result.getBoolean("isTutor");
				return true;
			} else {
				System.out.println("RS: " + Boolean.toString(result != null) + " EMPTY: " + empty);
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void addNewBlock(LocalDateTime start, LocalDateTime end) {
		String query = String.format("INSERT INTO appointments(startTime, endTime, offerdBy) VALUES (&s, %s, %d);",
				start.toString(), end.toString(), ID);
		DB.runQuery(query);
	}

	private void displayAvalibleAppointments(String courseCode) {
		String query = String.format("SELECT startTime, endTime from appointments where offeredBy = %D", ID);
		DB.runQuery(query);
	}

	private void displayTakenAppointments() {

	}

	private void removeBlock(LocalDateTime start, LocalDateTime end) {

	}


	public void driver() {
		String userIn;
		System.out.print("Please enter your System ID");
		ID = input.nextInt();
		if (!auth()) {
			System.out.println("Im sorry, that ID is not in the database");
			return;
		}
		if (isTutor) {

		} else {

		}

	}

}
