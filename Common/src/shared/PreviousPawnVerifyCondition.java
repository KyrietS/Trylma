package shared;

public class PreviousPawnVerifyCondition implements AdditionalVerifyCondition
{
    private int currentX;
    private int currentY;
    private int previousX;
    private int previousY;

    public PreviousPawnVerifyCondition()
    {

    }

    public PreviousPawnVerifyCondition(int currentX, int currentY, int previousX, int previousY)
    {
        this.currentX = currentX;
        this.currentY = currentY;
        this.previousX = previousX;
        this.previousY = previousY;
    }

    //zwraca true jeżeli koordynaty "poprzednie" różnią się od obecnych
    @Override
    public boolean verify()
    {
        return currentX != previousX || currentY != previousY;
    }


    public void setCurrentX(int currentX)
    {
        this.currentX = currentX;
    }

    public int getCurrentY()
    {
        return currentY;
    }

    public void setCurrentY(int currentY)
    {
        this.currentY = currentY;
    }

    public int getPreviousX()
    {
        return previousX;
    }

    public void setPreviousX(int previousX)
    {
        this.previousX = previousX;
    }

    public int getPreviousY()
    {
        return previousY;
    }

    public void setPreviousY(int previousY)
    {
        this.previousY = previousY;
    }

    public void setCurrentXY(int x, int y)
    {
        this.currentX = x;
        this.currentY = y;
    }

    public void setPreviousXY(int x, int y)
    {
        this.previousX = x;
        this.previousY = y;
    }


}
