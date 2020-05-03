/**
 * Main controller class to interact with the gui Planner
 * 
 * 
 * 
 * @author Kevin ioi - 0848838
 * @version Nov 2018
 */


import java.io.*;
import java.util.*;
import univ.*;

public class Controller
{
  Student theStudent;
  CourseCatalog cc;
  HashMap<String, Degree> degrees;

  /**
   * zero parameter contructor
   * initalizes all vital elements
   */
  public Controller()
  {
    theStudent = new Student();
    cc = new CourseCatalog();

    //degrees are hardcoded in this assignemnt...
    degrees = new HashMap<String, Degree>();
    degrees.put("GeneralDegree:BCG",new BCG());
    degrees.put("HonoursDegree:SEng",new SEng());
    degrees.put("HonoursDegree:CS",new CS());
  }

  /**
   * Adds an attempt for a course to the student's transcript
   * 
   * @param code code of course completed
   * @param semester semester it was taken
   * @param grade grade recieved
   * @throws CourseNotFoundException course is not offered
   * @throws InvalidChoiceException course not offered in that semester
   * @throws NumberFormatException bad grade provided
   */
  public void completeCourse(String code, String semester, String grade) throws CourseNotFoundException, InvalidChoiceException
  {
    Course completed = cc.findCourse(code);

    if(completed == null)
      throw new CourseNotFoundException();
    
    if(semester.contains("F")||semester.contains("f"))//wants to take it in the fall
    {
      if(!(completed.getSemesterOffered().equals("F")) && !(completed.getSemesterOffered().equals("B")))//can't take winter course in the fall
      {
        throw new InvalidChoiceException();
      }
    }
    else
    {
      if(!(completed.getSemesterOffered().equals("W")) && !(completed.getSemesterOffered().equals("B")))//can't take winter course in the fall
      {
        throw new InvalidChoiceException();
      }
    }

    theStudent.addToTranscript(completed, semester, grade);

  }

  public ArrayList<String> getOfferedCourses()
  {
    ArrayList<String> courseStr = new ArrayList<String>();
    ArrayList<Course> courses = cc.getCourseList();

    for (Course c : courses) {
      courseStr.add(c.toString());
    }
    return courseStr;
  }

  /**
   * Adds a course to the course plan of the student
   * 
   * 
   * @param code course code to be added
   * @param semester semester it's to be taken
   * @throws CourseNotFoundException if the specified course it not availble 
   * @throws InvalidChoiceException in the course is not available in that semester
   */
  public void planCourse(String code, String semester) throws CourseNotFoundException, InvalidChoiceException
  {
    Course planned = cc.findCourse(code);

    if(planned == null)
      throw new CourseNotFoundException();
    
    if(semester.contains("F")||semester.contains("f"))//wants to take it in the fall
    {
      if(planned.getSemesterOffered().equals("W") ||planned.getSemesterOffered().equals("w"))//can't take winter course in the fall
        throw new InvalidChoiceException();
    }
    else
    {
      if(planned.getSemesterOffered().equals("F") || planned.getSemesterOffered().equals("f"))//can't take winter course in the fall
        throw new InvalidChoiceException();
    }

    theStudent.addToPlan(planned, semester);
  }

  /**
   * remove planned course
   * 
   * 
   * @param code code of planned course
   * @param semester semester course was planned for
   * @throws CourseNotFoundException if course was not in plan
   */
  public void removeFromPlan(String code, String semester) throws CourseNotFoundException
  {
    try {
      theStudent.removeFromPlan(code, semester);
    } catch (CourseNotFoundException e) {
      throw e;
    }
  }

  /**
   * removes a course form the trascript
   * 
   * @param code code of course to be removed
   * @param semester semester course was taken
   * @throws CourseNotFoundException if course was not in transcript
   */
  public void removeFromTranscript(String code, String semester) throws CourseNotFoundException
  {
    try {
      theStudent.removeFromTranscript(code, semester);
    } catch (CourseNotFoundException e) {
      throw e;
    }
  }

