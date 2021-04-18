package com.horsemq;

import java.nio.ByteBuffer;

public class Main {

    public static void main(String[] args) {

        HorseClient client = new HorseClient();
        boolean connected = client.connect("hmq://localhost:9999");

        HorseResult result = client.getQueueOperator().subscribe("jqueue1", true);
        System.out.println("Subscription: " + result.getCode());

        while (true) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
        }
    }
}
