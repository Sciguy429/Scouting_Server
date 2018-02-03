package com.Sciguy429.ScoutingServer;

import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;

public class BTConnectionThread implements Runnable {

    private int connectionNumber;
    private Connection H2Connection;
    private StreamConnection BTConnection;
    private InputStream BTIS;
    private OutputStream BTOS;

    BTConnectionThread(int connectionNumber, Connection H2Connection, StreamConnection BTConnection) {
        this.connectionNumber = connectionNumber;
        this.H2Connection = H2Connection;
        this.BTConnection = BTConnection;
    }

    @Override
    public void run() {
        System.out.println("Connection " + connectionNumber + ": Thread Start");
        try {
            BTIS = BTConnection.openInputStream();
            BTOS = BTConnection.openOutputStream();
            while (true) {
                if (BTIS.available() > 0) {
                    System.out.println(BTIS.read());
                }
                Thread.sleep(10);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
