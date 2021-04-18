package com.horsemq;

/**
 * Endpoint for Horse Protocol
 */
public class HorseEndpoint {

    private final String _address;
    private final int _port;
    private final boolean _isSecure;

    public String getAddress() {
        return _address;
    }

    public int getPort() {
        return _port;
    }

    public boolean isSecure() {
        return _isSecure;
    }

    public HorseEndpoint(String address, int port, boolean secure) {
        _address = address;
        _port = port;
        _isSecure = secure;
    }

}
