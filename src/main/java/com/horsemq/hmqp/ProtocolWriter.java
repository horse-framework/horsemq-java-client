package com.horsemq.hmqp;

import com.github.javaparser.utils.Pair;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ProtocolWriter {

    /**
     * Creates new byte buffer from Horse Message object.
     * ByteBuffer includes HMQ protocol frame data and can be written to Horse MQ Protocol network socket.
     */
    public void write(HorseMessage message, SocketChannel channel, List<Pair<String, String>> additionalHeaders) throws Exception {

        boolean hasHeaders = message.hasHeaders() || (additionalHeaders != null && additionalHeaders.size() > 0);

        writeFrame(message, channel, hasHeaders);

        if (hasHeaders)
            writeHeaders(message, channel, additionalHeaders);

        if (message.getMessageLength() > 0)
            writeContent(message, channel);
    }

    /**
     * Writes protocol frame definition data
     */
    private void writeFrame(HorseMessage message, SocketChannel channel, boolean hasHeaders) throws Exception {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        int proto = message.type;

        if (message.waitResponse)
            proto += 128;
        if (message.highPriority)
            proto += 64;
        if (hasHeaders)
            proto += 32;

        byte reserved = 0;
        byte[] frame = {(byte)proto, reserved, (byte) message.getMessageIdLength(), (byte) message.getSourceLength(), (byte) message.getTargetLength()};
        stream.write(frame);

        byte[] contentType = ByteBuffer.allocate(4).putInt(message.contentType).array();
        stream.write(new byte[]{contentType[3], contentType[2]}); //we need small endian

        long length = message.getMessageLength();
        if (length > 0)
            writeMessageLength(stream, length);

        if (message.getMessageIdLength() > 0) {
            String id = message.getMessageId();
            stream.write(id.getBytes(StandardCharsets.UTF_8));
        }

        if (message.getSourceLength() > 0) {
            String source = message.getSource();
            stream.write(source.getBytes(StandardCharsets.UTF_8));
        }

        if (message.getTargetLength() > 0) {
            String target = message.getTarget();
            stream.write(target.getBytes(StandardCharsets.UTF_8));
        }

        channel.write(ByteBuffer.wrap(stream.toByteArray()));
    }

    /**
     * Writes message content length
     */
    private void writeMessageLength(ByteArrayOutputStream stream, long messageLength) {

        if (messageLength < 253) {
            stream.write((byte) messageLength);
        } else if (messageLength <= 65535) {
            stream.write(253);

            byte[] intArray = ByteBuffer.allocate(4).putInt((int) messageLength).array();
            stream.write(new byte[]{intArray[3], intArray[2]}, 0, 2); //to small endian unsigned short
        } else {
            stream.write(254);
            byte[] intArray = ByteBuffer.allocate(4).putInt((int) messageLength).array();
            stream.write(new byte[]{intArray[3], intArray[2], intArray[1], intArray[0]}, 0, 4); //small endian
        }

        //for messages larger than 4,294,967,295, write 255 then 8 bytes
    }

    /**
     * Writes message headers
     */
    private void writeHeaders(HorseMessage message, SocketChannel
            channel, List<Pair<String, String>> additionalHeaders) throws Exception {

    }

    /**
     * Writes message content
     */
    private void writeContent(HorseMessage message, SocketChannel channel) throws Exception {

        ByteBuffer content = message.getContent();

        if (content != null)
            channel.write(content);
    }

}
