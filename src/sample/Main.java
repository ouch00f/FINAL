package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;


import javax.swing.*;
//import java.awt.*;
import java.awt.*;
import java.io.IOException;
import java.sql.*;

public class Main extends Application {

    // Character/object instantiation
    int ground = 300;
    Squidward SQUIDWARD = new Squidward(675, ground);
    Shaggy SHAGGY = new Shaggy(25, ground);


    public Main() throws IOException {

    }

    @Override
    public void start(Stage primaryStage) {



        Group root = new Group();
        primaryStage.setTitle("Shaggy VS Squidward");
        Scene scene = new Scene(root, 800, 450);

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnHiding(event -> {
            Runtime.getRuntime().exit(0);
        });//Ends all processes of application on stage close



        // Adding object characters to scene.
        root.getChildren().add(SHAGGY);
        root.getChildren().add(SQUIDWARD);


        //Key event filters
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {//key press controls
            this.keyPress(keyEvent.getCode());
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {//key press controls
            this.keyRelease(keyEvent.getCode());
        });

        //All timing and motion tools
        Timer timer = new Timer(45, e -> {
            checkConditions(SHAGGY);
            checkConditions(SQUIDWARD);
        });
        timer.start();

    }


    public void keyPress(KeyCode keycode) {//when keys are pressed


        switch (keycode) {


            // SHAGGY CONTROLS
            case D:
                SHAGGY.runRight();
                break;
            case A:
                SHAGGY.runLeft();
                break;
            case C:
                SHAGGY.attack(SQUIDWARD, SHAGGY);
                //SHAGGY.attack(SQUIDWARD, SHAGGY.getLayoutX(), SQUIDWARD.getLayoutX());
                break;
            case W:
                SHAGGY.jumpVariable = SHAGGY.jumpVariable / 2;
                break;
            case S:
                SHAGGY.block();
                SHAGGY.setBlocking(true);

                break;


            // SQUIDWARD CONTROLS
            case L:
                SQUIDWARD.runRight();
                break;
            case J:
                SQUIDWARD.runLeft();
                break;
            case P:
                SQUIDWARD.attack(SHAGGY, SQUIDWARD);
                break;
            //SQUIDWARD.attack(SHAGGY,SQUIDWARD.getLayoutX(), SHAGGY.getLayoutX());
            case I:
                SQUIDWARD.jumpVariable = SQUIDWARD.jumpVariable / 2;
                break;
            case K:
                SQUIDWARD.block();
                SQUIDWARD.setBlocking(true);
                break;

        }
        // System.out.println("Shaggy Position(X):"+SHAGGY.getLayoutX());
        System.out.println("Shaggy Health:" + SHAGGY.getHealth());

    }

    public void keyRelease(KeyCode keycode) {//when keys are released




        switch (keycode) {
            // SHAGGY's
            case W:
                SHAGGY.jump();
                break;

            case S:
                if (!SHAGGY.getFacingRight()) {
                    SHAGGY.standLeft();
                } else {
                    SHAGGY.standRight();
                }
                SHAGGY.setBlocking(false);
                break;

            case D:
                SHAGGY.standRight();
                break;
            case A:
                SHAGGY.standLeft();
                break;

            case C:
                // Upon attacking, this key release will set the image for shaggy position depending on where he faces.
                if (SHAGGY.isRunningLeft || !SHAGGY.getFacingRight()) {
                    SHAGGY.standLeft();
                } else {
                    SHAGGY.standRight();
                }
                endCondition();
                break;

            // SQUIDWARD's
            case I:
                SQUIDWARD.jump();
                break;

            case L:
                SQUIDWARD.standRight();
                break;

            case J:
                SQUIDWARD.standLeft();
                break;

            case K:
                if (!SQUIDWARD.getFacingRight()) {
                    SQUIDWARD.standLeft();
                } else {
                    SQUIDWARD.standRight();
                }
                SQUIDWARD.setBlocking(false);
                break;

            // Upon attacking, this key release will set the image for squidward position depending on where he faces.
            case P:
                if (SQUIDWARD.isRunningLeft() || !SQUIDWARD.getFacingRight()) {
                    SQUIDWARD.standLeft();
                } else {
                    SQUIDWARD.standRight();
                }
                endCondition();
                break;
        }
    }


    public void checkConditions(Character player) {//continuously checks conditions on the tick of a timer
//        //////////////////////message
//        if (SHAGGY.isTouching(SQUIDWARD)) {
//            System.out.println("The characters are touching: " + SHAGGY.isTouching(SQUIDWARD));
//        }


        if (player.isRunningRight()) {
            player.setLayoutX(player.getLayoutX() + player.getSpeed());
        }

        if (player.isRunningLeft()) {
            player.setLayoutX(player.getLayoutX() - player.getSpeed());
        }

        if (player.getLayoutY() <= ground) {//when the player is falling
            //player.jump();
            player.fall();

            //check to see if player has changed to hit ground
            if (player.getLayoutY() >= ground) {
                player.land();
            }
        }

        //boundaries of the stage and ensuring player does not exceed them
        if (player.getLayoutX() > 750) {
            player.setLayoutX(750);
        }
        if (player.getLayoutX() < 0) {
            player.setLayoutX(0);
        }

    }

    public static void main(String[] args) {
        // database instantiation
        SQLite sql = new SQLite();

        // connects to db.
        sql.getConnection();
        // creates a table in db.
        sql.createTable();

        sql.insertScore(200);
        sql.insertCoins(5);
        // updates the highscore into the database.
        sql.update(500);
        // displays db data.
        sql.displayScore();
        sql.displayCoins();
        launch(args);

    }

    // Early testing for now, using the console.
    // With an end condition that will calculate score and winner.
    public void endCondition(){

        if (SHAGGY.health<=0){
            System.out.println("\nShaggy's Score: "+SHAGGY.score);
            System.out.println("Squidward's Score: "+SQUIDWARD.score+"\nSQUIDWARD WINS! by "+(SQUIDWARD.score-SHAGGY.score));
            SQUIDWARD.coins += 1;

            Menu menu = new Menu();
        } else if (SQUIDWARD.health<=0){
            System.out.println("\nSquidward's Score: "+SQUIDWARD.score);
            System.out.println("Shaggy's Score: "+SHAGGY.score+"\nSHAGGY WINS! by "+(SHAGGY.score-SQUIDWARD.score));
            SHAGGY.coins += 1;
            Menu menu = new Menu();
        }
    }

    // Early upgrade method, not sure how i'm going to implement it atm.
    public void upgradeCondition(){
        if (SHAGGY.coins == 10 || SQUIDWARD.coins ==10){
            // be able to purchase upgrade
        }
    }
}