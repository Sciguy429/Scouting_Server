package com.Sciguy429.ScoutingServer;

import javax.microedition.io.StreamConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;

public class BTConnectionThread implements Runnable {

    private int connectionNumber;
    private String connectionLogTag;
    private Connection H2Connection;
    private StreamConnection BTConnection;
    private InputStream BTIS;
    private OutputStream BTOS;

    BTConnectionThread(int connectionNumber, Connection H2Connection, StreamConnection BTConnection) {
        this.connectionNumber = connectionNumber;
        this.connectionLogTag = "Connection " + connectionNumber + ": ";
        this.H2Connection = H2Connection;
        this.BTConnection = BTConnection;
    }

    @Override
    public void run() {
        System.out.println(connectionLogTag + "Thread Start");
        ArrayList<byte[]> command = new ArrayList<>();
        try {
            BTIS = BTConnection.openInputStream();
            BTOS = BTConnection.openOutputStream();
            while (true) {
                if (BTIS.available() > 0) {
                    command.clear();
                    while(BTIS.available() > 0) {
                        command.add(toBytes(BTIS.read()));
                    }
                    System.out.print(connectionLogTag + "Command Received: ");
                    for (byte[] b : command) {
                        System.out.print(new String(b, "UTF-8"));
                    }
                    System.out.print("\r\n");
                }
                Thread.sleep(10);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private byte[] toBytes(int i)
    {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);

        return result;
    }
}
