/**
  * The Student class represents a single student. Holds the student's first, last name
  * ,student number, degree name, and information regarding their transcript and planned courses
  *
  * @author Kevin Ioi
  * @version Oct, 2018
**/

import java.io.*;
import java.util.*;

import univ.*;

public class Student
{
  private String firstName;//student's first name
  private String lastName;//student's last name
  private String sNum;//student number
  private String degree;//string name of program/degree

  private ArrayList<Attempt> transcript;
  private ArrayList<Attempt> planned;

  /**
  * Zero parameter constructor
  */
  public Student()
  {
    setFirstName("n/a");
    setLastName("n/a");
    setStudentNumber(0);
    setDegree("n/a");

    transcript = new ArrayList<Attempt>();
    planned = new ArrayList<Attempt>();
  }

  public Student(String fName, String lName, String sNum)
  {
    this.firstName = fName;
    this.lastName = lName;
    this.sNum = sNum;
    this.degree = "n/a";

    transcript = new ArrayList<Attempt>();
    planned = new ArrayList<Attempt>();
  }

  /**
  * Four parameter constructor
  *
  * @param fName string to use as the first name
  * @param lName string to use as the last name
  * @param sNum  integer to use as the student number
  * @param deg string value of degree
  */
  public Student(String fName, String lName, String sNum, String deg)
  {
    this.firstName = fName;
    this.lastName = lName;
    this.sNum = sNum;
    this.degree = deg;

    transcript = new ArrayList<Attempt>();
    planned = new ArrayList<Attempt>();
  }

  /**
   * Loads student information from database
   * 
   */
  public void initializeInfo() throws StudentNotFoundException
  {
    MyConnection c = new MyConnection(DBDetails.username, DBDetails.password);
    DBStudent dbStud = c.loadStudent(this.sNum, this.firstName + " " + this.lastName); 

    if(dbStud.getId() == null)
      throw new StudentNotFoundException("ERROR STUDENT NOT IN DATABASE");

    String name = dbStud.getName();
    this.firstName = name.split(" ")[0];
    this.lastName = name.split(" ")[1];
    this.sNum = dbStud.getId();
    setDegree(dbStud.getDegree());
    setPlan(dbStud.getCourses());
  }


  /**
   * Adds a course to planned courses
   * 
   * @param course course to be planned
   * @param semester semester planned to take the course
   */
  public void addToPlan(Course course, String semester)
  {
    ListIterator<Attempt> itr = planned.listIterator();
    Attempt attPtr;
    while(itr.hasNext())//remove course if it was planned, we'll update it
    {
      attPtr = itr.next();

      if(attPtr.getCourseAttempted().equals(course) && attPtr.getSemesterTaken().equals(semester))
        itr.remove();
    }

    planned.add(new Attempt(course, semester, "planned"));
  }

  /**
   * Adds course to transcript
   * 
   * 
   * @param course the course that was completed
   * @param semester the semester it was taken
   * @param grade grade recieved
   */
  public void addToTranscript(Course course, String semester, String grade)
  {
    ListIterator<Attempt> itr = planned.listIterator();
    Attempt attPtr;

    while(itr.hasNext())//remove course if it was planned, we're moving it to transcript
    {
      attPtr = itr.next();

      if(attPtr.getCourseAttempted().equals(course) && attPtr.getSemesterTaken().equals(semester))
        itr.remove();
    }

    itr = transcript.listIterator();
    while(itr.hasNext())//remove course if it was already taken, we'll update it
    {
      attPtr = itr.next();

      if(attPtr.getCourseAttempted().equals(course) && attPtr.getSemesterTaken().equals(semester))
        itr.remove();
    }

    transcript.add(new Attempt(course, semester, grade, "complete"));
  }

  /**
   * removes a course that was planned
   * 
   * @param code code of course to be removed
   * @param semester semester it was taken
   */
  public void removeFromPlan(String code, String semester) throws CourseNotFoundException
  {
    ListIterator<Attempt> itr = planned.listIterator();
    Attempt attPtr;

    while(itr.hasNext())//remove course if it was planned
    {
      attPtr = itr.next();

      if(attPtr.getCourseAttempted().getCourseCode().equals(code) && attPtr.getSemesterTaken().equals(semester))
      {
        itr.remove();
        return;
      }
    }

    throw new CourseNotFoundException();
  } 

    /**
   * removes a course that was planned
   * 
   * @param code code of the course to be removed
   * @param semester semester it was taken
   */
  
   public void removeFromTranscript(String code, String semester) throws CourseNotFoundException
  {
    ListIterator<Attempt> itr = transcript.listIterator();
    Attempt attPtr;

    while(itr.hasNext())//remove course if it was planned
    {
      attPtr = itr.next();

      if(attPtr.getCourseAttempted().getCourseCode().equals(code) && attPtr.getSemesterTaken().equals(semester))
      {
        itr.remove();
        return;
      }
    }

    throw new CourseNotFoundException();
  } 

