public class CourseNotFoundException extends Throwable
{
    private String info;

    public CourseNotFoundException()
    {
        this("COURSE NOT FOUND");
    }

    public CourseNotFoundException(String str)
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