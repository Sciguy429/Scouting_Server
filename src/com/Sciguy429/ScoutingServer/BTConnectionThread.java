package com.Sciguy429.ScoutingServer;

import javax.microedition.io.StreamConnection;
import java.sql.*;

public class BTConnectionThread implements Runnable {

    Connection H2Connection;
    StreamConnection BTConnection;

    BTConnectionThread(Connection H2Connection, StreamConnection BTConnection) {
        this.H2Connection = H2Connection;
        this.BTConnection = BTConnection;
    }

    @Override
    public void run() {

    }
}
