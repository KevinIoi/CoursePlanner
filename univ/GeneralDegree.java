/**
 * This class represents a General Degree and constains all of the generalized
 * requirements which musst be met in order to gradiate with a general degree
 * in computer science
 *
 * @author Kevin Ioi
 * @version Oct, 2018
 **/

  package univ;

  import java.util.*;

public abstract class GeneralDegree extends Degree
{
  /*
  * requirements which must be met inorder for a subclasses (program) to complete a GeneralDegree
  */
  static protected final Double minCredits = 15.0;//total required number of credits
  static protected final Double maxFirstYearCredits = 6.0;//max first year credits applied
  static protected final Double minThirdYearCredits = 6.0;//min third year credits or above
  static protected final Double maxCreditsInDept = 11.0;//maximum amount of credits in specific dicipline
  static protected final Double minStatsCredit = 0.5;//min additional stats or CIS credit at 2nd  year lvl or above
  static protected final Double minThirdYearCISCredits = 1.0;//min additional CIS credits at third year or above
  static protected final Double minSciCredits = 2.0;//min number of science credits
  static protected final Double minSocialCredits = 2.0;//min number of social science credits

  static protected final String degreeName = "GeneralDegree";


  /**
  * zero parameter constructor
  */
  public GeneralDegree(){}

  /**
  * abstract  method required to be overridden by subclasses
  *
  * @return string name of the program
  */
  public abstract String getProgramName();

  /**
  * accessor method for degreeName
  *
  * @return string name of the degree
  */
  public String getDegreeName()
  {
    return GeneralDegree.degreeName;
  }

  /**
  * calculates how many more cedits the student must complete inorder to be eligible
  * to graduate
  *
  * @param plannedAndTaken courseplan to be assessed
  * @return number of uncompleted credits reminaing to complete the program
  */
  public double numberOfCreditsRemaining(ArrayList<Course> plannedAndTaken)
  {
    double creditCount = 0;
    
    for (Course c : plannedAndTaken) {
      creditCount += c.getCourseCredit();
    }

    return GeneralDegree.minCredits - creditCount;
  }


  /**
  * comparison method for GeneralDegree class
  *
  * @param deg degree to be compared to
  * @return true if the name of the degree is the same, false if not
  */
  public boolean equals(Degree deg)
  {
    return deg.getDegreeName().equals(this.getDegreeName());
  }

  /**
  * @return const "GeneralDegree"
  */
  public String toString()
  {
    return "GeneralDegree";
  }

}
