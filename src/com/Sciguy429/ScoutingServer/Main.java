package com.Sciguy429.ScoutingServer;

import java.sql.*;

public class Main {

    public static void main(String[] args) {
        // write your code here
        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:~/scouting", "JAVAADMIN", "%Lc3W+z~Y`sQ*?Zk"); //Connect To The H2 DB With JAVAADMIN
            isDatabaseValid(conn);
            new Thread(new BTManagerThread(conn)).start();
            while(true) {}
            //conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static boolean isDatabaseValid(Connection conn) {
        try {
            conn.getClientInfo();
        } catch (SQLException e) {
            e.printStackTrace();
            return false; //Sql issue so database is definitely broke
        }
        return true;
    }
}
