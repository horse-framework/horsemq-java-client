package com.horsemq.events;

import com.horsemq.HorseSocket;
import com.horsemq.hmqp.HorseMessage;

public interface MessageListener {

    void received(HorseSocket socket, HorseMessage message);
}
