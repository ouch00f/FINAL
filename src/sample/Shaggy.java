package sample;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.io.IOException;

public class Shaggy extends Character {
    public Shaggy(double initialX, double initialY) throws IOException {//Default constructor
        super(initialX,initialY,5,5);//initially facing right with the attributes called into the super
        this.speed = 5;
        this.standRight();
    }
}
