package tips.zadanie1.tips_zadanie1;

public class HelperClass {

    public static void printMenu() {
        System.out.println("===== MENU =====");
        System.out.println("1. Zakodowanie podanego łańcucha znaków.");
        System.out.println("2. Odkodowanie podanego łańcucha znaków.");
        System.out.println("3. Zakończenie działania programu.");
        System.out.println("================");
    }

    public static void printTextChoice() {
        System.out.println("===== Wybór sposobu wprowadzania znaków =====");
        System.out.println("1. Wpisanie znaku z klawiatury.");
        System.out.println("2. Wczytanie znaku z pliku.");
        System.out.println("=============================================");
    }

    public static void printParityBitsChoice() {
        System.out.println("===== Wybór liczby bitów parzystości =====");
        System.out.println("1. Kodowanie naprawiające jeden błąd (4 bity).");
        System.out.println("2. Kodowanie naprawiające jeden lub dwa błędy (8 bitów).");
        System.out.println("==========================================");
    }

}
