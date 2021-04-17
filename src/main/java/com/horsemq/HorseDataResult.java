package com.horsemq;

import com.horsemq.hmqp.HorseMessage;

public class HorseDataResult<TData> extends HorseResult {

    private final TData _data;

    /**
     * Response model
     */
    public TData getModel() {
        return _data;
    }

    public HorseDataResult(TData data, short code) {
        super(code);
        _data = data;
    }

    public HorseDataResult(TData data, HorseMessage message, short code) {
        super(code);

        _data = data;
        _message = message;

        if (code != HorseResultCode.Ok) {
            if (message.getMessageLength() > 0)
                _reason = message.getContentAsString();
        }
    }

    /**
     * Creates new HorseResult with no model
     */
    public HorseDataResult(short code, String reason) {
        super(code);
        _data = null;
        _reason = reason;
    }
}
