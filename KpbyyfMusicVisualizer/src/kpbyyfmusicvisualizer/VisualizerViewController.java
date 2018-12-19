/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kpbyyfmusicvisualizer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author kevinbowers
 */
public class VisualizerViewController extends Switchable implements Initializable, PropertyChangeListener{
    
    @FXML
    private AnchorPane vizPane;
    
    @FXML
    public Text userInfoText;
    
    @FXML
    private Text signOutText;
    
    @FXML
    private MediaView mediaView;
    
    @FXML
    private Text filePathText;
    
    @FXML
    private Text lengthText;
    
    @FXML
    private Text currentText;
    
    @FXML
    private Text bandsText;
    
    @FXML
    private Text visualizerNameText;
    
    @FXML
    private Text errorText;
    
    @FXML
    private Menu visualizersMenu;
    
    @FXML
    private Menu bandsMenu;
    
    @FXML
    private Slider timeSlider;
    
    @FXML
    private Button premiumButton;
    
    VisualizerModel visualizerModel;
    
    private int count;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        visualizerModel = new VisualizerModel();
        visualizerModel.addPropertyChangeListener(this);
        
        visualizerModel.setupVisualizers();
        
        count = 0;
        
        
        bandsText.setText("Bands: " + Integer.toString(visualizerModel.getNumBands()));
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        
        String propertyName = evt.getPropertyName();
        Object newValue = evt.getNewValue();
        Object oldValue = evt.getOldValue();
        
        switch (propertyName) {
            case "AddVisualizerToMenu":
                visualizersMenu.getItems().add((MenuItem) newValue);
                break;
            case "AddBandsToMenu":
                bandsMenu.getItems().add((MenuItem) newValue);
                break;
            case "SetVisualizerNameText":
                visualizerNameText.setText((String) newValue);
                break;
            case "SetBandsText":
                bandsText.setText("Bands: " + (String) newValue);
                break;
            case "StartVisualizer":
                visualizerModel.startCurrentVisualizer(vizPane);
                break;
            case "SetFilePathText":
                filePathText.setText((String) newValue);
                break;
            case "SetErrorText":
                errorText.setText((String) newValue);
                break;
            case "SetMediaPlayer":
                mediaView.setMediaPlayer((MediaPlayer) newValue);
                break;
            case "SetLengthText":
                lengthText.setText((String) newValue);
                break;
            case "SetCurrentText":
                currentText.setText((String) newValue);
                break;
            case "SetTimeSliderMinMax":
                timeSlider.setMin(0);
                timeSlider.setMax(((Duration) newValue).toMillis());
                break;
            case "SetTimeSliderValue":
                timeSlider.setValue((double) newValue);
                break;
            case "SetBackgroundColor":
                vizPane.setStyle((String) evt.getNewValue());
                break;
            case "CheckForPremium":
                System.out.println(userInfoText.getText());
                if (userInfoText.getText().contains("Premium")) {
                    System.out.println("Premium detected in viewController");
                    visualizerModel.setPremium(true);
                } else {
                    System.out.println("Guest detected in viewController");
                    visualizerModel.setPremium(false);
                }
                break;
            default:
                System.out.println("***Property Name Not Recognized***");
                break;
        }
        
    }
    
    @FXML
    private void signOut() {
        visualizerModel.end();
        count = 0;
        premiumButton.setStyle(null);
        Switchable.switchTo("StartPage");
    }
    
    @FXML
    private void handleOpen(Event event) {
        Stage primaryStage = (Stage)vizPane.getScene().getWindow();
        
        visualizerModel.chooseFile(primaryStage);
    }
    
    @FXML
    private void handlePlay(ActionEvent event) {
        visualizerModel.playMediaPlayer();
        
    }
    
    @FXML
    private void handlePause(ActionEvent event) {
        visualizerModel.pauseMediaPlayer();
    }
    
    @FXML
    private void handleStop(ActionEvent event) {
        visualizerModel.stopMediaPlayer();
        
    }
    
    @FXML
    private void handleSliderMousePressed(Event event) {
        visualizerModel.pauseMediaPlayer();
    }
    
    @FXML
    private void handleSliderMouseReleased(Event event) {
        visualizerModel.seek(timeSlider.getValue());
    }
    
    @FXML
    private void premiumButtonClick(ActionEvent event) {
        if (count >= 6 && userInfoText.getText().contains("Premium")) {
            count = 0;
            Switchable.switchTo("PremiumView");
        } else {
            Random rand = new Random();
            int nextInt = rand.nextInt(0xffffff + 1);
            String color = String.format("#%06x", nextInt);
            premiumButton.setStyle("-fx-background-color: " + color + ";");
            count++;
        }
    }
    
}
