import org.junit.jupiter.api.Test;
import tips.zadanie1.model.ErrorsRepairClass;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageRepairClassTests {

    private tips.zadanie1.model.MessageRepairClass MessRepair = new tips.zadanie1.model.MessageRepairClass();

    @Test
    public void convertStringToBinaryTest() {
        String someText = "ABC";
        byte[] expectedOutput = {0,1,0,0,0,0,0,1,0,1,0,0,0,0,1,0,0,1,0,0,0,0,1,1};
        byte[] finalResult = MessRepair.stringTo8BitBinaryConversion(someText.getBytes());
        assertTrue(Arrays.equals(expectedOutput, finalResult));
    }

    @Test
    public void convertBinaryToStringTest() {
        byte[] byteArrayInput = {0,1,0,0,0,0,0,1,0,1,0,0,0,0,1,0,0,1,0,0,0,0,1,1};
        byte[] expectedOutput = "ABC".getBytes();
        byte[] finalResult = MessRepair.eightBitBinaryToStringConversion(byteArrayInput);
        assertTrue(Arrays.equals(expectedOutput, finalResult));
    }

    @Test
    public void changeCharsToValuesTest() {
        byte[] inputByteArray = {48, 49, 48, 49, 48, 49, 49, 48};
        byte[] expectedResultArray = {0, 1, 0, 1, 0, 1, 1, 0};
        byte[] finalResult = MessRepair.changeCharValuesToNumbers(inputByteArray);
        assertTrue(Arrays.equals(expectedResultArray, finalResult));
    }

    @Test
    public void changeValuesToCharsTest() {
        byte[] inputByteArray = {0, 1, 0, 1, 0, 1, 1, 0};
        byte[] expectedResultArray = {48, 49, 48, 49, 48, 49, 49, 48};
        byte[] finalResult = MessRepair.changeNumbersToCharValues(inputByteArray);
        assertTrue(Arrays.equals(expectedResultArray, finalResult));
    }

    @Test
    public void testEncoding() {
        byte[] enteredString = "AB".getBytes();
        System.out.println("Preencoded: ");
        for (byte oneByte : enteredString) {
            System.out.println("Byte value: " + oneByte);
        }
        byte[] binaryRep_1 = MessRepair.stringTo8BitBinaryConversion(enteredString);
        byte[] encodedText = MessRepair.add4ParityBits(binaryRep_1);
        System.out.println("Encoded: ");
        for (byte oneByte : encodedText) {
            System.out.println("Byte value: " + oneByte);
        }
        byte[] decodedBinary = MessRepair.correctGivenMessage(encodedText, 4, ErrorsRepairClass.oneErrorMatrix);
        byte[] decodedText = MessRepair.eightBitBinaryToStringConversion(decodedBinary);
        System.out.println("Decoded: ");
        for (byte oneByte : decodedText) {
            System.out.println("Byte value: " + oneByte);
        }
        assertTrue(Arrays.equals(enteredString, decodedText));
    }
}
