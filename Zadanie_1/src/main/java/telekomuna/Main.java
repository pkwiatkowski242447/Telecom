package telekomuna;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Message message_object = new Message();
        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj wiadomość: ");
        String lala;
        String message_input = sc.nextLine();
        lala = message_object.convert_string_to_binary(message_input.getBytes());
        System.out.println(lala);
    }
}