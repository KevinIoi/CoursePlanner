/**
 * represents a software Engineering program and it's requirments
 *
 * @author Kevin Ioi
 * @version Oct, 2018
 **/

package univ;

import java.io.*;
import java.util.*;

public class SEng extends HonoursDegree
{

  static final public String programName = "Software Engineering";//name of the program is constant

  /*
  * zero parameter constructor
  */
  public SEng()
  {
    this.title = "SEng";
    this.requiredCourses = new ArrayList<Course>();
    this.cc = new CourseCatalog();
    this.requiredCourseCodes = "CIS*1250:CIS*1500:CIS*1910:CIS*2250:CIS*2500:CIS*2030:CIS*2430:CIS*2520:CIS*3250:CIS*2750:CIS*3110:CIS*3490:CIS*3750:STAT*2040:CIS*3760:CIS*3260:CIS*4150:CIS*4300:CIS*4250";
    initRequiredCourses();
  }

  /*
  * single parameter  constructor
  */
  public SEng(CourseCatalog catalog)
  {
    this.title = "SEng";
    requiredCourses = new ArrayList<Course>();
    this.setCourseCatalog(catalog);
    this.requiredCourseCodes = "CIS*1250:CIS*1500:CIS*1910:CIS*2250:CIS*2500:CIS*2030:CIS*2430:CIS*2520:CIS*3250:CIS*2750:CIS*3110:CIS*3490:CIS*3750:STAT*2040:CIS*3760:CIS*3260:CIS*4150:CIS*4300:CIS*4250";
    initRequiredCourses();
  }

  /*
  * getter for program name
  */
  public String getProgramName()
  {
    return SEng.programName;
  }

  /*
  * checks if the proviede program meets the requiments for this program
  */
  public boolean meetsRequirements(ArrayList<Course> plannedAndTaken)
  {
    Double creditCount = 0.0;//count of credits applied towards minimum credit count
    Double aoACredits = 0.0;//count of AoA credits
    Double thirdYearAoA = 0.0;//count of 3rd year credits
    Double firstCredits = 0.0;//count of first year credits applied
    Double thirdCisCredits = 0.0;//count of third year credits or above
    Double fourthCisCredits = 0.0;//count of fourth year CIS credits

    Iterator<Course> iter;//iterator used to cycle thorugh courses in transcript
    Course tempCourse;//pointer used to handle courses as we iterate through them

    Integer tempCourseNum;//holds course number of course being reviewed
    String tempCourseCode;//holds course code of course being reviewed
    ArrayList<Course> transcript = plannedAndTaken;
    boolean foundMatch;

    for (Course requiredCourse: this.requiredCourses)//loop through requied courses
    {
      iter = transcript.iterator();
      foundMatch = false;
      while(iter.hasNext())//look through transcript for the required course
      {
        tempCourse = iter.next();

        if (tempCourse.equals(requiredCourse))//found a match
        {
          foundMatch = true;//start with assumption that it's good, was previously used as a check if course was failed

          if (foundMatch == true)//valid match for course
          {
            //split up course code and dept name
            tempCourseNum = Integer.parseInt((tempCourse.getCourseCode().split("\\*")[1]).trim());
            tempCourseCode = (tempCourse.getCourseCode().split("\\*")[1]).trim();

            if (tempCourseNum<2000)//first year course
            {
              if (firstCredits<HonoursDegree.maxFirstCredits)//check if have too many first year credits
              {
                creditCount += tempCourse.getCourseCredit();
                firstCredits += tempCourse.getCourseCredit();
              }
            }
            else//2nd year plus
            {
              creditCount += tempCourse.getCourseCredit();

              if(tempCourseNum >= 3000 && tempCourseNum < 4000)//3rd year
              {
                if (tempCourseCode.equals("CIS"))
                {
                  thirdCisCredits +=  tempCourse.getCourseCredit();
                }
              }
              else//4th year
              {
                if (tempCourseCode.equals("CIS"))
                {
                  fourthCisCredits +=  tempCourse.getCourseCredit();
                }
              }
            }
          }
        }
      }
      if (foundMatch != true)//didn't find a required credit
        return false;

      iter.remove();
    }

    iter = transcript.iterator();
    while(iter.hasNext())//loop through transcript summing credits for other courses
    {
      tempCourse = iter.next();
      foundMatch = true;//start with assumption that it's goods

      if (foundMatch == true)//valid match for course
      {
        //split up course code and dept name
        tempCourseNum = Integer.parseInt((tempCourse.getCourseCode().split("\\*")[1]).trim());
        tempCourseCode = (tempCourse.getCourseCode().split("\\*")[1]).trim();

        if (!tempCourse.getCourseCode().contains("CIS"))
          aoACredits += tempCourse.getCourseCredit();

        if (tempCourseNum<2000)//first year course
        {
          if (firstCredits<HonoursDegree.maxFirstCredits)//check if have too many first year credits
          {
            creditCount += tempCourse.getCourseCredit();
            firstCredits += tempCourse.getCourseCredit();
          }
        }
        else//2nd year plus
        {
          creditCount += tempCourse.getCourseCredit();

          if(tempCourseNum >= 3000 && tempCourseNum < 4000)//3rd year
          {
            if (tempCourseCode.equals("CIS"))
            {
              thirdCisCredits +=  tempCourse.getCourseCredit();
            }
            if (!tempCourse.getCourseCode().contains("CIS"))
              thirdYearAoA += tempCourse.getCourseCredit();
          }
          else//4th year
          {
            if (tempCourseCode.equals("CIS"))
            {
              fourthCisCredits +=  tempCourse.getCourseCredit();
            }
            if (!tempCourse.getCourseCode().contains("CIS"))
              thirdYearAoA += tempCourse.getCourseCredit();
          }
        }
      }
    }

    if (creditCount < minCredits || aoACredits < minAoACredits || thirdYearAoA < minThirdYearAoA || thirdCisCredits < minThirdCisCredits ||
      fourthCisCredits < minFourthCisCredits)
      return false;

    return true;
  }

  /*
  * compare method of this object
  */
  public boolean equals(Degree deg)
  {
    return deg.toString().equals(this.toString());
  }

  /*
  * provides string representation of object
  */
  public String toString()
  {
    return super.toString() + ":SEng";
  }

}
