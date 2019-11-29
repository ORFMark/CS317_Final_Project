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
			Boolean empty = !result.next();
			if (result != null && empty == false) {
				isTutor = result.getBoolean("isTutor");
				System.out.println("Welcome " + result.getString("firstName") + " " + result.getString("lastName"));
				return true;
			} else {
				System.out.println("RS: " + Boolean.toString(result != null) + "Empty: " + Boolean.toString(empty));
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

	private void displayMyAppointments() {

	}

	private void newAppoitnment() {

	}

	public void driver() {
		String userIn = null;
		boolean done = false;
		System.out.print("Please enter your System ID: ");
		ID = input.nextInt();
		if (!auth()) {
			System.out.println("Im sorry, that ID is not in the database");
			return;
		}
		userIn = input.nextLine();
		if (isTutor) {
			while (!done) {
				System.out.println(
						"Do you want to to view taken appointments(t), view your appointments(v), edit appointment hours (e), scedchule an appointment(a), or quit(q): ");
				userIn = input.nextLine();
				switch (userIn) {
				case "t":
				case "T":
					displayTakenAppointments();
					break;
				case "v":
				case "V":
					displayMyAppointments();
					break;
				case "e":
				case "E":
					// TODO: edit interface
					break;
				case "a":
				case "A":
					// TODO: new appointment interface
					break;
				case "q":
				case "Q":
					done = true;
					System.out.println("Thanks for visting!");
					break;
				default:
					System.out.println("Im sorry, that was not a valid input");

				}
			}

		} else {
			while (!done) {
				System.out.println(
						"Do you want to view your appointments(v),  scedchule an appointment(a), or quit(q): ");
				userIn = input.nextLine();
				switch (userIn) {
				case "v":
				case "V":
					displayMyAppointments();
					break;
				case "a":
				case "A":
					// TODO: new appointment interface
					break;
				case "q":
				case "Q":
					done = true;
					System.out.println("Thanks for visting!");
					break;
				default:
					System.out.println("Im sorry, that was not a valid input");

				}
			}
		}

	}

}
