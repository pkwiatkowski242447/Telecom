package tips.zadanie1.tips_zadanie1;

public class MessageRepairClass {

    public String stringTo8BitBinaryConversion(String inputMessage) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte someByte : inputMessage.getBytes()) {
            int byteValue = someByte;
            for (int i = 0; i < 8; i++) {
                stringBuilder.append((byteValue & 128) == 0 ? 0 : 1);
                byteValue <<= 1;
            }
            stringBuilder.append(' ');
        }
        return stringBuilder.toString();
    }

}
