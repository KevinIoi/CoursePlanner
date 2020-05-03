/**
 *
 *
 * @author Kevin Ioi
 * @version Oct, 2018
 **/

package univ;

import java.util.*;

public abstract class HonoursDegree extends Degree
{
  static protected final Double minCredits = 20.0;//total credits needed
  static protected final Double minAoACredits = 4.0;//required area of application credits
  static protected final Double minThirdYearAoA = 1.0;//required AoA credits at third year or above
  static protected final Double maxFirstCredits = 6.0;//max first year credits applied
  static protected final Double minThirdCisCredits = 6.0;//min third year credits or above
  static protected final Double minFourthCisCredits = 2.0;//min fourth year credits

  //not used anymore
  //static protected final Double cumulativeAvgGen = 60.0;//min cumulative average
  //static protected final Double cumulativeAvgCIS = 70.0;//min average of CIS courses

  static protected final String degreeName = "HonoursDegree";//degree

  public HonoursDegree(){}

  public abstract String getProgramName();

  public String getDegreeName()
  {
    return HonoursDegree.degreeName;
  }

  /*
  *
  */
  public double numberOfCreditsRemaining(ArrayList<Course> plannedAndTaken)
  {
    double creditCount = 0;
    
    for (Course c : plannedAndTaken) {
      creditCount += c.getCourseCredit();
    }

    return creditCount;
  }

  public boolean equals(Degree deg)
  {
    return deg.getDegreeName().equals(this.getDegreeName());
  }


  public String toString()
  {
    return "HonoursDegree";
  }
}
