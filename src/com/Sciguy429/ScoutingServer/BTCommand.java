package com.Sciguy429.ScoutingServer;

public class BTCommand {

    //Data Storage Object For BT Command Info To Be Stored Into After It Has Been Parsed
    //Commands Can Have Arguments Attached As Well As References To Allow for Responses

    public static final class ValidateLogin extends BTCommand {
        //Checks Login Information Against Database And Returns User Information
        //Requires A Username And Password
        String userName;
        String userPassword;
        public ValidateLogin(String userName, String userPassword) {
            this.userName = userName;
            this.userPassword = userPassword;
        }
    }

    public static final class Sync extends BTCommand {
        //Requests A Database Re-Sync From The Server
        //If Full Is Set To True The Re-Sync Sends All Data Regardless Of The Condition Of the Remote Devices Internal Database
        //Otherwise A Md5 Of The Clients Config DB Copy Is Sent, Just To Make Sure A Data Sync Is Possible
        //NOTE: A Full DB Sync Will Overwrite The Clients Config Table UNCONDITIONALLY And Thus Will Only Be Used On First App Start Or After A Hard DB Reset On The Client
        Boolean full = false;
        String clientConfigDBMd5;
        public Sync(Boolean full) {
            this.full = full;
        }

        public Sync(String clientConfigDBMd5) {
            this.clientConfigDBMd5 = clientConfigDBMd5;
        }
    }
}
