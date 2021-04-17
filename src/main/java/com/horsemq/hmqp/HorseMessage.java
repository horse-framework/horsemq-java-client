package com.horsemq.hmqp;

import com.github.javaparser.utils.Pair;
import com.horsemq.helpers.StringHelper;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Horse MQ Protocol message object
 */
public class HorseMessage {

    //region FIELDS

    private int _messageIdLength;
    private int _sourceLength;
    private int _targetLength;
    private String _messageId;
    private String _target;
    private String _source;
    private ByteBuffer _content;
    private List<Pair<String, String>> _headers = null;

    /**
     * True means, client is waiting for response (or acknowledge).
     * Sending response is not mandatory but it SHOULD sent.
     */
    public boolean waitResponse;

    /**
     * If true, message should be at first element in the queue
     */
    public boolean highPriority;

    /**
     * Message type.
     * Use known messages in MessageType class.
     */
    public byte type;

    /**
     * Content type code.
     * May be useful to know how content should be read, convert, serialize/deserialize.
     */
    public short contentType;

    //endregion

    //region ID - TARGET - SOURCE

    /**
     * Returns message id value
     */
    public String getMessageId() {
        return _messageId;
    }

    /**
     * Sets message id.
     * Max message id length is 255 in bytes)
     */
    public void setMessageId(String value) throws UnsupportedOperationException {
        int length = StringHelper.getLengthInBytes(value);
        if (length > 255)
            throw new UnsupportedOperationException("Message Id maximum length is 255 (in bytes)");

        _messageId = value;
        _messageIdLength = length;
    }

    /**
     * Returns message id length in bytes
     */
    public int getMessageIdLength() {
        return _messageIdLength;
    }

    /**
     * Message target (queue name, client name or server)
     */
    public String getTarget() {
        return _target;
    }

    /**
     * Sets message target.
     * Max target length is 255 in bytes)
     */
    public void setTarget(String value) throws UnsupportedOperationException {
        int length = StringHelper.getLengthInBytes(value);
        if (length > 255)
            throw new UnsupportedOperationException("Message target maximum length is 255 (in bytes)");

        _target = value;
        _targetLength = length;
    }

    /**
     * Returns message target length in bytes
     */
    public int getTargetLength() {
        return _targetLength;
    }

    /**
     * Message source.
     * Source may be client id or queue name.
     */
    public String getSource() {
        return _target;
    }

    /**
     * Sets message source.
     * Max source length is 255 in bytes)
     */
    public void setSource(String value) throws UnsupportedOperationException {
        int length = StringHelper.getLengthInBytes(value);
        if (length > 255)
            throw new UnsupportedOperationException("Message source maximum length is 255 (in bytes)");

        _source = value;
        _sourceLength = length;
    }

    /**
     * Returns message source length in bytes
     */
    public int getSourceLength() {
        return _sourceLength;
    }

    /**
     * Returns message content length
     */
    public long getMessageLength() {
        return _content != null ? _content.capacity() : 0;
    }

    //endregion

    //region HEADERS

    /**
     * Returns true if message has header data
     */
    public boolean hasHeaders() {
        return _headers != null && _headers.size() > 0;
    }

    /**
     * Returns all message headers
     */
    public Iterable<Pair<String, String>> getHeaders() {
        if (_headers == null)
            return new ArrayList<Pair<String, String>>();

        return _headers;
    }

    /**
     * Finds header value by key.
     * If header doesn't exist, returns null
     */
    public String findHeaderValue(String key) {
        var pair = _headers.stream()
                .filter(p -> p.a.equalsIgnoreCase(key))
                .findFirst();

        if (pair.isEmpty())
            return null;

        return pair.get().b;
    }

    /**
     * Adds a header.
     * If same key already exists, previous header value is overwritten.
     */
    public void setHeader(String key, String value) {
        if (_headers == null)
            _headers = new ArrayList<Pair<String, String>>();

        var pair = _headers.stream()
                .filter(kv -> kv.a.equalsIgnoreCase(key))
                .findFirst();

        pair.ifPresent(stringStringPair -> _headers.remove(stringStringPair));

        _headers.add(new Pair<String, String>(key, value));
    }

    /**
     * Removes a header
     */
    public void removeHeader(String key) {
        if (_headers == null)
            return;

        _headers.removeIf(p -> p.a.equalsIgnoreCase(key));
    }

    /**
     * Clears all headers
     */
    public void clearHeaders() {
        if (_headers != null)
            _headers.clear();
    }

    //endregion

    //region CONTENT

    /**
     * Sets message content
     */
    public void setStringContent(String value) {
        _content = ByteBuffer.wrap(value.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Sets message content
     */
    public void setByteContent(byte[] value) {
        _content = ByteBuffer.wrap(value);
    }

    /**
     * Reads message content as string.
     * If there is no message content, returns null.
     */
    public String getContentAsString() {
        if (_content == null || !_content.hasArray())
            return null;

        byte[] stringBuffer = _content.array();
        return new String(stringBuffer, StandardCharsets.UTF_8);
    }

    public ByteBuffer getContent() {
        return _content;
    }

    //endregion

}
