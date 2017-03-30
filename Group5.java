import java.io.IOException;
import java.util.Locale;
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
 */
public class Group5 extends Application {
    
    //create the logger
    private static final Logger LOGGER = Logger.getLogger(Group5.class.getName());

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
                
                ToggleGroup group = new ToggleGroup();
                
                RadioButton tableBtn = new RadioButton("Table View");
                tableBtn.setToggleGroup(group);

                grid2.add(tableBtn, 1, 3);
                
                RadioButton columnBtn = new RadioButton("Column View");
                columnBtn.setToggleGroup(group);
                grid2.add(columnBtn, 1, 4);
                
                RadioButton graphBtn = new RadioButton("Graphical View");
                graphBtn.setToggleGroup(group);
                grid2.add(graphBtn, 2, 3);
                
                RadioButton statBtn = new RadioButton("Statistical View");
                statBtn.setToggleGroup(group);
                grid2.add(statBtn, 2, 4);
                
                Label startDate = new Label("Start Date:");
                grid2.add(startDate, 1, 7);
                DatePicker startDatePicker = new DatePicker();
                grid2.add(startDatePicker, 1, 8);
                
                Label endDate = new Label("End Date:");
                grid2.add(endDate, 1, 9);
                DatePicker endDatePicker2 = new DatePicker();
                grid2.add(endDatePicker2, 1, 10);
                
                Button showData = new Button("Show Data");
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

