package uark.application;

import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//Class to Hold Google OR Tools Model

public class Model {
    //Class Variables
    List<Course> curriculum; //List of classes remaining
    List<Course> data; //List of all classes
    int numSemesters = 0; //number of semesters desired
    int maxHours = 0; //max number of hours in a semester
    int minHours = 12;
    String objectiveChoice = "Min Max Difficulty Semester";
    boolean fallStart = true; //First Semester to start planning (fall or spring)
    MPSolver solver; //OR solver
    MPObjective objective;
    MPVariable[][] enroll; //OR variables
    MPSolver.ResultStatus resultStatus; //holds the results of solver


    //Constructor
    Model(){
        curriculum = new ArrayList<Course>();
        data = new ArrayList<Course>();
//        this.loadData();
        numSemesters = 8; //Create with a number larger than user's input (allocate enough varibles)
        minHours = 12;
        maxHours = 16;
        //Load Courses
        this.loadCourses(Json.load("data/courses.json"));

    }

    //Method to populate Data
    public void loadData(){
        //////////////////////////////////////////////////////////////////////////////////////////////////////
        //New Plan
        //ENGL
        data.add(new Course("ENGL", 1013, 3, false, false));
        data.add(new Course("ENGL", 1033, 4, false, false, new String[]{"ENGL 1013"}));
        //Arts and History
        data.add(new Course("ECON", 2143, 3, false, false));
        data.add(new Course("ACCT", 2403, 3, false, false));
        data.add(new Course("HIST",  3, false, false));
        data.add(new Course("FINEART",1,false,false));
        data.add(new Course("HUMMAN",3,false,false));
        data.add(new Course("SOCISCN 1st",  3,false,false));
        data.add(new Course("SOCISCN 2nd",  3,false,false));
        //Math and Science and GNEG
        data.add(new Course("MATH", 2554, 4, false, false));
        data.add(new Course("MATH", 2564, 5, false, false,  new String[]{"MATH 2554"}));
        data.add(new Course("MATH", 3083, 3, false, false, new String[]{"MATH 2554"}));
        data.add(new Course("CHEM", 1103, 4, false, false));
        data.add(new Course("PHYS", 2054, 4, false, false, new String[]{"MATH 2554"}));
        data.add(new Course("GEOS", 1113, 3, false, false));
        data.add(new Course("GNEG", 1111, 2, false, false, new String[]{}, new String[]{"MATH 2554"}));
        data.add(new Course("GNEG", 1121, 2, false, false,  new String[]{}, new String[]{"MATH 2564", "GNEG 1121"}));
        //INEG
        data.add(new Course("INEG", 2001, 2, false, false));
        data.add(new Course("INEG", 2103, 3, false, false, new String[]{"MATH 2554"}));
        data.add(new Course("INEG", 2214, 4, false, false, new String[]{}, new String[]{"INEG 2103"}));
        data.add(new Course("INEG", 2314, 4, false, false, new String[]{}, new String[]{"INEG 2103"}));
        data.add(new Course("INEG", 2223, 4, false, false, new String[]{"INEG 2214"}));
        data.add(new Course("INEG", 2323, 3, false, false, new String[]{}, new String[]{"INEG 2103"}));
        data.add(new Course("INEG", 2413, 3, false, false, new String[]{"MATH 2554"}));
        data.add(new Course("INEG", 2613, 5, false, false, new String[]{"INEG 2214"}, new String[]{"INEG 2103"}));
        data.add(new Course("INEG", 3333, 4, false, false,  new String[]{"INEG 2223", "INEG 2314", "INEG 2323"}));
        data.add(new Course("INEG", 3443, 3, false, false));
        data.add(new Course("INEG", 3543, 3, false, false, new String[]{"INEG 2413"}, new String[]{"INEG 2613"}));
        data.add(new Course("INEG", 3624, 3, false, false, new String[]{"INEG 2223", "INEG 2314", "INEG 2323"}, new String[]{"INEG 2413"}));
        data.add(new Course("INEG", 3533, 3, false, false, new String[]{"INEG 2613"}, new String[]{"INEG 2223"}));
        data.add(new Course("INEG", 3553, 3, false, false, new String[]{"INEG 2314"}, new String[]{"INEG 2613"}));
        data.add(new Course("INEG", 3714, 3, false, false,  new String[]{"INEG 2314"}));
        data.add(new Course("INEG", 3833, 3, false, false,new String[]{}, new String[]{"INEG 2223"}));
        data.add(new Course("INEG", 4433, 3, true, false, new String[]{"INEG 2413"}));
        data.add(new Course("INEG", 4913, 3, true, false, new String[]{"INEG 2001", "INEG 2103", "INEG 3333", "INEG 3443", "INEG 3543", "INEG 3624"},
                new String[]{"INEG 3533", "INEG 3553", "INEG 3714", "INEG 3833", "INEG 4433"}));
        data.add(new Course("INEG", 4924, 3, false, true, new String[]{"INEG 3533", "INEG 3553", "INEG 3714", "INEG 3833", "INEG 4433", "INEG 4913"}));
        //Tech Electives
        data.add(new Course("IETECH 1st",  3,false,false));
        data.add(new Course("IETECH 2nd",  3,false,false));
        data.add(new Course("TECH 3rd",  3,false,false));
        data.add(new Course("TECH 4th",  3,false,false));

    }


