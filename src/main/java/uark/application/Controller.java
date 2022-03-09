package uark.application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.Arrays;
import java.util.Objects;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

//Controller Class for Each Scene
public class Controller {
    //Class variables to hold
    Stage stage;
    Scene scene;
    Parent root;
    static Model model; //model object that contains the MILP

    //Constructors
    public Controller(){    } //Blank for when the scene instantiates a Controller
    public Controller(Model m){
       model = m;
    }

    //Class Variables to Hold User Selections
    public static String tmpCourse = "";
    public static String tmpSemester = "";
    public static int prePlaceCounter = 0;

    //-------------------------------------------------------------------------------------------------------
    //Scene Switching Methods
    //-------------------------------------------------------------------------------------------------------
    @FXML
    //Method to switch to a new scene given fxml and css
    public void switchScene(String sceneName, String stylingName, ActionEvent event){
        try {
            //Load the File
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(sceneName)));
            this.root = loader.load();
            //Set the stage
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(this.root);
            stage.setScene(scene);
            //Get the CSS and add it the scene
            String css = Objects.requireNonNull(this.getClass().getResource(stylingName)).toExternalForm();
            scene.getStylesheets().add(css);
            stage.show();
        } catch(Exception e){
            System.out.print("Error in Switching Scenes");
            e.printStackTrace();
        }
    }



    //Method to Go Back to welcome screen
    public void showWelcome(ActionEvent event){
        model.curriculum.clear();
        switchScene("Welcome.fxml", "application.css", event);
    }

    //Method to Launch course selection screen
    public void showCourses(ActionEvent event){
        model.curriculum.clear();
        switchScene("CourseSelection.fxml","selections.css", event);
        loadClasses();
    }

    //Method to Launch Goals selection screen
    public void showGoals(ActionEvent event){
        switchScene("GoalSelection.fxml","Goals.css", event);
        addGoalChoices();
    }

    //Method to Launch Goals selection screen
    public void showPrePlace(ActionEvent event){
        switchScene("PrePlaceCourse.fxml","PrePlace.css", event);
        addCourseChoices();
    }
    //Method to exit the application
    public void Exit(){
        //Exit the System
        System.exit(0);
    }

    //-------------------------------------------------------------------------------------------------------
    //Methods to populate scenes and handle events
    //-------------------------------------------------------------------------------------------------------

    //Method to populate Course Selection scene with classes in curriculum
    public void loadClasses() {
        //Add the Classes to Combo Boxes
        int x = 30;
        int y = 200;
        for (int i = 0; i < model.data.size(); i++) {
            CheckBox tmp = new CheckBox(model.data.get(i).name);
            tmp.setId(model.data.get(i).name);
            tmp.setOnAction(this::updateCurriculum);
            tmp.setSelected(true);
            model.curriculum.add(model.data.get(i));
            tmp.setLayoutX(x);
            tmp.setLayoutY(y);
            tmp.setStyle(
                    "-fx-text-fill: white;"
                            + "-fx-font-family: Cambria;"
                            + "-fx-font-size: 24;"
            );
            ((AnchorPane) root).getChildren().add(tmp);
            y+=50;
            if(y > 550){
                y = 200;
                x+= 250;
            }
        }
    }

    //Method to update curriculum list from curriculum buttons as they are clicked
    public void updateCurriculum(ActionEvent event) {
        CheckBox tmp = (CheckBox) event.getSource();
        if (tmp.isSelected()) {
            for (Course i : model.data) {
                if ((i.name).equals(tmp.getId())) {
                    model.curriculum.add(i);
                }
            }
        } else {
            for (Course i : model.data) {
                if ((i.name).equals(tmp.getId())) {
                    model.curriculum.remove(i);
                }
            }
        }
    }

    //Method to add data to the Goal Screen
    public void addGoalChoices(){
        //Declare Choices
        String[] semesterChoices = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11","12"};
        String[] minHoursChoices = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"};
        String[] maxHoursChoices = {"12", "13", "14", "15", "16", "17", "18", "19"};
        String[] startingSemesterChoices = {"Fall", "Spring"};
        String[] objectiveOptions = {"Min Max Difficulty Semester" , "Min Semesters", "Min Difficulty of Last Year"};
        //Add New Boxes
        ComboBox <String> semesters = new ComboBox<String>();
        ComboBox <String> minHours = new ComboBox<String>();
        ComboBox <String> maxHours = new ComboBox<String>();
        ComboBox <String> startingSemester = new ComboBox<String>();
        ComboBox <String> objective = new ComboBox<String>();
        //Add Items
        semesters.getItems().addAll(semesterChoices);
        minHours.getItems().addAll(minHoursChoices);
        maxHours.getItems().addAll(maxHoursChoices);
        startingSemester.getItems().addAll(startingSemesterChoices);
        objective.getItems().addAll(objectiveOptions);
        //Add Action
        semesters.setOnAction(this::updateSemesters);
        minHours.setOnAction(this::updateMinHours);
        maxHours.setOnAction(this::updateMaxHours);
        startingSemester.setOnAction(this::updateStartingSemester);
        objective.setOnAction(this::updateObjective);
        //Set Formatting
        semesters.setId("semesters");
        semesters.setLayoutX(300);
        semesters.setLayoutY(200);
        semesters.setPrefSize(300,25);
        semesters.setVisibleRowCount(5);
        semesters.setValue(String.valueOf(model.numSemesters));

        minHours.setId("minHours");
        minHours.setLayoutX(300);
        minHours.setLayoutY(250);
        minHours.setPrefSize(300,25);
        minHours.setValue(String.valueOf(model.minHours));
        minHours.setVisibleRowCount(5);

        maxHours.setId("maxHours");
        maxHours.setLayoutX(300);
        maxHours.setLayoutY(300);
        maxHours.setPrefSize(300,25);
        maxHours.setValue(String.valueOf(model.maxHours));
        maxHours.setVisibleRowCount(5);

        startingSemester.setId("startingSemester");
        startingSemester.setLayoutX(300);
        startingSemester.setLayoutY(350);
        startingSemester.setPrefSize(300,25);
        startingSemester.setValue("Fall");

        objective.setId("objective");
        objective.setLayoutX(300);
        objective.setLayoutY(400);
        objective.setPrefSize(300,25);
        objective.setValue(model.objectiveChoice);

        ((AnchorPane) root).getChildren().add(semesters);
        ((AnchorPane) root).getChildren().add(minHours);
        ((AnchorPane) root).getChildren().add(maxHours);
        ((AnchorPane) root).getChildren().add(startingSemester);
        ((AnchorPane) root).getChildren().add(objective);
    }

    //Method to update Max Semesters
    public void updateSemesters(ActionEvent event){
        ComboBox tmp = (ComboBox) event.getSource();
        model.numSemesters = parseInt((String)tmp.getValue());
    }

    //Method to allow hard coding of courses
    public void addCourseChoices(){
        //Get the course names
        String [] semesterChoices = new String[model.numSemesters + 1];
        String [] courseChoices = new String[model.curriculum.size()];
        //Add the Semester Choices
        for (int i = 0; i < model.numSemesters; i++){
            semesterChoices[i] = String.valueOf(i + 1);
        }
        //Add the Course Choices
        for (int i = 0; i < model.curriculum.size(); i++){
            courseChoices[i] = model.curriculum.get(i).name;
        }
        //Add the Combo Boxes
        ComboBox<String> courseChoice = new ComboBox<String>();
        ComboBox<String> semesterChoice = new ComboBox<String>();
        courseChoice.getItems().addAll(courseChoices);
        semesterChoice.getItems().addAll(semesterChoices);
        //Set on Action
        courseChoice.setOnAction(this::updateTmpCourse);
        semesterChoice.setOnAction(this::updateTmpSemester);
        //Set Formatting
        courseChoice.setId("courseChoice");
        courseChoice.setLayoutX(225);
        courseChoice.setLayoutY(200);
        courseChoice.setPrefSize(300,25);
        courseChoice.setVisibleRowCount(5);

        //Set Formatting
        semesterChoice.setId("semesterChoice");
        semesterChoice.setLayoutX(625);
        semesterChoice.setLayoutY(200);
        semesterChoice.setPrefSize(300,25);
        semesterChoice.setVisibleRowCount(5);
        //Add to Scene
        ((AnchorPane) root).getChildren().add(courseChoice);
        ((AnchorPane) root).getChildren().add(semesterChoice);

    }
    //Method to add Constraint of PrePlace
    public void addPrePlace(){
        //Check to make sure a course has been selected
        if(!Objects.equals(tmpCourse, "") && !Objects.equals(tmpSemester, "")){
            if(model.curriculum.get(model.lookup(tmpCourse)).prePlace != parseInt(tmpSemester)){
                model.curriculum.get(model.lookup(tmpCourse)).prePlace = parseInt(tmpSemester);
                prePlaceCounter++;
                Alert added = new Alert(Alert.AlertType.INFORMATION);
                added.setTitle("Constraint Added!");
                added.setHeaderText("Course: " + tmpCourse + " has been added to semester " + tmpSemester);
                added.setContentText("Please click clear all to remove this constraint!");
                added.showAndWait();
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("Course: " + tmpCourse + " has been already been added to semester " + tmpSemester);
                error.setContentText("Please click clear all to remove this constraint!");
                error.showAndWait();
            }
        }else{
            System.out.println("Nothing has been selected");
        }
    }

    //Method to Clear PrePlace
    public void clearPrePlace(){
        for(int i = 0; i < model.curriculum.size(); i++){
            model.curriculum.get(i).prePlace = 0;
        }
        prePlaceCounter = 0;
        Alert added = new Alert(Alert.AlertType.WARNING);
        added.setTitle("Constraints Cleared!");
        added.setHeaderText("All Classes have been cleaed!");
        added.setContentText("Please add any constraints!");
        added.showAndWait();
    }

    //Method to update Max Hours
    public void updateMaxHours(ActionEvent event){
        ComboBox tmp = (ComboBox) event.getSource();
        model.maxHours = parseInt((String)tmp.getValue());
    }

    //Method to update Max Hours
    public void updateMinHours(ActionEvent event){
        ComboBox tmp = (ComboBox) event.getSource();
        model.minHours = parseInt((String)tmp.getValue());
    }
    //Method to update Max Hours
    public void updateObjective(ActionEvent event){
        ComboBox tmp = (ComboBox) event.getSource();
        model.objectiveChoice = (String)tmp.getValue();
    }

    //Method to update tmpCourse
    public void updateTmpCourse(ActionEvent event){
        ComboBox tmp = (ComboBox) event.getSource();
        tmpCourse = (String)tmp.getValue();

    }

    //Method to update tmpCourse
    public void updateTmpSemester(ActionEvent event){
        ComboBox tmp = (ComboBox) event.getSource();
        tmpSemester = (String)tmp.getValue();

    }
    //Method to update Max Hours
    public void updateStartingSemester(ActionEvent event){
        ComboBox tmp = (ComboBox) event.getSource();
        String choice = (String)tmp.getValue();
        if(choice == "Fall"){
            model.fallStart = true;
        } else if (choice == "Spring"){
            model.fallStart = false;
        }
    }

    //Method to call the model to solve and switch to results Scene
    public void solveModel(ActionEvent event){
        if(model.runModel()) {
            switchScene("Results.fxml", "results.css", event);
            displayResults();
        } else {
            switchScene("Error.fxml", "application.css", event);
        }
    }

    //Method to Display Model Results in a tableview on results scene
    public void displayResults() {
        //Create Blank Table
        TableView<Course> results = new TableView<>();
        results.setEditable(true);

        //Edit Table Size and Layout
        results.setLayoutX(40);
        results.setLayoutY(50);
        results.setPrefSize(1200,650);

        //Add Columns
        TableColumn<Course, String> course = new TableColumn<>("Course");
        course.setPrefWidth(800);
        TableColumn<Course, Integer> semester = new TableColumn<>("Semester");
        semester.setPrefWidth(400);
        semester.setId("semestercol");
        results.getColumns().addAll(Arrays.asList(course, semester));

        //Populate the Data
        ObservableList<Course> resultsData = FXCollections.observableList(model.curriculum);
        course.setCellValueFactory(new PropertyValueFactory<>("name"));
        semester.setCellValueFactory(new PropertyValueFactory<>("enrolledSemester"));
        results.setItems(resultsData);
        results.getSortOrder().add(semester);
        //Add Table to Scene
        ((AnchorPane) root).getChildren().add(results);
    }

    public void saveCourses(){
        Json tmp = model.marshal();
        tmp.save("data/courses.json");
        System.out.println("Courses Saved!");
    }
}
