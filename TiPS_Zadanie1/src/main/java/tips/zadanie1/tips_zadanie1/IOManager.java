package tips.zadanie1.tips_zadanie1;

import tips.zadanie1.tips_zadanie1.excpetions.IOManagerSaveException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class IOManager {

    private String fileName;


    public IOManager(String fileName) {
        this.fileName = fileName;
    }

    public void saveToFile(String toFile) {
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
        OutputStreamWriter writer = new OutputStreamWriter(fileOut, StandardCharsets.US_ASCII)) {
            writer.write(toFile);
        } catch (IOException exc) {
            throw new IOManagerSaveException("Nie powiodło się wykonanie operacji wyjścia.");
        }
    }

    public void saveBytesToFile(byte[] byteArray) {
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            fileOut.write(byteArray);
        } catch (IOException exc) {
            throw new IOManagerSaveException("Nie powiodło się wykonanie operacji wyjścia.");
        }
    }

    public String readFromFile(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileInputStream fileIn = new FileInputStream(fileName)) {
            stringBuilder.append(new String(fileIn.readAllBytes(), StandardCharsets.US_ASCII));
        } catch (IOException exc) {
            throw new IOManagerSaveException("Nie powiodło się wykonanie operacji wejścia.");
        }
        return stringBuilder.toString();
    }

    public byte[] readBytesFromFile(String fileName) {
        byte[] byteArray;
        try (FileInputStream fileIn = new FileInputStream(fileName)) {
            byteArray = fileIn.readAllBytes();
        } catch (IOException exc) {
            throw new IOManagerSaveException("Nie powiodło się wykonanie operacji wejścia.");
        }
        return byteArray;
    }
}
