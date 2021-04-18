package com.horsemq;

import com.github.javaparser.utils.Pair;
import com.horsemq.events.ConnectionStateListener;
import com.horsemq.events.MessageListener;
import com.horsemq.hmqp.*;
import com.horsemq.operators.DirectOperator;
import com.horsemq.operators.QueueOperator;
import com.horsemq.operators.RouterOperator;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Client class for Horse MQ Server.
 * HorseClient is wrapper for socket connections.
 * Supports reconnect operations etc.
 */
public class HorseClient {

    private String _name;
    private String _type;
    private String _token;
    private String _uniqueId;

    private final QueueOperator _queueOperator;
    private final RouterOperator _routerOperator;
    private final DirectOperator _directOperator;

    private HorseSocket _socket;
    private int _responseTimeoutSeconds = 15;
    private final MessageTracker _tracker = new MessageTracker();

    public String getUniqueId() {
        return _uniqueId;
    }

    public String getName() {
        return _name;
    }

    public String getType() {
        return _type;
    }

    public String getToken() {
        return _token;
    }

    public void setUniqueId(String value) {
        _uniqueId = value;
    }

    public void setName(String value) {
        _name = value;
    }

    public void setType(String value) {
        _type = value;
    }

    public void setToken(String value) {
        _token = value;
    }

    public int getResponseTimeout() {
        return _responseTimeoutSeconds;
    }

    public void setResponseTimeout(int seconds) {
        _responseTimeoutSeconds = seconds;
    }

    /**
     * Operator class for all queue operations such as create, delete, subscribe, unsubscribe, push etc.
     */
    public QueueOperator getQueueOperator() {
        return _queueOperator;
    }

    /**
     * Operator class for all router operations such as create, delete, manage bindings, publish etc.
     */
    public RouterOperator getRouterOperator() {
        return _routerOperator;
    }

    /**
     * Operator class for all direct message operations such as send direct message, response direct message etc.
     */
    public DirectOperator getDirectOperator() {
        return _directOperator;
    }

    public HorseClient() {
        _queueOperator = new QueueOperator(this);
        _routerOperator = new RouterOperator(this);
        _directOperator = new DirectOperator(this);
        _uniqueId = createUniqueId();
    }

    public String createUniqueId() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    public boolean connect(String hostname) {
        disconnect();

        List<Pair<String, String>> properties = new ArrayList<>();
        properties.add(new Pair<>(HorseHeaders.CLIENT_ID, _uniqueId));
        properties.add(new Pair<>(HorseHeaders.CLIENT_NAME, _name));
        properties.add(new Pair<>(HorseHeaders.CLIENT_TYPE, _type));

        if (_token != null && _token.length() > 0)
            properties.add(new Pair<>(HorseHeaders.CLIENT_TOKEN, _token));

        _socket = new HorseSocket(hostname, properties);

        _socket.setMessageListener(new MessageListener() {
            @Override
            public void received(HorseSocket socket, HorseMessage message) {

                //todo: handle received message

                if (message.type == MessageType.QueueMessage) {
                    //a message is consumed from a queue
                    String queueName = message.getTarget();
                } else if (message.type == MessageType.Response) {
                    //response or ack message is received
                    //search tracking message by received message id in MessageTracker
                    CompletableFuture<HorseMessage> future = _tracker.find(message.getMessageId());
                    if (future != null) {
                        _tracker.release(message.getMessageId());
                        future.complete(message);
                    }
                } else if (message.type == MessageType.DirectMessage) {
                    //we received a direct message
                } else if (message.type == MessageType.Server) {
                    //server messages are for server operations
                } else if (message.type == MessageType.Event) {
                    //if we subscribed to an event, that message tells us the event is triggered
                } else if (message.type == MessageType.Ping) {
                    //we need to send PONG message
                } else if (message.type == MessageType.Pong) {
                    //if we sent PING message and waiting for pong, we need to set the value as we received and not still waiting for pong
                }
            }
        });

        _socket.setConnectionListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(HorseSocket socket, boolean isConnected) {
                //todo: actions when connected or disconnected
            }
        });

        return _socket.connect();
    }

    public void disconnect() {
        if (_socket != null && _socket.isConnected()) {
            _socket.disconnect();
            _socket.dispose();
            _socket = null;
        }
    }

    /**
     * Sends a message. If send operation is success returns ok, othervise returns failed
     */
    public HorseResult send(HorseMessage message) {
        try {
            message.setSource(_uniqueId);
            _socket.write(message);
            return HorseResult.ok();
        } catch (Exception e) {
            return HorseResult.failed();
        }
    }

    /**
     * Sends the message and waits a response from the server
     */
    public HorseResult sendAndTrackMessage(HorseMessage message) {
        if (message.getMessageId() == null)
            message.setMessageId(createUniqueId());

        String id = message.getMessageId();

        CompletableFuture<HorseMessage> future = _tracker.track(id);

        try {
            _socket.write(message);
        } catch (Exception e) {
            future.complete(null);
            _tracker.release(id);
            return HorseResult.failed();
        }

        try {
            HorseMessage response = future.get(_responseTimeoutSeconds, TimeUnit.SECONDS);
            return new HorseResult(message.contentType, response);
        } catch (Exception ex) {
            _tracker.release(id);
            return HorseResult.timeout();
        }
    }
}
