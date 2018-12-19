/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kpbyyfmusicvisualizer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;



/**
 * FXML Controller class
 *
 * @author kevinbowers
 */
public class SignUpViewController extends Switchable implements Initializable, PropertyChangeListener {

    @FXML
    private CheckBox premiumCheckBox;
    
    @FXML
    private TextField newUsernameTextField;
    
    @FXML
    private Label passwordLabel;
    
    @FXML
    private PasswordField newPasswordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private Button createAccountButton;
    
    @FXML
    private Label usernameTakenLabel;
    
    @FXML
    private Label passwordErrorLabel;
    
    SignUpModel signUpModel;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
        signUpModel = new SignUpModel();
        signUpModel.addPropertyChangeListener(this);
        
        newPasswordField.setVisible(false);
        confirmPasswordField.setVisible(false);
        passwordLabel.setVisible(false);
        usernameTakenLabel.setVisible(false);
        passwordErrorLabel.setVisible(false);
    }   
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        
        switch (propertyName) {
            case "UsernameExists":
                usernameTakenLabel.setVisible(true);
                break;
            case "AccountCreated":
                newUsernameTextField.clear();
                newPasswordField.clear();
                confirmPasswordField.clear();
                premiumCheckBox.setSelected(false);
                Switchable.switchTo("VisualizerView");
                VisualizerViewController c = (VisualizerViewController) getControllerByName("VisualizerView");
                User user = (User) evt.getNewValue();
                c.userInfoText.setText(user.getUserInfo());
                break;
            default:
                System.out.println("***Property Name Not Recognized***");
                break;
        }
    }
    
    @FXML
    private void premiumCheck() {
        if (premiumCheckBox.selectedProperty().get()) {
            newPasswordField.setVisible(true);
            confirmPasswordField.setVisible(true);
            passwordLabel.setVisible(true);
        } else {
            newPasswordField.clear();
            confirmPasswordField.clear();
            newPasswordField.setVisible(false);
            confirmPasswordField.setVisible(false);
            passwordLabel.setVisible(false);
        }
    }
    
    @FXML
    private void createAccount() {
        
        usernameTakenLabel.setText("Username taken. Please try another");
        usernameTakenLabel.setVisible(false);
        passwordErrorLabel.setVisible(false);
        try {
            TimeUnit.MILLISECONDS.sleep(800);
        } catch (InterruptedException ex) {
            Logger.getLogger(SignUpViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!newUsernameTextField.getText().isEmpty()) {
            if (premiumCheckBox.selectedProperty().get()) {
                
                if (!(newPasswordField.getText().isEmpty() && newPasswordField.getText().isEmpty())) {
                
                    if (newPasswordField.getText().equals(confirmPasswordField.getText())) {
                         signUpModel.signUp(newUsernameTextField.getText(), newPasswordField.getText());
                    } else {
                        passwordErrorLabel.setText("Passwords do not match. Try again");
                        passwordErrorLabel.setVisible(true);
                    }
                } else {
                    passwordErrorLabel.setText("Please enter a valid password");
                    passwordErrorLabel.setVisible(true);
                }
                
            } else {
                signUpModel.signUp(newUsernameTextField.getText());
            }
        } else {
            usernameTakenLabel.setText("Please enter a username");
            usernameTakenLabel.setVisible(true);
        }
    }
    
    @FXML
    private void goBack() {
        Switchable.switchTo("StartPage");
    }
}
