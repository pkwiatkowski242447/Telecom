package tips.zadanie1.tips_zadanie1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static tips.zadanie1.tips_zadanie1.ErrorsRepairClass.oneErrorMatrix;
import static tips.zadanie1.tips_zadanie1.ErrorsRepairClass.twoErrorMatrix;

public class Main {

    public static void main(String[] args) throws IOException {
        MessageRepairClass errorRepair = new MessageRepairClass();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Podaj wiadomość do zamiany na system binarny: ");
        String userInput = reader.readLine();
        String binaryResult = errorRepair.stringTo8BitBinaryConversion(userInput.getBytes());
        System.out.println("Podana wiadomość binarnie: " + binaryResult);
        String backToString = errorRepair.eightBitBinaryToStringConversion(binaryResult);
        System.out.println("Podana wiadomość w postaci ciągu znaków: " + backToString);
        String encodingResult = errorRepair.add4ParityBits(binaryResult);
        System.out.println("Wynik zakodowania danej wiadomości przy korekcji jednego błędu bitowego: " + encodingResult);
        String decodingResult = new String(errorRepair.correctGivenMessage(encodingResult, 4, oneErrorMatrix));
        System.out.println("Wynik odkodowania wiadomości: " + decodingResult);
        System.out.println("Ciąg znaków będących wynikiem: " + errorRepair.eightBitBinaryToStringConversion(decodingResult));
    }
}
