package com.horsemq.operators;

import com.github.javaparser.utils.Pair;
import com.horsemq.HorseClient;
import com.horsemq.HorseResult;
import com.horsemq.hmqp.HorseMessage;
import com.horsemq.hmqp.KnownContentTypes;
import com.horsemq.hmqp.MessageType;

/**
 * Queue manager object for hmq client
 */
public class QueueOperator {

    private final HorseClient _client;

    public QueueOperator(HorseClient client) {
        _client = client;
    }

    /**
     * Subscribes to a queue
     *
     * @param queue          Name of the queue
     * @param verifyResponse If true, ack message will be requested from the server
     */
    public HorseResult subscribe(String queue, boolean verifyResponse) {
        HorseMessage message = new HorseMessage();
        message.type = MessageType.Server;
        message.contentType = KnownContentTypes.Subscribe;
        message.waitResponse = verifyResponse;
        message.setMessageId(_client.createUniqueId());
        message.setTarget(queue);

        if (verifyResponse)
            return _client.sendAndTrackMessage(message);
        else
            return _client.send(message);
    }

    /**
     * Unsubscribes from a queue
     *
     * @param queue          Name of the queue
     * @param verifyResponse If true, ack message will be requested from the server
     */
    public HorseResult unsubscribe(String queue, boolean verifyResponse) {
        HorseMessage message = new HorseMessage();
        message.type = MessageType.Server;
        message.contentType = KnownContentTypes.Unsubscribe;
        message.waitResponse = verifyResponse;
        message.setMessageId(_client.createUniqueId());
        message.setTarget(queue);

        if (verifyResponse)
            return _client.sendAndTrackMessage(message);
        else
            return _client.send(message);
    }

    /**
     * Pushes a string message into a queue
     *
     * @param queue        Name of the queue
     * @param content      Message content
     * @param waitResponse If true, ack message will be requested from the server
     */
    public HorseResult push(String queue, String content, boolean waitResponse) {
        return push(queue, content, waitResponse, null);
    }

    /**
     * Pushes a string message into a queue
     *
     * @param queue             Name of the queue
     * @param content           Message content
     * @param waitResponse      If true, ack message will be requested from the server
     * @param additionalHeaders Additional headers of the message
     */
    public HorseResult push(String queue, String content, boolean waitResponse, Iterable<Pair<String, String>> additionalHeaders) {
        HorseMessage message = new HorseMessage();
        message.type = MessageType.QueueMessage;
        message.waitResponse = waitResponse;

        message.setMessageId(_client.createUniqueId());
        message.setSource(_client.getUniqueId());
        message.setTarget(queue);
        message.setStringContent(content);

        if (additionalHeaders != null)
            additionalHeaders.forEach(pair -> message.setHeader(pair.a, pair.b));

        if (waitResponse)
            return _client.sendAndTrackMessage(message);
        else
            return _client.send(message);
    }
}
