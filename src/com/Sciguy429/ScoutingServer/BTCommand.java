package com.Sciguy429.ScoutingServer;

public class BTCommand {

    //Data Storage Object For BT Command Info To Be Stored Into After It Has Been Parsed

    public static final class ValidateLogin extends BTCommand {  //Checks Login Information Against Database And Returns User Information
        String userName;
        String userPassword;
        public ValidateLogin(String userName, String userPassword) {
            this.userName = userName;
            this.userPassword = userPassword;
        }
    }

    public static final class Sync extends BTCommand {  //Requests A Full Database Re-Sync From The Server
        public Sync() {
            //No Special Options Needed For A Simple Sync Request
        }
    }
}
