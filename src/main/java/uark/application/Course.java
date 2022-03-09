package uark.application;

import java.util.ArrayList;

public class Course {

    //Class variables
    String department;
    String name;
    int num, hours, difficulty, enrolledSemester, prePlace;
    boolean fallOnly, springOnly;
    ArrayList<String> preReq;
    ArrayList<String> coReq;

    //Constructor for course with pre and co reqs
    Course(String department, int num, int difficulty, boolean fallOnly, boolean springOnly, String[] pre, String[] co){
        preReq = new ArrayList<String>();
        coReq = new ArrayList<String>();
        this.name = department + " " + num;
        this.department = department;
        this.num = num;
        this.hours = num % 10;
        this.difficulty = difficulty;
        this.fallOnly = fallOnly;
        this.springOnly = springOnly;
        for(int i = 0; i < pre.length; i++){
            this.preReq.add(pre[i]);
        }
        for(int i = 0; i < co.length; i++){
            this.coReq.add(co[i]);
        }
        this.enrolledSemester = 0;
        this.prePlace = 0;
    }

    //Constructor for class with no co reqs
    Course(String department, int num, int difficulty, boolean fallOnly, boolean springOnly, String[] pre){

        preReq = new ArrayList<String>();
        coReq = new ArrayList<String>();
        this.department = department;
        this.name = department + " " + num;
        this.num = num;
        this.hours = num % 10;
        this.difficulty = difficulty;
        this.fallOnly = fallOnly;
        this.springOnly = springOnly;
        for(int i = 0; i < pre.length; i++){
            this.preReq.add(pre[i]);
        }
        this.enrolledSemester = 0;
        this.prePlace = 0;
    }

    //Constructor for class with no pre or co reqs
    Course(String department, int num, int difficulty, boolean fallOnly, boolean springOnly){

        preReq = new ArrayList<String>();
        coReq = new ArrayList<String>();
        this.department = department;
        this.name = department + " " + num;
        this.num = num;
        this.hours = num % 10;
        this.difficulty = difficulty;
        this.fallOnly = fallOnly;
        this.springOnly= springOnly;
        this.enrolledSemester = 0;
        this.prePlace = 0;
    }

    //Constructor for class with no pre or co reqs, or course number (placeholder classes)
    Course(String name, int difficulty, boolean fallOnly, boolean springOnly){

        preReq = new ArrayList<String>();
        coReq = new ArrayList<String>();
        this.department = name;
        this.name = name;
        this.num = 1003;
        this.hours = 3;
        this.difficulty = difficulty;
        this.fallOnly = fallOnly;
        this.springOnly= springOnly;
        this.enrolledSemester = 0;
        this.prePlace = 0;
    }
    //Constructor for class with no pre or co reqs, or course number (placeholder classes)
    Course(String name, int difficulty, boolean fallOnly, boolean springOnly,String[] pre){

        preReq = new ArrayList<String>();
        coReq = new ArrayList<String>();
        this.department = name;
        this.name = name;
        this.num = 1003;
        this.hours = 3;
        this.difficulty = difficulty;
        this.fallOnly = fallOnly;
        this.springOnly= springOnly;
        this.enrolledSemester = 0;
        this.prePlace = 0;
        for(int i = 0; i < pre.length; i++){
            this.preReq.add(pre[i]);
        }
    }


    //Unmarshal Constructor
    Course(Json ob){
        preReq = new ArrayList<String>();
        coReq = new ArrayList<String>();
        this.department = ob.getString("department");
        this.name = ob.getString("name");
        this.num = (int)ob.getLong("num");
        this.difficulty = (int)ob.getLong("difficulty");
        this.fallOnly = ob.getBool("fallOnly");
        this.springOnly = ob.getBool("springOnly");
        this.hours = this.num % 10;

        Json preReqList = ob.get("preReq");
        for(int i = 0; i < preReqList.size(); i++){
            preReq.add(preReqList.get(i).asString());
        }

        Json coReqList = ob.get("coReq");
        for(int i = 0; i < coReqList.size(); i++){
            coReq.add(coReqList.get(i).asString());
        }
        this.enrolledSemester = 0;
        this.prePlace = 0;
    }

    //Getters to Populate Data
    public String getName(){ return this.name;}
    public int getEnrolledSemester(){return this.enrolledSemester;}


    //Method to print a course out
    @Override
    public String toString() {
        return("Course Name: " + name + " Course Number: " + num + " Course Hours: " + hours + " diffculty: " + difficulty +
                " fall only: " + fallOnly + " spring only: " + springOnly + " num preReq: " + preReq.size() + " num coReq " +
                coReq.size());
    }

    //Method to Marshal
    public Json marshal(){
        //Declare objects and Lists
        Json ob = Json.newObject();
        Json preReqList = Json.newList();
        Json coReqList = Json.newList();

        //Add Data to Json object
        ob.add("department", this.department);
        ob.add("num", this.num);
        ob.add("name", this.name);
        ob.add("difficulty", this.difficulty);
        ob.add("fallOnly", this.fallOnly);
        ob.add("springOnly", this.springOnly);
        ob.add("preReq", preReqList);
        ob.add("coReq", coReqList);
        for(int i = 0; i < preReq.size(); i++){
            preReqList.add(preReq.get(i));
        }
        for(int i = 0; i < coReq.size(); i++){
            coReqList.add(coReq.get(i));
        }


        //Return Object
        return ob;
    }
}
