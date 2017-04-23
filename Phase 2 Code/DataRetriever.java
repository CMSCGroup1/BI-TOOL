/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.sql.*;
/*
 * This class will establish JDBC connection to the MySQL database. It contains methods that issue SQL statements to retrieve different types of data.
 * Phase 1: Contains methods getTableList(), getColumnName(), getColumnCount(), getTableData(), getAscendingOrder(), getDescendingOrder()
 * Phase 2: getTableData(),getAscendingOrder(), getDescendingOrder takes in startDate and endDate param
 * 			getTableList() does not show lookup table as a viewable option
 * 			Created new methods: getTotalCustomerNum(),getTotalEmployeeNum(), getTotalSales(), getRegionalTableName(), getRegionalManagerName(), getRegionalEmployeeCount(), getRegionalCustomerCount(), getRegionalTransactionCount(), getRegionalMaxTransactionAmount(), getRegionalMinTransactionAmount(), getRegionalAvgTransactionAmount(), getRegionalTotalTransactionAmount()
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
	
	// Get the list of existing tables from the database. 
	// Phase 2: Does not show lookup table as view option
	public ResultSet getTableList(){
		try {
			results = sqlStatement.executeQuery("select table_name FROM information_schema.tables where table_schema='CMSC495' and table_name<>'lookup'");
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
	public ResultSet getTableData(String tableName, String startDate, String endDate){
		try {
			if(tableName.startsWith("region")){
				results = sqlStatement.executeQuery("select * FROM " + tableName + " where transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "'");
			}
			else{
				results = sqlStatement.executeQuery("select * FROM " + tableName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	// Order the full table data in ascending order based on a column. Limit ordering on one column
	public ResultSet getAscendingOrder(String tableName, String columnName, String startDate, String endDate){
		try {
			if(tableName.startsWith("region")){
				results = sqlStatement.executeQuery("select * FROM " + tableName + " where transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "' order by " + columnName + " asc");
			}
			else{
				results = sqlStatement.executeQuery("select * FROM " + tableName + " order by " + columnName + " asc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	// Order the full table data in descending order based on a column. Limit ordering on one column
	public ResultSet getDescendingOrder(String tableName, String columnName, String startDate, String endDate){
		try {
			if(tableName.startsWith("region")){
				results = sqlStatement.executeQuery("select * FROM " + tableName + " where transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "' order by " + columnName + " desc");
			}
			else{
				results = sqlStatement.executeQuery("select * FROM " + tableName + " order by " + columnName + " desc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	
	// Get the total number of customers
	public int getTotalCustomerNum(String startDate, String endDate) throws SQLException{
		results = sqlStatement.executeQuery("select (select count(distinct customer_id) from region1_sales_2016 where transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "') + (select count(distinct customer_id) from region2_sales_2016 where transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "') + (select count(distinct customer_id) from region3_sales_2016 where transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "') as total_cust_count");
		results.absolute(1);	//focus on first sql returned row (there is only one row returned)
		return results.getInt("total_cust_count");
	}
	
	// Get the total number of employees
	public int getTotalEmployeeNum() throws SQLException{
		results = sqlStatement.executeQuery("select count(distinct employee_id) as employee_num from employees");
		results.absolute(1);	//focus on first sql returned row (there is only one row returned)
		return results.getInt("employee_num");
	}
	
	// Get the total company sales
	public int getTotalSales(String startDate, String endDate) throws SQLException{
		results = sqlStatement.executeQuery("select (select sum(amount) from region1_sales_2016 where transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "')+(select sum(amount) from region2_sales_2016 where transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "')+(select sum(amount) from region3_sales_2016 where transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "') as total_sales");
		results.absolute(1);	//focus on first sql returned row (there is only one row returned)
		return results.getInt("total_sales");
	}
	
	// Determine which regional sales table to look at
	private String getRegionalTableName(int regionNum){
		String regionalTableName="";
		if(regionNum==1){
			regionalTableName="region1_sales_2016";
		}
		else if(regionNum==2){
			regionalTableName="region2_sales_2016";
		}
		else{
			regionalTableName="region3_sales_2016";
		}
		return regionalTableName;
	}
	
	// Get Regional Manager name
	public String getRegionalManagerName(int regionNum) throws SQLException{
		results = sqlStatement.executeQuery("select employee_name as manager_name from employees e inner join regions r on r.manager_employee_id=e.employee_id where r.region_id=" + regionNum);
		results.absolute(1);		//focus on first sql returned row (there is only one row returned)
		return results.getString("manager_name");
	}
	
	// Get Regional Employee count
	public int getRegionalEmployeeCount(int regionNum) throws SQLException{
		results = sqlStatement.executeQuery("select count(distinct employee_id) as region_emp_count from " + getRegionalTableName(regionNum));
		results.absolute(1);		//focus on first sql returned row (there is only one row returned)
		return results.getInt("region_emp_count");
	}
	
	// Get Regional Customer count
	public int getRegionalCustomerCount(int regionNum, String startDate, String endDate) throws SQLException{
		results = sqlStatement.executeQuery("select count(distinct customer_id) as region_cust_count from " + getRegionalTableName(regionNum) + " where transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "'");
		results.absolute(1);		//focus on first sql returned row (there is only one row returned)
		return results.getInt("region_cust_count");
	}
	
	// Get Regional Transaction count
	public int getRegionalTransactionCount(int regionNum, String startDate, String endDate) throws SQLException{
		results = sqlStatement.executeQuery("select count(distinct transaction_id) as region_trans_count FROM " + getRegionalTableName(regionNum) + " where transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "'");
		results.absolute(1);		//focus on first sql returned row (there is only one row returned)
		return results.getInt("region_trans_count");
	}		
		
	// Get Regional Maximum Transaction Amount
	public int getRegionalMaxTransactionAmount(int regionNum, String startDate, String endDate) throws SQLException{
		results = sqlStatement.executeQuery("select max(amount) as region_max_amt FROM " + getRegionalTableName(regionNum) + " where transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "'");
		results.absolute(1);		//focus on first sql returned row (there is only one row returned)
		return results.getInt("region_max_amt");
	}			
	
	
	// Get Regional Minimum Transaction Amount
	public int getRegionalMinTransactionAmount(int regionNum, String startDate, String endDate) throws SQLException{
		results = sqlStatement.executeQuery("select min(amount) as region_min_amt FROM " + getRegionalTableName(regionNum) + " where transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "'");
		results.absolute(1);		//focus on first sql returned row (there is only one row returned)
		return results.getInt("region_min_amt");
	}	
	
	
	// Get Regional Average Transaction Amount
	public int getRegionalAvgTransactionAmount(int regionNum, String startDate, String endDate) throws SQLException{
		results = sqlStatement.executeQuery("select avg(amount) as region_avg_amt FROM  " + getRegionalTableName(regionNum) + " where transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "'");
		results.absolute(1);		//focus on first sql returned row (there is only one row returned)
		return results.getInt("region_avg_amt");
	}		
	
	// Get Regional Total Transaction Amount
	public int getRegionalTotalTransactionAmount(int regionNum, String startDate, String endDate) throws SQLException{
		results = sqlStatement.executeQuery("select sum(amount) as region_sum_amt FROM  " + getRegionalTableName(regionNum) + " where transaction_date>='" + startDate + "' and transaction_date<='" + endDate + "'");
		results.absolute(1);		//focus on first sql returned row (there is only one row returned)
		return results.getInt("region_sum_amt");
	}
        
        //Get the oldest transaction date in region 1
        public Date getOldestTransaction() throws SQLException{
                results = sqlStatement.executeQuery("select min(transaction_date) as EarliestDate from CMSC495.region1_sales_2016");
                results.absolute(1);
                return results.getDate("EarliestDate");
        }
	
        //Get the newest transaction date in region 1
        public Date getNewestTransaction() throws SQLException{
                results = sqlStatement.executeQuery("select max(transaction_date) as NewestDate from CMSC495.region1_sales_2016");
                results.absolute(1);
                return results.getDate("NewestDate");
        }
}
