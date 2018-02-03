package com.Sciguy429.ScoutingServer;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.sql.*;

public class BTManagerThread implements Runnable {

    private Connection H2Connection;
    private int connectionNumber = 0;

    BTManagerThread(Connection H2Connection) {
        this.H2Connection = H2Connection;
        try {
            H2Connection.close(); //TODO REMOVE THIS!
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        waitForConnection();
    }

    /**
     * Waiting for connection from devices
     */
    private void waitForConnection() {
        // retrieve the local Bluetooth device object
        LocalDevice local = null;

        StreamConnectionNotifier notifier;
        StreamConnection connection = null;

        // setup the server to listen for connection
        try {
            local = LocalDevice.getLocalDevice();
            local.setDiscoverable(DiscoveryAgent.GIAC);

            UUID uuid = new UUID("05f7185017bd4805a32dd5e22be90d6d", false);
            System.out.println(uuid.toString());

            String url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
            notifier = (StreamConnectionNotifier) Connector.open(url);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // waiting for connection
        while (true) {
            try {
                System.out.println("waiting for connection...");
                connection = notifier.acceptAndOpen();
                System.out.println("After AcceptAndOpen...");
                new Thread(new BTConnectionThread(connectionNumber, H2Connection, connection)).start();
                connectionNumber++;

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
