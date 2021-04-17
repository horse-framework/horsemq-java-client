package com.horsemq.helpers;

public class ByteHelper {

    public static int getUnsigned(byte b) {
        return b & 0xFF;
    }
}
