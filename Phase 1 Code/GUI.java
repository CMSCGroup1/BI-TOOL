import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleGroup;


/**
 *
 * @author jay herford
 * modified by Thomas Barton
 */
public class GUI extends Application {
    
    //create the logger
    private static final Logger LOGGER = Logger.getLogger(GUI.class.getName());
    //enumerated value set to be TABLE by default, used by show data button
    private DisplayChoice choice = DisplayChoice.TABLE;
    //used to remember the table that is being selected from in from the columb choice
    private String currentTb = "";
    
    //DataRetriever object
    private static final DataRetriever retriever = new DataRetriever();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Business Intelligence Software");
        // create gridpane
        GridPane grid = new GridPane();
        // Align to Center
        grid.setAlignment(Pos.CENTER);
        // Set gap between the components
        grid.setHgap(10);
        grid.setVgap(10);

        // Display the welcome message in the scene
        Text scenetitle = new Text("Login to continue.");
        // Add text to grid 0,0 span 2 columns, 1 row
        grid.add(scenetitle, 0, 0, 2, 1);

        // Create Label for the user name
        Label userName = new Label("User Name:");
        // Add label to grid 0,1
        grid.add(userName, 0, 1);

        // Create Textfield to accept username
        TextField userTextField = new TextField();
        // Add textfield to grid 1,1
        grid.add(userTextField, 1, 1);

        // Create Label for password
        Label pw = new Label("Password:");
        // Add label to grid 0,2
        grid.add(pw, 0, 2);

        // Create Passwordfield for password
        PasswordField pwBox = new PasswordField();
        // Add Password field to grid 1,2
        grid.add(pwBox, 1, 2);

        // Create Login Button
        Button btn = new Button("Login");
        // Add button to grid 0,4
        grid.add(btn, 0, 4);
        
