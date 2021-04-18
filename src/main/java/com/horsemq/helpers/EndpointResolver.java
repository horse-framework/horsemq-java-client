package com.horsemq.helpers;

import com.horsemq.HorseEndpoint;

import java.net.UnknownHostException;

public class EndpointResolver {

    /**
     * Resolves hostname and returns Horse Endpoint
     */
    public static HorseEndpoint resolve(String hostname) throws UnknownHostException {

        int i1 = hostname.indexOf("://");
        if (i1 < 0) {
            throw new UnknownHostException("Invalid hostname");
        }

        int i2 = hostname.indexOf(":", i1 + 2);

        String protocol = hostname.substring(0, i1).toLowerCase();
        if (!protocol.startsWith("hmq"))
            throw new UnknownHostException("Unsupported protocol. Supported protocols are HMQ and HMQS");

        boolean secure = protocol.equals("hmqs");
        int port;
        String address;

        if (i2 > 0) {
            address = hostname.substring(i1 + 3, i2);
            port =  Integer.parseInt(hostname.substring(i2 + 1));
        } else {
            port = 26222;
            address = hostname.substring(i1 + 3);
        }

        return new HorseEndpoint(address, port, secure);
    }
}
