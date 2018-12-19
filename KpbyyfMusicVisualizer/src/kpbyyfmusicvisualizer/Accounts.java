/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kpbyyfmusicvisualizer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author kevinbowers
 */
public class Accounts {
    
    /**
     *
     */
    private static HashMap<String,User> accounts;
    
    private Accounts() {}
    
    public static HashMap getInstance() {
        if(accounts == null) {
            // TODO: Try to read from stored data
            
            JSONParser parser = new JSONParser();
            accounts = new HashMap();
            
            try (FileReader reader = new FileReader("accounts.json")) {
                
                Object obj = parser.parse(reader);
                
                JSONArray accountList = (JSONArray) obj;
                
                accountList.forEach( user -> parseUser((JSONObject) user));
                
            } catch ( IOException | ParseException ex) {}
            
        }
        return accounts;
    }
    
//    public static String printHashMap() {
//        
//        StringBuilder sb = new StringBuilder();
//        
//        for (Object x : accounts.values()) {
//            User user = (User) x;
//            sb.append(user.getUserInfo() + "\n");
//        }
//        
//        return sb.toString();
//    }
    
    public static void save() {
        JSONArray arr = new JSONArray();
        
        for (Object x : accounts.values()) {
            User user = (User) x;
            
            arr.add(user.encodeJSON());
        }
        
        try (FileWriter file = new FileWriter("accounts.json")) {
            
            file.write(arr.toJSONString());
            file.flush();
            
        } catch (IOException e) {}
        
    }
    
    private static void parseUser(JSONObject obj) {
        
        if ((Enum.valueOf(UserType.class, (String) obj.get("userType"))).equals(UserType.PREMIUM)) {
            PremiumUser user = new PremiumUser((String) obj.get("username"), (String) obj.get("pass"));
            accounts.put(user.getUsername(), user);
        } else {
            GuestUser user = new GuestUser((String) obj.get("username"));
            accounts.put(user.getUsername(), user);
        }
    }
    
}
