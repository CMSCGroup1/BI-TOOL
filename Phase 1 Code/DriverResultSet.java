import java.sql.ResultSet;
import java.sql.SQLException;

// Shows how to use the DataRetriever class. The examples may be passing in "customers" for the table reference.
public class DriverResultSet {
	//The following variables will probably not be static in the actual program
	static ResultSet results;
	static int columnCount;
	static String tableName="customers";	// The value this variable holds will probably GUI selected by user
	static String columnName="customer_name";	// The value this variable holds will probably GUI selected by user
	
	public static void main(String[] args){
		DataRetriever test = new DataRetriever();
		
		//Table list example
		results = test.getTableList();
		try {
			while (results.next()){
				System.out.println(results.getString("table_name"));		//table_name is the column name so leave as is
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("End getTableList example \n\n\n\n");
		// End getTableList example
		
		
		
		/* Table Data View example
		 * This example requires a call to method getColumnCount() before running the actual query. 
		 * The number of columns retrieved from this method will allow all the column data in the table to be presented.
		 * For formatting, adding column names in the first line by using method getColumnName().
		 */
		columnCount = test.getColumnCount(tableName);
		
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
					System.out.print(results.getString(i) + "\t");	//Separate each column with tab
				}
				System.out.println();		// Separate each row with new line
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("End getTableData example \n\n\n\n");
		//End getTableData example
		
		
		
		/* Order ascending example. The example is ordering ascending on column customer_name. 
		 * For descending, use getDescendingOrder().
		 * Requires uses getColumnCount() to display all the data. Since this method has already been called, and int columnCount has been set, it will not be called again.
		*/
		results = test.getAscendingOrder(tableName, columnName);
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
		System.out.println("End getAscendingOrder example \n\n\n\n");
		//End getAscendingOrder example
	}
}
