package com.horsemq;

import com.horsemq.hmqp.HorseMessage;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This object for tracking sent messages.
 * When a message is sent with it's unique id and client waits for acknowledge for the message,
 * Sent message should be tracked by MessageTracker.
 * When a message is received from server with MessageType.Response.
 * We need to find received message's id value in currently tracking messages.
 * If we can find a tracking message with same message id,
 * The received message is a response (or acknowledge) message for the tracking message.
 */
public class MessageTracker {

    private final Lock _lock = new ReentrantLock();
    private final Dictionary<String, CompletableFuture<HorseMessage>> _trackingMessages = new Hashtable<>();


    public CompletableFuture<HorseMessage> track(String messageId) {
        try {
            _lock.lock();
            CompletableFuture<HorseMessage> future = new CompletableFuture<>();
            _trackingMessages.put(messageId, future);
            return future;
        } finally {
            _lock.unlock();
        }
    }

    public CompletableFuture<HorseMessage> find(String messageId) {
        if (messageId == null)
            return null;

        _lock.lock();
        CompletableFuture<HorseMessage> future = _trackingMessages.get(messageId);
        _lock.unlock();

        return future;
    }

    public void release(String messageId) {
        if (messageId == null)
            return;

        _lock.lock();
        _trackingMessages.remove(messageId);
        _lock.unlock();
    }
}
