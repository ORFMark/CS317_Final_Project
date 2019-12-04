package ui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Scanner;
import database.DatabaseConnector;

public class TextUI {
	Scanner input;
	int personID;
	DatabaseConnector DB;
	boolean isTutor;

	public TextUI(Scanner in, DatabaseConnector DB) {
		input = in;
		this.DB = DB;
	}

	private boolean auth() {
		String query = String.format("SELECT * from people WHERE ID = %d;", personID);
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
		startTime = startTime + ":00";
		System.out.print("When do you want the block to end (enter as 24hour time):");
		endTime = input.nextLine();
		endTime = endTime + ":00";
		String query = "INSERT INTO appointments (startTime, endTime, offeredBy, takenBy, course)" + String
				.format("VALUES ('%s', '%s', %d, NULL, NULL);", date + " " + startTime, date + " " + endTime, personID);
		DB.updateDatabase(query);
	}

	private void displayAvalibleAppointments(String courseCode) {
		String query = String.format(
				"SELECT startTime, endTime FROM appointments WHERE (offeredBy = %d AND takenBy IS NULL);", personID);
		DB.runQuery(query);
	}

	private void displayTakenAppointments() {
		String query = "SELECT CONCAT(people.firstName, \" \", people.lastName) as student, course, startTime, endTime FROM appointments INNER JOIN people ON people.id = takenBy WHERE offeredby = "
				+ Integer.toString(personID);
		DB.printResultSet(DB.runQuery(query));
	}

	private void removeBlock() {
		String query = "SELECT ID, startTime, endTime FROM appointments WHERE offeredBy = " + Integer.toString(personID)
				+ " AND takenBy IS NULL";
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
				+ " appointments INNER JOIN people ON people.ID = offeredBy AND takenBy = " + Integer.toString(personID)
				+ ";";
		DB.printResultSet(DB.runQuery(query));
	}

	private void newAppoitnment() {
		String search = null;
		String query = null;
		ResultSet rs = null;
		String course = null;
		boolean done = false;
		while (!done) {
			System.out.print("Do you want to search based on course(c) or based on Tutor(t):");
			search = input.nextLine();
			if (search.equals("t") || search.equals("T")) {
				System.out.print("Enter the tutor's first name: ");
				String first = input.next();
				System.out.print("Enter the tutor's last name: ");
				String last = input.next();
				query = "SELECT appointments.ID as apptID, startTime, endTime, CONCAT(people.firstName, \" \", people.lastName) as tutor FROM appointments INNER JOIN people ON "
						+ "people.ID = appointments.offeredBy WHERE takenBy IS " + "NULL AND people.firstName = \""
						+ first + "\" AND people.lastName = \"" + last + "\";";
				rs = DB.runQuery(query);
				DB.printResultSet(rs);
				search = input.nextLine();
			} else if (search.equals("c") || search.equals("C")) {
				System.out.print("Enter the course code with no spaces(EX. CEC220): ");
				course = input.nextLine();
				query = String.format(
						"SELECT appointments.ID as apptID, CONCAT(people.firstName, \" \", people.lastName) as Tutor, people.bio, appointments.startTime, appointments.endTime  "
								+ "FROM appointments INNER JOIN people ON offeredBy = people.id INNER JOIN tutored on tutored.tutorID = people.id "
								+ " INNER JOIN courses ON courses.courseCode = tutored.courseCode WHERE takenBy IS NULL AND courses.courseCode = \"%s\";",
						course);
				rs = DB.runQuery(query);
				DB.printResultSet(rs);
			} else {
				System.out.println("Im sorry, that was invalid input");
				continue;
			}
			System.out.print("Is there a time that works for you in these options (Y/N): ");
			search = input.nextLine();
			done = search.equals("y") || search.equals("Y");
			if (search.equals("n") || search.equals("N")) {
				System.out.print("Do you want to retry the search (Y/N): ");
				search = input.nextLine();
				if (search.equals("n") || search.equals("N")) {
					return;
				}
			}
		}
		done = false;
		while (!done) {
			System.out.print("What is the ID of the block you want to make the appointment in: ");
			int ID = input.nextInt();
			input.nextLine();
			System.out.print("What date do you want to add hours on (enter as yyyy-mm-dd):");
			String date = input.nextLine();
			System.out.print("When do you want the block to start (enter as 24hour time):");
			String startTime = input.nextLine();
			System.out.print("When do you want the block to end (enter as 24hour time):");
			String endTime = input.nextLine();
			startTime = date + " " + startTime + ":00";
			endTime = date + " " + endTime + ":00";
			query = String.format(
					"SELECT * FROM appointments WHERE ID = %d AND '%s:' between startTime and endtime AND '%s' between startTime and endTime",
					ID, startTime, endTime);
			rs = DB.runQuery(query);
			try {
				if (rs.next()) {
					String update = null;
					DB.getDatabase().setAutoCommit(false);
					if (!startTime.equals(rs.getString("startTime"))) {
						update = String.format(
								"INSERT INTO appointments (startTime, endTime, offeredBy, takenBy, course) VALUES ('%s', '%s', %d, NULL, NULL);",
								rs.getString("startTime"), startTime, rs.getInt("offeredBy"));
						DB.updateDatabase(update);
					}
					update = String.format(
							"INSERT INTO appointments (startTime, endTime, offeredBy, takenBy, course) VALUES ('%s', '%s', %d, %d, '%s');",
							startTime, endTime, rs.getInt("offeredBy"), personID, course);
					DB.updateDatabase(update);
					if (!endTime.equals(rs.getString("endTime"))) {
						update = String.format(
								"INSERT INTO appointments (startTime, endTime, offeredBy, takenBy, course) VALUES ('%s', '%s', %d, NULL, NULL);",
								endTime, rs.getString("endTime"), rs.getInt("offeredBy"));
						DB.updateDatabase(update);
					}
					update = "DELETE FROM appointments WHERE ID = " +  rs.getInt("ID") + ";";
					DB.updateDatabase(update);
					DB.getDatabase().setAutoCommit(true);
					System.out.println("Successfully made appointment");
					return;
				}
				System.out.print("unable to make the appoinment");
				done = false;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.print("Something dident work");
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
		personID = input.nextInt();
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
		}

	}

}
