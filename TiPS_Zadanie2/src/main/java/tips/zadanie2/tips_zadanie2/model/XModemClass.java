package tips.zadanie2.tips_zadanie2.model;

import java.util.List;

public class XModemClass {

    private byte[] packetToSend;
    private final boolean withAddedCRC;
    private int packetSize;

    public XModemClass(boolean withAddedCRC) {
        this.withAddedCRC = withAddedCRC;
        if (!withAddedCRC) {
            packetSize = XModemDefinitions.PACKET_SIZE_NO_CRC;
        } else {
            packetSize = XModemDefinitions.PACKET_SIZE_CRC;
        }
    }

    public byte[][] transformToArrayOfPackets(byte[] messageToTransmit) {
        byte[][] arrayOfDataPackets;
        byte[] dataPacket = new byte[packetSize];
        int numberOfPackets = messageToTransmit.length / 128;
        int requiredLength = 0;
        // Correcting the number of required packets.
        if (numberOfPackets == 0 && messageToTransmit.length != 0) {
            numberOfPackets = 1;
        } else if ((messageToTransmit.length & 0x7F) != 0) {
            numberOfPackets++;
        }
        // Checking whether number of packets if between 1 and 255.
        // We are checking if numberOfPackets is greater than 254 because we are adding one, since
        // numeration starts from 1.
        if (numberOfPackets > 254) {
            numberOfPackets = 254;
        }
        arrayOfDataPackets = new byte[numberOfPackets][packetSize];
        byte numberOfAppendedZeros = 0;
        for (int i = 0; i < numberOfPackets; i++) {
            if (withAddedCRC) {
                dataPacket[0] = XModemDefinitions.C;
            } else {
                dataPacket[0] = XModemDefinitions.SOH;
            }
            dataPacket[1] = (byte) (i + 1);
            dataPacket[2] = (byte) (255 - (i + 1));
            for (int j = 0; j < XModemDefinitions.SIZE_OF_DATA; j++) {
                if (i * XModemDefinitions.SIZE_OF_DATA + j < messageToTransmit.length) {
                    dataPacket[j + 3] = messageToTransmit[i * XModemDefinitions.SIZE_OF_DATA + j];
                } else {
                    dataPacket[j + 3] = 0;
                    numberOfAppendedZeros++;
                }
            }
            dataPacket[131] = numberOfAppendedZeros;
            short checksumValue = 0;
            if (withAddedCRC) {
                checksumValue = CalculateChecksum.calculateCyclicRedundancyCheck16Bit(dataPacket);
                dataPacket[dataPacket.length - 1] = (byte) (checksumValue & 0xFF);
                dataPacket[dataPacket.length - 2] = (byte) (checksumValue >> 8);
            } else {
                checksumValue = CalculateChecksum.calculateAlgebraicSumMod256(dataPacket);
                dataPacket[dataPacket.length - 1] = (byte) (checksumValue & 0xFF);
            }
            System.arraycopy(dataPacket, 0, arrayOfDataPackets[i], 0, dataPacket.length);
        }

        for (int i = 0; i < numberOfPackets; i++) {
            for (int j = 0; j < packetSize; j++) {
                System.out.print(arrayOfDataPackets[i][j] + " ");
            }
            System.out.println(" ");
        }

        return arrayOfDataPackets;
    }

    public byte[] transformBackToArrayOfBytes(List<byte[]> originalArrayOfPackets) {
        int iterator = 0;
        byte numberOfAddedZeros = 0;
        byte[] processedMessageInBytes = new byte[XModemDefinitions.SIZE_OF_DATA * originalArrayOfPackets.size()];
        for (int i = 0; i < originalArrayOfPackets.size(); i++) {
            System.arraycopy(originalArrayOfPackets.get(i), 3, processedMessageInBytes, iterator, XModemDefinitions.SIZE_OF_DATA);
            iterator += XModemDefinitions.SIZE_OF_DATA;
            numberOfAddedZeros = originalArrayOfPackets.get(i)[131];
        }
        byte[] originalMessageInBytes = new byte[processedMessageInBytes.length - numberOfAddedZeros];
        System.arraycopy(processedMessageInBytes, 0, originalMessageInBytes, 0, originalMessageInBytes.length);
        return originalMessageInBytes;
    }

    public boolean checkIfCheckSumIsCorrect(byte[] someDataPacket) {
        short calculatedCheckSumValue = 0;
        short checkSumValueFromPacket = 0;
        if (this.withAddedCRC) {
            calculatedCheckSumValue = CalculateChecksum.calculateCyclicRedundancyCheck16Bit(someDataPacket);
            checkSumValueFromPacket = (short) ((someDataPacket[someDataPacket.length - 2] << 8) | (someDataPacket[someDataPacket.length - 1] & 0xFF));
            System.out.println("Top gets executed.");
        } else {
            calculatedCheckSumValue = CalculateChecksum.calculateAlgebraicSumMod256(someDataPacket);
            checkSumValueFromPacket |= someDataPacket[someDataPacket.length - 1];
            System.out.println("Bottom gets executed.");
        }
        System.out.println("Checksum in packet: " + checkSumValueFromPacket);
        System.out.println("Calculated checksum: " + calculatedCheckSumValue);
        if (checkSumValueFromPacket == calculatedCheckSumValue) {
            return true;
        } else {
            return false;
        }
    }
}
