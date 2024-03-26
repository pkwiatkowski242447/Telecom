package tips.zadanie2.tips_zadanie2.model;

public class CalculateChecksum {
    private static final int CRC16_POLYNOMIAL = 0x1021;

    public static short calculateCyclicRedundancyCheck16Bit(byte[] basePacket) {
        short calculatedCRC = 0;
        System.out.println("Checksum for CRC.");
        // Loop for calculating value of 16-bit CRC.
        for (int i = 0; i < XModemDefinitions.PACKET_SIZE_CRC - 2; i++) {
            calculatedCRC ^= (basePacket[i] << 8);
            for (int j = 0; j < 8; j++) {
                if ((calculatedCRC & 0x8000) != 0) {
                    calculatedCRC = (short) ((calculatedCRC << 1) ^ CRC16_POLYNOMIAL);
                } else {
                    calculatedCRC <<= 1;
                }
            }
        }
        return calculatedCRC;
    }

    public static byte calculateAlgebraicSumMod256(byte[] basePacket) {
        int sumOfBytes = 0;
        System.out.println("Checksum algebraic.");
        for (int i = 0; i < basePacket.length - 1; i++) {
            sumOfBytes += basePacket[i];
        }
        return (byte) (sumOfBytes & 0xFF);
    }

}
