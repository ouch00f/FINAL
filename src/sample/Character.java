/*
abstract class implementing java.scene.layout.HBox which is used to load a player onto the Main stage under loadGame
Composed of java.scene.image.ImageView which is used for the visual display of the player
*/



package sample;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import java.io.IOException;


public abstract class Character extends HBox {

    //Fields------------------------------------------------------------------------------------------------------------
    protected double speed, jumpConstant, jumpVariable, fallConstant;
    protected boolean isBlocking, isFalling,isJumping, facingRight, isRunningRight, isRunningLeft;
    protected int health = 100;
    protected int damage = 10;
    protected int dmgAmount, coins;
    protected int atkRange = 5;
    protected int score = 0;
    protected Image standingRight, standingLeft, runningRight, runningLeft,jumpingRight,jumpingLeft, attackingLeft, attackingRight, blockingLeft, blockingRight, damagedLeft, damagedRight;
    protected Image currentImage;//place holder such that images are not loaded continuously: Currently being tested to see if it's faster than loading continuously
    ImageView characterImage;

    //Constructor-------------------------------------------------------------------------------------------------------
    public Character(double initialX, double initialY,double startingSpeed,double startingJumpConstant) throws IOException {//Constructor
        this.setLayoutX(initialX);
        this.setLayoutY(initialY);
        this.setSpeed(startingSpeed);
        this.setJumpConstant(startingJumpConstant);

        characterImage = new ImageView(jumpingLeft);
        this.getChildren().add(characterImage);
    }


    //------------------------------------------------------------------------------------------------------------------
    /***************************************************************************
     * * Image Checking * *
     **************************************************************************/
    public void setImage(Image image){
        characterImage.setImage(image);
    }
    // Sets the displayed image of the Character to the parameter image

    public Image getCurrentImage(){return this.currentImage;}//The image that makes the character visible
    //Returns the value of the Image currently being displayed on the Character

    protected boolean differentImage(Image img){ return !(img == this.getCurrentImage()); }
    //true when characterImage is not the same as the parameter passed here


    //------------------------------------------------------------------------------------------------------------------
    /***************************************************************************
     * * Horizontal Motion * *
     **************************************************************************

    The following methods set the character to the appropriate frame based on the direction she should be facing and
     adjusts boolean fields to signify this change has happened.*/

    public void standRight(){
        this.facingRight = true;
        this.isRunningRight = false;
        this.isRunningLeft = false;

        if(!this.isFalling){
            if(this.differentImage(this.standingRight)) {
                this.setImage(standingRight);
            }
        }else{
            if(this.differentImage(this.jumpingRight)){
                this.setImage(jumpingRight);
            }
        }
    }


    public void standLeft(){
        this.facingRight = false;
        this.isRunningRight = false;
        this.isRunningLeft = false;

        if(!this.isFalling){
            if(this.differentImage(this.standingLeft)) {
                this.setImage(standingLeft);
            }
        }else{
            if(this.differentImage(this.jumpingLeft)) {
                this.setImage(jumpingLeft);
            }
        }

    }

    public void runRight(){
        if(!this.isFalling){
            if(this.differentImage(this.runningRight)) {
                this.setImage(runningRight);
            }
        }else{
            if(this.differentImage(this.jumpingRight)) {
                this.setImage(jumpingRight);
            }
        }
        this.isRunningRight = true;
        this.isRunningLeft = false;
        this.facingRight = true;
    }

    public void runLeft(){
        if(!this.isFalling){
            if(this.differentImage(this.runningLeft)) {
                this.setImage(runningLeft);
            }
        }else{
            if(this.differentImage(this.jumpingLeft)) {
                this.setImage(jumpingLeft);
            }
        }
        this.isRunningLeft = true;
        this.isRunningRight = false;
        this.facingRight = false;
    }


    //------------------------------------------------------------------------------------------------------------------
    /***************************************************************************
     * * Vertical Motion * *
     **************************************************************************/

    public void jump(){
        this.jumpVariable = this.jumpConstant;
        this.gravity();
    }
    //Ensures that the player falls at the same speed after jumping as when she is simply falling

    public void fall(){
        this.setJumping(false);
        this.gravity();
    }
    //Ensures that the player falls

