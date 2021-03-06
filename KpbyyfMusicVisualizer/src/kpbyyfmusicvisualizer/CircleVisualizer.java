/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kpbyyfmusicvisualizer;

import static java.lang.Integer.min;
import java.util.ArrayList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
/**
 *
 * @author kevinbowers
 */
public class CircleVisualizer implements Visualizer {
    private final String name = "Circle Visualizer";
    
    private Integer numBands;
    private AnchorPane vizPane;
    
    private final Double bandHeightPercentage = 1.3;
    private final Double minEllipseRadius = 10.0;  // 10.0
    
    private Double width = 0.0;
    private Double height = 0.0;
    
    private Double bandWidth = 0.0;
    private Double bandHeight = 0.0;
    private Double halfBandHeight = 0.0;
    
    private final Double startHue = 260.0;
    
    private ArrayList<Ellipse> ellipses;
    
    
    
    public CircleVisualizer() {
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
        ellipses = new ArrayList();
        double angle = -(Math.PI/2);
        double step = (2*Math.PI)/numBands;
        
        for (int i = 0; i < numBands; i++) {
            Ellipse ellipse = new Ellipse();
            ellipse.setCenterX(width/2 + 100 * Math.cos(angle));
            ellipse.setCenterY(height/2 + 100 * Math.sin(angle));
            ellipse.setRadiusX(bandWidth / 3);
            ellipse.setRadiusY(minEllipseRadius);
            ellipse.setRotate(Math.toDegrees(angle)+90);
            ellipse.setFill(Color.hsb(startHue, 1.0, 1.0, 1.0));
            vizPane.getChildren().add(ellipse);
            ellipses.add(i, ellipse);
            angle += step;
        }
        
        

    }

    @Override
    public void end() {

        if (ellipses != null) {
             for (Ellipse ellipse : ellipses) {
                 vizPane.getChildren().remove(ellipse);
             }
            ellipses = null;
        } 

    }

    @Override
    public String getName() {
        
        return name;
        
    }

    @Override
    public void update(double timestamp, double duration, float[] magnitudes, float[] phases) {

        if (ellipses == null) {
            return;
        }
        
        Integer num = min(ellipses.size(), magnitudes.length);
        
        double angle = -(Math.PI/2);
        double step = (2*Math.PI)/numBands;
        
        double mags = 0;
        for (float mag : magnitudes) {
            mags += mag;
        }
        
        double radius = (((mags)/magnitudes.length)+80) * -4;
        
        
        for (int i = 0; i < num; i++) {
            ellipses.get(i).setRadiusY( (((60.0 + magnitudes[i])/60.0) * halfBandHeight + minEllipseRadius)/2);
            ellipses.get(i).setFill(Color.hsb(startHue - (magnitudes[i] * -6.0), 1.0, 1.0, 1.0));
            
    
            ellipses.get(i).setCenterX(width/2 + radius * Math.cos(angle));
            ellipses.get(i).setCenterY(height/2 + radius * Math.sin(angle));
            angle += step;
        }

    }
    
    @Override
    public String getBackgroundColor() {
        return "-fx-background-color: #FFFFFF;";
    }
}
