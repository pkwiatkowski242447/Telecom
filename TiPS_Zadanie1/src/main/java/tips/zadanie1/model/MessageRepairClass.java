package tips.zadanie1.model;

import java.util.Arrays;

import static tips.zadanie1.model.ErrorsRepairClass.oneErrorMatrix;
import static tips.zadanie1.model.ErrorsRepairClass.twoErrorMatrix;

public class MessageRepairClass {

    /*
        Stała dla rozmiaru bajtu - jako oktetu, czyli ośmiu bitów.
     */
    private final static int byteSize = 8;

    /*
    `   Stałe przechowujące liczbę dodawanych bitów parzystości.
     */
    private final static int fourParityBits = 4;
    private final static int eightParityBits = 8;

    /*
        Metoda służąca do konwersji tablicy bajtów, zawierającej kody ASCII, na tablicę bajtów
        zawierającą odpowiednie znaki w formie binarnej - jednym słowem wydłuża to ośmiokrotnie podstawową
        tablicę bajtów, gdzie na każdym bajcie przenoszona jest informacja, o tym czy jest to 0 czy 1.
     */

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

    /*
        Metoda służąca do konwersji tablicy bajtów, zawierającej na kolejnych elementach bity, na tablicę bajtów
        zawierającą kody znaków ASCII, uzyskane w wyniku konwersji binarnego ciągu znaków na wartość dziesiętną.
     */

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

    /*
        Metoda służąca do zamiany tablicy bajtów, w której kolejne elemetny stanowią wartości kody ASCII
        poszczególnych cyfr, na tablicę bajtów, która na kolejnych bajtach zawiera wartości dziesiętne tych cyfr.
        Wykorzysytwana jest głównie do konwersji tablicy bajtów zawierającej kody ASCII zer i jedynek, na tablicę batjów
        zawierających wartości dziesiętne - w celu łatwej konwersji z łańcucha znaków do postaci binarnej (w celu zapisu
        zmian w formie binarnej).
     */

    public byte[] changeCharValuesToNumbers(byte[] inputByteArray) {
        byte[] resultArray = new byte[inputByteArray.length];
        for (int iterator = 0; iterator < inputByteArray.length; iterator++ ) {
            resultArray[iterator] = (byte) (inputByteArray[iterator] - 0x30);
        }
        return resultArray;
    }

    /*
        Metoda służąca do zamiany tablicy bajtów, w której kolejne elemetny stanowią wartości dziesiętne
        poszczególnych cyfr, na tablicę bajtów, która na kolejnych bajtach zawiera kody ASCII tych cyfr.
        Wykorzysytwana jest głównie do konwersji tablicy bajtów zawierającej kod binarny, na tablicę batjów
        zawierających kody ASCII dla zer i jedynek - w celu łatwej konwersji do łańcucha znaków (do edycji binarnej).
     */

    public byte[] changeNumbersToCharValues(byte[] inputByteArray) {
        byte[] resultArray = new byte[inputByteArray.length];
        int iterator = 0;
        for (byte someByte : inputByteArray) {
            resultArray[iterator] = (byte) (inputByteArray[iterator] + 0x30);
            iterator++;
        }
        return resultArray;
    }

