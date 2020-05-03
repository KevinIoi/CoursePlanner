/**
 * This class represents a gernal program in computing. It holds all of the
 * requirments neccessary to graduate the program and methods used to check if
 * a plann of study satisfies the requirements
 *
 * @author Kevin Ioi
 * @version Oct, 2018
 **/

package univ;
import java.util.*;
import java.io.*;

public class BCG extends GeneralDegree
{
  static public final String programName = "Computer Science";//the name of the program, always "Computer Science"

  /**
  * Zero parameter constructor
  */
  public BCG()
  {
    this.title = "BCG";
    this.requiredCourses = new ArrayList<Course>();
    this.cc = new CourseCatalog();
    this.requiredCourseCodes = "CIS*1500:CIS*1910:CIS*2430:CIS*2500:CIS*2520:CIS*2750:CIS*2910:CIS*3530";
    initRequiredCourses();
  }

  /**
  * Single parameter to create BCG that contains the provided available course list
  *
  * @param catalog CourseCatalog to use as source of available courses
  */
  public BCG(CourseCatalog catalog)
  {
    this.title = "BCG";
    requiredCourses = new ArrayList<Course>();
    this.setCourseCatalog(catalog);
    this.requiredCourseCodes = "CIS*1500:CIS*1910:CIS*2430:CIS*2500:CIS*2520:CIS*2750:CIS*2910:CIS*3530";
    initRequiredCourses();
  }

  /**
  * Acessor method used to get the program name for this object
  *
  * @return this objects programname
  */
  public String getProgramName()
  {
    return BCG.programName;
  }

