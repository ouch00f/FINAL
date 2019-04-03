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
    protected int damage = 10;
    protected int dmgAmount;
    protected int atkRange = 5;


    protected Image standingRight, standingLeft, runningRight, runningLeft,jumpingRight,jumpingLeft, attackingLeft, attackingRight;
    protected Image currentImage;//place holder such that images are not loaded continuously: Currently being tested to see if it's faster than loading continuously
    ImageView characterImage;


    public Character(double initialX, double initialY,double startingSpeed,double startingJumpConstant) throws IOException {//Constructor
        this.setLayoutX(initialX);
        this.setLayoutY(initialY);

        //Movement Images (these are arrows for the abstract character, but subclasses will overwrite the following Images)
        standingRight = new Image("shaggy_idle_right.png");
        standingLeft = new Image("shaggy_idle_left.png");
        runningRight = new Image("shaggy_move_right.png");
        runningLeft = new Image("shaggy_move_left.png");
        jumpingLeft = new Image("shaggy_jump_left.png");
        jumpingRight = new Image("shaggy_jump_right.png");
        attackingLeft = new Image("shaggy_attack_left.png");
        attackingRight = new Image("shaggy_attack_right.png");


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
            // not sure else if is needed, check later
        }else if (facingRight == false){
            this.setImage(jumpingLeft);
        }
    }



    // [Attacks]
    // takeHit where if inreach is within range and is not blocking than decrease opponent hp
    public void takeHit(double pos1, double pos2){
        boolean reach = false;
        if (pos1 <= (pos2+atkRange) || pos1 >= (pos2+atkRange) || pos1 <=(pos2-atkRange) || pos1 >= (pos2-atkRange)){
            reach = true;
        }
        if(!this.isBlocking() && reach == true){ //when this character is not blocking it then takes damage
            this.setHealth(this.getHealth()-this.damage);
        }
    }

    // attack method
    public void attack(Character opponent, double pos1, double pos2){
        opponent.takeHit(pos1, pos2);
        if (isRunningLeft == true || facingRight == false) {
            this.setImage(attackingLeft);
        } else if (isRunningLeft == false || facingRight == true){
            this.setImage(attackingRight);
        }
    }


    /*// inReach method where attack will be valid if position 1 and near position 2
    public boolean inReach(int pos1, int pos2){
        boolean reach = false;
        if (pos1 <= (pos2+this.atkRange) || pos1 >= (pos2+this.atkRange)){
            reach = true;
        } return reach;
    }*/


    protected boolean differentImage(Image img){ return !(img == this.getCurrentImage()); }


    //Accessors and mutators
    public boolean isBlocking(){ return this.isBlocking;}

    public boolean isRunningRight() {return this.isRunningRight;}

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