    /*
        Metoda wykorzysytwana do dodania 4 bitów parzystości do wiadomości wejściowej. Bity obliczane są na
        podstawie iloczynu wiadomości początkowej (np. poszczególnych znaków), ich zsumowanie i przeniesienie na drugą
        stronę równania (wszystkie operacje są oczywiście modulo 2).
     */

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
        if (currentIndex % 16 != 0) {
            byte[] finalResultArray = new byte[resultArray.length + 4];
            byte[] zeroByteArray = {0, 0, 0, 0};
            System.arraycopy(resultArray, 0, finalResultArray, 0,resultArray.length);
            System.arraycopy(zeroByteArray, 0, finalResultArray, resultArray.length, zeroByteArray.length );
            return finalResultArray;
        } else {
            return resultArray;
        }
    }

    /*
        Metoda wykorzysytwana do dodania 8 bitów parzystości do wiadomości wejściowej. Bity obliczane są na
        podstawie iloczynu wiadomości początkowej (np. poszczególnych znaków), ich zsumowanie i przeniesienie na drugą
        stronę równania (wszystkie operacje są oczywiście modulo 2).
     */

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

    /*
        Metoda wykorzysytwana do sprawdzenia czy występuje przekłamanie na jakimkolwiek bicie - w związku z tym oblicza
        dla każdego bajtu wiadomości (tj. givenMessage) iloczyn z macierzą H i sprawdza czy jest on wektorem zerowym - jeżeli
        nie to następuje przejście do metody checkIfThereIsSuchAColumn(), a w sytuacji przeciwnej oznacza to, że na danym
        bajcie nie występuje żadne przekłamanie.
     */

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
            if (i + (byteSize + numberOfParityBits) <= givenMessage.length) {
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
            } else {
                i++;
            }
        }
        return resultArray;
    }

    /*
        Poniższa metoda służy do sprawdzenia, w sytaucji, w której występuje przekłamanie na odpowiedniej
        liczbie bitów - czy istnieje kolumna (w przypadku kodu z 4 bitami parzystości) lub też czy istnieja kolumny (w
        przypadku kodu z 8 bitami parzystości), których suma daje uzyskany w wyniku mnożenia wiadomości wejściowej
        przez macierz H wektor. Jeżeli taka sytuacja zachodzi, to owa metoda dokona zmian na odpowiednich bitach,
        przez co efektywnie naprawi uszkodzoną wiadomość.
     */

    private byte[] checkIfThereIsSuchAColumn(byte[] inputByteArray, int numberOfParityBits, byte[] extendedByte) {
        boolean isCorrect = false;
        byte[] byteWithPBits = new byte[extendedByte.length];
        byte[] resultByte = new byte[byteSize];
        System.arraycopy(extendedByte, 0, byteWithPBits, 0, extendedByte.length);
        if (numberOfParityBits != 8) {
            numberOfParityBits = 4;
            byte[] subColumn = new byte[fourParityBits];
            for (int i = 0; i < extendedByte.length; i++) {
                for (int j = 0; j < numberOfParityBits; j++) {
                    subColumn[j] = (byte) oneErrorMatrix[j][i];
                }
                if (Arrays.equals(subColumn, inputByteArray) && !isCorrect) {
                    byteWithPBits[i] = (byte) ((extendedByte[i] + 1) % 2);
                    isCorrect = true;
                }
            }
        } else {
            byte[] subColumn = new byte[eightParityBits];
            for (int i = 0; i < extendedByte.length; i++) {
                for (int j = 0; j < byteSize; j++) {
                    subColumn[j] = (byte) twoErrorMatrix[j][i];
                }
                if (Arrays.equals(subColumn, inputByteArray) && !isCorrect) {
                    byteWithPBits[i] = (byte) ((extendedByte[i] + 1) % 2);
                    isCorrect = true;
                }
            }
            for (int i = 0; i < extendedByte.length; i++) {
                for (int k = i + 1; k < extendedByte.length; k++) {
                    for (int j = 0; j < numberOfParityBits; j++) {
                        subColumn[j] = (byte) correctModuloFunction(twoErrorMatrix[j][i] + twoErrorMatrix[j][k], 2);
                    }
                    if (Arrays.equals(subColumn, inputByteArray) && !isCorrect) {
                        byteWithPBits[i] = (byte) ((extendedByte[i] + 1) % 2);
                        byteWithPBits[k] = (byte) ((extendedByte[k] + 1) % 2);
                        isCorrect = true;
                    }
                }
            }
        }
        System.arraycopy(byteWithPBits, 0, resultByte, 0, byteSize);
        return resultByte;
    }

    /*
        Metoda wykorzystywana do obliczania wyniku operacji firstArg mod secondArg.

        Wynika to z faktu, że operacja % (reszta z dzielenia) potrafi zwrócić wynik ujemny, co nie jest
        zgodne z założeniami matematycznymi tej operacji.

        & firstArg      -> liczba całkowita, której resztę z dzielenia przez secondArg chcemy obliczyć
        & secondArg     -> liczba naturalna, która wykorzystana jest w owej operacji jako dzielnik
     */

    private int correctModuloFunction(int firstArg, int secondArg) {
        if (firstArg >= 0) {
            return firstArg % secondArg;
        } else if (firstArg < 0 && (firstArg % secondArg) >= 0) {
            return firstArg % secondArg;
        } else {
            return (firstArg % secondArg) + Math.abs(secondArg);
        }
    }

    /*
        Metoda służąca do podnoszenia wartości całkowitej do całkowitej potęgi.

        @ firstArg      -> liczba całkowita, która będzie podnoszona do potęgi
        @ secondArg     -> potęga całkowita, do której będzie podnoszona liczba
     */

    private double correctPow(int firstArg, int secondArg) {
        if (firstArg != 0 || secondArg != 0) {
            return Math.pow(firstArg, secondArg);
        } else {
            return 0;
        }
    }
}