  /**
  * Checks if the courses taken in the parameter coursePlan satisfies the program
  * requirements outlined by this object
  *
  * @param plannedAndTaken course plan to be assessed
  * @return true if plan meets requirments / false if not
  */
  public boolean meetsRequirements(ArrayList<Course> plannedAndTaken)
  {
    Double firstYearCount = 0.0;//# of 1000 level credits applied
    Double thirdYearPlusCount = 0.0;//# of 3000+ level credits applied
    Double thirdYearPlusCISCount = 0.0;//# of 3000+ level credits applied
    Double creditCount = 0.0;//# of total credits applied
    Double cisCreditCount = 0.0;//# of CIS credits applied
    Double statsCount = 0.0;//keep track of STATS requirement, I don't understand why CIS courses are valid replacements
    Double socialCreditCount = 0.0;//# of social science credits taken
    Double sciCreditCount = 0.0;//# of science elective credits taken

    Iterator<Course> iter;//iterator used to cycle thorugh courses in transcript
    Course tempCourse;//pointer used to handle courses as we iterate through them

    Integer tempCourseNum;//holds course number of course being reviewed
    String tempCourseCode;//holds course code of course being reviewed
    ArrayList<Course> transcript = plannedAndTaken;

    boolean foundMatch;

    for (Course reqCourse: this.requiredCourses)//run through required course list
    {
      iter = transcript.iterator();
      foundMatch = false;
      while(iter.hasNext())//run through transcript checking for this requiredCourse
      {
        tempCourse = iter.next();
        if (tempCourse.equals(reqCourse))//found a course with same code
        {
          foundMatch = true;

          if (foundMatch == true)//tally credit
          {
            //split up course code and dept name
            tempCourseNum = Integer.parseInt((tempCourse.getCourseCode().split("\\*")[1]).trim());
            tempCourseCode = (tempCourse.getCourseCode().split("\\*")[1]).trim();

            if (tempCourseNum < 2000)//check if course is 1000 level
            {
              if (firstYearCount < 6.0)
              {
                creditCount += tempCourse.getCourseCredit();
                firstYearCount += tempCourse.getCourseCredit();
                if (tempCourseCode.equals("CIS"))
                  cisCreditCount += tempCourse.getCourseCredit();
              }
            }
            else if(tempCourseNum >= 2000 && tempCourseNum < 3000)//check if course is 2000 level
            {
              creditCount += tempCourse.getCourseCredit();
              if (tempCourseCode.equals("CIS"))
                cisCreditCount += tempCourse.getCourseCredit();
            }
            else if(tempCourseNum >= 3000)//check if course is 3000 level+
            {
              creditCount += tempCourse.getCourseCredit();
              thirdYearPlusCount += tempCourse.getCourseCredit();
              if (tempCourseCode.equals("CIS"))
                cisCreditCount += tempCourse.getCourseCredit();
            }
            else
            {
              System.out.println("WHA");
            }

            iter.remove();//remove course from list after being reviewed
            break;//break loop, found match
          }
        }
        if(foundMatch != true)
          return false;//a required course was not found in the transcript
      }
    }

    iter = transcript.iterator();
    while(iter.hasNext())//iterate though reminaing courses accounting their additions to requirements
    {
      tempCourse = iter.next();
      foundMatch = true;// start with assumption that the credit is valid

      if (foundMatch == true)
      {
        //split up course code and dept name
        tempCourseNum = Integer.parseInt((tempCourse.getCourseCode().split("\\*")[1]).trim());//isolate course number
        tempCourseCode = (tempCourse.getCourseCode().split("\\*")[0]).trim();//isolate dept name

        if (tempCourseCode.equals("CIS"))//CIS credit
        {
          if (cisCreditCount < GeneralDegree.maxCreditsInDept)//ignore credit if more than 11 CIS credits
          {
            if (tempCourseNum < 2000)//first year CIS credit
            {
              if (firstYearCount < GeneralDegree.maxFirstYearCredits)//make sure we don;t have too many 1000 level courses
              {
                creditCount += tempCourse.getCourseCredit();
                firstYearCount += tempCourse.getCourseCredit();
                cisCreditCount += tempCourse.getCourseCredit();
              }
            }
            else if(tempCourseNum >= 3000) //3rd year or fourth year CIS credit
            {
              if (thirdYearPlusCISCount >= minThirdYearCISCredits)
              {
                statsCount += tempCourse.getCourseCredit();
              }
              thirdYearPlusCISCount += tempCourse.getCourseCredit();
              thirdYearPlusCount += tempCourse.getCourseCredit();
              cisCreditCount += tempCourse.getCourseCredit();
              creditCount += tempCourse.getCourseCredit();
            }
            else //second year CIS credit
            {
              statsCount += tempCourse.getCourseCredit();
              cisCreditCount += tempCourse.getCourseCredit();
              creditCount += tempCourse.getCourseCredit();
            }
          }
        }
        else//not CIS credit
        {
          if (tempCourseCode.equals("STATS") && tempCourseNum >= 2000 && statsCount < minStatsCredit)//if still need STATS credit
          {
            statsCount += tempCourse.getCourseCredit();
            creditCount += tempCourse.getCourseCredit();

            if (tempCourseNum >= 3000)
            {
              thirdYearPlusCount += tempCourse.getCourseCredit();
            }
          }
          else//check what kind of elective the course is
          {
            if (sciCourse.contains(tempCourseCode))//science elective
              sciCreditCount += tempCourse.getCourseCredit();
            else
              socialCreditCount += tempCourse.getCourseCredit();

            //if(socialSciCourse.contains(tempCourseCode))//social science elective

            if (tempCourseNum < 2000)//first year elective credit
            {
              if (firstYearCount < GeneralDegree.maxFirstYearCredits)//don't count towards required creditcount if too many first year courses
                creditCount += tempCourse.getCourseCredit();
            }
            else if (tempCourseNum >= 2000 && tempCourseNum < 3000)//Second year elective
            {
              creditCount += tempCourse.getCourseCredit();
            }
            else//third or fourth year elective
            {
              creditCount += tempCourse.getCourseCredit();
              thirdYearPlusCount += tempCourse.getCourseCredit();
            }
          }
        }
      }
      iter.remove();//remove course from list after being reviewed
    }

    if(creditCount < minCredits || thirdYearPlusCount < minThirdYearCredits || statsCount < minStatsCredit || thirdYearPlusCISCount < minThirdYearCISCredits ||
      sciCreditCount < minSciCredits || socialCreditCount < minSocialCredits)
      return false;

    return true;
  }

  /**
  * comparison method for the class
  *
  * @param deg the degree to be compared against
  * @return true if they're a match, false if not
  */
  public boolean equals(Degree deg)
  {
    return deg.toString().equals(this.toString());
  }

  /**
  * @return constant "GeneralDegree:BCG"
  */
  public String toString()
  {
    return super.toString() + ":BCG";
  }

}
