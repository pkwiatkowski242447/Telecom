package tips.zadanie2.tips_zadanie2.model;

public class XModemDefinitions {

    /*
        @ Attributes: Attributes below are defined in XModem documentation, and
        therefore are used in this implementation of XModem.

        * SOH -> Used as first byte of the packet - with algebraic checksum packet.
        * EOT -> Used for ending communication from sender side.
        * ACK -> Used for indicating to sender that sent packet is correct.
        * NAK -> Used for indicating to sender that sent packet is somehow incorrect.
        * C -> Used as first byte of the packet - with CRC checksum.
        * CAN -> Symbol used in place of ACK or NAK in some implementations.
     */

    public final static byte SOH = 0x01;
    public final static byte EOT = 0x04;
    public final static byte ACK = 0x06;
    public final static byte NAK = 0x15;
    public final static byte CAN = 0x18;
    public final static byte C = 0x43;

    /*
        @ Attributes:

        * PACKET_SIZE_NO_CRC        -> size of the packet with algebraic checksum - 1 x SOH / 1 x packetNumber /
        1 x (255 - packetNumber) / 128 x byte of data / 1 x byte of checksum
        * PACKET_SIZE_CRC           -> size of the packet with algebraic checksum - 1 x C / 1 x packetNumber /
        1 x (255 - packetNumber) / 128 x byte of data / 2 x byte of checksum
        * TOTAL_NUMBER_OF_BLOCKS    -> total number of blocks that can be sent through single connection
        * SIZE_OF_DATA              -> number of data in single packet.

     */

    public static final int PACKET_SIZE_NO_CRC = 133;
    public static final int PACKET_SIZE_CRC = 134;
    public static final int TOTAL_NUMBER_OF_BLOCKS = 255;
    public static final int SIZE_OF_DATA = 128;
}
