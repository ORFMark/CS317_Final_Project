import database.DatabaseConnector;

public class Main {
	public static void main(String[] args) {
		try {
			DatabaseConnector tutorDB = new DatabaseConnector();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("THINGS WENT WRONG");
		}
	}
}
