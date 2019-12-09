/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kpbyyfmusicvisualizer;

import org.json.simple.JSONObject;

/**
 *
 * @author kevinbowers
 */
public class GuestUser extends User {
    
    public GuestUser(String username) {
        super(username, UserType.GUEST);
    }

    @Override
    public String getUserInfo() {
        return "Account Type: Guest\nUsername: " + username;
    }
    
    @Override
    public JSONObject encodeJSON() {
        JSONObject obj = new JSONObject();
        obj.put("username", username);
        obj.put("userType", userType.toString());
        return obj;
    }
}
