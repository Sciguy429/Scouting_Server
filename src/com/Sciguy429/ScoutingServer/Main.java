package com.Sciguy429.ScoutingServer;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;

import java.sql.*;
import java.util.ArrayList;

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

        ArrayList<Column> ConfigAllowedColumns = new ArrayList<>();
        ConfigAllowedColumns.add(new Column("CONFIG_DATA_NUMBER", Types.INTEGER));
        ConfigAllowedColumns.add(new Column("CONFIG_DATA_ID", Types.VARCHAR));
        ConfigAllowedColumns.add(new Column("CONFIG_DATA_TYPE", Types.VARCHAR));
        ConfigAllowedColumns.add(new Column("CONFIG_DATA_SHOWAVG", Types.BOOLEAN));

        Statement stmt = null;
        try {
            conn.getMetaData();
            System.out.print("\tChecking CONFIG Table: ");
            if (doesTableExsist(conn, "CONFIG")) {
                if (checkTableRows(conn, "CONFIG", ConfigAllowedColumns)) {
                    System.out.println('✔');
                }
                else {
                    return false;
                }
            } else {
                System.out.println('✗');
                System.out.println("ERROR: CONFIG Table Not Found In Database");
                return false;
            }

            System.out.print("\tChecking MATCHES Table: ");
            if (doesTableExsist(conn, "MATCHES")) {
                System.out.println('✔');
            } else {
                System.out.println('✗');
                System.out.println("ERROR: MATCHES Table Not Found In Database");
                return false;
            }

            System.out.print("\tChecking TEAMS Table: ");
            if (doesTableExsist(conn, "TEAMS")) {
                System.out.println('✔');
            } else {
                System.out.println('✗');
                System.out.println("ERROR: TEAMS Table Not Found In Database");
                return false;
            }

            System.out.print("\tChecking USERS Table: ");
            if (doesTableExsist(conn, "USERS")) {
                System.out.println('✔');
            } else {
                System.out.println('✗');
                System.out.println("ERROR: USERS Table Not Found In Database");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; //Sql issue so database is definitely broke
        }
        System.out.println("Database Is Valid");
        return true;
    }

    private static class Column {
        private String name;
        private int type;
        private Column (String name, int type) {
            this.name = name;
            this.type = type;
        }
    }

    private static boolean checkTableRows(Connection conn, String tableName, ArrayList<Column> allowedColumns) {
        try {
            ArrayList<Column> col = new ArrayList<>();
            ResultSet rset = conn.getMetaData().getColumns(null, null, tableName, null);
            while (rset.next()) {
                col.add(new Column(rset.getString(4), (int) rset.getShort(5)));
            }
            for (Column c : col) {
                boolean found = false;
                for (Column b : allowedColumns) {
                    if (c.name.equals(b.name)) {
                        if (c.type == b.type) {
                            allowedColumns.remove(b);
                            found = true;
                            break;
                        } else {
                            System.out.println('✗');
                            System.out.println("Error: Column (" + c.name + ") Has Incorrect Data Type");
                            return false;
                        }
                    }
                }
                if (!found) {
                    System.out.println('✗');
                    System.out.println("Error: Unknown Column (" + c.name + ")");
                    return false;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println('✗');
            System.out.println("Error: Unknown SQL Error");
            return false;
        }
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
