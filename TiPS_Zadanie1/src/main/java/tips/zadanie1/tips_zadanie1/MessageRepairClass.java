package tips.zadanie1.tips_zadanie1;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static tips.zadanie1.tips_zadanie1.ErrorsRepairClass.oneErrorMatrix;
import static tips.zadanie1.tips_zadanie1.ErrorsRepairClass.twoErrorMatrix;

public class MessageRepairClass {

    private final static int byteSize = 8;
    private final static int fourParityBits = 4;
    private final static int eightParityBits = 8;

    public byte[] stringTo8BitBinaryConversion(byte[] inputMessage) {
        byte[] resultBinaryArray = new byte[inputMessage.length * 8];
        for (int iterator = 0; iterator < inputMessage.length; iterator++) {
            int byteValue = inputMessage[iterator];
            for (int i = 0; i < 8; i++) {
                if ((byteValue & 128) == 0) {
                    resultBinaryArray[iterator * 8 + i] = 0;
                } else {
                    resultBinaryArray[iterator * 8 + i] = 1;
                }
                byteValue <<= 1;
            }
        }
        return resultBinaryArray;
    }

    public byte[] eightBitBinaryToStringConversion(byte[] binaryInput) {
        byte[] resultStringArray = new byte[binaryInput.length / byteSize];
        int charValue = 0;
        for (int index = 0; index < binaryInput.length / 8; index++) {
            for (int j = 0; j < 8; j++) {
                charValue += correctPow(binaryInput[index * byteSize + j] * 2, 7 - j);
            }
            resultStringArray[index] = (byte) charValue;
            charValue = 0;
        }
        return resultStringArray;
    }

