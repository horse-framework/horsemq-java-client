package com.horsemq.hmqp;

/**
 * Horse Message Types
 */
public final class MessageType {

    /**
     * Unknown message, may be peer to peer
     */
    public static final byte Other = (byte) 0x00;

    /**
     * Connection close request
     */
    public static final byte Terminate = (byte) 0x08;

    /**
     * Ping message from server
     */
    public static final byte Ping = (byte) 0x09;

    /**
     * Pong message to server
     */
    public static final byte Pong = (byte) 0x0A;

    /**
     * A message to directly server.
     * Server should deal with it directly.
     */
    public static final byte Server = (byte) 0x10;

    /**
     * A message to a queue
     */
    public static final byte QueueMessage = (byte) 0x11;

    /**
     * Direct message, by Id, @type or @name
     */
    public static final byte DirectMessage = (byte) 0x12;

    /**
     * A response message, point to a message received before.
     */
    public static final byte Response = (byte) 0x14;

    /**
     * Used for requesting to pull messages from the queue
     */
    public static final byte QueuePullRequest = (byte) 0x15;

    /**
     * Notifies events if it's from server to client.
     * Subscribes or unsubscribes events if it's from client to server.
     */
    public static final byte Event = (byte) 0x16;

    /**
     * Message is routed to a custom exchange in server
     */
    public static final byte Router = (byte) 0x17;
}
