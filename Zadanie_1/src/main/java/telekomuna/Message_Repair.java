package telekomuna;

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

    public int[][] add4ParityBits(String binary_text) {
        int rows = binary_text.length() / 8;
        int[][] matrix = new int[rows][12];
        int h = 0;
        int g = 8;
        int index_with_1 = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 8; j++) {
                if (h < binary_text.length())
                    matrix[i][j] = Character.getNumericValue(binary_text.charAt(h));
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
                        System.out.println(i);
                        System.out.println(g);
                    } else {
                        matrix[i][g] = 0;
                    }
                }
                g++;
                index_with_1 = 0;
            }
            g = 8;
        }
        return matrix;
    }
}
