import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.ComboBox;
import javafx.collections.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;



/**
 *
 * @author jay herford
 * modified by Thomas Barton
 */
public class GUI extends Application {
    
    /**
 * 
 * @author Thomas Barton
 * This enumerated value is used by the GUI class.
 */
public enum DisplayChoice {
	TABLE, COLUMN,GRAPH,STAT
}

    
    //create the logger
    //private static final Logger LOGGER = Logger.getLogger(GUI.class.getName());
    //enumerated value set to be TABLE by default, used by show data button
    private DisplayChoice choice = DisplayChoice.TABLE;
    //used to remember the table that is being selected from in from the columb choice
    private String currentTb = "";
    private String currentCol = "";
    
    //DataRetriever object
    private static final DataRetriever retriever = new DataRetriever();
    
    //Current defaults for dates.
    private static LocalDate startValue = LocalDate.of(1800, 1, 1);
    private static LocalDate endValue = LocalDate.of(2020, 1, 1);
    
    // Variables for Statistical View. The startDate and endDate variables should be added to GUI.java
    static String startDate="2016-01-01";	//Set to default start date if user doesn't select a date
    static String endDate="2016-12-31";		//Set to default end date if user doesn't select a date
    static StringBuilder sb = new StringBuilder();
    static DataRetriever test = new DataRetriever();
    
    Stage setStage = new Stage();//stage for alternating windows
    

    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setTitle("H.B.B. & P. Business Intelligence Software");
        //create gridpane
        GridPane grid = new GridPane(); //removed for image test
        
         //all below removed for image test
        // Align to Center
        grid.setAlignment(Pos.CENTER);
        //Set gap between the components
        grid.setHgap(10);
        grid.setVgap(10);

        // Display the welcome message in the scene
        Text scenetitle = new Text("Login to continue.");
        //Add text to grid 0,0 span 2 columns, 1 row
        grid.add(scenetitle, 0, 0, 2, 1);
         

        // Create Label for the user name
        Label userName = new Label("User Name:");
        // Add label to grid 0,1
        grid.add(userName, 0, 1);

        // Create Textfield to accept username
        TextField userTextField = new TextField();
        userTextField.setPromptText("group5");
        // Add textfield to grid 1,1
        grid.add(userTextField, 1, 1);

        // Create Label for password
        Label pw = new Label("Password:");
        // Add label to grid 0,2
        grid.add(pw, 0, 2);

        // Create Passwordfield for password
        PasswordField pwBox = new PasswordField();
        pwBox.setPromptText("senioritis");
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
            //LOGGER.log(Level.INFO, "username entered: {0}", userTextField.getText());
            //LOGGER.log(Level.INFO, "password entered: {0}", pwBox.getText());
            //LOGGER.log(Level.INFO, "Success: {0}", Boolean.toString(isValid));
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
                scenetitle1.setFill(Color.BLUE);
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
                
                /* Combo box experiment.  Attempting to return Columns to combo box
                ObservableList<String> options =
                            FXCollections.observableArrayList(
                            "Bar Graph",
                            "Pie Chart",
                            "Line Graph",
                            "Scatter Chart");
                
                ComboBox graphType = new ComboBox(options);
                graphType.setValue("Graph View");
                grid2.add(graphType, 2, 3); */
                
                
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
                Label startDateLabel = new Label("Transaction Start Date:");
                grid2.add(startDateLabel, 1, 7);
                DatePicker startDatePicker = new DatePicker();
                grid2.add(startDatePicker, 1, 8);
                
                Label endDateLabel = new Label("Transaction End Date:");
                grid2.add(endDateLabel, 1, 9);
                DatePicker endDatePicker2 = new DatePicker();
                grid2.add(endDatePicker2, 1, 10);
                
