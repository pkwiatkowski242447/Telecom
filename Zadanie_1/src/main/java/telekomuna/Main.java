package telekomuna;

import java.util.Scanner;

public class Main {
    public static int[][] oneErrorMatrix = {
            {0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0},
            {0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0},
            {1, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0},
            {1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1}
    };

    public static void main(String[] args) {
        Message_Repair message_object = new Message_Repair();
        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj wiadomość: ");
        int[][] lala;
        String string_in_binary;
        String message_input = sc.nextLine();
        string_in_binary = message_object.convert_string_to_binary(message_input.getBytes());
        System.out.println("Twoja wiadomość w kodzie binarnym");
        System.out.println(string_in_binary);
        lala = message_object.add4ParityBits(string_in_binary);

        /*
         11100000   1001
         11110000   0000
         00111111   0110

         00111001   1000
         01100110   0100
         11001011   0010
         10010111   0001
         */
    }
}