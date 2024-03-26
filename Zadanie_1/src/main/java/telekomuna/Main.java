package telekomuna;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Message_Repair message_object = new Message_Repair();
        FileManager fileManager_object = new FileManager("binary_output.txt");
        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj wiadomość: ");
        int[] lala;
        String string_in_binary;
        String message_input = sc.nextLine();
        string_in_binary = message_object.convert_string_to_binary(message_input.getBytes());
        System.out.println("Twoja wiadomość w kodzie binarnym");
        System.out.println(string_in_binary);
        lala = message_object.add8ParityBits(string_in_binary);
        fileManager_object.save_to_file(lala);
    }
}