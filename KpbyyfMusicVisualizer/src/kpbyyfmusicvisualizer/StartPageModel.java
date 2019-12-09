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
public class StartPageModel extends AbstractModel{
    
    HashMap accounts;
    
    public StartPageModel() {
        accounts = Accounts.getInstance();
    }
    
    public void checkForUser(String username, String password) {
        
        if (accounts.containsKey(username)) {
            User user = (User) accounts.get(username);
            if (user.getUserType() == UserType.PREMIUM) {
                PremiumUser premiumUser = (PremiumUser) user;
                if (premiumUser.checkPass(password)) {
                    firePropertyChange("SignIn", null, premiumUser);
                } else {
                    firePropertyChange("InvalidLogin", null, null);
                }
            } else {
                firePropertyChange("SignIn", null, user);
            }
        } else {
            firePropertyChange("InvalidLogin", null, null);
        }


    }
    
}
