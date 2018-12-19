/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kpbyyfmusicvisualizer;

import java.util.HashMap;

/**
 *
 * @author kevinbowers
 */
public class SignUpModel extends AbstractModel{
    
    HashMap accounts;
    
    public SignUpModel() {
        accounts = Accounts.getInstance();
    }
    
    public void signUp(String username, String password) {
        
        if (accounts.containsKey(username)) {
            firePropertyChange("UsernameExists", null, null);
        } else {
            PremiumUser user = new PremiumUser(username, password);
            accounts.put(username, user);
            Accounts.save();
            firePropertyChange("AccountCreated", null, user);
        }
        
    }
    
    public void signUp(String username) {
        if (accounts.containsKey(username)) {
            firePropertyChange("UsernameExists", null, null);
        } else {
            GuestUser user = new GuestUser(username);
            Accounts.getInstance().put(username, user);
            Accounts.save();
            firePropertyChange("AccountCreated", null, user);
        }
    }
}
