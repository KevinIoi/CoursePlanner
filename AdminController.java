import univ.DBDetails;
import univ.MyConnection;
import java.util.*;

public class AdminController
{
  MyConnection c;

  public AdminController()
  {
    c = new MyConnection(DBDetails.username, DBDetails.password);
  }

  public void addCourse(String code, String credit, String name, String semester, String prereq)
  {
    c.addCourse(code, credit, name, semester, prereq);
  }

  public void deleteCourse(String code)
  {
    c.deleteCourse(code);
  }

  public ArrayList<String> getAllCourses()
  {
    return c.getAllCourses();
  }  

  public boolean equals(AdminController obj) {
    return false;
  }

  public String toString() {
    return "why?";
  }
}