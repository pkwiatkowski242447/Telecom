package tips.zadanie3.tips_zadanie3.utils;

import java.nio.charset.StandardCharsets;

public class Converter {
    public static byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);

    public static byte[] convertHexadecimalToAscii(byte[] inputArrayOfHexadecimal) {
        byte[] outputArrayOfAscii = new byte[inputArrayOfHexadecimal.length / 2];
        for (int i = 0; i < inputArrayOfHexadecimal.length; i += 2) {
            outputArrayOfAscii[i / 2] = (byte) ((getHexadecimalDigitValue(inputArrayOfHexadecimal[i]) << 4)
                    | getHexadecimalDigitValue(inputArrayOfHexadecimal[i + 1]));
        }
        return outputArrayOfAscii;
    }

    public static byte[] convertAsciiToHexadecimal(byte[] inputArrayOfAscii) {
        byte[] outputArrayOfHexadecimal = new byte[inputArrayOfAscii.length * 2];
        for (int i = 0; i < inputArrayOfAscii.length; i++) {
            int charValue = inputArrayOfAscii[i] & 0xFF;
            outputArrayOfHexadecimal[2 * i] = HEX_ARRAY[charValue >> 4];
            outputArrayOfHexadecimal[2 * i + 1] = HEX_ARRAY[charValue & 0x0F];
        }
        return outputArrayOfHexadecimal;
    }

    private static int getHexadecimalDigitValue(byte hexDigit) {
        for (int i = 0; i < HEX_ARRAY.length; i++) {
            if (hexDigit == HEX_ARRAY[i]) {
                return i;
            }
        }
        return -1;
    }

    public static byte[] convertStringToEightBitBinary(byte[] inputStringByteArray) {
        byte[] outputBinaryByteArray = new byte[inputStringByteArray.length * 8];
        for (int i = 0; i < inputStringByteArray.length; i++) {
            byte byteValue = inputStringByteArray[i];
            for (int j = 0; j < 8; j++) {
                if ((byteValue & 0x80) != 0) {
                    outputBinaryByteArray[i * 8 + j] = 1;
                } else {
                    outputBinaryByteArray[i * 8 + j] = 0;
                }
                byteValue <<= 1;
            }
        }
        return outputBinaryByteArray;
    }

    public static byte[] convertEightBitBinaryToString(byte[] inputBinaryByteArray) {
        byte[] outputStringByteArray = new byte[inputBinaryByteArray.length / 8];
        for (int i = 0; i < outputStringByteArray.length; i++) {
            byte byteValue = 0;
            for (int j = 0 ; j < 8; j++) {
                byteValue = (byte) ((byteValue << 1) | inputBinaryByteArray[i * 8 + j]);
            }
            outputStringByteArray[i] = byteValue;
        }
        return outputStringByteArray;
    }

    public static byte[] convertNumValuesToStringCodes(byte[] inputNumByteArray) {
        byte[] outputStringCodesArray = new byte[inputNumByteArray.length];
        for (int i = 0; i < inputNumByteArray.length; i++) {
            outputStringCodesArray[i] = (byte) (inputNumByteArray[i] + 0x30);
        }
        return outputStringCodesArray;
    }

    public static byte[] convertStringCodesToNumValues(byte[] inputStringByteArray) {
        byte[] outputNumValuesArray = new byte[inputStringByteArray.length];
        for (int i = 0; i < inputStringByteArray.length; i++) {
            outputNumValuesArray[i] = (byte) (inputStringByteArray[i] - 0x30);
        }
        return outputNumValuesArray;
    }
}
