package uark.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;


public class Main extends Application{
    //Overiding start method
    @Override
    public void start(Stage stage) {
        try {
            //Load the Welcome Page
            Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("Welcome.fxml")));
            //Set the Scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("INEG Success Planner");
            //Get the CSS and add it to each scene
            String css = Objects.requireNonNull(this.getClass().getResource("application.css")).toExternalForm();
            scene.getStylesheets().add(css);
            //Show the Stage (Window)
            stage.setResizable(false);
            stage.show();
        } catch(Exception e){
            System.out.println("Error in Start Method");
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        Controller controller = new Controller(new Model());
        try {
            launch();
        } catch(Exception e){
            System.out.println("Error in Main Method");
            e.printStackTrace();
        }
    }
}