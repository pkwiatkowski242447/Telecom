package tips.zadanie1.model;

public class HelperClass {

    private static final byte[] HEX_TABLE = "0123456789ABCDEF".getBytes();

    /*
        Metoda wykorzystywana do konwersji zadanej tablicy bajtów do tablicy bajtów zawierającej kody
        ASCII znaków heksadecymalnych (tj. szesnastkowych).
     */

    public static byte[] convertHexToByteArray(byte[] inputHexArray) {
        byte[] outputByteArray = new byte[inputHexArray.length / 2];
        for (int i = 0; i < inputHexArray.length; i += 2) {
            outputByteArray[i / 2] = (byte) (((digitValue(inputHexArray[i]) << 4) + digitValue(inputHexArray[i + 1])));
        }
        return outputByteArray;
    }

    /*
        Metoda wykorzystywana do konwersji zadanej tablicy bajtów, zawierającej kody ASCII znaków heksadecymalnych
        (tj. szesnastkowych) do tablicy bajtów, zawierającej bajty - gdzie na jeden taki bajt składają się dwa znaki
        heksadecymalne.
     */

    public static byte[] convertByteArrayToHex(byte[] inputByteArray) {
        byte[] outputHexArray = new byte[2 * inputByteArray.length];
        for (int i = 0; i < inputByteArray.length; i++) {
            int value = inputByteArray[i] & 0xFF;
            outputHexArray[2 * i] = HEX_TABLE[value >> 4];
            outputHexArray[2 * i + 1] = HEX_TABLE[value & 0x0F];
        }
        return outputHexArray;
    }

    /*
        Metoda służąca do ustalenia wartości dziesiętnej znaku heksadecymalnego.
     */

    private static int digitValue(byte value) {
        for (int i = 0; i < HEX_TABLE.length; i++) {
            if (value == HEX_TABLE[i]) {
                return i;
            }
        }
        return -1;
    }

    /*
        Metoda sprawdzająca wymagania formalne dla tablicy wykorzystywanej do poprawiania dwóch błędów
        bitowych. Zwraca ona wartość logiczną true, jeżeli wymagania są przez macierz spełnione.
     */

    public static boolean checkTwoErrorMatrixCorrectness(int[][] errorCorrectionMatrix) {
        int numberOfDifferentBits;

        // Sprawdzenie warunku na odległość pomiędzy kolejnymi słowami kodowymi

        for (int i = 0; i < errorCorrectionMatrix.length; i++) {
            for (int j = i + 1; j < errorCorrectionMatrix.length; j++) {
                numberOfDifferentBits = 0;
                for (int k = 0; k < errorCorrectionMatrix[0].length; k++) {
                    if (errorCorrectionMatrix[i][k] != errorCorrectionMatrix[j][k]) {
                        numberOfDifferentBits++;
                    }
                }
                if (numberOfDifferentBits <= 5) {
                    return false;
                }
            }
        }

        // Sprawdzenie warunku na poprawność kolumn:

        // Sprawdzenie czy dana jest kolumna zerowa

        boolean isZeroColumn = true;
        for (int i = 0; i < errorCorrectionMatrix[0].length; i++) {
            for (int k = 0; k < errorCorrectionMatrix.length; k++) {
                if (errorCorrectionMatrix[k][i] != 0) {
                    isZeroColumn = false;
                }
            }
            if (isZeroColumn) {
                return false;
            }
        }

        // Sprawdzenie, czy żadna kolumna nie jest sumą dwóch innych

        boolean isDuplicate;
        int[] column;
        for (int i = 0; i < errorCorrectionMatrix[0].length; i++) {
            column = new int[errorCorrectionMatrix.length];
            isDuplicate = true;
            for (int j = 0; j < errorCorrectionMatrix[0].length; j++) {
                for (int k = 0; k < errorCorrectionMatrix.length; k++) {
                    column[k] = (errorCorrectionMatrix[k][i] + errorCorrectionMatrix[k][j]) % 2;
                }
            }
            for (int p = 0; p < errorCorrectionMatrix[0].length; p++) {
                for (int k = 0; k < errorCorrectionMatrix.length; k++) {
                    if (column[k] != errorCorrectionMatrix[k][p]) {
                        isDuplicate = false;
                    }
                }
                if (isDuplicate) {
                    return false;
                }
            }
        }
        return true;
    }

    /*
        Metoda sprawdzająca wymagania formalne dla tablicy wykorzystywanej do poprawiania jednego błędu
        bitowego. Zwraca ona wartość logiczną true, jeżeli wymagania są przez macierz spełnione.
     */

    public static boolean checkOneErrorMatrixCorrectness(int[][] errorCorrectionMatrix) {

        // Sprawdzanie warunków dla macierzy naprawiającej jeden błąd bitowy:

        int numberOfDifferentBits = 0;
        for (int i = 0; i < errorCorrectionMatrix.length; i++) {
            for (int j = i + 1; j < errorCorrectionMatrix.length; j++) {
                for (int k = 0; k < errorCorrectionMatrix[0].length; k++) {
                    if (errorCorrectionMatrix[i][k] != errorCorrectionMatrix[j][k]) {
                        numberOfDifferentBits++;
                    }
                }
                if (numberOfDifferentBits <= 4) {
                    return false;
                }
            }
        }

        // Sprawdzenie czy w kolumnie znajduje się macierz zerowa

        boolean isZeroColumn;
        for (int i = 0; i < errorCorrectionMatrix[0].length; i++) {
            isZeroColumn = true;
            for (int j = 0; j < errorCorrectionMatrix.length; j++) {
                if (errorCorrectionMatrix[j][i] != 0) {
                    isZeroColumn = false;
                }
            }
            if (isZeroColumn) {
                return false;
            }
        }

        return true;
    }
}
