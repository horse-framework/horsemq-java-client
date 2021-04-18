package com.horsemq.hmqp;

import com.github.javaparser.utils.Pair;
import com.horsemq.helpers.ByteHelper;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ProtocolReader {

    private final ByteBuffer _frameBuffer = ByteBuffer.allocate(8);

    public HorseMessage read(SocketChannel channel) throws Exception {

        HorseMessage message = new HorseMessage();

        Pair<Boolean, Integer> headerAndLength = readFrameBytes(channel, message);
        if (headerAndLength.a)
            readHeaders(channel, message);

        if (headerAndLength.b > 0)
            readContent(channel, message, headerAndLength.b);

        return message;
    }

    private Pair<Boolean, Integer> readFrameBytes(SocketChannel channel, HorseMessage message) throws Exception {
        boolean hasHeader = false;
        _frameBuffer.clear();
        int read = 0;

        while (read < _frameBuffer.capacity())
            read += channel.read(_frameBuffer);

        byte[] bytes = _frameBuffer.array();

        int proto = ByteHelper.getUnsigned(bytes[0]);
        if (proto >= 128) {
            message.waitResponse = true;
            proto -= 128;
        }

        if (proto >= 64) {
            message.highPriority = true;
            proto -= 64;
        }

        if (proto >= 32) {
            proto -= 32;
            message.type = (byte) proto;

            if (message.type != MessageType.Ping && message.type != MessageType.Pong)
                hasHeader = true;
        } else
            message.type = (byte) proto;

        // bytes[1] is reserved

        //read content type as big ending integer form small ending unsigned short
        ByteBuffer contentTypeBuffer = ByteBuffer.wrap(new byte[]{0, 0, bytes[6], bytes[5]});
        message.contentType = contentTypeBuffer.getInt();

        int length = readContentLength(channel, bytes[7], message);

        int messageIdLength = ByteHelper.getUnsigned(bytes[2]);
        int sourceLength = ByteHelper.getUnsigned(bytes[3]);
        int targetLength = ByteHelper.getUnsigned(bytes[4]);

        if (messageIdLength > 0)
            message.setMessageId(readExactString(channel, messageIdLength));

        if (sourceLength > 0)
            message.setSource(readExactString(channel, sourceLength));

        if (targetLength > 0)
            message.setTarget(readExactString(channel, targetLength));

        return new Pair<>(hasHeader, length);
    }

    private int readContentLength(SocketChannel channel, byte firstByte, HorseMessage message) throws Exception {
        int length = ByteHelper.getUnsigned(firstByte);

        if (length < 253) {
            return length;
        } else if (length == 253) {
            //2 bytes unsigned short
            return readUnsignedShort(channel);
        } else {
            //4 bytes int
            return readInteger(channel);
        }
    }

    private String readExactString(SocketChannel channel, int length) throws Exception {

        ByteBuffer buffer = ByteBuffer.allocate(length);
        int read = 0;

        while (read < length)
            read += channel.read(buffer);

        return new String(buffer.array(), 0, length, StandardCharsets.UTF_8);
    }

    private void readHeaders(SocketChannel channel, HorseMessage message) throws Exception {

        int size = readUnsignedShort(channel);
        if (size == 0)
            throw new Exception("Message is corrupted");

        ByteBuffer buffer = ByteBuffer.allocate(size);
        int read = 0;
        while (read < size)
            read += channel.read(buffer);

        String headersContent = new String(buffer.array(), StandardCharsets.UTF_8);
        String[] headers = headersContent.split("\\r?\\n");

        for (String header : headers) {
            int i = header.indexOf(':');
            if (i < 1)
                continue;

            String key = header.substring(0, i);
            String value = header.substring(i + 1);
            message.setHeader(key, value);
        }
    }

    private void readContent(SocketChannel channel, HorseMessage message, int length) throws Exception {

        ByteBuffer buffer = ByteBuffer.allocate(length);
        int read = 0;
        while (read < length)
            read += channel.read(buffer);

        message.setContent(buffer);
    }

    private int readUnsignedShort(SocketChannel channel) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        channel.read(buffer);
        byte[] shortArray = buffer.array();
        ByteBuffer shortBuffer = ByteBuffer.wrap(new byte[]{0, 0, shortArray[1], shortArray[0]});
        return shortBuffer.getInt();
    }

    private int readInteger(SocketChannel channel) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        channel.read(buffer);
        byte[] intArray = buffer.array();
        ByteBuffer intBuffer = ByteBuffer.wrap(new byte[]{intArray[3], intArray[2], intArray[1], intArray[0]});
        return intBuffer.getInt();
    }
}
