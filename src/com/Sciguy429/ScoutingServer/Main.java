package com.Sciguy429.ScoutingServer;

import java.sql.*;

public class Main {

    public static void main(String[] args) {
        // write your code here
        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:~/scouting", "JAVAADMIN", "%Lc3W+z~Y`sQ*?Zk"); //Connect To The H2 DB With JAVAADMIN
            if (!isDatabaseValid(conn)) {
                System.out.println("Database Failure, Exiting");
                System.exit(5);
            }
            new Thread(new BTManagerThread(conn)).start();
            while (true) {
            }
            //conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isDatabaseValid(Connection conn) {
        System.out.println("Starting Database Sanity Check...");
        Statement stmt = null;
        try {
            conn.getMetaData();
            System.out.print("\tChecking CONFIG Table: ");
            if (!doesTableExsist(conn, "CONFIG")) {
                System.out.println('✗');
                System.out.println("ERROR: CONFIG Table Not Found In Database");
                return false;
            } else {
                ResultSet rset = conn.getMetaData().getColumns(null, null, "CONFIG", null);
                boolean CDN = false;
                boolean CDI = false;
                boolean CDT = false;
                boolean CDS = false;
                while (rset.next()) {
                    switch (rset.getString(4)) {
                        case "CONFIG_DATA_NUMBER":
                            if (!CDN) {
                                CDN = true;
                                if (rset.getShort(5) != Types.INTEGER) {
                                    System.out.println('✗');
                                    System.out.println("ERROR: " + rset.getString(4) + " Has Wrong Data Type");
                                    return false;
                                }
                            } else {
                                System.out.println('✗');
                                System.out.println("ERROR: Column " + rset.getString(4) + " Is Defined Twice");
                                return false;
                            }
                            break;
                        case "CONFIG_DATA_ID":
                            if (!CDI) {
                                CDI = true;
                                if (rset.getShort(5) != Types.VARCHAR) {
                                    System.out.println('✗');
                                    System.out.println("ERROR: " + rset.getString(4) + " Has Wrong Data Type");
                                    return false;
                                }
                            } else {
                                System.out.println('✗');
                                System.out.println("ERROR: Column " + rset.getString(4) + " Is Defined Twice");
                                return false;
                            }
                            break;
                        case "CONFIG_DATA_TYPE":
                            if (!CDT) {
                                CDT = true;
                                if (rset.getShort(5) != Types.VARCHAR) {
                                    System.out.println('✗');
                                    System.out.println("ERROR: " + rset.getString(4) + " Has Wrong Data Type");
                                    return false;
                                }
                            } else {
                                System.out.println('✗');
                                System.out.println("ERROR: Column " + rset.getString(4) + " Is Defined Twice");
                                return false;
                            }
                            break;
                        case "CONFIG_DATA_SHOWAVG":
                            if (!CDS) {
                                CDS = true;
                                if (rset.getShort(5) != Types.BOOLEAN) {
                                    System.out.println('✗');
                                    System.out.println("ERROR: " + rset.getString(4) + " Has Wrong Data Type");
                                    return false;
                                }
                            } else {
                                System.out.println('✗');
                                System.out.println("ERROR: Column " + rset.getString(4) + " Is Defined Twice");
                                return false;
                            }
                            break;
                        default:
                            System.out.println('✗');
                            System.out.println("ERROR: Unknown Column (" + rset.getString(4) + ") In CONFIG Table");
                            return false;
                    }
                }
                if (!CDN) {
                    System.out.println('✗');
                    System.out.println("ERROR: Column CONFIG_DATA_NUMBER Not Present");
                    return false;
                }
                if (!CDI) {
                    System.out.println('✗');
                    System.out.println("ERROR: Column CONFIG_DATA_ID Not Present");
                    return false;
                }
                if (!CDT) {
                    System.out.println('✗');
                    System.out.println("ERROR: Column CONFIG_DATA_TYPE Not Present");
                    return false;
                }
                if (!CDS) {
                    System.out.println('✗');
                    System.out.println("ERROR: Column CONFIG_DATA_SHOWAVG Not Present");
                    return false;
                }

                System.out.println('✔');
            }

            System.out.print("\tChecking MATCHES Table: ");
            if (!doesTableExsist(conn, "MATCHES")) {
                System.out.println('✗');
                System.out.println("ERROR: MATCHES Table Not Found In Database");
                return false;
            } else {
                System.out.println('✔');
            }

            System.out.print("\tChecking TEAMS Table: ");
            if (!doesTableExsist(conn, "TEAMS")) {
                System.out.println('✗');
                System.out.println("ERROR: TEAMS Table Not Found In Database");
                return false;
            } else {
                System.out.println('✔');
            }

            System.out.print("\tChecking USERS Table: ");
            if (!doesTableExsist(conn, "USERS")) {
                System.out.println('✗');
                System.out.println("ERROR: USERS Table Not Found In Database");
                return false;
            } else {
                System.out.println('✔');
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; //Sql issue so database is definitely broke
        }
        System.out.println("Database Is Valid");
        return true;
    }

    private static boolean doesTableExsist(Connection conn, String name) {
        try {
            ResultSet rset = conn.getMetaData().getTables(null, null, name, null);
            if (rset.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
