/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kpbyyfmusicvisualizer;

import static java.lang.Integer.min;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author kevinbowers
 */
public class StandardVisualizer implements Visualizer {
    private final String name = "Standard Visualizer";
    
    private Integer numBands;
    private AnchorPane vizPane;
    
    private final Double bandHeightPercentage = 1.3;
    
    private final Double minRectRadius = 10.0;
    
    private Double width = 0.0;
    private Double height = 0.0;
    
    private Double bandWidth = 0.0;
    private Double bandHeight = 0.0;
    private Double halfBandHeight = 0.0;
    
    private final Double startHue = 260.0;
    
    private Rectangle[] rectangles;
    
    public StandardVisualizer() {
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void start(Integer numBands, AnchorPane vizPane) {
        end();
        
        this.numBands = numBands;
        this.vizPane = vizPane;
        
        height = vizPane.getHeight();
        width = vizPane.getWidth();
        
        bandWidth = width / numBands;
        bandHeight = height * bandHeightPercentage;
        halfBandHeight = bandHeight / 2;
        rectangles = new Rectangle[numBands];
        
        for (int i = 0; i < numBands; i++) {
            Rectangle rect = new Rectangle();
            rect.setLayoutX(bandWidth / 2 + bandWidth * i);
            rect.setLayoutY(height / 2);
            rect.setWidth(bandWidth / 2);
            rect.setHeight(minRectRadius);
            rect.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            vizPane.getChildren().add(rect);
            rectangles[i] = rect;
        }

    }
    
    @Override
    public void end() {
         
         if (rectangles != null) {
             for (Rectangle rect : rectangles) {
                 vizPane.getChildren().remove(rect);
             }
             rectangles = null;
         }
    }
    
    @Override
    public void update(double timestamp, double duration, float[] magnitudes, float[] phases) {
        if (rectangles == null) {
            return;
        }
        Integer num = min(rectangles.length, magnitudes.length);
        
        for (int i = 0; i < num; i++) {
            rectangles[i].setHeight(((60.0 + magnitudes[i])/60.0) * halfBandHeight + minRectRadius);
            rectangles[i].setLayoutY(height/2 - rectangles[i].getHeight());
            rectangles[i].setFill(Color.hsb(startHue - (magnitudes[i] * -6.0), 1.0, 1.0, 1.0));
        }
    }
    
    @Override
    public String getBackgroundColor() {
        return "-fx-background-color: #FFFFFF;";
    }
}