                Button showData = new Button("Show Data");

                
                showData.setOnAction((ActionEvent f) -> {
                	if(startDatePicker.getValue() != null && endDatePicker2.getValue() != null)
                	{
                		if(startDatePicker.getValue().isAfter(endDatePicker2.getValue()))
                		{
                			final Text actiontarget1 = new Text();
                            grid2.add(actiontarget1, 1, 11);
                            actiontarget1.setFill(Color.FIREBRICK);
                            actiontarget1.setText("Invalid Dates");
                            return;
                		}
                	startValue = startDatePicker.getValue();
                	endValue = endDatePicker2.getValue();
                	}
                	
                        switch(choice){ //Each switch choice brings the user to a different view.
                        case TABLE: //default view
                        	
                        	grid2.setVisible(false);
                            GridPane grid3 = new GridPane();
                            grid3.setAlignment(Pos.TOP_CENTER);
                            grid3.setHgap(10);
                            grid3.setVgap(10);
                            
                            Text sceneTitle2 = new Text("Tables displayed below.");
                            grid3.add(sceneTitle2, 1,1);
                            
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
                            tableBox.setFont(Font.font("Monospaced"));
                            grid3.add(tableBox, 1,2);
                            
                            Button ShowTable = new Button("Show Table"); //Will show the contents of a selected table, default SQL ordering
                            ShowTable.setOnAction((ActionEvent g) -> {
                            	String n = tableBox.getSelectedText();
                            	sceneTitle2.setText(n + ":");
                            	System.out.println(n);
                            	String ts = "";
                            	ResultSet res3 = retriever.getColumnName(n);
                            	try {
                        			while (res3.next()){
                        					ts += String.format("%1$-25s", res3.getString("column_name"));	//Separate each column
                        			}
                        		} catch (SQLException s) {
                        			// TODO Auto-generated catch block
                        			s.printStackTrace();
                        		}
                            	ts += "\n";
                            	int t = retriever.getColumnCount(n);  //gets number of columns
                            	ResultSet res = retriever.getTableData(n, startValue.toString(), endValue.toString()); //gets table data
                            	
                        		try {
                        			while (res.next()){
                        				for(int i=1; i <= t; i++){
                        					ts += String.format("%1$-25s", res.getString(i));	//Separate each column with tab
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
                            
                            Button goBack = new Button("Go to Selection");
                            goBack.setOnAction((ActionEvent g)->{
                            	grid3.setVisible(false);
                            	grid2.setVisible(true);
                            	currentTb = "";
                            	currentCol = "";
                            	tableBox.setText("");
                            	grid2.setVisible(true);
                            	grid3.setVisible(false);
                            });
                            grid3.add(goBack, 2, 4);
                            Scene scene = new Scene(grid3, 500, 400);
                            primaryStage.setScene(scene);  //redraw
                            primaryStage.show();
                        	break;
                        	
                        	/*
                        	 * This section is for column viewing and the ordering that is possible to do afterwards
                        	 */
                        case COLUMN:
                        	//grid2.setVisible(false);
                            GridPane grid4 = new GridPane();
                            grid4.setAlignment(Pos.TOP_CENTER);
                            grid4.setHgap(10);
                            grid4.setVgap(10);
                            
                            Text sceneTitle3 = new Text("Select Table to View Columns");
                            grid4.add(sceneTitle3, 1,1); 
                            
                            String tbls2 = "";
                            ResultSet r2 = retriever.getTableList(); //gets table list for selection
                            //MenuButton menu = new MenuButton("Column Name"); //Create a drop down menu attempts
                            //grid4.add(menu, 1,2);//add the menu to the grid
                                try {
                    			while (r2.next()){
                                            tbls2 += r2.getString("table_name") + "\n";    
                                            //menu.getItems().addAll(new MenuItem(tbls2));//populate the drop down with the column names
                    						//table_name is the column name so leave as is
                    			
                                        
                                        }
                    		} catch (SQLException s) {
                    			// TODO Auto-generated catch block
                    			s.printStackTrace();
                    		}
                            TextArea tableBox2 = new TextArea(tbls2); //display area
                            tableBox2.setFont(Font.font("Monospaced"));
                            grid4.add(tableBox2, 1,3);
                            //buttons declared below due to them making each other visible/invisible
                            Button ShowColumns = new Button("Show Columns"); //will show the columns of selected table
                            grid4.add(ShowColumns, 1, 4);
                            
                            Button ShowAscending = new Button("Show Ascending");
                            Button ShowDescending = new Button("Show Descending");
                            ShowAscending.setVisible(false);
                            ShowDescending.setVisible(false);
                           
                     
                            ShowColumns.setOnAction((ActionEvent h) -> { //gets the columns of the selected table
                              
                                //currentTb = menu.getItems().toString();//attempting to convert menu items to string
                                currentTb = tableBox2.getSelectedText(); //requires highlighting
                                sceneTitle3.setText("Select a Column for " + currentTb);
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
                            
                            
                            /*
                             * Ascending and descending buttons below.
                             */
                            
                            ShowAscending.setOnAction((ActionEvent g) -> {
                            	String n2 = currentTb; //gets current table
                            	sceneTitle3.setText(currentTb + ":");
                            	if(currentCol.equals(""))
                            	{
                            	currentCol = tableBox2.getSelectedText(); //gets selected column
                            	}
                            	String ts2 = "";
                            	ResultSet res3 = retriever.getColumnName(currentTb);
                            	try {
                        			while (res3.next()){
                        				ts2 += String.format("%1$-25s", res3.getString("column_name"));	//Separate each column
                        			}
                        		} catch (SQLException s) {
                        			// TODO Auto-generated catch block
                        			s.printStackTrace();
                        		}
                            	ts2 += "\n";
                            	
                            	int t = retriever.getColumnCount(n2); //the rest retrieves the data in the requested order and displays it.
                            	ResultSet res2 = retriever.getAscendingOrder(n2, currentCol, startValue.toString(), endValue.toString());
                            	
                        		try {
                        			while (res2.next()){
                        				for(int i=1; i <= t; i++){
                        					ts2 += String.format("%1$-25s", res2.getString(i));	//Separate each column with tab
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
                            	sceneTitle3.setText(currentTb + ":");
                            	if(currentCol.equals(""))
                            	{
                            	currentCol = tableBox2.getSelectedText(); //gets selected column
                            	}
                            	String ts2 = "";
                            	ResultSet res3 = retriever.getColumnName(currentTb);
                            	try {
                        			while (res3.next()){
                        				ts2 += String.format("%1$-25s", res3.getString("column_name"));	//Separate each column
                        			}
                        		} catch (SQLException s) {
                        			// TODO Auto-generated catch block
                        			s.printStackTrace();
                        		}
                            	ts2 += "\n";
                            	
                            	int t = retriever.getColumnCount(n2); //the rest retrieves the data in the requested order and displays it.
                            	ResultSet res2 = retriever.getDescendingOrder(n2, currentCol, startValue.toString(), endValue.toString());
                        		try {
                        			while (res2.next()){
                        				for(int i=1; i <= t; i++){
                        					ts2 += String.format("%1$-25s", res2.getString(i));	//Separate each column with tab
                        				}
                        				ts2 += "\n";		// Separate each row with new line
                        			}
                        		} catch (SQLException s) {
                        			// TODO Auto-generated catch block
                        			s.printStackTrace();
                        		}
                        		tableBox2.setText(ts2);
                            });
                            grid4.add(ShowDescending,1,8);
                            
                            Button goBack2 = new Button("Go to Selection");
                            goBack2.setOnAction((ActionEvent g)->{
                            	grid4.setVisible(false);
                            	grid2.setVisible(true);
                            	currentTb = "";
                            	currentCol = "";
                            	ShowColumns.setVisible(true);
                            	ShowAscending.setVisible(false);
                            	ShowDescending.setVisible(false);
                            	tableBox2.setText("");
                            });
                            grid4.add(goBack2, 2,8);
                            
                            Scene scene2 = new Scene(grid4, 500, 400);
                            primaryStage.setScene(scene2);
                            primaryStage.show();
                        	break;
                                
                        case GRAPH:
                           // show the stage for a graphical view selection 
                           setStage.setTitle("Graphical View Selector");                       
                           //grid.setVisible(false);
                           GridPane graphGrid = new GridPane();
                
                            // Align to Center
                            graphGrid.setAlignment(Pos.TOP_CENTER);
                            // Set gap between the components
                            graphGrid.setHgap(10);
                            graphGrid.setVgap(10);
                            Text graphSelect = new Text("Select a graph form the options below:");
                            
                            graphGrid.add(graphSelect, 1,1);
                            
                            Scene graphScene = new Scene(graphGrid, 400, 200);
                            setStage.setScene(graphScene);
                            setStage.show();
                
                            
                            //Radio button declaration
                            ToggleGroup graphGroup = new ToggleGroup();
                            
                            //Create graph button and place in scene
                            RadioButton barGraph = new RadioButton("Bar Graph");
                            barGraph.setToggleGroup(graphGroup);
                            graphGrid.add(barGraph, 1,3);
                            
                            //Create pie button and place in scene
                            RadioButton pieChart = new RadioButton("Pie Chart");
                            pieChart.setToggleGroup(graphGroup);
                            graphGrid.add(pieChart, 2,3);
                            
                            //Create line button and place in scene
                            RadioButton lineGraph = new RadioButton("Line Grpah");
                            lineGraph.setToggleGroup(graphGroup);
                            graphGrid.add(lineGraph, 1,4);
                            
                            //Create scatter button and place in scene
                            RadioButton scatter = new RadioButton("Scatter Chart");
                            scatter.setToggleGroup(graphGroup);
                            graphGrid.add(scatter, 2,4);
                            
                            Button selectGraph = new Button("Show Graph");
                            graphGrid.add(selectGraph, 1, 5);
                            
                            //action event for selecting graph
                            selectGraph.setOnAction((ActionEvent graph) -> {
                            
                            //determine which graph was selected and call appropriate class 
                            if (barGraph.isSelected()) {//bar graph
                            createBarGraph();
                            }
                            else if (pieChart.isSelected()) {//pie chart
                            createPieChart();  
                            
                            }
                            else if (lineGraph.isSelected()) {//line graph
                            createLineGrap();    
                            }
                            else if (scatter.isSelected()) {//scatter plot   
                            createScatterChart();    
                            }
                            
                            });
                         
                        	break;
                        case STAT:
                            //this case gives the statisticla view
                            stat();//call the stat method to reurn the stage with results
                                                 
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
        
        //print working directory for testing
        //System.out.println("Working Directory = " +
             // System.getProperty("user.dir"));
        
        Locale.setDefault(Locale.US);//set locale for date widget
        //FileHandler fh;//create a file handler to contain logs
        
        // write the log messages to a file
        //try {
         //   fh = new FileHandler("/Users/jayherford/Desktop/logs/logfile.log");//path of the log file
         //   LOGGER.addHandler(fh);//create a handler for the static variable logger
         //   SimpleFormatter formatter = new SimpleFormatter();//create a formatter object
         //   fh.setFormatter(formatter);

         //   LOGGER.info("My Log is Working");//test statement that should be the first message in the log
            
       // }
        // print error message if there is one
        //catch (IOException io) {
        //    System.out.println("File IO Exception" + io.getMessage());
       // }
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
    
    //method for bar graph stage
    public Stage createBarGraph() {
        try {
                            setStage.setTitle("Sales by Employee");
                            final CategoryAxis xAxis = new CategoryAxis();
                            final NumberAxis yAxis = new NumberAxis();
                            final BarChart<String,Number> bc = 
                            new BarChart<String,Number>(xAxis,yAxis);
                            bc.setTitle("Sales Leaders");
                            xAxis.setLabel("Employee Name");       
                            yAxis.setLabel("Sales Total");
 
                            XYChart.Series series = new XYChart.Series();
                            series.setName("All Regions");
                            
                            ResultSet empSales = retriever.getSalesByEmployee();
                            
                            
                            while (empSales.next()) {
                                series.getData().add(new XYChart.Data(empSales.getString("employee_name"), empSales.getInt("sales_amt")));
                                
                            }
                              
                            Scene scene5  = new Scene(bc,800,600);
                            bc.getData().addAll(series);//region2, region3
                            setStage.setScene(scene5);
                            setStage.show();
                            
                            } catch (SQLException s) {
                                s.printStackTrace();
                            }
        return setStage;
    }
    
    //method for creating pie chart stage
    public Stage createPieChart() {
        try {
            Scene pieScene = new Scene(new Group());
        setStage.setTitle("Pie Chart Sales Statistics");
        setStage.setWidth(500);
        setStage.setHeight(500);
 
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Region 1", retriever.getSalesbyRegion(1, startDate, endDate)),
                new PieChart.Data("Region 2", retriever.getSalesbyRegion(2, startDate, endDate)),
                new PieChart.Data("Region 3", retriever.getSalesbyRegion(3, startDate, endDate)));
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Sales by Region");

        ((Group) pieScene.getRoot()).getChildren().add(chart);
        setStage.setScene(pieScene);
        setStage.show();
    
            
        } catch (SQLException n) {
            n.printStackTrace();
        }
                
        return setStage;
        }
    
    // method for creating scatter chart stage
    public Stage createScatterChart() {
        try {
        setStage.setTitle("Transactions");
        final NumberAxis xAxis = new NumberAxis(0, 12, 1);
        final NumberAxis yAxis = new NumberAxis(0, 1000, 100);
        final ScatterChart<Number,Number> sc = new
        ScatterChart<Number,Number>(xAxis,yAxis);
        xAxis.setLabel("Months in 2016");                
        yAxis.setLabel("Sale Amount");
        sc.setTitle("Transaction Dates and Totals");
       
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("All Regions");
        
        ResultSet transTotals = retriever.getSalesTransactions();
                            
                            
                            while (transTotals.next()) {
                                series1.getData().add(new XYChart.Data(transTotals.getInt("date"), transTotals.getInt("amount")));
                                
                            }
        
 
        sc.getData().addAll(series1);
        Scene scatter  = new Scene(sc, 800, 600);
        setStage.setScene(scatter);
        setStage.show();
        
        } catch (SQLException n) {
            n.printStackTrace();
        }
        
        return setStage;
    }
    
    //method for creating line graph stage
    public Stage createLineGrap(){
        try {
        setStage.setTitle("Quarterly Report");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Quarter");
        yAxis.setLabel("Sales Amount in Dollars");
        //creating the chart
        final LineChart<Number,Number> lineChart = 
                new LineChart<Number,Number>(xAxis,yAxis);
                
        lineChart.setTitle("Quarterly Sales");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Total Across All Regions");
        
        ResultSet quarter = retriever.getSalesByQuarter();//result set to hold Sale by quarter
                            
                            
                            while (quarter.next()) {
                                //populate line graph
                                series.getData().add(new XYChart.Data(quarter.getInt("quarter"), quarter.getInt("quarterly_sales")));
                                
                            }
        
        Scene lineScene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);
       
        setStage.setScene(lineScene);
        setStage.show();
        
        } catch (SQLException n) {
            n.printStackTrace();
        }
        
        return setStage;
    }
    
    //method for statitistics window
    public Stage stat() {
                          
                            setStage.setTitle("Regional and Total Statistics");
                            GridPane grid5 = new GridPane();
                            grid5.setAlignment(Pos.TOP_LEFT);
                            grid5.setHgap(15);
                            grid5.setVgap(15);
                            
                          
                            try {
                            getStatistics();//calls the get statistics method which builds a string of results
                            TextArea txtArea = new TextArea(sb.toString()); //display area
                            txtArea.setEditable(false);//ensure text cannot be edited
                            txtArea.setPrefSize(400, 300);
                            grid5.add(txtArea, 1,2);      
                             
                            
                            } catch (SQLException s) {
                        			// TODO Auto-generated catch block
                        			s.printStackTrace();
                                                TextArea txtArea = new TextArea("Stats Unavailable");
                                                grid5.add(txtArea, 1, 2);
                           }  
                          
                            
                            Scene scene3 = new Scene(grid5, 500, 400);
                            setStage.setScene(scene3);
                            setStage.show();
        
        return setStage;
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
		
		//System.out.println(sb);  //print the result for testing
                 
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
