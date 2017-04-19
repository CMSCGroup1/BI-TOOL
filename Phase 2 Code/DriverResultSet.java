import java.sql.ResultSet;
import java.sql.SQLException;

// Shows how to use the DataRetriever class. The examples may be passing in "customers" for the table reference.
// Phase 2: Added statistics functionality and filter by date functionality
public class DriverResultSet {
	//The following variables will probably not be static in the actual program
	static ResultSet results;
	static int columnCount;
	static String tableName="customers";	// The value this variable holds will probably GUI selected by user
	static String columnName="customer_name";	// The value this variable holds will probably GUI selected by user
	
	// NEW Variables for Phase 2. The startDate and endDate variables should be added to GUI.java
	static String startDate="2016-01-01";	//Set to default start date if user doesn't select a date
	static String endDate="2016-12-31";		//Set to default end date if user doesn't select a date
	static StringBuilder sb = new StringBuilder();		//Format output for stats
	
	static DataRetriever test = new DataRetriever();	// Initiation of class moved to class level compared to phase 1
	
	// I've commented out phase 1 examples. At the end you can find phase 2 examples
	public static void main(String[] args){
		//Table list example
		/*results = test.getTableList();
		try {
			while (results.next()){
				System.out.println(results.getString("table_name"));		//table_name is the column name so leave as is
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("End getTableList example \n\n\n\n");*/
		// End getTableList example

		
		/* Table Data View example
		 * This example requires a call to method getColumnCount() before running the actual query. 
		 * The number of columns retrieved from this method will allow all the column data in the table to be presented.
		 * For formatting, adding column names in the first line by using method getColumnName().
		 */
		/*columnCount = test.getColumnCount(tableName);
		
		results = test.getColumnName(tableName);
		// Print column names
		try {
			while (results.next()){
					System.out.print(results.getString("column_name") + "\t");	//Separate each column with tab. column_name is the column name so leave as is
			}
			System.out.println();		// new line
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Print actual data of the table
		results = test.getTableData(tableName);
		try {
			while (results.next()){
				for(int i=1; i <= columnCount; i++){
					//System.out.print(results.getString(i).trim() + "\t");	//Separate each column with tab
					//sb.append(String.format("| %-10s",results.getString(i)));
					System.out.printf(results.getString(i).trim(), "%-10s");
				}
				System.out.println();		// Separate each row with new line
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("End getTableData example \n\n\n\n");*/
		//End getTableData example
		

		/* Order ascending example. The example is ordering ascending on column customer_name. 
		 * For descending, use getDescendingOrder().
		 * Requires uses getColumnCount() to display all the data. Since this method has already been called, and int columnCount has been set, it will not be called again.
		*/
		/*results = test.getAscendingOrder(tableName, columnName);
		try {
			while (results.next()){
				for(int i=1; i <= columnCount; i++){
					System.out.print(results.getString(i) + "\t");	//Separate each column with tab
				}
				System.out.println();		// Separate each row with new line
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("End getAscendingOrder example \n\n\n\n");*/
		//End getAscendingOrder example
		
		
		// Phase 2: Example to show how to run the getStatistics() method. Basically just call getStatisics() method. The getStatistics() and getRegionalStats() method should be placed in the GUI class
		DriverResultSet obj = new DriverResultSet();
		try {
			obj.getStatistics();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// Collects statistics from Database, formats, and prints the result
	public void getStatistics() throws SQLException {
		int totalRegionCount=3;
		sb.append("Regional breakdown");
		sb.append("\n*************************************************\n");
		// Collect regional statistics for all regions in the company
		for(int regionNum=1; regionNum<=totalRegionCount; regionNum++){
			getRegionalStats(regionNum);	
		}

		// Collect total stats across all regions
		sb.append("Company breakdown");
		sb.append("\n*************************************************\n");
		sb.append("Employee Count:\t");
		sb.append(test.getTotalEmployeeNum());
		sb.append("\nCustomer Count:\t");
		sb.append(test.getTotalCustomerNum(startDate, endDate));
		sb.append("\nTotal Sales:\t$");
		sb.append(test.getTotalSales(startDate, endDate));
		
		System.out.println(sb);		//print the result
	}
	
	// Collect regional stats from database and format. Takes in the regional number as a param.
	private void getRegionalStats(int regionNum) throws SQLException{
		sb.append("Region " + regionNum);
		sb.append("\n-------------------------------------------------\n");
		sb.append("Regional Manager:\t");
		sb.append(test.getRegionalManagerName(regionNum));
		sb.append("\nNumber of Employees:\t");
		sb.append(test.getRegionalEmployeeCount(regionNum));
		sb.append("\nNumber of Customers:\t");
		sb.append(test.getRegionalCustomerCount(regionNum, startDate,endDate));
		sb.append("\nNumber of Transactions:\t");
		sb.append(test.getRegionalTransactionCount(regionNum, startDate,endDate));
		sb.append("\nMaximum Sale Price:\t$");
		sb.append(test.getRegionalMaxTransactionAmount(regionNum, startDate,endDate));
		sb.append("\nMinimum Sale Price:\t$");
		sb.append(test.getRegionalMinTransactionAmount(regionNum, startDate,endDate));
		sb.append("\nAverage Sale Price:\t$");
		sb.append(test.getRegionalAvgTransactionAmount(regionNum, startDate,endDate));
		sb.append("\nTotal Sale Amount:\t$");
		sb.append(test.getRegionalTotalTransactionAmount(regionNum, startDate,endDate));
		sb.append("\n\n");
	}
	

}
