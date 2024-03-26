import org.junit.jupiter.api.Test;
import tips.zadanie2.tips_zadanie2.utils.Converter;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import java.nio.charset.StandardCharsets;

public class ConverterTest {

    @Test
    public void convertHexadecimalToAsciiTest() {
        byte[] inputHexadecimal = "536F6D65207465787420746F207468652074657374732E".getBytes(StandardCharsets.UTF_8);
        byte[] expectedOutputAscii = "Some text to the tests.".getBytes(StandardCharsets.UTF_8);
        byte[] actualOutputAscii = Converter.convertHexadecimalToAscii(inputHexadecimal);
        assertArrayEquals(expectedOutputAscii, actualOutputAscii);
    }

    @Test
    public void convertAsciiToHexadecimalTest() {
        byte[] inputAscii = "Some text to the tests.".getBytes(StandardCharsets.UTF_8);
        byte[] expectedOutputHexadecimal = "536F6D65207465787420746F207468652074657374732E".getBytes(StandardCharsets.UTF_8);
        byte[] actualOutputHexadecimal = Converter.convertAsciiToHexadecimal(inputAscii);
        assertArrayEquals(expectedOutputHexadecimal, actualOutputHexadecimal);
    }

    @Test
    public void convertStringToBinary() {
        byte[] inputAscii = "Some text to the tests.".getBytes(StandardCharsets.UTF_8);
        byte[] expectedBinaryOutput = "0101001101101111011011010110010100100000011101000110010101111000011101000010000001110100011011110010000001110100011010000110010100100000011101000110010101110011011101000111001100101110".getBytes(StandardCharsets.UTF_8);
        byte[] actualBinaryOutput = Converter.changeNumValuesToAsciiCodes(Converter.convertStringToBinary(inputAscii));
        assertArrayEquals(expectedBinaryOutput, actualBinaryOutput);
    }

    @Test
    public void convertBinaryToString() {
        byte[] inputBinary = Converter.changeAsciiCodesToNumValues("0101001101101111011011010110010100100000011101000110010101111000011101000010000001110100011011110010000001110100011010000110010100100000011101000110010101110011011101000111001100101110".getBytes(StandardCharsets.UTF_8));
        byte[] expectedAsciiOutput = "Some text to the tests.".getBytes(StandardCharsets.UTF_8);
        byte[] actualAsciiOutput = Converter.convertBinaryToString(inputBinary);
        assertArrayEquals(expectedAsciiOutput, actualAsciiOutput);
    }

    @Test
    public void changeNumValuesToAsciiCodesTest() {
        byte[] numValuesArray = {0, 0, 1, 1, 0, 1, 1, 0};
        byte[] expectedAsciiCodeArray = {48, 48, 49, 49, 48, 49, 49, 48};
        byte[] actualAsciiCodeArray = Converter.changeNumValuesToAsciiCodes(numValuesArray);
        assertArrayEquals(expectedAsciiCodeArray, actualAsciiCodeArray);
    }

    @Test
    public void changeAsciiCodesToNumValues() {
        byte[] asciiCodeArray = {48, 48, 49, 49, 48, 49, 49, 48};
        byte[] expectedNumValuesArray = {0, 0, 1, 1, 0, 1, 1, 0};
        byte[] actualNumValuesArray = Converter.changeAsciiCodesToNumValues(asciiCodeArray);
        assertArrayEquals(expectedNumValuesArray, actualNumValuesArray);
    }
}
