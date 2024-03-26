package telekomuna;

import java.nio.ByteBuffer;
import java.util.BitSet;

public class Message_Repair {

    public static int[][] oneErrorMatrix = {
            {0, 0, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0},
            {0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0},
            {1, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0},
            {1, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 1}
    };

    public static int[][] twoErrorMatrix = {
            {0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0},
            {1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0, 0},
            {1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0},
            {1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0},
            {1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0},
            {1, 1, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0},
            {1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1}
    };

    private int h = 0;

    private int k = 0;
    private int g = 8;
    private int index_with_1 = 0;

    public String convert_string_to_binary(byte[] text) {
        StringBuilder in_binary = new StringBuilder();
        for (byte b : text) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                in_binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return in_binary.toString();
    }

    public String convert_binary_to_string(String binary_text) {
        StringBuilder string_text = new StringBuilder();
        int charCode;
        for (int i = 0; i < binary_text.length(); i = i + 8) {
            charCode = Integer.parseInt(binary_text.substring(i, i + 8), 2);
            String returnChar = Character.toString((char) charCode);
            string_text.append(returnChar);
        }
        return string_text.toString();
    }

    public byte[] add4ParityBits(String binary_text) {
        int rows = binary_text.length() / 8;
        BitSet bs = new BitSet();
        byte[][] matrix = new byte[rows][12];
        int matrix_to_file_size = rows * 12;
        byte[] matrix_to_file = new byte[matrix_to_file_size];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 8; j++) {
                if (h < binary_text.length())
                    matrix[i][j] = (byte) Character.getNumericValue(binary_text.charAt(h));
                h++;
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int l = 0; l < 4; l++) {
                for (int j = 0; j < 8; j++) {
                    if (oneErrorMatrix[l][j] == 1 && matrix[i][j] == 1) {
                        index_with_1 += 1;
                    }
                }
                if (g < 12) {
                    if (index_with_1 % 2 != 0) {
                        matrix[i][g] = 1;
                    } else {
                        matrix[i][g] = 0;
                    }
                }
                g++;
                index_with_1 = 0;
            }
            g = 8;
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 12; j++) {
                if (k < matrix_to_file_size) {
                    matrix_to_file[k] = matrix[i][j];
                    k++;
                }
            }
        }
        return matrix_to_file;
    }

    public int[] add8ParityBits(String binary_text) {
        int rows = binary_text.length() / 8;
        int matrix_to_file_size = rows * 16;
        int[][] matrix = new int[rows][16];
        int[] matrix_helper = new int[matrix_to_file_size];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 8; j++) {
                if (h < binary_text.length())
                    matrix[i][j] = Character.getNumericValue(binary_text.charAt(h));
                h++;
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int l = 0; l < 8; l++) {
                for (int j = 0; j < 8; j++) {
                    if (twoErrorMatrix[l][j] == 1 && matrix[i][j] == 1) {
                        index_with_1 += 1;
                    }
                }
                if (g < 16) {
                    if (index_with_1 % 2 != 0) {
                        matrix[i][g] = 1;
                    } else {
                        matrix[i][g] = 0;
                    }
                }
                g++;
                index_with_1 = 0;
            }
            g = 8;
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 16; j++) {
                if (k < matrix_to_file_size) {
                    matrix_helper[k] = matrix[i][j];
                    k++;
                }
            }
        }
        return matrix_helper;
    }
}
