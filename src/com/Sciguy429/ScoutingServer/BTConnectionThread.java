package com.Sciguy429.ScoutingServer;

import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        InputStream BTIS;
        OutputStream BTOS;
        try {
            BTIS = BTConnection.openInputStream();
            BTOS = BTConnection.openOutputStream();
            while (true) {
                if (BTIS.available() > 0)
                System.out.println(BTIS.read());
                Thread.sleep(10);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
