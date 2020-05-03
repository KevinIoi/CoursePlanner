/**
 * Course class which contains the field variables, accessors and mutators to
 * hold, access and update the information of a course
 *
 * @author Kevin Ioi
 * @version Oct, 2018
**/

package univ;
import java.util.*;
import java.io.*;

public class Course
{
  private ArrayList<Course> preReqList;//list if prerequisites courses
  private Double courseCredit;//the credit value of the course
  private String courseCode;//unique code identifier of the course, contains the department and course number
  private String courseTitle;//full name of the course
  private String semesterOffered;//semester offered

  /**
   * Zero parameter constructor
  */
  public Course()
  {
    this(0.0, "", "", "", new ArrayList<Course>());
  }

  public Course(String courseStr)
  {
    String info[] = courseStr.split(",");

    setCourseCode(info[0]);
    setCourseTitle(info[1]);
    setCourseCredit(info[2]);
    setSemesterOffered(info[3]);

    if(info.length>4)
    {
      setPrerequisites(Course.parsePreReqs(info[4]));
    }
    else
    {
      preReqList = new ArrayList<Course>();
    }
  }

  public Course(String course, boolean on)
  {
    this(0.0, course, "", "", new ArrayList<Course>());
  }

  /*
  * Deep copy constructor
  */
  public Course(Course c1)
  {
    this.setCourseCode(c1.getCourseCode());
    this.setCourseCredit(c1.getCourseCredit());
    this.setCourseTitle(c1.getCourseTitle());
    this.setSemesterOffered(c1.getSemesterOffered());

    this.preReqList = new ArrayList<Course>();

    if(!(c1.getPrerequisites().isEmpty()))//if there are preReqs in the list
    {
      ArrayList<Course> temp = c1.getPrerequisites();
      for (Course c: temp)//clone all of the preReqs
      {
        this.preReqList.add(c.clone());
      }
    }
  }

  /**
   * seven parameter constructor
   *
   * @param  courseCredit value to set object's courseCredit element
   * @param  code string to set object's courseCode element
   * @param  title tring to set obect's courseTitle element
   * @param  semester string to set object's semesterOffered element
   * @param  preReqs arraylist containing the prerequisite courses, to set preReqList to
   *
   */
  public Course(Double courseCredit, String code, String title, String semester, ArrayList<Course> preReqs)
  {
    this.setCourseCredit(courseCredit);
    this.setCourseCode(code);
    this.setCourseTitle(title);
    this.setSemesterOffered(semester);
    this.setPrerequisites(preReqs);
  }

  protected static ArrayList<Course> parsePreReqs(String preReqs)
  {
    String preReqInfo[] = preReqs.split(":");
    ArrayList<Course> courseList = new ArrayList<Course>();

    for (String str : preReqInfo) 
    {
      courseList.add(new Course(str, true));
    }

    return courseList;
  }
  

  /**
   * Accessor method for the courseCode variable
   *
   * @return String value of the courseCode
   */
  public String getCourseCode()
  {
    return this.courseCode;
  }

  /**
   * mutator method for the courseCode variable
   *
   * @param courseCode string value to set the courseCode to
   */
  protected void setCourseCode(String courseCode)
  {
    if (courseCode != null)
      this.courseCode = courseCode;
  }

  /**
   * Accessor method for the courseTitle variable
   *
   * @return String value of the course title
   */
  public String getCourseTitle()
  {
    return this.courseTitle;
  }

  /**
   * mutator method for the courseTitle variable
   *
   * @param courseTitle string value to set the courseTitle to
   */
  protected void setCourseTitle(String courseTitle)
  {
    if (courseTitle != null)
      this.courseTitle = courseTitle;
  }

  /**
   * Accessor method for the courseCredit variable
   *
   * @return Double value of courseCredit
   */
  public Double getCourseCredit()
  {
    return this.courseCredit;
  }

  /**
   * mutator method for the courseCredit variable
   *
   * @param credit double value to set the courseCredit to
   */
  protected void setCourseCredit(Double credit)
  {
    this.courseCredit = credit;
  }

  /**
   * mutator method for the courseCredit variable
   *
   * @param credit double value to set the courseCredit to
   */
  protected void setCourseCredit(String credit)
  {
    this.courseCredit = Double.valueOf(credit);
  }

  /**
   * Accessor method for the preReqsList variable
   *
   * @return ArrayList containing the list of prerequisite courses
   */
  public ArrayList<Course> getPrerequisites()
  {
    return this.preReqList;
  }

  /**
   * Accessor method for the preReqsList variable, but converted to String from
   *
   * @return string containing the course codes of all the prereqcourses delimited by semi-colons
   */
  public String getPrereqsStr()
  {
    String str = "";

    for (Course c : this.preReqList)
      str += c.getCourseCode() + ":";  

    return str;
  }

  /**
   * mutator method for the preReqList variable
   *
   * @param preReqList ArrayList containing the prerequisite courses for this course
   */
  protected void setPrerequisites(ArrayList<Course> preReqList)
  {
    if(preReqList != null)
      this.preReqList = preReqList;
  }

  /**
   * mutator method for the semesterOffered variable
   *
   * @param semester string value to set the semesterOffered to
   */
  protected void setSemesterOffered(String semester)
  {
    if(semester != null)
    {
      this.semesterOffered = semester;
    }
  }

  /**
   * Accessor method for the semesterOffered variable
   *
   * @return String value of the semesterOffered
   */
  public String getSemesterOffered()
  {
    return this.semesterOffered;
  }

  /**
   * compares object with other course object to determine if they are equivalent
   *
   * @param c course object to compare against
   * @return true if objects are equivalent, false if not
   */
  public boolean equals(Course c)
  {
    if(c == null)
      return false;

    if (!(this.getCourseCode().equals(c.getCourseCode())))
      return false;

    return true;
  }

  /**
   * creates a new Course object with equivalent values to this object
   *
   * @return the newly created clone
   */
  public Course clone()
  {
    Course cloneCourse = new Course();

    //clonse all simple variables
    cloneCourse.setCourseCode(this.getCourseCode());
    cloneCourse.setCourseCredit(this.getCourseCredit());
    cloneCourse.setCourseTitle(this.getCourseTitle());
    cloneCourse.setSemesterOffered(this.getSemesterOffered());

    ArrayList<Course> clonePreReqs = new ArrayList<Course>();

    if (this.getPrerequisites() != null)//avoid null pointers
    {
      if(!(this.getPrerequisites().isEmpty()))//if there are preReqs in the list
      {
        ArrayList<Course> temp = this.getPrerequisites();

        for (Course c: temp)//clone all preReqs
          clonePreReqs.add(c.clone());
      }
      cloneCourse.setPrerequisites(clonePreReqs);
    }

    return cloneCourse;
  }


  /**
   * converts object information into a string
   *
   * @return string containg course code and title
   */
  public String toString()
  {
    //concatenate all variables in the class (except prereqs), comma delimited
    String courseString = new String(this.getCourseCode());
    courseString += "," + this.getCourseTitle();
    courseString += "," + this.getCourseCredit();
    courseString += "," + this.getSemesterOffered() + ",";

    for (Course c : this.preReqList) {
      courseString += ":" + c.getCourseCode();
    }

    return courseString;
  }
}