  /**
   * updates the grade of an attempt in transcript
   * 
   * 
   * @param code course code to search for
   * @param semester semester to search for
   * @param grade grade to update to
   * @throws CourseNotFoundException if the course is not in transcript
   */
  public void updateAttemptGrade(String code, String semester, String grade) throws CourseNotFoundException
  {
    boolean found = false;
    
    for (Attempt att : transcript)
    {
      if(att.getCourseCode().equals(code) && att.getSemesterTaken().equals(semester))
      {
        att.setAttemptGrade(grade);
        found = true;  
        break;
      }
    }

    if(found == false)
      throw new CourseNotFoundException();
  }
  
  /**
   * updates the grade of an attempt in transcript
   * overloaded to handle integer grades
   * 
   * @param code course code to search for
   * @param semester semester to search for
   * @param grade grade to update to
   * @throws CourseNotFoundException if the course is not in transcript
   */
  public void updateAttemptGrade(String code, String semester, Integer grade) throws CourseNotFoundException
  {
    boolean found = false;
    
    for (Attempt att : transcript)
    {
      if(att.getCourseCode().equals(code) && att.getSemesterTaken().equals(semester))
      {
        att.setAttemptGrade(grade);
        found = true;  
        break;
      }
    }

    if(found == false)
      throw new CourseNotFoundException();
  }

  /**
   * Setter for transcript and planned courses
   * 
   * 
   * @param courses arrayList of attempt information in String format
   */
  public void setPlan(ArrayList<String> courses)
  {
    for (String str : courses)
    {
      String strsplit[] = str.split(";");
      String infoSplit[] = strsplit[0].split(",");

      Attempt nextAtt = new Attempt(strsplit[1], infoSplit[1], infoSplit[2], infoSplit[0]);
      
      if(nextAtt.getStatus().equals("complete"))
        transcript.add(nextAtt);
      else
        planned.add(nextAtt);
    }
  }

  /**
   * gets an arraylist of the attempts planned and taken in string form
   * 
   * @return arraylist of all attempts planned and taken
   */
  public ArrayList<String> getAllAttemptsStr()
  {
    ArrayList<String> list = new ArrayList<String>();

    for (Attempt att: this.planned) {
      list.add(att.toString());
    }

    for (Attempt a : this.transcript) {
      list.add(a.toString());
    }

    return list;
  }

  public ArrayList<Attempt> getAllAttempts()
  {
    ArrayList<Attempt> list = new ArrayList<Attempt>();

    for (Attempt att: this.planned) {
      list.add(att);
    }

    for (Attempt a : this.transcript) {
      list.add(a);
    }

    return list;
  }

  /**
   * gets an arraylist of all courses planned and taken 
   * 
   * @return arraylist of all courses planned and taken
   */
  public ArrayList<Course> getAllCourses()
  {
    ArrayList<Course> list = new ArrayList<Course>();

    for (Attempt att: this.planned) {
      list.add(att.getCourseAttempted());
    }

    for (Attempt a : this.transcript) {
      list.add(a.getCourseAttempted());
    }

    return list;
  }

  /**
   * getter for the list of planned courses
   *  
   * @return arraylist containined all planned attempts
   */
  public ArrayList<Attempt> getPlannedCourses()
  {
    return this.planned;
  }

  /**
   * Getter for transcript information
   * 
   * 
   * @return arraylist containing trancript informatin
   */
  public ArrayList<Attempt> getTranscript()
  {
    return this.transcript;
  }

  /**
   * setter for the degree information
   * 
   * @param degree String form of degree title
   */
  public void setDegree(String deg)
  {
    this.degree = deg;
  }

  /**
   * getter for the degre information
   * 
   * @return String form of the degree title
   */
  public String getDegree()
  {
    return this.degree;  
  }
  
  /**
   * constructs and provides the student's full name
   *
   * @return last name concatenated onto first name was a whitespace character dividing them
   */
  public String getFullName()
  {
    String fullName;
    fullName = firstName + " " + lastName;
    return fullName;
  }

  /**
   * mutator method to change the student's firstName
   *
   * @param first string to set the first name as
   */
  public void setFirstName(String first)
  {
    if (first != null)
      this.firstName = first;
    else
      this.firstName = "n/a";
  }

  /**
   * mutator method to change the student's last name
   *
   * @param last string to set the last name
   */
  public void setLastName(String last)
  {
    if (last != null)
      this.lastName = last;
    else
      this.lastName = "n/a";
  }

  /**
   * accessor method to for firstName
   *
   * @return string value of firstName element
   */
  public String getFirstName()
  {
    return this.firstName;
  }

  /**
   * accessor method to for lastName
   *
   * @return string value of lastName element
   */
  public String getLastName()
  {
    return this.lastName;
  }

  /**
   * mutator method to change the student number
   *
   * @param studentNum integer to set the student number
   */
  public void setStudentNumber(Integer studentNum)
  {
    this.sNum = studentNum.toString();
  }

  /**
   * accessor method to for studentNum
   *
   * @return integer value of studentNum
   */
  public Integer getStudentNumber()
  {
    return Integer.parseInt(this.sNum);
  }

  /**
   * Checks if a provided Student object is equivalent to this student
   *
   * @param s Student object to compare against
   * @return true if equivalent, false if not
   */
  boolean equals(Student s)
  {
    if (!(this.sNum.equals(s.sNum)))
      return false;

    return true;
  }

}
