package com.Sciguy429.ScoutingServer;

import javax.bluetooth.*;
import java.sql.*;

public class Main {

    private static Object lock = new Object();

    public static class MyDiscoveryListener implements DiscoveryListener {

        @Override
        public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) {
            String name;
            Boolean isTrusted;
            try {
                name = btDevice.getFriendlyName(false);
            } catch (Exception e) {
                name = btDevice.getBluetoothAddress();
            }
            isTrusted = btDevice.isTrustedDevice();

            System.out.println("device found: " + name + " Trusted: " + isTrusted);

        }

        @Override
        public void inquiryCompleted(int arg0) {
            synchronized (lock) {
                lock.notify();
            }
        }

        @Override
        public void serviceSearchCompleted(int arg0, int arg1) {
        }

        @Override
        public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
        }

    }

    public static void main(String[] args) {
        // write your code here
        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:~/scouting", "JAVAADMIN", "%Lc3W+z~Y`sQ*?Zk");
            // add application code here
            String sql = "SELECT user_id, user_name FROM users";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("user_id") + "\t" +
                        rs.getString("user_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();
            agent.startInquiry(DiscoveryAgent.GIAC, new MyDiscoveryListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
