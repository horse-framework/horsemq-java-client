package com.horsemq.hmqp;

import java.nio.charset.StandardCharsets;

/**
 * Predefined messages for HMQ Protocol
 */
public class PredefinedHorseMessages {

    /**
     * "HMQP/2.1" as bytes, protocol handshaking message
     */
    public static final byte[] PROTOCOL_BYTES_V2 = "HMQP/2.1".getBytes(StandardCharsets.US_ASCII);

    /**
     * PING message for HMQ "0x89, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00"
     */
    public static final byte[] PING = {(byte) 0x89, (byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

    /**
     * PONG message for HMQ "0x8A, 0xFF, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00"
     */
    public static final byte[] PONG = {(byte) 0x8A, (byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

}