  /**
   * drops the current student
   */
  public void dropStudent()
  {
    theStudent = null;
  }

  /**
   * updates the grade of a course in the transcript
   * 
   * @param code code of course being updated
   * @param semester semester course was taken
   * @param grade grade recieved
   * @throws CourseNotFoundException course not in transcript
   * @throws NumberFormatException bad grade provided
   */
  public void updateTranscript(String code, String semester, String grade) throws CourseNotFoundException, NumberFormatException
  {
    try {
      Integer.valueOf(grade);
      theStudent.updateAttemptGrade(code, semester, grade);
    } catch (CourseNotFoundException e) {
      throw e;}
    catch(NumberFormatException nfe){
      throw nfe;}
  }

  /**
   * gets a list of available degrees
   * 
   * @return
   */
  public Set<String> getDegreeOptions()
  {
    return degrees.keySet();
  }

  /**
   * Getter for the full title of the student's degree (Honours Degree Computer Science) 
   * 
   * @return
   */
  public String getStudentDegree()
  {
    Degree deg = degrees.get(theStudent.getDegree());
    return deg.getDegreeName() + " " + deg.getProgramName();
  }

  /**
   * gets all courses that are not in the students plan but are required for their degree
   *  
   * 
   * @return arraylist of course codes
  */
  public ArrayList<String> getMissingCourses()
  {
    ArrayList<String> courseStr = new ArrayList<String>();
    ArrayList<Course> courses;

    courses = degrees.get(theStudent.getDegree()).remainingRequiredCourses(theStudent.getAllCourses());
    
    for (Course c : courses)
    {
      courseStr.add(c.getCourseCode());  
    }

    return courseStr;
  }

  /**
   * gets a list of courses that are required for the students degree
   * but are not in their trancript
   * 
   * @return 
   */
  public ArrayList<String> getIncompleteReqs()
  {
    ArrayList<Attempt> attempts = theStudent.getTranscript(); 
    ArrayList<String> courseStr = new ArrayList<String>();
    ArrayList<Course> courses = new ArrayList<Course>();
    ArrayList<Course> missingCourses;

    for (Attempt a : attempts) {
      courses.add(a.getCourseAttempted());
    }

    missingCourses = degrees.get(theStudent.getDegree()).remainingRequiredCourses(courses);

    for (Course c : missingCourses) {
      courseStr.add(c.getCourseCode());
    }

    return courseStr;
  }

  /**
   * finds the number of credits needed to be added for the plan to meet the degree requirments
   * 
   * @return
   */
  public Double creditsNeeded()
  {
    ArrayList<Course> courses = theStudent.getAllCourses();
    Double credits;
    credits = degrees.get(theStudent.getDegree()).numberOfCreditsRemaining(courses);

    return credits;
  } 

  /**
   * gets a String of the prereq course codes for a course
   * 
   * @param code the course to look up for prereqs
   * @throws CourseNotFoundException
   */
  public String getPreReqs(String code) throws CourseNotFoundException
  {
    Course theCourse;
    theCourse = cc.findCourse(code);//find course in database

    if (theCourse == null)//course wasn't found, throw exception
      throw new CourseNotFoundException();

    return theCourse.getPrereqsStr();
  }

  /**
   * setter for the student's degree
   * 
   */
  public void setStudentDegree(String deg) throws DegreeNotFoundException
  {
    if(degrees.get(deg) != null)
      theStudent.setDegree(deg);
    else
      throw new DegreeNotFoundException();
  }

  /**
   * creates a new student
   * 
   * @param fname student's first name
   * @param lname student's last name
   * @param id student's id number
   */
  public void createStudent(String fname, String lname, String id)
  {
    theStudent = new Student(fname, lname, id);
  }

  /**
   * gets the student's full name
   * @return the student's full name
   */
  public String getStudentName()
  {
    if(theStudent != null)
      return theStudent.getFullName();
    else
      return null;
  }

