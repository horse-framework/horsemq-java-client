package com.horsemq;

/**
 * Code for me
 */
public final class HorseResultCode {

    /**
     * Operation succeeded
     */
    public static final short Ok = 0;

    /**
     * Unknown failed response
     */
    public static final short Failed = 1;

    /**
     * Request successful but response has no content
     */
    public static final short NoContent = 204;

    /**
     * Request is not recognized or verified by the server
     */
    public static final short BadRequest = 400;

    /**
     * Access denied for the operation
     */
    public static final short Unauthorized = 401;

    /**
     * PaymentRequired = 402
     */
    public static final short PaymentRequired = 402;

    /**
     * Forbidden = 403
     */
    public static final short Forbidden = 403;

    /**
     * Target could not be found
     */
    public static final short NotFound = 404;

    /**
     * MethodNotAllowed = 405
     */
    public static final short MethodNotAllowed = 405;

    /**
     * Request is not acceptable. Eg, queue status does not support the operation
     */
    public static final short Unacceptable = 406;

    /**
     * RequestTimeout = 408
     */
    public static final short RequestTimeout = 408;

    /**
     * Conflict = 409
     */
    public static final short Conflict = 409;

    /**
     * Gone = 410
     */
    public static final short Gone = 410;

    /**
     * LengthRequired = 411
     */
    public static final short LengthRequired = 411;

    /**
     * PreconditionFailed = 412
     */
    public static final short PreconditionFailed = 412;

    /**
     * RequestEntityTooLarge = 413
     */
    public static final short RequestEntityTooLarge = 413;

    /**
     * RequestUriTooLong = 414
     */
    public static final short RequestUriTooLong = 414;

    /**
     * UnsupportedMediaType = 415
     */
    public static final short UnsupportedMediaType = 415;

    /**
     * RequestedRangeNotSatisfiable = 416
     */
    public static final short RequestedRangeNotSatisfiable = 416;

    /**
     * ExpectationFailed = 417
     */
    public static final short ExpectationFailed = 417;

    /**
     * MisdirectedRequest = 421
     */
    public static final short MisdirectedRequest = 421;

    /**
     * UnprocessableEntity = 422
     */
    public static final short UnprocessableEntity = 422;

    /**
     * Locked = 423
     */
    public static final short Locked = 423;

    /**
     * FailedDependency = 424
     */
    public static final short FailedDependency = 424;

    /**
     * UpgradeRequired = 426
     */
    public static final short UpgradeRequired = 426;

    /**
     * PreconditionRequired = 428
     */
    public static final short PreconditionRequired = 428;

    /**
     * TooManyRequests = 429
     */
    public static final short TooManyRequests = 429;

    /**
     * RequestHeaderFieldsTooLarge = 431
     */
    public static final short RequestHeaderFieldsTooLarge = 431;

    /**
     * UnavailableForLegalReasons = 451
     */
    public static final short UnavailableForLegalReasons = 451;

    /**
     * Requested data is already exists
     */
    public static final short Duplicate = 481;

    /**
     * Client, consumer, queue or message limit is exceeded
     */
    public static final short LimitExceeded = 482;

    /**
     * InternalServerError = 500
     */
    public static final short InternalServerError = 500;

    /**
     * NotImplemented = 501
     */
    public static final short NotImplemented = 501;

    /**
     * BadGateway = 502
     */
    public static final short BadGateway = 502;

    /**
     * Target is busy to complete the process
     */
    public static final short Busy = 503;

    /**
     * GatewayTimeout = 504
     */
    public static final short GatewayTimeout = 504;

    /**
     * HttpVersionNotSupported = 505
     */
    public static final short HttpVersionNotSupported = 505;

    /**
     * VariantAlsoNegotiates = 506
     */
    public static final short VariantAlsoNegotiates = 506;

    /**
     * InsufficientStorage = 507
     */
    public static final short InsufficientStorage = 507;

    /**
     * LoopDetected = 508
     */
    public static final short LoopDetected = 508;

    /**
     * NotExtended = 510
     */
    public static final short NotExtended = 510;

    /**
     * NetworkAuthenticationRequired = 511
     */
    public static final short NetworkAuthenticationRequired = 511;

    /**
     * Message could not be sent to the server
     */
    public static final short SendError = 581;

}
