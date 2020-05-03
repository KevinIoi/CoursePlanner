/**
* The CourseCatalog class provides an object and methods used to store and handle
* the list of courses being offered
*
* @author Kevin Ioi
* @version Oct, 2018
*/

package univ;
import java.io.*;
import java.util.*;

public class CourseCatalog
{
  MyConnection c;

  /**
  * Class constructor initializing an empty ArrayList to store course information
  */
  public CourseCatalog()
  {
    c = new MyConnection(DBDetails.username, DBDetails.password);
  }


  /**
   * adds Course object to the database
   *
   * @param  toAdd Course object being added to the database
   */
  protected void addCourse(Course toAdd)
  {
    c.addCourse(toAdd.getCourseCode(), toAdd.getCourseCredit().toString(), toAdd.getCourseTitle(), toAdd.getSemesterOffered(), toAdd.getPrereqsStr());
  }

  /**
   * removes Course object from the 'offeredCourses' ArrayList, if the object
   * exists in the list
   *
   * @param  toRemove Course object being removed from arraylist
   */
  protected void removeCourse(Course toRemove)
  {
    c.deleteCourse(toRemove.getCourseCode());
  }


  /**
   * locates and returns course object contained in the database
   *
   * @param  courseCode the courseCode of the course object being requested
   * @return course object contained in offeredCourses with matching courseCode OR null if
   *         no match was found
   */
  public Course findCourse(String courseCode)
  {
    String searchResult;
    Course foundCourse;

    searchResult = c.findCourse(courseCode);
    
    if(searchResult == null)
      return null;

    String splitResult[] = searchResult.split(",");

    if(splitResult.length > 4)
    {
      foundCourse = new Course(Double.parseDouble(splitResult[1]),splitResult[0], splitResult[2], splitResult[3], Course.parsePreReqs(splitResult[4]));
    }
    else
    {
      foundCourse = new Course(Double.parseDouble(splitResult[1]),splitResult[0], splitResult[2], splitResult[3], new ArrayList<Course>());
    }


    return foundCourse;
  }

  /*
  * getter method for the arrayList of courses being offered
  *
  */
  public ArrayList <Course> getCourseList()
  {
    ArrayList<String> strCourses = c.getAllCourses();
    ArrayList<Course> listCourses = new ArrayList<Course>();
    String splitInfo[];

    for (String str : strCourses) 
    {
      splitInfo = str.split(",");

      if (splitInfo.length>4) {
        listCourses.add( new Course(Double.valueOf(splitInfo[1]),splitInfo[0], splitInfo[2], splitInfo[3], Course.parsePreReqs(splitInfo[4])));
      }
      else
      {
        listCourses.add( new Course(Double.valueOf(splitInfo[1]),splitInfo[0], splitInfo[2], splitInfo[3], new ArrayList<Course>()));
      }

    }

    return listCourses;
  }

  /*
  * overridden comparator operator
  *
  */
  public boolean equals(CourseCatalog cc)
  {
    //nope
    return false;
  }

  /*
  * Overridden to string
  */
  public String toString()
  {
    return "Catalog";
  }
}