  /**
   * loads this student from a mysql database
   * 
   * @param fname firstname to use in query for student
   * @param lname last name to use in query for student
   * @param id id number to use in query for student
   * @throws StudentNotFoundException if the student isn't found
   */
  public void loadStudent(String fname, String lname, String id) throws StudentNotFoundException
  {
    theStudent = new Student(fname, lname, id);

    try {
      theStudent.initializeInfo();
    } catch (StudentNotFoundException e) {
      throw e;
    }
  }

  /**
   * overwrites/saves this student object to the mysql database
   * 
   */
  public void saveStudent()
  {
    MyConnection mc = new MyConnection();

    mc.deleteSavedStudent(String.valueOf(theStudent.getStudentNumber()), theStudent.getFullName());
    DBStudent save = new DBStudent(String.valueOf(theStudent.getStudentNumber()), theStudent.getFullName(), theStudent.getDegree(),theStudent.getAllAttemptsStr());
    mc.deleteSavedStudent(String.valueOf(theStudent.getStudentNumber()), theStudent.getFullName());
    mc.saveStudent(save);
  }

  /**
   * gets an array List of strings representing all transcript entries
   * 
   * @return array list of strings for transcript entries
   */
  public ArrayList<String> getTranscript()
  {
    ArrayList<Attempt> tranAtt = theStudent.getTranscript();
    ArrayList<String> tranStr = new ArrayList<String>();

    for (Attempt att : tranAtt){
      tranStr.add(att.getCourseCode() + " " + att.getSemesterTaken() + "-" + att.getAttemptGrade());
    }

    return tranStr;
  }

  public ArrayList<String> getPlannedCourses()
  {
    ArrayList<Attempt> planAtt = theStudent.getPlannedCourses();
    ArrayList<String> planStr = new ArrayList<String>();

    for (Attempt att : planAtt){
      planStr.add(att.getCourseCode() + " " + att.getSemesterTaken());
    }

    return planStr;
  }

  public ArrayList<String> getAllPrereqsForPlan()
  {
    ArrayList<Attempt> planAtt = theStudent.getPlannedCourses();
    ArrayList<String> prereqsStr = new ArrayList<String>();
    String prereqs;

    for (Attempt att : planAtt){
      prereqs = att.getCourseAttempted().getPrereqsStr();

      if(prereqs.length()>0)
      {
        prereqsStr.add(att.getCourseCode() + " requires: " + prereqs);
      }
    }

    return prereqsStr; 
  }

  /**
   * counts the number of credits earned on the student's transcript
   * 
   * @return
   */
  public Double countCreditsEarned()
  {
    Double total = 0.0;
    ArrayList<Attempt> transcript;

    transcript = theStudent.getTranscript();

    for (Attempt a : transcript) {
      total += a.getCourseAttempted().getCourseCredit();
    }
    
    return total;
  }

  /**
   * counts the number of credits planned
   * 
   * @return
   */
  public Double countCreditsPlanned()
  {
    Double total = 0.0;
    ArrayList<Attempt> planned;
    planned = theStudent.getPlannedCourses();

    for (Attempt a : planned) {
      total += a.getCourseAttempted().getCourseCredit();
    }

    return total;
  }

  /**
   * checks if the student's plan satisfies their degree requirements
   * 
   * @return
   */
  public boolean evaluatePlan()
  {
    ArrayList<Course> courses = theStudent.getAllCourses();//get all courses in plan
    return degrees.get(theStudent.getDegree()).meetsRequirements(courses);//submit them to degree to check
  }

