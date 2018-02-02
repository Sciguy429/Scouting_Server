package com.Sciguy429.ScoutingServer;

import javax.bluetooth.*;
import java.sql.*;

public class Main {

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
                System.out.println(rs.getInt("user_id") + "\t" + rs.getString("user_name"));
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}