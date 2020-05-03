/**
 * The superclass used to represent an undergraduate degree
 *
 * @author Kevin Ioi
 * @version Oct, 2018
 **/


package univ;

import java.util.*;
import java.io.*;

public abstract class Degree
{
  protected String title;//shorthand title of this program
  protected ArrayList<Course> requiredCourses;//Courses that must be taken to complete this degree
  protected CourseCatalog cc;
  protected String degreeName;//
  protected String requiredCourseCodes;

  //Judi said treat all non science credits as social science or arts
  /*
  static final List<String> socialSciCourse = Arrays.asList(
  "ANTH", "FRHD", "GEO", "GEOG", "POLS", "UNIV", "PSYC", "ANTH", "SOAN", "SOC",
  "THST", "ENGL", "ARTH", "MUSC", "SART", "ARAB", "CHIN", "CLAS", "EURO", "FREN",
  "GERM", "HUMN", "GREK", "ITAL", "LACS", "LAT", "LING", "PORT", "SPAN", "PHIL", "HIST");
  */

  static final List<String> sciCourse = Arrays.asList(
  "BSCG", "BSCH", "ABIO", "BIOC", "BIOD", "BMPH", "BPCH", "BIOS", "BIOL", "BIOM", "BTOX",
  "BIOT", "BECN", "CHPY", "CHEM", "CIS", "ECOL", "ENVB", "EGG", "FOOD", "GIS", "HK", "MFB",
  "MSCI", "MATH", "MICR", "MBG", "NANO", "NEUR", "NANS", "PSCI", "PHYS", "PLSC", "STAT",
  "THPY", "WBC", "ZOO");

  public Degree(){}

  protected void initRequiredCourses()
  {
    String codes[] = requiredCourseCodes.split(":");

    for (String str : codes) {
      Course required = cc.findCourse(str);

      if(required == null)
        required = new Course(str, true);//the required course was not in the database, make a stub

      requiredCourses.add(required);
    }
  }


  /**
  * Accessor method for degree title
  *
  * @return an the degree title
  */
  public String getDegreeTitle()
  {
    return this.title;
  }

  /**
  * Mutator method for degree title
  *
  * @param title the new title for this
  */
  protected void setDegreeTitle(String title)
  {
    this.title = title;
  }

  /**
  * Mutator method for requiredCourses
  *
  * @param listOfRequiredCourseCodes list of course codes for the courses to be added
  */
  protected void setRequiredCoursesStr(ArrayList<String> listOfRequiredCourseCodes)
  {
    requiredCourses = new ArrayList<Course>();
    Course newCourse;
    for (String str: listOfRequiredCourseCodes)//check all course codes provided
    {
      if (this.cc.findCourse(str) != null)
        newCourse = this.cc.findCourse(str);//if course is available, grab a clone of it's infor
      else
        newCourse = new Course(str);//make skeleton if its not available

      this.requiredCourses.add(newCourse);
    }
  }

  /**
  * Overloaded method which takes an arrayList of courses, because the assignment is worded horribly and i don't know how you're testing this
  *
  * @param listOfRequiredCourseCodes list of course codes for the courses to be added
  */
  protected void setRequiredCourses(ArrayList<Course> listOfRequiredCourses)
  {
    requiredCourses = listOfRequiredCourses;
  }


  /**
  * Mutator method for cc (Coures catalog)
  *
  * @param courses CourseCatalog to reference for this object
  */
  protected void setCourseCatalog(CourseCatalog courses)
  {
      this.cc = courses;
  }

  /**
  * Acessor method to get an arraylist of the required courses for this degree
  *
  * @return an ArrayList containing the required course objects
  */
  public ArrayList<Course> getRequiredCourses()
  {
    return this.requiredCourses;
  }

  public ArrayList<String> getRequiredCourseCodes()
  {
    ArrayList<String> requiredCodes = new ArrayList<String>();

    for (Course c: this.requiredCourses)
    {
      requiredCodes.add(c.getCourseCode());
    }

    return requiredCodes;
  }

  /**
   * Compares the provided list of courses against the required courses for degree
   * provides list of required courses not planned
   * 
   * @param plannedAndTaken list of courses that are planned or have been taken 
   * @return
   */
  public ArrayList<Course> remainingRequiredCourses(ArrayList<Course> plannedAndTaken)
  {
    ArrayList<Course> missingCourses = new ArrayList<Course>();
    boolean found;

    for (Course reqc: this.requiredCourses)//look for every required course
    {
      found = false;
      for (Course planc : plannedAndTaken)//look through all courses planned and taken
      {
        if(planc.equals(reqc))
        {
          found = true;//found this required course
          break;
        }        
      }
      if(found == false)
        missingCourses.add(reqc);
    }

    return missingCourses;
  }

  public abstract String getDegreeName();

  public abstract boolean meetsRequirements(ArrayList<Course> plannedAndTaken);

  public abstract double numberOfCreditsRemaining(ArrayList<Course> plannedAndTaken);

  public abstract String getProgramName();

  public boolean equals(Degree deg)
  {
    return this.title.equals(deg.getDegreeTitle());
  }

  public String toString()
  {
    return "Degree";
  }
}