  /**
   * get list of attempted and planned courses chronologically ordered
   *  
   * @return Arraylist of Strings containing planned information (Format: 'semester,code,grade')
   */
  public ArrayList<String> getCoursePlan()
  {
    ArrayList<String> sortedCourses = new ArrayList<String>();//string to provide to front end
    ArrayList<Attempt> attempts = theStudent.getAllAttempts();//get all attempts planned and completed

    //sort the attempts by semester taken, using anonomous comparator class method
    Collections.sort(attempts, new Comparator<Attempt>() {
      public int compare(Attempt one, Attempt two)
      {
        //get semester values (Season/Year)
        String semOne = one.getSemesterTaken();
        String semTwo = two.getSemesterTaken();

        //extract Year
        Integer semOneYr = Integer.valueOf(semOne.substring(1, semOne.length()));
        Integer semTwoYr = Integer.valueOf(semTwo.substring(1, semTwo.length()));

        if (semOneYr>semTwoYr)//first taken in later year
        {
         return -1; 
        }
        else if(semOneYr<semTwoYr)//first taken in earlier year
        {
          return 1;
        }
        else if(semOne.startsWith("W") || semOne.startsWith("w"))//Taken in same year, first taken in winter
        {
          if(semTwo.startsWith("F") || semTwo.startsWith("f"))//second taken in fall
            return 1;
          else
            return 0;
        }
        else if(semOne.startsWith("F") || semOne.startsWith("f"))//Taken in same year, first taken in fall
        {
          if(semTwo.startsWith("W") || semTwo.startsWith("w"))//second taken in winter
            return -1;
          else
            return 0;
        }
        else//whaaaa
          return 0;
      }});

      //extract relevent information
      for (Attempt a : attempts) {
        sortedCourses.add(a.getSemesterTaken() + "," + a.getCourseCode() + "," + a.getAttemptGrade());
      }

    return sortedCourses;
  }

  /**
   * Calculcates the students gpa for all of their completed courses 
   * 
   * @return
   */
  public Double calculateGPA() throws InvalidChoiceException
  { 
    try {
      return calculateGPA(null);
    } catch (InvalidChoiceException e) {
      throw e;
    }
  }

  /**
   * calculates the student's total gpa in specified course type
   * if null is provided all courses will be considered 
   * 
   * 
   * @param deptCode deptartment code to be evaluated
   * @return
   */
  public Double calculateGPA(String deptCode) throws InvalidChoiceException
  {
    ArrayList<Attempt> attempts = theStudent.getTranscript();
    Integer grades = 0;
    Integer count = 0;

    //go through all entries in transcipt
    for (Attempt a : attempts) {
      if(deptCode == null || a.getCourseCode().contains(deptCode))//if the course is the correct code, null calculates all courses
      {        
        try {
          grades += Integer.valueOf(a.getAttemptGrade());//get grade earned     
          count++;
        } catch (NumberFormatException e) {
          continue;
        }
      }
    }

    if(count == 0)
      throw new InvalidChoiceException();

    return (grades*1.0)/(count*1.0);//return avg
  }

  /**
   * calculates the GPA of the student's 10 most recent courses
   * 
   * @return Double value of grade averagae
   * @throws InvalidChoiceException there were less than 10 courses completed
   */
  public Double calcRecentGPA() throws InvalidChoiceException
  {
    ArrayList<Attempt> courses = theStudent.getTranscript();
    Integer courseCount = 0;
    Integer grades = 0;
    String courseInfo[];

    for (Attempt att : courses)
    {
        try {
          grades += Integer.valueOf(att.getAttemptGrade());//get grade earned
          courseCount++;//count course
      } catch (NumberFormatException e) {
        continue;
      }
      if(courseCount == 10)//exit loop when 10 courses have been found
        break;
    }

    if (courseCount<10)
      throw new InvalidChoiceException();

    return (grades*1.0)/(courseCount*1.0);//return avg
  }

  /**
   * Overridden equals method
   * @param obj student to compare to
   * @return 
   */
  public boolean equals(Student obj) {
    return false;//i don't care
  }

  /**
   * overriden toString method
   * @return String value of controller
   */
  public String toString() {
    return "Controller";
  }
}