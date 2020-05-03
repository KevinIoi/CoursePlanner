
class DegreeNotFoundException extends Throwable
{
  private String info;

  public DegreeNotFoundException()
  {
    this("DEGREE NOT FOUND");
  }

  public DegreeNotFoundException(String str)
  {
    this.setInfo(str);
  }

  public void setInfo(String str)
  {
    this.info = str;
  }

  @Override
  public String toString() {
    return this.info;
  }
}