import java.sql.*;
/*
 * This class will establish JDBC connection to the MySQL database. It contains methods that issue SQL statements to retrieve different types of data.
 * Contains methods getTableList(), getColumnName(), getColumnCount(), getTableData(), getAscendingOrder(), getDescendingOrder()
 */
public class DataRetriever {

	// Define variables required to establish the JDBC connection. All fields are immutable.
	private final String dbName = "CMSC495";
	private final String dbUserName = "administrator";
	private final String dbPassword = "administrator";
	private final String hostname = "cmsc495.cqlogjzddetg.us-east-1.rds.amazonaws.com";
	private final String port = "3306";
	private final String jdbcURL = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName;
	// Define other variables needed to run SQL queries
	Statement sqlStatement;
	ResultSet results;
	int columnCount;
	
	// The constructor will establish the JDBC connection and initialize the SQL query statement
	public DataRetriever(){
		try {
			Connection connect = DriverManager.getConnection(jdbcURL, dbUserName, dbPassword);
			sqlStatement = connect.createStatement();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Get the list of existing tables from the database
	public ResultSet getTableList(){
		try {
			results = sqlStatement.executeQuery("select table_name FROM information_schema.tables where table_schema='CMSC495'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	
	/* Get the name of columns of a table.
	 * Will be used to retrieve the list of columns of a table and as the header to format the data that has been retrieved
	 */
	public ResultSet getColumnName(String tableName){
		try {
			results = sqlStatement.executeQuery("select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = '" + tableName +"'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
	
	
	// Get the number of columns of a specified table
	public int getColumnCount(String tableName){
		try {
			results = sqlStatement.executeQuery("select * FROM " + tableName);
			columnCount = results.getMetaData().getColumnCount();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return columnCount;
	}
	
	
	// Get the data of a selected table
	public ResultSet getTableData(String tableName){
		try {
			results = sqlStatement.executeQuery("select * FROM " + tableName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	// Order the full table data in ascending order based on a column. Limit ordering on one column
	public ResultSet getAscendingOrder(String tableName, String columnName){
		try {
			results = sqlStatement.executeQuery("select * FROM " + tableName + " order by " + columnName + " asc");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	// Order the full table data in descending order based on a column. Limit ordering on one column
	public ResultSet getDescendingOrder(String tableName, String columnName){
		try {
			results = sqlStatement.executeQuery("select * FROM " + tableName + " order by " + columnName + " desc");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	
	
}
