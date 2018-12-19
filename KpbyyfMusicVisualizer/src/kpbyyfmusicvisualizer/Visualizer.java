/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kpbyyfmusicvisualizer;

import java.beans.PropertyChangeSupport;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author kevinbowers
 */
public interface Visualizer {
    
    public void start(Integer numBands, AnchorPane vizPane);
    public void end();
    public String getName();
    public void update(double timestamp, double duration, float[] manitudes, float[] phases);
    public abstract String getBackgroundColor();
}