    public byte[] changeCharValuesToNumbers(byte[] inputByteArray) {
        byte[] resultArray = new byte[inputByteArray.length];
        for (int iterator = 0; iterator < inputByteArray.length; iterator++ ) {
            resultArray[iterator] = (byte) (inputByteArray[iterator] - 0x30);
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

    public byte[] add4ParityBits(byte[] inputBinaryText) {
        byte[] oneByte = new byte[byteSize];
        byte[] arrayOfParityBits = new byte[fourParityBits];
        byte[] resultArray = new byte[3 * inputBinaryText.length / 2];
        int currentIndex = 0;
        byte prevInfo = 0;
        for (int i = 0; i < inputBinaryText.length;) {
            for (int j = 0; j < byteSize; j++) {
                oneByte[j] = inputBinaryText[i];
                i++;
            }
            for (int k = 0; k < fourParityBits; k++) {
                for (int l = 0; l < byteSize; l++) {
                    prevInfo += oneErrorMatrix[k][l] * oneByte[l];
                }
                arrayOfParityBits[k] = (byte) correctModuloFunction(prevInfo, 2);
                prevInfo = 0;
            }
            System.arraycopy(oneByte, 0, resultArray, currentIndex, oneByte.length);
            currentIndex += 8;
            System.arraycopy(arrayOfParityBits, 0 ,resultArray, currentIndex, arrayOfParityBits.length);
            currentIndex += 4;
        }
        return resultArray;
    }

    public byte[] add8ParityBits(byte[] inputBinaryText) {
        byte[] oneByte = new byte[byteSize];
        byte[] arrayOfParityBits = new byte[eightParityBits];
        byte[] resultArray = new byte[2 * inputBinaryText.length];
        int currentIndex = 0;
        byte prevInfo = 0;
        for (int i = 0; i < inputBinaryText.length;) {
            for (int j = 0; j < byteSize; j++) {
                oneByte[j] = inputBinaryText[i];
                i++;
            }
            for (int k = 0; k < eightParityBits; k++) {
                for (int l = 0; l < byteSize; l++) {
                    prevInfo += twoErrorMatrix[k][l] * oneByte[l];
                }
                arrayOfParityBits[k] = (byte) correctModuloFunction(prevInfo, 2);
                prevInfo = 0;
            }
            System.arraycopy(oneByte, 0, resultArray, currentIndex, oneByte.length);
            currentIndex += 8;
            System.arraycopy(arrayOfParityBits, 0 ,resultArray, currentIndex, arrayOfParityBits.length);
            currentIndex += 8;
        }
        return resultArray;
    }

    public byte[] correctGivenMessage(byte[] givenMessage, int numberOfParityBits, int[][] correctionArray) {
        byte[] oneByte = new byte[byteSize];
        int iterator = 0;
        byte prevInfo = 0;
        byte[] resultArray = null;
        byte[] extendedByte = new byte[numberOfParityBits + byteSize];
        byte[] matrixColumn = new byte[numberOfParityBits];
        boolean isCorrected = false;
        if (numberOfParityBits == 4) {
            resultArray = new byte[2 * givenMessage.length / 3];
        } else {
            resultArray = new byte[givenMessage.length / 2];
        }
        for (int i = 0; i < givenMessage.length;) {
            for (int j = 0; j < (byteSize + numberOfParityBits); j++) {
                extendedByte[j] = givenMessage[i];
                i++;
            }
            for (int k = 0; k < numberOfParityBits; k++) {
                for (int l = 0; l < extendedByte.length; l++) {
                    prevInfo += correctionArray[k][l] * extendedByte[l];
                }
                matrixColumn[k] = (byte) correctModuloFunction(prevInfo, 2);
                prevInfo = 0;
            }
            for (int m = 0; m < numberOfParityBits; m++) {
                if (matrixColumn[m] != 0) {
                    oneByte = checkIfThereIsSuchAColumn(matrixColumn, numberOfParityBits, extendedByte);
                    isCorrected = true;
                    break;
                }
            }
            if (!isCorrected) {
                System.arraycopy(extendedByte, 0, oneByte, 0, byteSize);
            }
            System.arraycopy(oneByte, 0, resultArray, iterator, byteSize);
            iterator += 8;
            isCorrected = false;
        }
        return resultArray;
    }

    private byte[] checkIfThereIsSuchAColumn(byte[] inputByteArray, int numberOfParityBits, byte[] extendedByte) {
        boolean isCorrect = false;
        byte[] oneByte = new byte[byteSize];
        System.arraycopy(extendedByte, 0, oneByte, 0, byteSize);
        if (numberOfParityBits != 8) {
            numberOfParityBits = 4;
            byte[] subColumn = new byte[fourParityBits];
            for (int i = 0; i < byteSize; i++) {
                for (int j = 0; j < numberOfParityBits; j++) {
                    subColumn[j] = (byte) oneErrorMatrix[j][i];
                }
                if (Arrays.equals(subColumn, inputByteArray) && !isCorrect) {
                    oneByte[i] = (byte) ((extendedByte[i] + 1) % 2);
                    isCorrect = true;
                }
            }
        } else {
            byte[] subColumn = new byte[eightParityBits];
            for (int i = 0; i < byteSize; i++) {
                for (int j = 0; j < byteSize; j++) {
                    subColumn[j] = (byte) twoErrorMatrix[j][i];
                }
                if (Arrays.equals(subColumn, inputByteArray) && !isCorrect) {
                    oneByte[i] = (byte) ((extendedByte[i] + 1) % 2);
                    isCorrect = true;
                }
            }
            for (int i = 0; i < byteSize; i++) {
                for (int k = 0; k < byteSize; k++) {
                    for (int j = 0; j < numberOfParityBits; j++) {
                        subColumn[j] = (byte) correctModuloFunction(twoErrorMatrix[j][i] + twoErrorMatrix[j][k], 2);
                    }
                    if (Arrays.equals(subColumn, inputByteArray) && !isCorrect) {
                        oneByte[i] = (byte) ((extendedByte[i] + 1) % 2);
                        oneByte[k] = (byte) ((extendedByte[k] + 1) % 2);
                        isCorrect = true;
                    }
                }
            }
        }
        return oneByte;
    }

    private int correctModuloFunction(int firstArg, int secondArg) {
        if (firstArg >= 0) {
            return firstArg % secondArg;
        } else if (firstArg < 0 && (firstArg % secondArg) >= 0) {
            return firstArg % secondArg;
        } else {
            return (firstArg % secondArg) + Math.abs(secondArg);
        }
    }

    private double correctPow(int firstArg, int secondArg) {
        if (firstArg != 0 || secondArg != 0) {
            return Math.pow(firstArg, secondArg);
        } else {
            return 0;
        }
    }

    private int reqLength(int numberOfParityBits, int arrayLength) {
        int requiredLength = 0;
        int nrOfBlocks = arrayLength / (byteSize + numberOfParityBits);
        if (nrOfBlocks == 0) {
            requiredLength = (byteSize + numberOfParityBits);
        } else if (arrayLength % (byteSize + numberOfParityBits) == 0) {
            requiredLength = (byteSize + numberOfParityBits) * nrOfBlocks;
        } else {
            requiredLength = (byteSize + numberOfParityBits) * (nrOfBlocks + 1);
        }
        return requiredLength;
    }
}