    //Method to marshall data
    public Json marshal(){
        Json ob = Json.newObject();
        Json courseList = Json.newList();
        ob.add("course", courseList);
        for(int i = 0; i < data.size(); i++){
            courseList.add(data.get(i).marshal());
        }
        return ob;
    }

    //Method to unmarshal data
    public void loadCourses(Json ob){
        Json courseList = ob.get("course");
        for(int i = 0; i < courseList.size(); i++){
            data.add(new Course(courseList.get(i)));
        }
    }



    //Method to Print
    public void print(){
        for(int i = 0; i < curriculum.size(); i++){
            System.out.println(curriculum.get(i));
        }
    }

    //method to look up index of a class in the curriculum array
    public int lookup(String search){
        for (int i = 0; i < curriculum.size(); i++){
            if(curriculum.get(i).name.equals(search))
                return i;
        }
        return -1;
    }

    public void addDecisionVariables(){
        //Allocated Memory for Decision Variables
        enroll = new MPVariable[100][numSemesters+1]; //for every semester and class in curriculum create decision variable enroll[i][j] if class is in semester j
        //Create the decision variables for every course in the curriculum array
        for(int i = 0; i < curriculum.size(); i++){
            for(int j = 1; j <= numSemesters; j++){
                enroll[i][j] = solver.makeIntVar(0, 1, "");
            }
        }
    }

