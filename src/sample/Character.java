package sample;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import java.io.IOException;


public abstract class Character extends HBox {
    //Fields
    protected float speed, jumpConstant;

    protected boolean isBlocking, isFalling, facingRight, isRunningRight, isRunningLeft;
    protected int health;


    protected Image standingRight, standingLeft, runningRight, runningLeft,jumpingRight,jumpingLeft;
    protected Image currentImage;//place holder such that images are not loaded continuously: Currently being tested to see if it's faster than loading continuously
    ImageView characterImage;


    public Character(double initialX, double initialY,double startingSpeed,double startingJumpConstant) throws IOException {//Constructor
        this.setLayoutX(initialX);
        this.setLayoutY(initialY);

        //Movement Images (these are arrows for the abstract character, but subclasses will overwrite the following Images)
        standingRight = new Image("characterStandingRight.png");
        standingLeft = new Image("characterStandingLeft.png");
        runningRight = new Image("characterRunningRight.png");
        runningLeft = new Image("characterRunningLeft.png");
        jumpingLeft = new Image("characterJumpingLeft.png");
        jumpingRight = new Image("characterJumpingRight.png");



        characterImage = new ImageView(jumpingLeft);
        this.getChildren().add(characterImage);
    }


    //Actions
    public void setImage(Image image){
        characterImage.setImage(image);
    }

    public Image getCurrentImage(){return this.currentImage;}

    public void standRight(){
        this.facingRight = true;
        this.isRunningRight = false;
        this.isRunningLeft = false;

        if(!this.isFalling && this.differentImage(this.standingRight) ) {
            this.setImage(standingRight);
        }
    }

    public void standLeft(){
        this.facingRight = false;
        this.isRunningRight = false;
        this.isRunningLeft = false;

        if(!this.isFalling && this.differentImage(this.standingLeft)) {
            this.setImage(standingLeft);
        }
    }

    public void runRight(){
        if(!this.isFalling && this.differentImage(this.runningRight)) {
            this.setImage(runningRight);
        }
        this.isRunningRight = true;
        this.isRunningLeft = false;
        this.facingRight = true;
    }

    public void runLeft(){
        if(!this.isFalling && this.differentImage(this.runningLeft)) {
            this.setImage(runningLeft);
        }

        this.isRunningLeft = true;
        this.isRunningRight = false;
        this.facingRight = false;
    }

    public void jump(){
        if(facingRight) {
            this.setImage(jumpingRight);
        }else{
            this.setImage(jumpingLeft);
        }
    }

    public void takeHit(int damage){
        if(!this.isBlocking()) { //when this character is not blocking it then takes damage
            this.setHealth(this.getHealth()-damage);
        }
    }

    public void attack(Character opponent, int damage){
        opponent.takeHit(damage);
    }


    protected boolean differentImage(Image img){ return !(img == this.getCurrentImage()); }




    //Accessors and mutators
    public boolean isBlocking(){ return this.isBlocking;}

    public boolean isRunningRight(){return this.isRunningRight;}

    public boolean isRunningLeft(){return  this.isRunningLeft;}

    public boolean isFalling(){return this.isFalling;}

    public int getHealth(){return this.health;}

    public void setHealth(int newHealth){ this.health = newHealth;}

    public void land(){
        this.setFalling(false);
        if(this.facingRight){
            this.standRight();
        }else{
            this.standLeft();
        }
    }



    public void setFacingRight(boolean newFacingRight){ this.facingRight = newFacingRight; }

    public boolean getFacingRight(){ return this.facingRight;}

    public void setBlocking(boolean newBlocking){ this.isBlocking = newBlocking; }

    public void setFalling(boolean newFalling){this.isFalling = newFalling;}

    public float getSpeed(){return this.speed;}

    public void setSpeed(float newSpeed){this.speed = newSpeed;}

    public double getJumpConstant(){return jumpConstant;}

    public void setJumpConstant(float newJumpConstant) {this.jumpConstant = newJumpConstant;}

}
