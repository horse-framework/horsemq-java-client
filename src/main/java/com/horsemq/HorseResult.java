package com.horsemq;

import com.horsemq.hmqp.HorseHeaders;
import com.horsemq.hmqp.HorseMessage;

public class HorseResult {

    private final short _code;
    protected String _reason;
    protected HorseMessage _message;

    /**
     * Result code
     */
    public short getCode() {
        return _code;
    }

    /**
     * Reason for unsuccessful results
     */
    public String getReason() {
        return _reason;
    }

    /**
     * Raw response message
     */
    public HorseMessage getMessage() {
        return _message;
    }

    /**
     * Creates new result without reason
     */
    public HorseResult(short code) {
        _code = code;
        _reason = null;
    }

    /**
     * Creates new result with a reason
     */
    public HorseResult(short code, String reason) {
        _code = code;
        _reason = reason;
    }

    /**
     * Creates new result with a reason
     */
    public HorseResult(short code, HorseMessage message) {
        _code = code;
        _message = message;

        if (message != null && message.hasHeaders()) {
            _reason = message.findHeaderValue(HorseHeaders.NEGATIVE_ACKNOWLEDGE_REASON);
        }
    }

    /**
     * Creates successful result with no reason
     */
    public static HorseResult ok() {
        return new HorseResult(HorseResultCode.Ok);
    }

    /**
     * Creates failed result with no reason
     */
    public static HorseResult failed() {
        return new HorseResult(HorseResultCode.Failed);
    }

    /**
     * Creates timeout failed result
     */
    public static HorseResult timeout() {
        return new HorseResult(HorseResultCode.RequestTimeout);
    }

    /**
     * Creates failed result with reason
     */
    public static HorseResult failed(String reason) {
        return new HorseResult(HorseResultCode.Failed, reason);
    }

}
