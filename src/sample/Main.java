package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
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
    int ground = 300;

    // Character/object instantiation
    Squidward SQUIDWARD = new Squidward(675, ground);
    Shaggy SHAGGY = new Shaggy(25, ground);

    // menu stuff
    Stage window;
    Scene menu1, menu2;


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
                SHAGGY.attack(SQUIDWARD);
                //SHAGGY.attack(SQUIDWARD, SHAGGY.getLayoutX(), SQUIDWARD.getLayoutX());
                break;
            case W:
                SHAGGY.jumpVariable = SHAGGY.jumpVariable / 2;
                break;
            case S:
                SHAGGY.block();
                SHAGGY.setBlocking(true);

//                // just to fix image scaling (REMOVE AFTER SPRITES ARE SUBMITTED FROM CHARAN.)
//                SHAGGY.setLayoutY(340);
//                //

                break;


            // SQUIDWARD CONTROLS
            case L:
                SQUIDWARD.runRight();
                break;
            case J:
                SQUIDWARD.runLeft();
                break;
            case P:
                SQUIDWARD.attack(SHAGGY);
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


        // TO IMPLEMENT MENU ONLY START WHEN GAME STARTS


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

//                 ONLY FOR SPRITE IMAGE SCALING PROBLEM (REMOVE WHEN CHARAN SUBMITS SPRITES.)
//                SHAGGY.setLayoutY(ground);


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
        // insert for new highscore in db.
        sql.insert(100);
        // displays db data.
        sql.displayDB();
        launch(args);

    }
}