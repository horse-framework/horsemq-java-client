package com.horsemq.hmqp;

/**
 * Known content types for MQ Server with HMQ protocol
 */
public class KnownContentTypes {

    /**
     * "500" Process failed
     */
    public static final short Failed = 1;

    /**
     * "101" After procotol handshake completed, first message is the hello message
     */
    public static final short Hello = 101;

    /**
     * "202" Message is accepted
     */
    public static final short Accepted = 202;

    /**
     * "204" No content
     */
    public static final short NoContent = 204;

    /**
     * "400" Message has invalid content
     */
    public static final short BadRequest = 400;

    /**
     * "401" Permission denied for client
     */
    public static final short Unauthorized = 401;

    /**
     * "404" Requested data not found
     */
    public static final short NotFound = 404;

    /**
     * "406" Message is unacceptable
     */
    public static final short Unacceptable = 406;

    /**
     * "481" Duplicate record, such as, you might send create queue operation when client is already created
     */
    public static final short Duplicate = 481;

    /**
     * "482" Limit exceeded, such as, maximum queue limit of the server
     */
    public static final short LimitExceeded = 482;

    /**
     * "503" Server is too busy to handle the message
     */
    public static final short Busy = 503;

    /**
     * "601" Subscribe to a queue
     */
    public static final short Subscribe = 601;

    /**
     * "602" Unsubscribe from a queue
     */
    public static final short Unsubscribe = 602;

    /**
     * "607" Gets all consumers of a queue
     */
    public static final short QueueConsumers = 607;

    /**
     * "610" Creates new queue
     */
    public static final short CreateQueue = 610;

    /**
     * "611" Deletes the queue with it's messages
     */
    public static final short RemoveQueue = 611;

    /**
     * "612" Changes queue properties and/or status
     */
    public static final short UpdateQueue = 612;

    /**
     * "613" Clears messages in queue
     */
    public static final short ClearMessages = 613;

    /**
     * "616" Gets queue information list
     */
    public static final short QueueList = 616;

    /**
     * "621" Gets active instance list
     */
    public static final short InstanceList = 621;

    /**
     * "631" Gets all connected clients
     */
    public static final short ClientList = 631;

    /**
     * "641" Node instance sends a decision to other nodes
     */
    public static final short DecisionOverNode = 641;

    /**
     * "651" Gets all routers
     */
    public static final short ListRouters = 651;

    /**
     * "652" Creates new router
     */
    public static final short CreateRouter = 652;

    /**
     * "653" Removes a router with it's bindings
     */
    public static final short RemoveRouter = 653;

    /**
     * "661" List all bindings of a router
     */
    public static final short ListBindings = 661;

    /**
     * "662" Creates new binding in a router
     */
    public static final short AddBinding = 662;

    /**
     * "663" Removes a binding from a router
     */
    public static final short RemoveBinding = 663;
}
