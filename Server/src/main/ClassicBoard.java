package main;

//klasyczny board - 6 ramienna gwiazda w której każde ramię ma 10 pól a wewnętrzny sześciokąt - 61 pól
public class ClassicBoard extends Board
{
    ClassicBoard()
    {
        columns = 13;
        rows = 17;
        //wypełnienie boarda polami
        fields = new Field[columns + 1][rows + 1];
        for (int i = 1; i <= columns; i++)
        {
            for (int j = 1; j <= rows; j++)
            {
                setField(i, j, new Field(false));
            }
        }
    }

    /*zwraca string który informuje o kolorze i położeniu pionków
        string jest w formacjie KOLOR X Y KOLOR X Y KOLOR X Y ... KOLOR X Y
     */
    @Override
    public String getAsString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= columns; i++)
        {
            for (int j = 1; j <= rows; j++)
            {
                if (fields[i][j].isPlayable())
                {
                    if (!fields[i][j].getCurrentColor().equals("none"))
                    {
                        //jezeli string jest niepusty to dodaj spacje
                        if (!stringBuilder.toString().equals(""))
                        {
                            stringBuilder.append(" ");
                        }
                        stringBuilder.append(fields[i][j].getCurrentColor());
                        stringBuilder.append(" ");
                        stringBuilder.append(i);
                        stringBuilder.append(" ");
                        stringBuilder.append(j);
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean isWinner(String color)
    {
        Field tempField;
        for (int i = 1; i <= columns; i++)
        {
            for (int j = 1; j <= rows; j++)
            {
                tempField = fields[i][j];
                if (tempField.isPlayable() && tempField.getCurrentColor().equals(color) && !tempField.getCurrentColor().equals(tempField.getTargetColor()))
                {
                    return false;
                }
            }
        }
        return true;
    }
}
