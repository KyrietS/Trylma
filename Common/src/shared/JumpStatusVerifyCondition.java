package shared;

//
public class JumpStatusVerifyCondition implements AdditionalVerifyCondition
{
    private int status;

    public JumpStatusVerifyCondition()
    {

    }

    public JumpStatusVerifyCondition(int status)
    {
        this.status = status;
    }


    public boolean verify()
    {
        return status != 2;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getStatus()
    {
        return status;
    }
}
