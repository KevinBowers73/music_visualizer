/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kpbyyfmusicvisualizer;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author kevinbowers
 */
public class VisualizerModel extends AbstractModel {
    
    private ArrayList<Visualizer> visualizers;
    private Visualizer currentVisualizer;
    
     private Media media;
    private MediaPlayer mediaPlayer;
    
    private Integer numBands = 40;
    private final Double updateInterval = 0.05;
    
    private final Integer[] bandsList = {1, 2, 4, 8, 16, 20, 40, 60, 100, 120, 140};
    
    private File file;
    
    private boolean isPremium;
    
    public VisualizerModel() {
        
    }
    
    public void setupVisualizers() {
        
        visualizers = new ArrayList<>();
        
        visualizers.add(new StandardVisualizer());
        visualizers.add(new CircleVisualizer());
        visualizers.add(new RotatingVisualizer());
        visualizers.add(new InvertedVisualizer());
        
        
        for (Visualizer visualizer : visualizers) {
            MenuItem menuItem = new MenuItem(visualizer.getName());
            menuItem.setUserData(visualizer);
            menuItem.setOnAction((ActionEvent event) -> {
                selectVisualizer(event);
            });
            firePropertyChange("AddVisualizerToMenu", 0, menuItem);
            
        }
        currentVisualizer = visualizers.get(0);
        firePropertyChange("SetVisualizerNameText", null, currentVisualizer.getName());
        
        for (Integer bands : bandsList) {
            MenuItem menuItem = new MenuItem(Integer.toString(bands));
            menuItem.setUserData(bands);
            menuItem.setOnAction((ActionEvent event) -> {
                selectBands(event);
            });
            firePropertyChange("AddBandsToMenu", null, menuItem);
        }
    }
    
    private void selectVisualizer(ActionEvent event) {
        MenuItem menuItem = (MenuItem)event.getSource();
        Visualizer visualizer = (Visualizer)menuItem.getUserData();
        changeVisualizer(visualizer);
    }
    
    private void changeVisualizer(Visualizer visualizer) {
        if (currentVisualizer != null) {
            currentVisualizer.end();
        }
        currentVisualizer = visualizer;
        
        firePropertyChange("StartVisualizer", null, null);
        firePropertyChange("SetVisualizerNameText", null, currentVisualizer.getName());
    }
    
    private void selectBands(ActionEvent event) {
        MenuItem menuItem = (MenuItem)event.getSource();
        numBands = (Integer)menuItem.getUserData();
        if (currentVisualizer != null) {
            firePropertyChange("StartVisualizer", null, null);
        }
        if (mediaPlayer != null) {
            mediaPlayer.setAudioSpectrumNumBands(numBands);
        }
        
        firePropertyChange("SetBandsText", null, Integer.toString(numBands));
        
    }
    
    private void openMedia(File file) {
        
        firePropertyChange("SetFilePathText", null, "");
        firePropertyChange("SetErrorText", null, "");
        
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
        
        try {
            media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            firePropertyChange("SetMediaPlayer", null, mediaPlayer);
            mediaPlayer.setOnReady(() -> {
                handleReady();
            });
            mediaPlayer.setOnEndOfMedia(() -> {
                handleEndOfMedia();
            });
            mediaPlayer.setAudioSpectrumNumBands(numBands);
            mediaPlayer.setAudioSpectrumInterval(updateInterval);
            mediaPlayer.setAudioSpectrumListener((double timestamp, double duration, float[] magnitudes, float[] phases) -> {
                handleUpdate(timestamp, duration, magnitudes, phases);
            });
            mediaPlayer.setAutoPlay(true);
            firePropertyChange("SetFilePathText", null, file.getName());
        } catch (Exception ex) {
            firePropertyChange("SetErrorText", null, ex.toString());
        }
    }
    
    private void handleReady() {
        Duration duration = mediaPlayer.getTotalDuration();
        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) duration.toMillis());
        long seconds = TimeUnit.MILLISECONDS.toSeconds((long) duration.toMillis());
        firePropertyChange("SetLengthText", null, minutes + ":" + seconds % 60);
        Duration ct = mediaPlayer.getCurrentTime();
        minutes = TimeUnit.MILLISECONDS.toMinutes((long) ct.toMillis());
        seconds = TimeUnit.MILLISECONDS.toSeconds((long) ct.toMillis());
        firePropertyChange("SetCurrentText", null, minutes + ":" + seconds % 60);
        firePropertyChange("StartVisualizer", null, null);
        firePropertyChange("SetTimeSliderMinMax", 0, duration);
        
    }
    
    private void handleEndOfMedia() {
        mediaPlayer.stop();
        mediaPlayer.seek(Duration.ZERO);
        firePropertyChange("SetTimeSliderValue", null, 0);
    }
    
    private void handleUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
        Duration ct = mediaPlayer.getCurrentTime();
        double ms = ct.toMillis();
        long minutes = TimeUnit.MILLISECONDS.toMinutes((long) ct.toMillis());
        long seconds = TimeUnit.MILLISECONDS.toSeconds((long) ct.toMillis());
        firePropertyChange("SetCurrentText", null, minutes + ":" + seconds % 60);
        firePropertyChange("SetCurrentText", null, minutes + ":" + seconds % 60); // TODO: Format to MM:SS
        firePropertyChange("SetTimeSliderValue", null, ms);
        
        
        currentVisualizer.update(timestamp, duration, magnitudes, phases);
    }
    
    public void playMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }
    
    public void pauseMediaPlayer() {
        if (mediaPlayer != null) {
           mediaPlayer.pause(); 
        }
    }
    
    public void stopMediaPlayer() {
        if (mediaPlayer != null) {
           mediaPlayer.stop(); 
        }
    }
    
    public void seek(double value) {
        if (mediaPlayer != null) {

            mediaPlayer.seek(new Duration(value));

            firePropertyChange("StartVisualizer", null, currentVisualizer);

            mediaPlayer.play();
        } 
    }
    
    public void chooseFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            openMedia(file);
        }
    }
    
    public Integer getNumBands() {
        return numBands;
    }
    
    public void startCurrentVisualizer(AnchorPane vizPane) {
        currentVisualizer.start(numBands, vizPane);
        firePropertyChange("SetBackgroundColor", 0, currentVisualizer.getBackgroundColor());
    }
    
    public void end() {
        currentVisualizer.end();
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
        firePropertyChange("SetFilePathText", null, "");
        firePropertyChange("SetLengthText", null, "");
        firePropertyChange("SetCurrentText", null, "");
        firePropertyChange("StartVisualizer", null, null);
        firePropertyChange("SetTimeSliderMinMax", 0, 0);
        firePropertyChange("SetTimeSliderValue", null, 0.0);
    }
    
    public void setPremium(boolean bool) {
        isPremium = bool;
    }
    
}