    public void land(){
        this.jumpVariable = 0;
        this.setFalling(false);
        this.setFallConstant(0);
        this.setJumping(false);
        if(this.facingRight){
            this.standRight();
        }else{
            this.standLeft();
        }
    }
    //Adjusts landing animation based on the direction of the player while falling

    private void gravity(){
        this.setFalling(true);
        this.setFallConstant(this.getFallConstant()+2);
        this.setLayoutY(this.getLayoutY() + this.getFallConstant() - this.jumpVariable);
        if(facingRight) {
            if(differentImage(jumpingRight)) {
                this.setImage(jumpingRight);
            }
        }else{
            if(differentImage(jumpingLeft)) {
                this.setImage(jumpingLeft);
            }
        }
    }
    /*Meant as a countinous call on a timer while a Character falls ensuring that the correct direction Image is loaded
    and the value of the player's layoutY is correct*/

    //------------------------------------------------------------------------------------------------------------------
    /***************************************************************************
     * * Combat Mechanics * *
     **************************************************************************/

    public void attack(Character opponent, Character player){
        this.setBlocking(false);
        if(this.isTouching(opponent) && opponent.isBlocking()) {
            System.out.println("Attack Blocked!");
        } else if(this.isTouching(opponent) && !opponent.isBlocking()){
            opponent.setHealth(opponent.getHealth() - this.damage);
            player.score += this.damage;
            // changes the opponent's character when damaged.
            // need to implement a timer where it changes back, thinking of a method for that hmm.
            if(opponent.facingRight || opponent.isRunningRight){
                opponent.setImage(opponent.damagedRight);
            } else{
                opponent.setImage(opponent.damagedLeft);
            }
        }
        //Successful attacks are dealt to an opponent Character

        //loading the attack animation
        if (facingRight || isRunningRight) {
            this.setImage(attackingRight);
        } else{
            this.setImage(attackingLeft);
        }
    }
    /*Loads attack animation and deals damage to an opponent: Character, who is not blocking and is in contact on
    the stage*/


    public void block(){
        this.setBlocking(true);
        if (facingRight || isRunningRight){
            this.setImage(blockingRight);
        } else if (!facingRight || isRunningLeft){
            this.setImage(blockingLeft);
        }
    }
    //Loads appropriate blocking frame based on direction facing and sets the blocking value to true

    public boolean isTouching( Character p2){//used to determine whether or not the characters are touching on the stage
        return (Math.abs(this.getLayoutX() -p2.getLayoutX())* 2 < (this.getWidth() + p2.getWidth())) && (Math.abs(this.getLayoutY() - p2.getLayoutY())*2<(this.getHeight()+p2.getHeight()));
    }
    //Returns true when this Character and p2: Character, physically intersect the same space on the stage


    //------------------------------------------------------------------------------------------------------------------
    /***************************************************************************
     * * Accessors and Mutators for getting and setting common fields * *
     **************************************************************************/
    public void setFallConstant(double newFallConstant){this.fallConstant = newFallConstant;}

    public double getFallConstant(){return this.fallConstant;}

    public boolean isBlocking(){ return this.isBlocking;}

    public boolean isRunningRight() {return this.isRunningRight;}

    public boolean isRunningLeft(){return  this.isRunningLeft;}

    public boolean isFalling(){return this.isFalling;}

    public boolean isJumping(){ return this.isJumping;}

    public void setJumping(boolean newJumping){this.isJumping = newJumping;}

    public int getHealth(){return this.health;}

    public void setHealth(int newHealth){ this.health = newHealth;}

    public int getScore(){return this.score;}

    public void setScore(int score){ this.health = score;}

    public int getCoins(){return this.coins;}

    public void setCoins(int coins){ this.health = coins;}

    public void setFacingRight(boolean newFacingRight){ this.facingRight = newFacingRight; }

    public boolean getFacingRight(){ return this.facingRight;}

    public void setBlocking(boolean newBlocking){ this.isBlocking = newBlocking; }

    public void setFalling(boolean newFalling){this.isFalling = newFalling;}

    public double getSpeed(){return this.speed;}

    public void setSpeed(double newSpeed){this.speed = newSpeed;}

    public double getJumpVariable(){return this.jumpVariable;}

    public void setJumpConstant(double newJumpConstant) {this.jumpConstant = newJumpConstant;}

}
