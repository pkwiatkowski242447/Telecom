import org.junit.jupiter.api.Test;
import tips.zadanie1.model.ErrorsRepairClass;
import tips.zadanie1.model.HelperClass;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageRepairClassTest {

    private tips.zadanie1.model.MessageRepairClass MessRepair = new tips.zadanie1.model.MessageRepairClass();

    @Test
    public void convertStringToBinaryTest() {
        String someText = "ABC";
        byte[] expectedOutput = {0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1};
        byte[] finalResult = MessRepair.stringTo8BitBinaryConversion(someText.getBytes());
        assertTrue(Arrays.equals(expectedOutput, finalResult));
    }

    @Test
    public void convertBinaryToStringTest() {
        byte[] byteArrayInput = {0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1};
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
        byte[] binaryRep_1 = MessRepair.stringTo8BitBinaryConversion(enteredString);
        byte[] encodedText = MessRepair.add4ParityBits(binaryRep_1);
        byte[] decodedBinary = MessRepair.correctGivenMessage(encodedText, 4, ErrorsRepairClass.oneErrorMatrix);
        byte[] decodedText = MessRepair.eightBitBinaryToStringConversion(decodedBinary);
        assertTrue(Arrays.equals(enteredString, decodedText));
    }

    @Test
    public void convertHexToByteArrayTest() {
        byte[] inputHexArray = "536F6D652074657874".getBytes(StandardCharsets.US_ASCII);
        byte[] expectedResult = "Some text".getBytes(StandardCharsets.US_ASCII);
        byte[] finalResult = HelperClass.convertHexToByteArray(inputHexArray);
        assertTrue(Arrays.equals(expectedResult, finalResult));
    }

    @Test
    public void convertByteArrayToHex() {
        byte[] inputByteArray = "Some text".getBytes(StandardCharsets.US_ASCII);
        byte[] expectedResult = "536F6D652074657874".getBytes(StandardCharsets.US_ASCII);
        byte[] finalResult = HelperClass.convertByteArrayToHex(inputByteArray);
        assertTrue(Arrays.equals(expectedResult, finalResult));
    }

    @Test
    public void twoErrorMatrixCorrectionTest() {
        assertTrue(HelperClass.checkTwoErrorMatrixCorrectness(ErrorsRepairClass.twoErrorMatrix));
    }

    @Test
    public void oneErrorMatrixCorrectionTest() {
        assertTrue(HelperClass.checkOneErrorMatrixCorrectness(ErrorsRepairClass.oneErrorMatrix));
    }

    @Test
    public void oneMistakeInByteAndOneInParityBitsTest() {
        byte[] someMessage = "a".getBytes(StandardCharsets.US_ASCII);
        byte[] binaryInput = MessRepair.stringTo8BitBinaryConversion(someMessage);
        byte[] result = MessRepair.add8ParityBits(binaryInput);
        result[0] = (byte) ((result[0] + 1) & 1);
        result[8] = (byte) ((result[8] + 1) & 1);
        byte[] previousMessage = MessRepair.eightBitBinaryToStringConversion(MessRepair.correctGivenMessage(result, 8, ErrorsRepairClass.twoErrorMatrix));
        assertTrue(Arrays.equals(someMessage, previousMessage));
    }

    @Test
    public void oneErrorTestWith4BitCode() {
        byte[] someMessage = {0x0F};
        byte[] binaryInput = MessRepair.stringTo8BitBinaryConversion(someMessage);
        byte[] result = MessRepair.add4ParityBits(binaryInput);
        result[0] = (byte) ((result[0] + 1) & 1);
        byte[] previousMessage = MessRepair.eightBitBinaryToStringConversion(MessRepair.correctGivenMessage(result, 4, ErrorsRepairClass.oneErrorMatrix));
        System.out.println("someMessage: " + someMessage[0]);
        System.out.println("previousMessage: " + previousMessage[0]);
        assertTrue(Arrays.equals(someMessage, previousMessage));
    }
}
