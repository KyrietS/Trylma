package main;

//klasyczny board - 6 ramienna gwiazda w której każde ramię ma 10 pól a wewnętrzny sześciokąt - 61 pól
public class ClassicBoard extends Board
{
    public ClassicBoard()
    {
        //wypełnienie boarda polami
        fields = new Field[13][17];
        for (int i = 0; i < 13; i++)
        {
            for (int j = 0; j < 17; j++)
            {
                fields[i][j] = new Field(true); //chwilowo wszystkie true
            }
        }
    }
}
