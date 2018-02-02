package com.Sciguy429.ScoutingServer;

import java.sql.*;

public class BTConnectionThread implements Runnable {

    Connection H2Connection;

    public BTConnectionThread(Connection H2Connection) {
        this.H2Connection = H2Connection;
    }

    @Override
    public void run() {

    }
}
