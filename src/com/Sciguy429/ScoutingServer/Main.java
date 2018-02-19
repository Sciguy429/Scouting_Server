package com.Sciguy429.ScoutingServer;

import java.lang.reflect.Type;
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

        ArrayList<Column> MatchesAllowedColumns = new ArrayList<>();
        MatchesAllowedColumns.add(new Column("MATCH_NUMBER", Types.INTEGER));

        ArrayList<Column> TeamsAllowedColumns = new ArrayList<>();
        TeamsAllowedColumns.add(new Column("TEAM_NUMBER", Types.INTEGER));
        TeamsAllowedColumns.add(new Column("TEAM_NAME", Types.VARCHAR));
        TeamsAllowedColumns.add(new Column("TEAM_DATA_DRIVE_TRAIN", Types.VARCHAR));
        TeamsAllowedColumns.add(new Column("TEAM_DATA_PICTURE_SERVER_URI", Types.VARCHAR));
        TeamsAllowedColumns.add(new Column("TEAM_DATA_PICTURE_CLIENT_URI", Types.VARCHAR));
        TeamsAllowedColumns.add(new Column("TEAM_DATA_HEIGHT", Types.DOUBLE));
        TeamsAllowedColumns.add(new Column("TEAM_DATA_PLACE_ON_SCALE", Types.BOOLEAN));
        TeamsAllowedColumns.add(new Column("TEAM_DATA_PLACE_ON_SWITCH", Types.BOOLEAN));
        TeamsAllowedColumns.add(new Column("TEAM_DATA_CAN_CLIMB", Types.BOOLEAN));
        TeamsAllowedColumns.add(new Column("TEAM_DATA_PLACE_IN_PORTAL", Types.BOOLEAN));
        TeamsAllowedColumns.add(new Column("TEAM_DATA_PICKUP_IN_ANY_ORIENTATION", Types.BOOLEAN));
        TeamsAllowedColumns.add(new Column("TEAM_DATA_CUBE_PLACE_METHOD", Types.VARCHAR));
        TeamsAllowedColumns.add(new Column("TEAM_DATA_COMMENTS", Types.VARCHAR));

        ArrayList<Column> UsersAllowedColumns = new ArrayList<>();
        UsersAllowedColumns.add(new Column("USER_ID", Types.INTEGER));
        UsersAllowedColumns.add(new Column("USER_NAME", Types.VARCHAR));
        UsersAllowedColumns.add(new Column("USER_PASS", Types.VARCHAR));
        UsersAllowedColumns.add(new Column("USER_FIRST_NAME", Types.VARCHAR));
        UsersAllowedColumns.add(new Column("USER_LAST_NAME", Types.VARCHAR));
        UsersAllowedColumns.add(new Column("USER_PERMISSION_LEVEL", Types.INTEGER));

        Statement stmt = null;
        try {
            conn.getMetaData();
            System.out.print("\tChecking CONFIG Table: ");
            if (doesTableExist(conn, "CONFIG")) {
                if (checkTableRows(conn, "CONFIG", ConfigAllowedColumns, false)) {
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
            if (doesTableExist(conn, "MATCHES")) {
                if (checkTableRows(conn, "MATCHES", MatchesAllowedColumns, true)) {
                    System.out.println('✔');
                }
                else {
                    return false;
                }
            } else {
                System.out.println('✗');
                System.out.println("ERROR: MATCHES Table Not Found In Database");
                return false;
            }

            System.out.print("\tChecking TEAMS Table: ");
            if (doesTableExist(conn, "TEAMS")) {
                if (checkTableRows(conn, "TEAMS", TeamsAllowedColumns, false)) {
                    System.out.println('✔');
                }
                else {
                    return false;
                }
            } else {
                System.out.println('✗');
                System.out.println("ERROR: TEAMS Table Not Found In Database");
                return false;
            }

            System.out.print("\tChecking USERS Table: ");
            if (doesTableExist(conn, "USERS")) {
                if (checkTableRows(conn, "USERS", UsersAllowedColumns, false)) {
                    System.out.println('✔');
                }
                else {
                    return false;
                }
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

    private static boolean checkTableRows(Connection conn, String tableName, ArrayList<Column> allowedColumns, boolean allowUnknown) {
        try {
            ArrayList<Column> col = new ArrayList<>();
            ResultSet rset = conn.getMetaData().getColumns(null, "PUBLIC", tableName, null);
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
                if (!found && !allowUnknown) {
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

    private static boolean doesTableExist(Connection conn, String name) {
        try {
            ResultSet rset = conn.getMetaData().getTables(null, "PUBLIC", name, null);
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
