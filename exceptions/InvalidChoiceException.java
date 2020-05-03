public class InvalidChoiceException extends Throwable
{
    private String info;

    public InvalidChoiceException()
    {
        this("Invalid selection");
    }

    public InvalidChoiceException(String str)
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