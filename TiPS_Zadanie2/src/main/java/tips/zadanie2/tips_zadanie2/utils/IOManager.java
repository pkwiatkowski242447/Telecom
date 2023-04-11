package tips.zadanie2.tips_zadanie2.utils;

import tips.zadanie2.tips_zadanie2.exceptions.IOManagerReadException;
import tips.zadanie2.tips_zadanie2.exceptions.IOManagerWriteException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOManager {

    /*
        @ Method: readBytesFromFile()

        @ Parameters:

        * fileName -> Name of the file, which data should be read from.

        @ Description: This method is used to read data from a file with given fileName.
        Read data is stored as bytes in inputMessageInByteRepresentation.
     */

    public static byte[] readBytesFromFile(String fileName) throws IOManagerReadException {
        byte[] inputMessageInByteRepresentation = null;
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            inputMessageInByteRepresentation = fileInputStream.readAllBytes();
        } catch (IOException ioException) {
            throw new IOManagerReadException("Nie powiodło się odczytanie zawartości pliku: " + fileName);
        }
        return inputMessageInByteRepresentation;
    }

    /*
        @ Method: writeBytesToFile()

        @ Parameters:

        * fileName                  -> Name of the file, which data should be written to.
        * bytesToBeWrittenToFile    -> Array of bytes to be written to a file with given fileName.

        @ Description: This method is used for writing data to a file with given, as a parameter, name.
     */

    public static void writeBytesToFile(String fileName, byte[] bytesToBeWrittenToFile) throws IOManagerWriteException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
             fileOutputStream.write(bytesToBeWrittenToFile);
        } catch (IOException ioException) {
            throw new IOManagerWriteException("Nie powiodło się zapisanie danych do pliku: " + fileName);
        }
    }
}
