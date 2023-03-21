package telekomuna;

public class Message {

    public String convert_string_to_binary(byte[] text) {
        StringBuilder in_binary = new StringBuilder();
        for (byte b : text) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                in_binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return in_binary.toString();
    }

    public String convert_binary_to_string(String binary_text) {
        StringBuilder string_text = new StringBuilder();
        int charCode;
        for (int i = 0; i < binary_text.length(); i = i + 8) {
            charCode = Integer.parseInt(binary_text.substring(i, i + 8), 2);
            String returnChar = Character.toString((char) charCode);
            string_text.append(returnChar);
        }
        return string_text.toString();
    }
}
