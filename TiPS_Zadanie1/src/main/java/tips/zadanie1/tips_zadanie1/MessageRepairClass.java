package tips.zadanie1.tips_zadanie1;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static tips.zadanie1.tips_zadanie1.ErrorsRepairClass.oneErrorMatrix;
import static tips.zadanie1.tips_zadanie1.ErrorsRepairClass.twoErrorMatrix;

public class MessageRepairClass {

    private final static int byteSize = 8;
    private final static int fourParityBits = 4;
    private final static int eightParityBits = 8;

    public String stringTo8BitBinaryConversion(byte[] inputMessage) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte someByte : inputMessage) {
            int byteValue = someByte;
            for (int i = 0; i < 8; i++) {
                stringBuilder.append((byteValue & 128) == 0 ? 0 : 1);
                byteValue <<= 1;
            }
        }
        return stringBuilder.toString();
    }

    public String eightBitBinaryToStringConversion(String binaryInput) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < binaryInput.length(); index += 8) {
            String temporaryString = binaryInput.substring(index, index + 8);
            int charValue = Integer.parseInt(temporaryString, 2);
            char someChar = (char) charValue;
            stringBuilder.append(someChar);
        }
        return stringBuilder.toString();
    }

    public byte[] changeCharValuesToNumbers(byte[] inputByteArray) {
        byte[] resultArray = new byte[inputByteArray.length];
        int iterator = 0;
        for (byte someByte : inputByteArray) {
            resultArray[iterator] = (byte) (inputByteArray[iterator] - 0x30);
            iterator++;
        }
        return resultArray;
    }

    public byte[] changeNumbersToCharValues(byte[] inputByteArray) {
        byte[] resultArray = new byte[inputByteArray.length];
        int iterator = 0;
        for (byte someByte : inputByteArray) {
            resultArray[iterator] = (byte) (inputByteArray[iterator] + 0x30);
            iterator++;
        }
        return resultArray;
    }

    public String add4ParityBits(String inputBinaryText) {
        byte[] byteArrayInputText = changeCharValuesToNumbers(inputBinaryText.getBytes());
        byte[] oneByte = new byte[byteSize];
        byte[] arrayOfParityBits = new byte[fourParityBits];
        byte[] resultArray = new byte[3 * inputBinaryText.getBytes().length / 2];
        int currentIndex = 0;
        byte prevInfo;
        for (int i = 0; i < byteArrayInputText.length;) {
            for (int j = 0; j < byteSize; j++) {
                oneByte[j] = byteArrayInputText[i];
                i++;
            }
            for (int k = 0; k < fourParityBits; k++) {
                for (int l = 0; l < byteSize; l++) {
                    prevInfo = (byte) correctModuloFunction(((-1) * (oneErrorMatrix[k][l] * oneByte[l])), 2);
                    arrayOfParityBits[k] = (byte) correctModuloFunction((arrayOfParityBits[k] + prevInfo), 2);
                }
            }
            System.arraycopy(oneByte, 0, resultArray, currentIndex, oneByte.length);
            currentIndex += 8;
            System.arraycopy(arrayOfParityBits, 0 ,resultArray, currentIndex, arrayOfParityBits.length);
            currentIndex += 4;
        }
        resultArray = changeNumbersToCharValues(resultArray);

        return new String(resultArray, StandardCharsets.UTF_8);
    }

    public String add8ParityBits(String inputBinaryText) {
        byte[] byteArrayInputText = changeCharValuesToNumbers(inputBinaryText.getBytes());
        byte[] oneByte = new byte[byteSize];
        byte[] arrayOfParityBits = new byte[eightParityBits];
        byte[] resultArray = new byte[2 * inputBinaryText.getBytes().length];
        int currentIndex = 0;
        byte prevInfo;
        for (int i = 0; i < byteArrayInputText.length;) {
            for (int j = 0; j < byteSize; j++) {
                oneByte[j] = byteArrayInputText[i];
                i++;
            }
            for (int k = 0; k < eightParityBits; k++) {
                for (int l = 0; l < byteSize; l++) {
                    prevInfo = (byte) correctModuloFunction(((-1) * (twoErrorMatrix[k][l] * oneByte[l])), 2);
                    arrayOfParityBits[k] = (byte) correctModuloFunction((arrayOfParityBits[k] + prevInfo), 2);
                }
            }
            System.arraycopy(oneByte, 0, resultArray, currentIndex, oneByte.length);
            currentIndex += 8;
            System.arraycopy(arrayOfParityBits, 0 ,resultArray, currentIndex, arrayOfParityBits.length);
            currentIndex += 8;
        }
        resultArray = changeNumbersToCharValues(resultArray);

        return new String(resultArray, StandardCharsets.UTF_8);
    }

    public byte[] correctGivenMessage(String givenMessage, int numberOfParityBits, int[][] correctionArray) {
        byte[] inputMessageArray = changeCharValuesToNumbers(givenMessage.getBytes());
        byte[] oneByte = new byte[byteSize];
        int iterator = 0;
        byte prevInfo = 0;
        byte[] resultArray = null;
        byte[] extendedByte = new byte[numberOfParityBits + byteSize];
        byte[] matrixColumn = new byte[numberOfParityBits];
        if (numberOfParityBits == 4) {
            resultArray = new byte[2 * inputMessageArray.length / 3];
        } else {
            resultArray = new byte[inputMessageArray.length / 2];
        }
        for (int i = 0; i < inputMessageArray.length;) {
            for (int j = 0; j < (byteSize + numberOfParityBits); j++) {
                extendedByte[j] = inputMessageArray[i];
                i++;
            }
            for (int k = 0; k < numberOfParityBits; k++) {
                for (int l = 0; l < extendedByte.length; l++) {
                    prevInfo = (byte) correctModuloFunction(correctionArray[k][l] * extendedByte[l], 2);
                    matrixColumn[k] = (byte) correctModuloFunction(matrixColumn[k] * prevInfo, 2);
                }
            }
            for (int m = 0; m < numberOfParityBits; m++) {
                if (matrixColumn[m] != 0) {
                    oneByte = checkIfThereIsSuchAColumn(matrixColumn, numberOfParityBits, extendedByte);
                    break;
                }
            }
            System.arraycopy(oneByte, 0, resultArray, iterator, oneByte.length);
            iterator += 8;
        }
        return changeNumbersToCharValues(resultArray);
    }

    /// Trzeba edytować tą metodę, tak aby zwracała tablicę 8 bajtów
    private byte[] checkIfThereIsSuchAColumn(byte[] inputByteArray, int numberOfParityBits, byte[] extendedByte) {
        if (numberOfParityBits != 8) {
            numberOfParityBits = 4;
            byte[] subColumn = new byte[fourParityBits];
            for (int i = 0; i < inputByteArray.length; i++) {
                for (int j = 0; j < numberOfParityBits; j++) {
                    subColumn[j] = (byte) oneErrorMatrix[j][i];
                }
                if (Arrays.equals(subColumn, inputByteArray)) {
                    extendedByte[i] = (byte) ((extendedByte[i] + 1) % 2);
                    break;
                }
            }
        } else {
            byte[] subColumn = new byte[eightParityBits];
            for (int i = 0; i < inputByteArray.length; i++) {
                for (int k = 0; k < inputByteArray.length; k++) {
                    for (int j = 0; j < numberOfParityBits; j++) {
                        subColumn[j] = (byte) correctModuloFunction(twoErrorMatrix[j][i] + twoErrorMatrix[j][k], 2);
                        if (Arrays.equals(subColumn, inputByteArray)) {
                            extendedByte[i] = (byte) ((extendedByte[i] + 1) % 2);
                            extendedByte[k] = (byte) ((extendedByte[k] + 1) % 2);
                        }
                    }
                }
            }
        }
        byte[] resultArray = new byte[byteSize];
        System.arraycopy(inputByteArray, 0, resultArray, 0, byteSize);
        return resultArray;
    }

    private int correctModuloFunction(int firstArg, int secondArg) {
        if (firstArg > 0) {
            return firstArg % secondArg;
        } else {
            return (firstArg % secondArg) + Math.abs(secondArg);
        }
    }
}
