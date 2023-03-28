package tips.zadanie1.model;

import tips.zadanie1.exceptions.IOManagerReadException;
import tips.zadanie1.exceptions.IOManagerWriteException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOManager {

    private String fileName;


    public IOManager(String fileName) {
        this.fileName = fileName;
    }

    /*
        Metoda wykorzysytwana do zapisu tablicy bajtów do pliku.

        @ byteArray -> tablica bajtów, która ma zostać zapisana do pliku

        @IOManagerWriteException -> wyjątek, który może pojawić się w ramach wykonywania operacji zapisu, świadczący
        o niepowodzeniu operacji
     */

    public void saveBytesToFile(byte[] byteArray) throws IOManagerWriteException {
        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
            fileOut.write(byteArray);
        } catch (IOException exc) {
            throw new IOManagerWriteException("Nie powiodło się wykonanie operacji wyjścia.", exc);
        }
    }

    /*
        Metoda wykorzysytwana do odczytu tablicy bajtów z pliku.

        @ fileName -> nazwa pliku, z którego tablica bajtów ma zostać odczytana

        @IOManagerReadException -> wyjątek, który może pojawić się w ramach wykonywania operacji zapisu, świadczący
        o niepowodzeniu operacji
     */

    public byte[] readBytesFromFile(String fileName) throws IOManagerReadException {
        byte[] byteArray;
        try (FileInputStream fileIn = new FileInputStream(fileName)) {
            byteArray = fileIn.readAllBytes();
        } catch (IOException exc) {
            throw new IOManagerReadException("Nie powiodło się wykonanie operacji wejścia.", exc);
        }
        return byteArray;
    }
}
