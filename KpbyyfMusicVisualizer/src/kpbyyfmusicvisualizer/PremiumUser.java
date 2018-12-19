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
public class PremiumUser extends User {
    
    private String pass;
    public PremiumUser(String username, String pass) {
        super(username, UserType.PREMIUM);
        this.pass = pass;
    }
  
    public boolean checkPass(String pass) {
        return this.pass.equals(pass);
    }
    
    public void setPass(String pass) {
        this.pass = pass;
    }
    
    @Override
    public String getUserInfo() {
        
        return "Account Type: Premium\nUsername: " + username;
    }
    
    @Override
    public JSONObject encodeJSON() {
        JSONObject obj = new JSONObject();
        obj.put("username", username);
        obj.put("pass", pass);
        obj.put("userType", userType.toString());
        return obj;
    }
}
