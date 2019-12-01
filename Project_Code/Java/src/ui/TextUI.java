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

	private void addNewBlock() {
		String startTime = null;
		String endTime = null;
		String date = null;
		System.out.print("What date do you want to add hours on (enter as yyyy-mm-dd):");
		date = input.nextLine();
		System.out.print("When do you want the block to start (enter as 24hour time):");
		startTime = input.nextLine();
		System.out.print("When do you want the block to end (enter as 24hour time):");
		endTime = input.nextLine();
		String query = "INSERT INTO appointments (startTime, endTime, offeredBy, takenBy, course)"
				+ String.format("VALUES ('%s', '%s', %d, NULL, NULL);", date + " " + startTime, date + " " + endTime, ID);
		DB.updateDatabase(query);
	}

	private void displayAvalibleAppointments(String courseCode) {
		String query = String
				.format("SELECT startTime, endTime FROM appointments WHERE (offeredBy = %d AND takenBy IS NULL);", ID);
		DB.runQuery(query);
	}

	private void displayTakenAppointments() {
		String query = "SELECT CONCAT(people.firstName, \" \", people.lastName) as student, course, startTime, endTime FROM appointments INNER JOIN people ON people.id = takenBy WHERE offeredby = "
				+ Integer.toString(ID);
		DB.printResultSet(DB.runQuery(query));
	}

	private void removeBlock() {
		String query = "SELECT ID, startTime, endTime FROM appointments WHERE offeredBy = " + Integer.toString(ID) + " AND takenBy IS NULL";
		int deadID = -1;
		DB.printResultSet(DB.runQuery(query));
		System.out.print("Enter the ID of the block you want to remove: ");
		deadID = input.nextInt();
		query = "DELETE FROM appointments WHERE ID = " + Integer.toString(deadID);
		DB.updateDatabase(query);
		input.nextLine();
	}

	private void displayMyAppointments() {
		String query = "SELECT startTime, endTime, course, CONCAT(people.firstName, \" \", people.lastName) as tutor FROM"
				+ " appointments INNER JOIN people ON people.ID = offeredBy AND takenBy = " + Integer.toString(ID)
				+ ";";
		DB.printResultSet(DB.runQuery(query));
	}

	private void newAppoitnment() {
		String search = null;
		String query = null;
		ResultSet rs = null;
		System.out.print("Do you want to search based on course(c) or based on Tutor(t):");
		search = input.nextLine();
		if(search.equals("t") || search.equals("T")) {
			System.out.print("Enter the tutor's first name
			query = "SELECT startTime, endTime, CONCAT(people.firstName, \" \", people.lastName) as tutor FROM appointments INNER JOIN people ON people.ID = appointments.offeredBy WHERE takenBy IS NULL AND people.firstName = \"Mark\" AND people.lastName = \"Burrell\";";
			rs = DB.runQuery(query);
			DB.printResultSet(rs);
		}
	}

	private void editAppointments() {
		String op = null;
		while (true) {
			System.out.print("Do you want to add(a), remove(r), or go back(b): ");
			op = input.nextLine();
			switch (op) {
			case "a":
			case "A":
				addNewBlock();
				break;
			case "r":
			case "R":
				removeBlock();
				break;
			case "b":
			case "B":
				return;
			default:
				System.out.println("Im sorry, that was not a valid input");

			}
		}
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
				System.out.print(
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
					editAppointments();
					break;
				case "a":
				case "A":
					newAppoitnment();
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
