/**
 * Attempt class
 *
 * @author Kevin Ioi
 * @version Nov, 2018
**/

import java.util.*;
import java.io.*;
import univ.*;

public class Attempt
{
  private String semesterTaken;//semester of attempt
  private String attemptStatus;//course status (complete, planned)
  private String attemptGrade;//grade recieved, empty string if no grade entered, always integer value stored in string format
  private Course attemptedCourse;//reference to course being attempted

  /**
   * Zero parameter constructor
  */
  public Attempt()
  {
    this(new Course(), "", "");
  }

  /**
   * 3 parameter constructor
   * 
   * @param attemped course being attempted
   * @param semester semester it is being attempted in
   * @param status status of the course (planned, completed)
   */
  public Attempt(Course attempted, String semester, String status)
  {
    setCourseAttempted(attempted);
    setSemesterTaken(semester);
    setStatus(status);
    setAttemptGrade(-1);
  }


  public Attempt(Course attempted, String semester, String grade, String status)
  {
    setCourseAttempted(attempted);
    setSemesterTaken(semester);
    setStatus(status);
    setAttemptGrade(grade);
  }


  public Attempt(String attempted, String semester, String status, Integer grade)
  {
    setCourseAttempted(new Course(attempted));
    setSemesterTaken(semester);
    setStatus(status);
    setAttemptGrade(grade);
  }

  public Attempt(String attempted, String semester, String status, String grade)
  {
    setCourseAttempted(new Course(attempted));
    setSemesterTaken(semester);
    setStatus(status);
    setAttemptGrade(grade);
  }

  /**
   * Accessor method for the courseCode variable
   *
   * @return String value of the courseCode
   */
  public String getCourseCode()   
  {
    if(this.attemptedCourse == null)
      return null;
    
    return this.attemptedCourse.getCourseCode();
  }


  /**
   * Accessor method for the courseTitle variable
   *
   * @return String value of the course title held in Course
   */
  public String getCourseTitle()
  {
    return this.attemptedCourse.getCourseTitle();
  }


  /**
   * Accessor method for the preReqsList variable
   *
   * @return ArrayList containing the list of prerequisite courses
   */
  public ArrayList<Course> getPrerequisites()
  {
    return this.getCourseAttempted().getPrerequisites();
  }

    /**
   * Accessor method for a String representing the preReqsList variable in Course 
   *
   * @return String containing the course codes for the prereq courses of the affiliated course, delimited by colons 
   */
  public String getPrereqsStr()
  {
    return this.attemptedCourse.getPrereqsStr();
  }

  /**
   * mutator method for the AttemptGrade variable
   *
   * @param grade Integer value to set the AttemptGrade to
   */
  public void setAttemptGrade(Integer grade)
  {
    this.attemptGrade = grade.toString();
  }

  /**
   * mutator method for the AttemptGrade variable
   *
   * @param grade string value to set the AttemptGrade to
   */
  public void setAttemptGrade(String grade)
  {
    this.attemptGrade = grade;
  }

  /**
   * Accessor method for the AttemptGrade variable
   *
   * @return String value of the AttemptGrade
   */
  public String getAttemptGrade()
  {
    return this.attemptGrade;
  }

  /**
   * setter for the course being attempted
   *
   * @param course the course object being attmepted
  **/
  public void setCourseAttempted(Course course)
  {
    this.attemptedCourse = course;
  }

  
  /**
   * getter for the course being attempted
   *
   * @return the course object being attmepted
  **/
  public Course getCourseAttempted()
  {
    return this.attemptedCourse;
  }


  /**
   * mutator method for the semesterTaken variable
   *
   * @param semester string value to set the semesterTaken to
   */
  public void setSemesterTaken(String semester)
  {
    if(semester != null)
    {
      this.semesterTaken = semester;
    }
  }

  /**
   * Accessor method for the semesterTaken variable
   *
   * @return String value of the semesterTaken
   */
  public String getSemesterTaken()
  {
    return this.semesterTaken;
  }

  /**
   * setter for attempt status
   * 
   * @param status string value of status
   */
  public void setStatus(String status)
  {
    this.attemptStatus = status;
  }

  /**
   * getter for attempt status
   * @return string value of the status (complete, planned)
   */
  public String getStatus()
  {
    return this.attemptStatus;
  }

  /**
   * compares object with other course object to determine if they are equivalent
   *
   * @param c course object to compare against
   * @return true if objects are equivalent, false if not
   */
  public boolean equals(Attempt c)
  {
    if(c == null)
      return false;

    if (!(this.getCourseCode().equals(c.getCourseCode())) || !(this.getSemesterTaken().equals(c.getSemesterTaken())))
      return false;

    return true;
  }

  /**
   * converts object information into a string
   *
   * @return string containg course code and title
   */
  public String toString()
  {
    //concatenate all variables in the class, comma delimited
    String attemptStr = this.getAttemptGrade() + "," + this.getSemesterTaken() + "," + this.getStatus() + ";" + this.getCourseAttempted().toString();

    return attemptStr;
  }

}
