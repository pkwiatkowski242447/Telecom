package tips.zadanie1.tips_zadanie1;

public class ErrorsRepairClass {

    /*
        Macierz wymagana do korekcji pojedynczego błędu bitowego dla wiadomości ośmiobitowych (1 bajt)

        Wymagania:

        * Minimalna odległość między słowami kodowymi musi pozwalać na wykrycie 1 błędu bitowego
        * Minimalna odległość między słowami kodowymi musi wynosić co najmniej 2 (D min - 1)
        * Ogległość między tymi słowami ponadto musi być większa lub równa 3 (D min - 1 / 2)

        = Oznacza to, że do słów 8 bitowych trzeba dodać 4 bity parzystości

        Ponadto:

        * Macierz nie może mieć identycznych kolumn - w tym przypadku oznacza to, że trzeba dodać co najmniej 4 bity parzystości
            (Inaczej otrzymamy tylko 8 kolumn, które musiałbybyć unikalne w ramach macierzy o 11 kolumanch)
        * Macierz nie może posiadać kolumny zerowej

     */

    // Macierz H o wymiarach 4 x 12 (dla wiadomości o długości 4 + 8 = 12 bitów)

    public static int[][] oneErrorMatrix = {
            {0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0},
            {0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0},
            {1, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0},
            {1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1}
    };

    /*
        Macierz wymagana do korekcji podwójnego błędu bitowego dla wiadomości ośmiobitowych (1 bajt)

        Wymagania:

        * Minimalna odległość między słowami kodowymi musi pozwalać na wykrycie 2 błędów bitowych
        * Minimalna odległość między słowami kodowymi musi wynosić co najmniej 3 (D min - 1), ale w tym przypadku musi być to 4.
          (Wynika to z wymagania niżej)
        * Ogległość między tymi słowami ponadto musi być większa lub równa 5 (D min - 1 / 2)

        = Oznacza to, że do słów 8 bitowych trzeba dodać 4 bity parzystości

        Ponadto:

        * Macierz nie może mieć identycznych kolumn -> zatem w tym przypadku wymaga to dodania co najmniej 4 bitów parzystości
            (zapewniają one wówczas 16 unikalnych kolumn)
        * Macierz nie może posiadać kolumny zerowej
        * Żadna kolumna nie może być sumą dwóch innych (w tej sytuacji wymagana jest macierz nieosobliwa -
        czyli taka o wyznaczniku 0, a zatem trzeba dodać minimum 8 bitów parzystości)

     */

    // Macierz H o wymiarach 8 x 16 (dla wiadomości o długości 8 + 8 = 16 bitów)

    public static int[][] twoErrorMatrix = {
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0 ,0, 0, 0, 0, 0},
            {1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0},
            {1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0},
            {1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0},
            {1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0},
            {1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1}
    };
}