    public void addConstraints() {
        //Constraint that each class is assigned to one semester
        for (int i = 0; i < curriculum.size(); i++) {
            MPConstraint assignmentConstraint = solver.makeConstraint(1, 1, "");
            for (int j = 1; j <= numSemesters; j++) {
                assignmentConstraint.setCoefficient(enroll[i][j], 1);
            }
        }

        //Constraint for Preq
        for (int k = 0; k < curriculum.size(); k++) { //For every Class
            for (int i = 0; i < curriculum.get(k).preReq.size(); i++) { //For every element in the preReq array of a given classes
                int preIndex = this.lookup(curriculum.get(k).preReq.get(i)); //get the location of the preReq in the classes
                //if the preReq is in the array, add the constraint for it
                if (preIndex >= 0) {
                    MPConstraint preReqConstraint = solver.makeConstraint(1, numSemesters, "");
                    for (int j = 1; j <= numSemesters; j++) { //For every Semester
                        preReqConstraint.setCoefficient(enroll[preIndex][j], -j);
                        preReqConstraint.setCoefficient(enroll[k][j], j);
                    }
                }
            }
        }

        //Constraint for CoPreq
        for (int k = 0; k < curriculum.size(); k++) { //For every Class
            for (int i = 0; i < curriculum.get(k).coReq.size(); i++) { //For every element in the preReq array of a given classes
                int coIndex = this.lookup(curriculum.get(k).coReq.get(i));
                if (coIndex >= 0) {
                    MPConstraint coReqConstraint = solver.makeConstraint(0, numSemesters, "");
                    for (int j = 1; j <= numSemesters; j++) { //For every Semester
                        coReqConstraint.setCoefficient(enroll[coIndex][j], -j);
                        coReqConstraint.setCoefficient(enroll[k][j], j);
                    }
                }
            }
        }

        //Constraint for GNEG I AND GNEG II  to be taken ASAP
        if ((lookup("GNEG 1111") != -1) && (lookup("GNEG 1121") != -1)) {
            MPConstraint GNEG1 = solver.makeConstraint(1, 1, "");
            MPConstraint GNEG2 = solver.makeConstraint(1, 1, "");
            GNEG1.setCoefficient(enroll[lookup("GNEG 1111")][1], 1);
            GNEG2.setCoefficient(enroll[lookup("GNEG 1121")][2], 1);
        } else if (lookup("GNEG 1121") != -1){
            MPConstraint GNEG2 = solver.makeConstraint(1, 1, "");
            GNEG2.setCoefficient(enroll[lookup("GNEG 1121")][1], 1);
        }

        //Constraint for Capstone | Capstone II must be in the semester after Capstone I
        //Check if both classes remain
        if ((lookup("INEG 4913") != -1) && (lookup("INEG 4924") != -1)) {
            MPConstraint capstone = solver.makeConstraint(1, 1, "");
            for (int j = 1; j <= numSemesters; j++) {
                capstone.setCoefficient(enroll[lookup("INEG 4924")][j], j);
                capstone.setCoefficient(enroll[lookup("INEG 4913")][j], -j);
            }
        }

        //Constraint that the total hours of a semester is less than max hours but more than min
        for (int j = 1; j <= numSemesters; j++) {
            MPConstraint maxEnroll = solver.makeConstraint(minHours, maxHours, "");
            for (int i = 0; i < curriculum.size(); i++) {
                maxEnroll.setCoefficient(enroll[i][j], curriculum.get(i).hours);
            }
        }

        //Constraint for the courses that the user has preChosen
        for(int i = 0; i < curriculum.size(); i++){
            if(curriculum.get(i).prePlace != 0){
                MPConstraint prePlace = solver.makeConstraint(1,1,"");
                prePlace.setCoefficient(enroll[i][curriculum.get(i).prePlace], 1);
            }
        }

        //Constraint for Fall Spring placement
        for (int i = 0; i < curriculum.size(); i++) {
            //Case for fall start
            if (fallStart) {
                if (curriculum.get(i).fallOnly) { //if a class if fall only
                    MPConstraint fallOnly = solver.makeConstraint(1, 1, ""); //the class must be placed in odd semester
                    for (int j = 1; j <= numSemesters; j++) { //For each semester
                        if (j % 2 == 1) { //if the semester is odd
                            fallOnly.setCoefficient(enroll[i][j], 1); //add the decision variable
                        }
                    }
                } else if (curriculum.get(i).springOnly) {
                    MPConstraint springOnly = solver.makeConstraint(1, 1, ""); //the class must be placed in odd semester
                    for (int j = 1; j <= numSemesters; j++) { //For each semester
                        if (j % 2 == 0) { //if the semester is even
                            springOnly.setCoefficient(enroll[i][j], 1); //add the decision variable
                        }
                    }
                }
            } else { //Spring Start
                if (curriculum.get(i).fallOnly) { //if a class if fall only
                    MPConstraint fallOnly = solver.makeConstraint(1, 1, ""); //the class must be placed in odd semester
                    for (int j = 1; j <= numSemesters; j++) { //For each semester
                        if (j % 2 == 0) { //if the semester is even
                            fallOnly.setCoefficient(enroll[i][j], 1); //add the decision variable
                        }
                    }
                } else if (curriculum.get(i).springOnly) {
                    MPConstraint springOnly = solver.makeConstraint(1, 1, ""); //the class must be placed in odd semester
                    for (int j = 1; j <= numSemesters; j++) { //For each semester
                        if (j % 2 == 1) { //if the semester is odd
                            springOnly.setCoefficient(enroll[i][j], 1); //add the decision variable
                        }
                    }
                }
            }
        }
    }

