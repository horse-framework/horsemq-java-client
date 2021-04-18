package com.horsemq;

import com.github.javaparser.utils.Pair;
import com.horsemq.events.ConnectionStateListener;
import com.horsemq.events.MessageListener;
import com.horsemq.helpers.StringHelper;
import com.horsemq.hmqp.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

public class HorseSocket {

    private HorseEndpoint _endpoint;

    private final ProtocolWriter _writer = new ProtocolWriter();
    private final ProtocolReader _reader = new ProtocolReader();

    private SocketChannel _channel;

    private MessageListener _messageListener;
    private ConnectionStateListener _connectionListener;
    private List<Pair<String, String>> _properties;

    public void setMessageListener(MessageListener listener) {
        _messageListener = listener;
    }

    public void setConnectionListener(ConnectionStateListener listener) {
        _connectionListener = listener;
    }

    public HorseSocket(HorseEndpoint endpoint, List<Pair<String, String>> properties) {
        _endpoint = endpoint;
        _properties = properties;
    }

    public boolean isConnected() {
        if (_channel == null)
            return false;

        return _channel.isConnected();
    }

    public boolean connect() {
        try {
            _channel = SocketChannel.open();
            _channel.connect(new InetSocketAddress(_endpoint.getAddress(), _endpoint.getPort()));

            while (!_channel.finishConnect()) {
                //connecting..
            }

            boolean connected = isConnected();
            if (connected)
                _connectionListener.stateChanged(this, true);

            handshake();

            new Thread(new Runnable() {
                public void run() {
                    read();
                }
            }).start();

            return connected;
        } catch (Exception e) {
            disconnect();
            return false;
        }
    }

    public void disconnect() {
        //todo

        _connectionListener.stateChanged(this, false);
    }

    public void dispose() {
        //todo
    }

    private void handshake() throws Exception {

        //protocol version
        _channel.write(ByteBuffer.wrap(PredefinedHorseMessages.PROTOCOL_BYTES_V2));

        //hello message
        HorseMessage message = new HorseMessage();
        message.type = MessageType.Server;
        message.contentType = KnownContentTypes.Hello;

        final String crlf = "\r\n";
        StringBuilder builder = new StringBuilder();

        String firstLine = "CONNECT /" + crlf;
        builder.append(firstLine);

        _properties.forEach(p -> {
            String line = p.a + ":" + p.b + crlf;
            builder.append(line);
        });

        message.setStringContent(builder.toString());
        _writer.write(message, _channel, null);

        //read protocol version
        ByteBuffer buffer = readExact(PredefinedHorseMessages.PROTOCOL_BYTES_V2.length);
        byte[] protocolArray = buffer.array();
        for (int i = 0; i < protocolArray.length; i++)
            if (protocolArray[i] != PredefinedHorseMessages.PROTOCOL_BYTES_V2[i]) {
                String serverVersion = new String(protocolArray, StandardCharsets.UTF_8);
                throw new Exception("HMQ Protocol version is not supported: " + serverVersion);
            }
    }

    private void read() {
        try {
            while (isConnected()) {
                HorseMessage message = _reader.read(_channel);
                _messageListener.received(this, message);
            }
        } catch (Exception e) {
            disconnect();
        }
    }

    /**
     * Writes message to network
     */
    public void write(HorseMessage message) throws Exception {
        _writer.write(message, _channel, null);
    }

    /**
     * Writes message to network
     */
    public void write(HorseMessage message, List<Pair<String, String>> additionalHeaders) throws Exception {
        if (!isConnected()) {
            throw new IOException("Connection is closed");
        }

        _writer.write(message, _channel, additionalHeaders);
    }

    /**
     * Reads exactly readCount bytes from socket channel
     */
    private ByteBuffer readExact(int readCount) throws Exception {

        int read = 0;
        ByteBuffer buffer = ByteBuffer.allocate(readCount);

        while (read < readCount)
            read += _channel.read(buffer);

        return buffer;
    }
}
