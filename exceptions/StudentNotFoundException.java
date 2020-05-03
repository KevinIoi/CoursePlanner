public class StudentNotFoundException extends Throwable
{
    private String info;

    public StudentNotFoundException()
    {
        this("STUDENT NOT FOUND");
    }

    public StudentNotFoundException(String str)
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