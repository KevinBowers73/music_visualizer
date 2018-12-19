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
public abstract class User {
    
    String username;
    protected UserType userType;
    
    public User(String username, UserType userType) {
        this.username = username;
        this.userType = userType;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public UserType getUserType() {
        return userType;
    }
    
    public void setUserType(UserType newType) {
        userType = newType;
    }
    
    public abstract String getUserInfo();
    
    public abstract JSONObject encodeJSON();
    
}
