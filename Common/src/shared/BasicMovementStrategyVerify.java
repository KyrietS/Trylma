package shared;

import static shared.PlayerColor.NONE;

/**
 Klasa obsługująca weryfikację ruchów dla BasicMovementStrategy
 */
public class BasicMovementStrategyVerify
{
    /**
     * Weryfikuje czy dany ruch z pola (x1,y1) na (x2,y2) dla danej planszy jest poprawny
     * Zwraca 0 jeżeli ruch był niepoprawny, 1 jeżeli był to poprawny ruch na przykegłe pole (ruch krótki), albo 2 gdy był to poprawny ruch przeskakujący lub -1 gdy wystąpił błąd
     * Do poprawnego działania wykorzystuje tablicę 2 dodatkowych warunków: JumpStatusVerifyCondition oraz PreviousPawnVerifyCondition
     * Funkcja musi przyjąć dokładnie te dwa warunki w podanej kolejności, w przeciwnym wypadku jej działanie będzie nieprzewidywalne i prawdopodobnie błędne
     * (Funckja oddelegowana do kasy BasicMovementStrategyVerify)
     */
    public static int verifyMove(IBoard board, int x1, int y1, int x2, int y2, AdditionalVerifyCondition[] additionalVerifyConditions)
    {
        //sprawdza listę dodatkowych warunków (muszą być 2)
        if (additionalVerifyConditions.length != 2)
        {
            return -1;
        }
        //zmienne pomocnicze
        int dx = x2 - x1;
        int dy = y2 - y1;
        //jeżeli któreś z pól jest nullem lub wypełniaczem to ruch na pewno nie jest prawidłowy
        if (board.getField(x1, y1) == null || board.getField(x2, y2) == null || !board.getField(x1, y1).isPlayable() || !board.getField(x2, y2).isPlayable())
        {
            return 0;
        }
        //pole z którego ruszamy musi zawierać pionek i pole do którego idziemy musi być puste
        if (!board.getField(x2, y2).getCurrentColor().equals( NONE ) || board.getField(x1, y1).getCurrentColor().equals(NONE))
        {
            return 0;
        }
        //Jeżeli wykonaliśmy juz skok (status==2) to musimy ruszyć tym samym pionkiem co poprzednio
        if (!additionalVerifyConditions[0].verify() && additionalVerifyConditions[1].verify())
        {
            return 0;
        }

        switch (Math.abs(dy))
        {
            //Pola leżą w tym samym rzędzie
            case 0:
            {
                //Sprawdza odległość na osi x
                switch (Math.abs(dx))
                {
                    //Jeżeli 0 to znaczy że (x1,y1)==(x2,y2) czyli jest to nieprawidłowy ruch
                    case 0:
                    {
                        return 0;
                    }
                    //Jeżeli odległość == 1 to pola są bezpośrednimi sąsiadami czyli wykonujemy ruch krótki
                    case 1:
                    {
                        return additionalVerifyConditions[0].verify() ? 1 : 0;
                    }
                    //Jeżeli odległość == 2 to sprawdzamy ruch przeskakujący
                    case 2:
                    {
                        //Sprawdza czy pomiędzy polami znajduje się jakiś pionek którego można przeskoczyć
                        if (!board.getField((x1 + x2) / 2, y1).getCurrentColor().equals(NONE))
                        {
                            return 2;
                        }
                        //Jeżeli nie ma to ruch nieprawidłowy
                        else
                        {
                            return 0;
                        }
                    }
                }
            }
            //Pola leżą w sąsiednich rzędach
            case 1:
            {
                //Sprawdza czy numer rzędu jest parzysty ( ze względu na przesunięcie współrzędne x sąsiadów są inne dla różnych parzystości rzędów)
                if (y1 % 2 == 0)
                {
                    //Dla rzędów parzystych sprawdza x2==x1 i x2==x1+1
                    if (dx == 0 || dx == 1)
                    {
                        return additionalVerifyConditions[0].verify() ? 1 : 0;
                    } else
                    {
                        return 0;
                    }
                } else
                {
                    //Dla rzędów nieparzystych sprawdza x2==x1 i x2==x1-1
                    if (dx == 0 || dx == -1)
                    {
                        return additionalVerifyConditions[0].verify() ? 1 : 0;
                    } else
                    {
                        return 0;
                    }
                }
            }
            //Numery rzędów pól różnią się o 2 (dalsi sąsiedzi)
            case 2:
            {
                if (Math.abs(dx) != 1)
                {
                    return 0;
                }
                //Trzeba rozważyć osobno dla rzędów parzystych i niepazystych
                //Dla rzędów parzystych
                if (y1 % 2 == 0)
                {
                    //Jeżeli skacze w lewo to pole pomiędzy ma współrzędne x==x1 , y==|y2-y1/2|
                    if (x2 < x1 && !board.getField(x1, (y1 + y2) / 2).getCurrentColor().equals(NONE))
                    {
                        return 2;
                    }
                    //Jeżeli skacze w prawo to pole pomiędzy ma współrzędne x==x1+1 , y==|y2-y1/2|
                    else if (x2 > x1 && !board.getField(x1 + 1, (y2 + y1) / 2).getCurrentColor().equals(NONE))
                    {
                        return 2;
                    } else
                    {
                        return 0;
                    }
                } else
                {
                    //Jeżeli skacze w lewo to pole pomiędzy ma współrzędne x==x1-1 , y==|y2-y1/2|
                    if (x2 < x1 && !board.getField(x1 - 1, (y1 + y2) / 2).getCurrentColor().equals(NONE))
                    {
                        return 2;
                    }
                    //Jeżeli skacze w prawo to pole pomiędzy ma współrzędne x==x1 , y==|y2-y1/2|
                    else if (x2 > x1 && !board.getField(x1, (y1 + y2) / 2).getCurrentColor().equals(NONE))
                    {
                        return 2;
                    } else
                    {
                        return 0;
                    }
                }

            }
            //dla różnicy rzędów >2 ruch jest niemożliwy
            default:
            {
                return 0;
            }
        }
    }
}