        //create cancel button which will clear fields
        Button btn2 = new Button("Cancel");
        //add button to grid 1, 4
        grid.add(btn2, 1, 4);
        
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        //create action event to hand the cancel button
        btn2.setOnAction((ActionEvent e) -> {
            userTextField.clear();//clear the username
            pwBox.clear();//clear the password box
        });
        // Set the Action when login button is clicked
        btn.setOnAction((ActionEvent e) -> {
            // Authenticate the user
            boolean isValid = authenticate(userTextField.getText(), pwBox.getText());
            
            //Log the user input and if it was valid
            LOGGER.log(Level.INFO, "username entered: {0}", userTextField.getText());
            LOGGER.log(Level.INFO, "password entered: {0}", pwBox.getText());
            LOGGER.log(Level.INFO, "Success: {0}", Boolean.toString(isValid));
            // If valid clear the grid and Welcome the user
            if (isValid) {
                grid.setVisible(false);
                GridPane grid2 = new GridPane();
                // Align to Center
                grid2.setAlignment(Pos.TOP_CENTER);
                // Set gap between the components
                grid2.setHgap(10);
                grid2.setVgap(10);
                Text scenetitle1 = new Text("Welcome " + userTextField.getText() + "!");
                grid2.add(scenetitle1, 3, 1, 3, 1);
                
                Text selectView = new Text("Select a Data View");
                grid2.add(selectView, 1, 2, 3, 1);
                //Radio button declaration
                ToggleGroup group = new ToggleGroup();
                
                RadioButton tableBtn = new RadioButton("Table View");
                tableBtn.setToggleGroup(group);
                tableBtn.setOnAction((ActionEvent f) -> {
                        choice = DisplayChoice.TABLE; // each radio button changes the choice variable, so that the show data button can be effected.
                });

                grid2.add(tableBtn, 1, 3);
                
                RadioButton columnBtn = new RadioButton("Column View");
                columnBtn.setToggleGroup(group);
                
                columnBtn.setOnAction((ActionEvent f) -> {
                        choice = DisplayChoice.COLUMN;
                });
                
                grid2.add(columnBtn, 1, 4);
                
                RadioButton graphBtn = new RadioButton("Graphical View");
                graphBtn.setToggleGroup(group);
                
                graphBtn.setOnAction((ActionEvent f) -> {
                        choice = DisplayChoice.GRAPH;
                });
                
                grid2.add(graphBtn, 2, 3);
                
                RadioButton statBtn = new RadioButton("Statistical View");
                statBtn.setToggleGroup(group);
                
                statBtn.setOnAction((ActionEvent f) -> {
                        choice = DisplayChoice.STAT;
                });
                
                grid2.add(statBtn, 2, 4);
                // end radio button declaration. 
                Label startDate = new Label("Start Date:");
                grid2.add(startDate, 1, 7);
                DatePicker startDatePicker = new DatePicker();
                grid2.add(startDatePicker, 1, 8);
                
                Label endDate = new Label("End Date:");
                grid2.add(endDate, 1, 9);
                DatePicker endDatePicker2 = new DatePicker();
                grid2.add(endDatePicker2, 1, 10);
                
                Button showData = new Button("Show Data");
                
                showData.setOnAction((ActionEvent f) -> {
                        switch(choice){ //Each switch choice brings the user to a different view.
                        case TABLE: //default view
                        	grid2.setVisible(false);
                            GridPane grid3 = new GridPane();
                            grid3.setAlignment(Pos.TOP_CENTER);
                            grid3.setHgap(10);
                            grid3.setVgap(10);
                            
                            Text sceneTitle2 = new Text("Table displayed below.");
                            grid3.add(sceneTitle2, 3, 1,3,1);
                            
                            String tbls = ""; //holds which tables are available
                            ResultSet r = retriever.getTableList(); //gets the available tables for display
                    		try {
                    			while (r.next()){
                    				tbls += r.getString("table_name") + "\n";		//table_name is the column name so leave as is
                    			}
                    		} catch (SQLException s) {
                    			// TODO Auto-generated catch block
                    			s.printStackTrace();
                    		}
                            
                            TextArea tableBox = new TextArea(tbls); //Main display area
                            grid3.add(tableBox, 3, 2, 5, 4);
                            
                            Button ShowTable = new Button("Show Table"); //Will show the contents of a selected table, default SQL ordering
                            ShowTable.setOnAction((ActionEvent g) -> {
                            	String n = tableBox.getSelectedText();
                            	System.out.println(n);
                            	int t = retriever.getColumnCount(n);  //gets number of columns
                            	ResultSet res = retriever.getTableData(n); //gets table data
                            	String ts = "";
                        		try {
                        			while (res.next()){
                        				for(int i=1; i <= t; i++){
                        					ts += res.getString(i) + "\t";	//Separate each column with tab
                        				}
                        				ts += "\n";		// Separate each row with new line
                        			}
                        		} catch (SQLException s) {
                        			// TODO Auto-generated catch block
                        			s.printStackTrace();
                        		}
                        		tableBox.setText(ts); //changes text
                            });
                            grid3.add(ShowTable,1,4);
                            
                        	Scene scene = new Scene(grid3, 500, 400);
                            primaryStage.setScene(scene);  //redraw
                            primaryStage.show();
                        	break;
                        	
                        	/*
                        	 * This section is for column viewing and the ordering that is possible to do afterwards
                        	 */
                        case COLUMN:
                        	grid2.setVisible(false);
                            GridPane grid4 = new GridPane();
                            grid4.setAlignment(Pos.TOP_CENTER);
                            grid4.setHgap(10);
                            grid4.setVgap(10);
                            
                            Text sceneTitle3 = new Text("Select Table to View Columns");
                            grid4.add(sceneTitle3, 3, 1,3,1); 
                            
                            String tbls2 = "";
                            ResultSet r2 = retriever.getTableList(); //gets table list for selection
                    		try {
                    			while (r2.next()){
                    				tbls2 += r2.getString("table_name") + "\n";		//table_name is the column name so leave as is
                    			}
                    		} catch (SQLException s) {
                    			// TODO Auto-generated catch block
                    			s.printStackTrace();
                    		}
                            TextArea tableBox2 = new TextArea(tbls2); //display area
                            grid4.add(tableBox2, 3, 2, 5, 4);
                            //buttons declared below due to them making each other visible/invisible
                            Button ShowColumns = new Button("Show Columns"); //will show the columns of selected table

                            Button ShowAscending = new Button("Show Ascending");
                            Button ShowDescending = new Button("Show Descending");
                            ShowAscending.setVisible(false);
                            ShowDescending.setVisible(false);
                            
                            ShowColumns.setOnAction((ActionEvent h) -> { //gets the columns of the selected table
                            	currentTb = tableBox2.getSelectedText(); //requires highlighting
                            	ResultSet res3 = retriever.getColumnName(currentTb);
                            	String ts3 = "";
                            	try {
                        			while (res3.next()){
                        					ts3 += res3.getString("column_name") + "\n";	//Separate each column
                        			}
                        		} catch (SQLException s) {
                        			// TODO Auto-generated catch block
                        			s.printStackTrace();
                        		}
                            	tableBox2.setText(ts3); //change text
                            	sceneTitle3.setText("Pick a column to sort the table by in Accending or Decending order");
                            	ShowColumns.setVisible(false);
                            	ShowAscending.setVisible(true); //switches button visibility for next view
                            	ShowDescending.setVisible(true);
                            });
                            grid4.add(ShowColumns, 2, 4);
                            
                            /*
                             * Ascending and descending buttons below.
                             */
                            
                            ShowAscending.setOnAction((ActionEvent g) -> {
                            	String n2 = currentTb; //gets current table
                            	String d = tableBox2.getSelectedText(); //gets selected column
                            	int t = retriever.getColumnCount(n2); //the rest retrieves the data in the requested order and displays it.
                            	ResultSet res2 = retriever.getAscendingOrder(n2, d);
                            	String ts2 = "";
                        		try {
                        			while (res2.next()){
                        				for(int i=1; i <= t; i++){
                        					ts2 += res2.getString(i) + "\t";	//Separate each column with tab
                        				}
                        				ts2 += "\n";		// Separate each row with new line
                        			}
                        		} catch (SQLException s) {
                        			// TODO Auto-generated catch block
                        			s.printStackTrace();
                        		}
                        		tableBox2.setText(ts2);
                            });
                            grid4.add(ShowAscending,1,7);
                            
                            ShowDescending.setOnAction((ActionEvent g) -> {
                            	String n2 = currentTb; //gets current table
                            	String d = tableBox2.getSelectedText(); //gets which column to order by
                            	int t = retriever.getColumnCount(n2); //the rest retrieves the data in the requested order and displays it.
                            	ResultSet res2 = retriever.getDescendingOrder(n2, d);
                            	String ts2 = "";
                        		try {
                        			while (res2.next()){
                        				for(int i=1; i <= t; i++){
                        					ts2 += res2.getString(i) + "\t";	//Separate each column with tab
                        				}
                        				ts2 += "\n";		// Separate each row with new line
                        			}
                        		} catch (SQLException s) {
                        			// TODO Auto-generated catch block
                        			s.printStackTrace();
                        		}
                        		tableBox2.setText(ts2);
                            });
                            grid4.add(ShowDescending,3,7);
                            
                        	Scene scene2 = new Scene(grid4, 500, 400);
                            primaryStage.setScene(scene2);
                            primaryStage.show();
                        	break;
                        case GRAPH:
                        	break;
                        case STAT:
                        	break;
                }
                });
                
                grid2.add(showData, 1, 12);
                
                Scene scene = new Scene(grid2, 500, 400);
                primaryStage.setScene(scene);
                primaryStage.show();
                // If Invalid Ask user to try again
            } else {
                final Text actiontarget1 = new Text();
                grid.add(actiontarget1, 1, 6);
                actiontarget1.setFill(Color.FIREBRICK);
                actiontarget1.setText("Please try again.");
            }
        });
        // Set the size of Scene
        Scene scene = new Scene(grid, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);//set locale for date widget
        FileHandler fh;//create a file handler to contain logs
        
        // write the log messages to a file
        try {
            fh = new FileHandler("/Users/jayherford/Desktop/logs/logfile.log");//path of the log file
            LOGGER.addHandler(fh);//create a handler for the static variable logger
            SimpleFormatter formatter = new SimpleFormatter();//create a formatter object
            fh.setFormatter(formatter);

            LOGGER.info("My Log is Working");//test statement that should be the first message in the log
            
        }
        // print error message if there is one
        catch (IOException io) {
            System.out.println("File IO Exception" + io.getMessage());
        }
        launch(args);
    }

    /**
     * @param user the username entered
     * @param pword the password entered
     * @return isValid true for authenticated
     */
    public boolean authenticate(String user, String pword) {
        boolean isValid = false;
        if (user.equalsIgnoreCase("Group5")
                && pword.equals("senioritis")) {
            isValid = true;
        }

        return isValid;
    }

}


