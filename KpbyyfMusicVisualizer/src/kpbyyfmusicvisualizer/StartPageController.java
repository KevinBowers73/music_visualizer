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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author kevinbowers
 */
public class StartPageController extends Switchable implements Initializable, PropertyChangeListener {

    @FXML
    private CheckBox premiumCheckBox;
    
    @FXML
    private Button signInButton;
    
    @FXML
    private Text invalidLoginText;
    
    @FXML
    private TextField usernameTextField;
    
    @FXML
    private PasswordField passwordTextField;
    
    @FXML
    private Button signUpButton;
    
    StartPageModel startPageModel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        startPageModel = new StartPageModel();
        startPageModel.addPropertyChangeListener(this);
        
        
        
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        
        switch (propertyName) {
            case "SignIn":
                usernameTextField.clear();
                passwordTextField.clear();
                passwordTextField.setVisible(false);
                premiumCheckBox.setSelected(false);
                invalidLoginText.setVisible(false);
                Switchable.switchTo("VisualizerView");
                VisualizerViewController c = (VisualizerViewController) getControllerByName("VisualizerView");
                User user = (User) evt.getNewValue();
                c.userInfoText.setText(user.getUserInfo());
                break;
            case "InvalidLogin":
                invalidLoginText.setVisible(true);
                break;
            default:
                System.out.println("***Property Name Not Recognized***");
                break;
        }
    }
    
    @FXML
    private void premiumCheck() {
        boolean premium = premiumCheckBox.selectedProperty().get();
        
        if (premium) {
            passwordTextField.visibleProperty().set(true);
        } else {
            passwordTextField.visibleProperty().set(false);
            passwordTextField.textProperty().set("");
        }
        
    }
    
    @FXML
    private void signIn() {
        startPageModel.checkForUser(usernameTextField.getText(), passwordTextField.getText());
    }
    
    @FXML
    private void signUp() {
        Switchable.switchTo("SignUpView");
    }

    @FXML
    private void goToAboutPage() {
        Switchable.switchTo("AboutView");
    }
    
}
