package tips.zadanie1.tips_zadanie1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static tips.zadanie1.tips_zadanie1.ErrorsRepairClass.oneErrorMatrix;
import static tips.zadanie1.tips_zadanie1.ErrorsRepairClass.twoErrorMatrix;
import static tips.zadanie1.tips_zadanie1.HelperClass.printMenu;

public class Main {

    public static void main(String[] args) throws IOException {
        MessageRepairClass errorRepair = new MessageRepairClass();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String enteredString;
        boolean isFinished = false;
        int numOfParBits = 0;
        do {
            printMenu();
            System.out.print("Twój wybór: ");
            enteredString = reader.readLine();

            if (Objects.equals(enteredString, "1")) {
                System.out.print("\nPodaj ciąg znaków do zakodowania: ");
                enteredString = reader.readLine();
                String encodedMessage = errorRepair.stringTo8BitBinaryConversion(enteredString.getBytes());
                System.out.println("Podana wiadomość binarnie: " + encodedMessage);
                String fourBitEncoding = errorRepair.add4ParityBits(encodedMessage);
                String eightBitEncoding = errorRepair.add8ParityBits(encodedMessage);
                System.out.println("Wynik kodowania z 4 bitami parzystości: " + fourBitEncoding);
                System.out.println("Wynik kodowania z 8 bitami parzystości: " + eightBitEncoding);
            } else if (Objects.equals(enteredString, "2")) {
                System.out.print("\nPodaj ciąg znaków do rozkodowania: ");
                enteredString = reader.readLine();
                System.out.print("Ile bitów parzystości uwzględnia podany kod: ");
                numOfParBits = Integer.parseInt(reader.readLine());
                if (numOfParBits == 4) {
                    byte[] decodedTextWithOneError = errorRepair.correctGivenMessage(enteredString, 4, oneErrorMatrix);
                    String oneErrorCorrection = new String(decodedTextWithOneError, StandardCharsets.UTF_8);
                    System.out.println("Po zdekodowaniu kodem naprawiającym jeden błąd: " + oneErrorCorrection);
                    System.out.println("Wiadomość w postaci znaków (jeden błąd bitowy): " + errorRepair.eightBitBinaryToStringConversion(oneErrorCorrection));
                } else {
                    byte[] decodedTextWithTwoErrors = errorRepair.correctGivenMessage(enteredString, 8, twoErrorMatrix);
                    String twoErrorCorrection = new String(decodedTextWithTwoErrors, StandardCharsets.UTF_8);
                    System.out.println("Po zdekodowaniu kodem naprawiającym dwa błędy: " + twoErrorCorrection);
                    System.out.println("Wiadomość w postaci znaków (dwa błędy bitowe): " + errorRepair.eightBitBinaryToStringConversion(twoErrorCorrection));
                }
            } else if (Objects.equals(enteredString, "3")) {
                System.out.println("Wybrano wyjście z programu.");
                isFinished = true;
            } else {
                System.out.println("\nNie ma takiej opcji w menu.");
            }
        } while(!isFinished);
    }
}