    public void minMaxObjective(){
        //Create the decision variables for every course in the curriculum array
        MPVariable maxDifficulty = solver.makeNumVar(0, MPSolver.infinity(), "");
        //Add the constraint
        for (int j = 1; j <= numSemesters; j++) {
            MPConstraint maxDiff = solver.makeConstraint(0, MPSolver.infinity(), "");
            maxDiff.setCoefficient(maxDifficulty, 1);
            for (int i = 0; i < curriculum.size(); i++) {
                maxDiff.setCoefficient(enroll[i][j], -curriculum.get(i).difficulty);
            }
        }
        //Create the objective
        objective = solver.objective();
        objective.setCoefficient(maxDifficulty, 1);

        objective.setMinimization();
    }

    public void minLastYearObjective(){
        objective = solver.objective();
        for(int j = numSemesters - 1; j <= numSemesters; j++){
            for(int i = 0; i < curriculum.size(); i++){
                objective.setCoefficient(enroll[i][j], curriculum.get(i).difficulty);
            }
        }
        objective.setMinimization();
    }

    public void minSemestersObjective(){
        //Get rid of how many min hours
        this.minHours = 0;

        //Add new decision variable
        MPVariable maxSemester = solver.makeNumVar(0, MPSolver.infinity(), "");

        //Add a new constraint
        for (int s = 1; s <= numSemesters; s++) {
            for (int i = 0; i < curriculum.size(); i++) {
                MPConstraint maxSem = solver.makeConstraint(0, MPSolver.infinity(), "");
                maxSem.setCoefficient(maxSemester, 1);
                maxSem.setCoefficient(enroll[i][s], -s);
            }
        }

        //Add Objective
        objective = solver.objective();
        objective.setCoefficient(maxSemester, 1);
        objective.setMinimization();


    }

    public void addObjective(){
        if(Objects.equals(this.objectiveChoice, "Min Max Difficulty Semester")) {
            this.minMaxObjective();
        } else if(Objects.equals(this.objectiveChoice, "Min Difficulty of Last Year")) {
            this.minLastYearObjective();
        } else if(Objects.equals(this.objectiveChoice, "Min Semesters")) {
            System.out.println("Min Semesters");
            this.minSemestersObjective();
        }
    }

    public boolean solveModel(){
        resultStatus = solver.solve(); //Solve the Model

        if (resultStatus == MPSolver.ResultStatus.OPTIMAL || resultStatus == MPSolver.ResultStatus.FEASIBLE) {
            System.out.println("Objective Value: " + solver.objective().value());
            for(int j = 1; j <= numSemesters; j++){
                for(int i = 0; i < curriculum.size(); i++){
                    if(enroll[i][j].solutionValue() > 0.5){
                        curriculum.get(i).enrolledSemester = j; //write the results to the course
                    }
                }
            }
            objective.clear();
            return true;
        } else {
            objective.clear();
            return false; //No Solution
        }
    }

    //Method to execute model
    public boolean runModel(){
        //Initialize Blank Solver
        Loader.loadNativeLibraries();
        solver = MPSolver.createSolver("SCIP");
        //check if the solver has been initialized
        if (solver == null) {
        System.out.println("Could not create solver SCIP");
            return false;
        }
        //Add the components of the model
        addDecisionVariables();
        addObjective();
        addConstraints();
        return(solveModel()); //Solve the model and return if it solved
    }
}