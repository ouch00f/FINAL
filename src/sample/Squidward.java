package sample;

import javafx.scene.image.Image;

import java.io.IOException;

public class Squidward extends Character {

    public Squidward(double initialX, double initialY) throws IOException {
        super(initialX,initialY,10,30);//initially facing left with the attributes called into the super
        this.setWidth(10);
        this.setHeight(10);
        this.standLeft();

        standingRight = new Image("squidward_idle_right.png");
        standingLeft =  new Image("squidward_idle_left.png");
        runningRight =  new Image("squidward_move_right.png");
        runningLeft =  new Image("squidward_move_left.png");
        jumpingLeft =  new Image("squidward_jump_left.png");
        jumpingRight =  new Image("squidward_jump_right.png");
        attackingLeft = new Image("squidward_attack_left.png");
        attackingRight = new Image("squidward_attack_right.png");
        blockingLeft = new Image("squidward_block_left.png");
        blockingRight = new Image ("squidward_block_right.png");
        damagedLeft = new Image ("squidward_damaged_left.png");
        damagedRight = new Image ("squidward_damaged_right.png");
    }
}
