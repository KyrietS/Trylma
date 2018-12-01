package main;

//klasyczny board - 6 ramienna gwiazda w której każde ramię ma 10 pól a wewnętrzny sześciokąt - 61 pól
public class ClassicBoard extends Board
{
    private final int columns = 13;
    private final int rows = 17;
    public ClassicBoard()
    {
        //wypełnienie boarda polami
        fields = new Field[13][17];
        for (int i = 0; i < columns; i++)
        {
            for (int j = 0; j < rows; j++)
            {
                fields[i][j] = new Field(false);
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
        for (int i = 0; i < columns; i++)
        {
            for (int j = 0; j < rows; j++)
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
}
