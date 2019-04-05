package sample;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.IOException;

public class Shaggy extends Character {
    public Shaggy(double initialX, double initialY) throws IOException {//Default constructor
        super(initialX,initialY,15,20);//initially facing right with the attributes called into the super
        this.setWidth(10);
        this.setHeight(10);

        this.standRight();

        standingRight = new Image("shaggy_idle_right.png");
        standingLeft = new Image("shaggy_idle_left.png");
        runningRight = new Image("shaggy_move_right.png");
        runningLeft = new Image("shaggy_move_left.png");
        jumpingLeft = new Image("shaggy_jump_left.png");
        jumpingRight = new Image("shaggy_jump_right.png");
        attackingLeft = new Image("shaggy_attack_left.png");
        attackingRight = new Image("shaggy_attack_right.png");
        blockingLeft = new Image("shaggy_block_left.png");
        blockingRight = new Image ("shaggy_block_right.png");
        damagedLeft = new Image ("shaggy_damaged_left.png");
        damagedRight = new Image ("shaggy_damaged_right.png");
    }

}
