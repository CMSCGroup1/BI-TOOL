import java.sql.*;

public class TestConnection {

	public static void main(String[] args){
		
		try{
			// Establish connection to database
			Connection conn = DriverManager.getConnection("jdbc:mysql://cmsc495.cqlogjzddetg.us-east-1.rds.amazonaws.com:3306/CMSC495", "administrator", "administrator");
			
			// Create SQL statement
			Statement stmt = conn.createStatement();
			
			// Execute SQL query
			ResultSet results = stmt.executeQuery("select * from employees");
			// Process result set
			while (results.next()){
				System.out.println(results.getString("employee_id")+ ": " + results.getString("employee_name"));
			}
			
			
		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
