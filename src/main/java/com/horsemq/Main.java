package com.horsemq;

import com.horsemq.hmqp.KnownContentTypes;

import java.util.concurrent.CompletableFuture;

public class Main {

    public static void main(String[] args) throws Exception {

        HorseClient client = new HorseClient();
        boolean connected = client.connect("hmq://localhost:9999");

        HorseResult result = client.getQueueOperator().subscribe("ModelA", true);

        if (result.getCode() == KnownContentTypes.Ok)
            System.out.println("Subscribed");

        CompletableFuture<String> future = new CompletableFuture<String>();
        String str = future.get();
    }
}
