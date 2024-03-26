package telekomuna;


import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    private String outputFile;

    public FileManager(String outputFile) {
        this.outputFile = outputFile;
    }

    public void save_to_file(int[] array) {
        try {
            FileWriter writer = new FileWriter(outputFile);

            for (int i = 0; i < array.length; i++) {
                writer.write(Integer.toString(array[i]));
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
