package com.horsemq.events;

import com.horsemq.HorseSocket;

public interface ConnectionStateListener {

    void stateChanged(HorseSocket socket, boolean isConnected);
}
