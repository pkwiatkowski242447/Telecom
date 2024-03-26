package tips.zadanie2.tips_zadanie2.utils;

import java.nio.charset.StandardCharsets;

public class Converter {

    /*
        @ Attribute: HEX_ARRAY

        @ Use: This array of bytes is used for conversion of bytes to HEX characters.
     */

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);

    /*
        @ Attribute: BYTE_SIZE

        @ Use: Constant used not to use "magical" values.
     */

    private static final byte BYTE_SIZE = 8;

    /*
        @ Method: convertHexadecimalToAscii()

        @ Parameters:

        * inputHexadecimal -> Byte array containing hexadecimal symbols, that later will
        be converted to bytes -> 2 hexadecimal symbol = 1 ascii code.

        @ Description: This method is used to convert byte array containing hexadecimal symbols (it codes) to
        byte array containing ascii codes.
     */

    public static byte[] convertHexadecimalToAscii(byte[] inputHexadecimal) {
        byte[] outputAscii = new byte[inputHexadecimal.length / 2];
        for (int i = 0; i < inputHexadecimal.length; i += 2) {
            outputAscii[i / 2] = (byte) ((getHexadecimalAsciiValue(inputHexadecimal[i]) << 4)
                    | getHexadecimalAsciiValue(inputHexadecimal[i + 1]));
        }
        return outputAscii;
    }

    /*
        @ Method: convertAsciiToHexadecimal()

        @ Parameters:

        * inputAscii -> Byte array containing ascii codes that later will be converted to
        hexadecimal symbols.

        @ Description: This method is used for converting byte array containing ascii codes to byte
        array containing hexadecimal symbols.
     */

    public static byte[] convertAsciiToHexadecimal(byte[] inputAscii) {
        byte[] outputHexadecimal = new byte[inputAscii.length * 2];
        for (int i = 0; i < inputAscii.length; i++) {
            int charValue = inputAscii[i];
            outputHexadecimal[2 * i] = HEX_ARRAY[(charValue >> 4) & 0x0F];
            outputHexadecimal[2 * i + 1] = HEX_ARRAY[charValue & 0x0F];
        }
        return outputHexadecimal;
    }

    /*
        @ Method: getHexadecimalAsciiValue()

        @ Parameters:

        * hexadecimalSymbol -> byte containing byte value of hexadecimal symbol

        @ Description: This method is used for getting decimal number from byte representing
        hexadecimal symbol.
     */

    private static int getHexadecimalAsciiValue(byte hexadecimalSymbol) {
        for (int i = 0; i < HEX_ARRAY.length; i++) {
            if (hexadecimalSymbol == HEX_ARRAY[i]) {
                return i;
            }
        }
        return -1;
    }

    /*
        @ Method: convertStringToBinary()

        @ Parameters:

        * inputAsciiArray -> byte array containing ascii codes for characters - either from user input or
        from input from a file.

        @ Description: This method is used for converting array of bytes, containing ascii codes to byte
        array that contains bit values for that codes - that means that result byte array is eight times bigger.
     */

    public static byte[] convertStringToBinary(byte[] inputAsciiArray) {
        byte[] outputBinaryArray = new byte[inputAsciiArray.length * 8];
        for (int i = 0; i < inputAsciiArray.length; i++) {
            int byteValue = inputAsciiArray[i];
            for (int j = 0; j < BYTE_SIZE; j++) {
                if ((byteValue & 0x80) != 0) {
                    outputBinaryArray[i * 8 + j] = 1;
                } else {
                    outputBinaryArray[i * 8 + j] = 0;
                }
                byteValue <<= 1;
            }
        }
        return outputBinaryArray;
    }

    /*
        @ Method: convertBinaryToString()

        @ Parameters:

        * inputBinaryArray -> byte array containing in each byte bit value - eight bits represent a
        byte - that is after a conversion - from binary to byte.

        @ Description: This method is used for conversion from byte array containing bits to
        byte array containing ascii codes representing bytes.
     */

    public static byte[] convertBinaryToString(byte[] inputBinaryArray) {
        byte[] outputAsciiArray = new byte[inputBinaryArray.length / 8];
        for (int i = 0; i < inputBinaryArray.length / BYTE_SIZE; i++) {
            for (int j = 0; j < BYTE_SIZE; j++) {
                outputAsciiArray[i] += (byte) (inputBinaryArray[i * 8 + j] << ((BYTE_SIZE - 1) - j));
            }
        }
        return outputAsciiArray;
    }

    /*
        @ Method: changeNumValuesToAsciiCodes()

        @ Parameters:

        * inputNumValues -> this method is used for converting array of ascii codes (for decimal digits from 0
        to 9) to their numerical values.

        @ Description: This method is used for converting ascii code byte array to byte array containing numerical
        values - that goal is achieved by subtracting from each ascii code an ascii code for 0.
     */

    public static byte[] changeNumValuesToAsciiCodes(byte[] inputNumValues) {
        byte[] outputAsciiCodes = new byte[inputNumValues.length];
        for (int i = 0; i < inputNumValues.length; i++) {
            outputAsciiCodes[i] = (byte) (inputNumValues[i] + 0x30); // Adding to every byte Ascii code for 0 in order to convert from number to letter
        }
        return outputAsciiCodes;
    }

    /*
        @ Method: changeAsciiCodesToNumValues()

        @ Parameters:

        * inputAsciiCodes -> byte array containing numerical values of decimal digits.

        @ Description: This method is used for converting numerical values to ascii code representing them.
     */

    public static byte[] changeAsciiCodesToNumValues(byte[] inputAsciiCodes) {
        byte[] outputNumValues = new byte[inputAsciiCodes.length];
        for (int i = 0; i < inputAsciiCodes.length; i++) {
            outputNumValues[i] = (byte) (inputAsciiCodes[i] - 0x30); // Adding to every byte Ascii code for 0 in order to convert from number to letter
        }
        return outputNumValues;
    }
}
