package telekomuna;


import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {
    private String outputFile;

    public FileManager(String outputFile) {
        this.outputFile = outputFile;
    }

    public void save_to_file(byte[] array) {
        try (FileOutputStream fos = new FileOutputStream("fullPathToFile")) {
            fos.write(array);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
