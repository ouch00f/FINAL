package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


import javax.swing.*;
import javax.xml.crypto.Data;
//import java.awt.*;
import java.io.IOException;

public class Main extends Application {

    // Character/object instantiation
    int ground = 300;
    Squidward SQUIDWARD = new Squidward(675, ground);
    Shaggy SHAGGY = new Shaggy(25, ground);


    public Main() throws IOException {

    }

    public void purchaseConfirm(Stage primaryStage){

        // Insantiation of the data object, used to retrieve data and use methods in the database class.
        Database sql = new Database();

        // This method will update the current coins to be loaded for future use such as displaying and purchasing
        // for upgrade purchases.
        sql.updateTotalCoins();

        // Instantiation and settings for text label that will inform how much the upgrade costs.
        Text purchaseInfo = new Text("20 coins for +5 damage");
        purchaseInfo.setLayoutY(100);
        purchaseInfo.setTextAlignment(TextAlignment.CENTER);
        purchaseInfo.setLayoutX(300);
        purchaseInfo.setFill(Color.DARKRED);
        purchaseInfo.setFont(new Font("Georgia", 18));

        // Instantiation and settings for the Label, that will show how much coins you currently have.
        Label wallet = new Label("Wallet: "+sql.totalAmount+" coins.");
        wallet.setLayoutX(325);
        wallet.setLayoutY(180);
        wallet.setTextFill(Color.GREEN);
        wallet.setFont(new Font ("Georgia", 15));
        wallet.setVisible(true);


        // Instantiation and settings for the button, this button will act as a purchase button and will apply the upgrade
        // if current coins >= 20.
        Button purchaseBtn = new Button("Purchase");
        purchaseBtn.setLayoutX(350);
        purchaseBtn.setLayoutY(200);
        purchaseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // This will change the stage to the purchase menu/stage, where info is given for upgrades
                purchaseConfirm(primaryStage);
                // Updates the databases to ensure coins are loaded.
                sql.updateTotalCoins();
                // If the total amount of the coins in the database is >= 20 then you're allowed to purchase.
                if (sql.totalAmount >= 20) {
                    // Purchase is made by calling the purchaseUpgrade method in the database class, this will
                    // subtract 20 coins and update the database to 1 entry only.
                    sql.purchaseUpgrade();
                    // Increase the attack of both Shaggy and Squidward as upgrade is applied.
                    SHAGGY.damage += 5;
                    SQUIDWARD.damage += 5;
                }
                // Brings it back to main menu
                mainMenu(primaryStage);

            }
        });


        Group root = new Group();
        root.getChildren().add(purchaseInfo);
        root.getChildren().add(purchaseBtn);
        root.getChildren().add(wallet);
        Scene scene = new Scene(root, 800, 450);
        primaryStage.setScene(scene);
        enableEscape(scene,primaryStage);
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        mainMenu(primaryStage);
    }

    /***************************************************************************
     * * Main Menu * *
     **************************************************************************/
    public void mainMenu(Stage primaryStage) {
        //Display for openning game-------------------------------------------------------------------------------------
        Text txtMenu = new Text("Ultimate Fighter:\nSHAGGY AT 0.01% POWER LEVEL VS SQUIDWARD EDITION");

        txtMenu.setLayoutY(50);
        txtMenu.setTextAlignment(TextAlignment.CENTER);
        txtMenu.setLayoutX(170);
        txtMenu.setFill(Color.DARKRED);
        txtMenu.setFont(new Font("Georgia", 18));

        double xAlignment = 40;

        Button btnStandardGame = new Button("Standard Match");
        btnStandardGame.setLayoutX(xAlignment);
        btnStandardGame.setLayoutY(100);
        btnStandardGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadGame(primaryStage, "Standard Match");
            }
        });

        Button btnTimedGame = new Button("Timed Match");
        btnTimedGame.setLayoutY(150);
        btnTimedGame.setLayoutX(xAlignment);
        btnTimedGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadGame(primaryStage, "Timed Match");
            }
        });

        Button btnSpecial = new Button("0.02% Power Level");
        btnSpecial.setLayoutX(xAlignment);
        btnSpecial.setLayoutY(200);
        btnSpecial.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadGame(primaryStage, "Timed Match");
            }
        });

        Button btnHelp = new Button("Help");
        btnHelp.setLayoutX(xAlignment);
        btnHelp.setLayoutY(250);
        btnHelp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadHelp(primaryStage);
            }
        });

        Button upgradeBtn = new Button("Upgrade");
        upgradeBtn.setLayoutX(xAlignment);
        upgradeBtn.setLayoutY(300);
        upgradeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Switches to purchase stage/menu.
                purchaseConfirm(primaryStage);
            }
        });

        Group root = new Group();
        root.getChildren().add(txtMenu);
        root.getChildren().add(btnStandardGame);
        root.getChildren().add(btnTimedGame);
        root.getChildren().add(btnHelp);
        root.getChildren().add(btnSpecial);
        root.getChildren().add(upgradeBtn);
        Scene scene = new Scene(root, 800, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void enableEscape(Scene scene,Stage stage){
        stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {//key press controls
            switch(keyEvent.getCode()){
                case ESCAPE:
                    this.mainMenu(stage);
            }
        });
    }//allows a particular menu to escape to the main menu

    /***************************************************************************
     * * Help Menu * *
     **************************************************************************/
    public void loadHelp(Stage stage){
        Text txtHelp = new Text("Help:");
        txtHelp.setLayoutY(75);
        txtHelp.setLayoutX(100);
        txtHelp.setFont(Font.font("Georgia", FontWeight.BOLD, 18));

        Text txtInstruction = new Text("Basic Controls Shaggy:\nWAD to move\nC to attack\nS to block\n\n" +
                "Basic Controls Squidward\nIJL to move\nP to attack\nK to block\n\n" +
                "Press escape at any time to return to the main menu");
        txtInstruction.setLayoutX(100);
        txtInstruction.setLayoutY(100);
        Group root = new Group();
        root.getChildren().add(txtHelp);
        root.getChildren().add(txtInstruction);

        Scene scene = new Scene(root,800,450);
        stage.setScene(scene);
        enableEscape(scene,stage);
        stage.show();
    }



    /***************************************************************************
     * * Game Loaded: The Ultimate Fighting Stage * *
     **************************************************************************/
    public void loadGame(Stage primaryStage, String mode) {

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


    /***************************************************************************
     * * Key Handling Functions * *
     **************************************************************************/
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
                break;
            case S:
                SHAGGY.block();
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
            case K:
                SQUIDWARD.block();
                break;

        }
        // System.out.println("Shaggy Position(X):"+SHAGGY.getLayoutX());
        System.out.println("Shaggy Health:" + SHAGGY.getHealth());

    }

    public void keyRelease(KeyCode keycode) {//when keys are released


        switch (keycode) {
            // SHAGGY's controls when finger is lifted from key
            case W:
                SHAGGY.jump();
                break;

            case S:
                //Upon blocking, this key release will set the image for SHAGGY depending on where he faces
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
                // Upon attacking, this key release will set the image for SHAGGY depending on where he faces
                if (SHAGGY.isRunningLeft || !SHAGGY.getFacingRight()) {
                    SHAGGY.standLeft();
                } else {
                    SHAGGY.standRight();
                }
                endCondition();//for high score keeping
                break;

            // SQUIDWARD's controls when finger is lifted from key
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
                //Upon blocking, this key release will set the image for SQUIDWARD depending on where he faces
                if (!SQUIDWARD.getFacingRight()) {
                    SQUIDWARD.standLeft();
                } else {
                    SQUIDWARD.standRight();
                }
                SQUIDWARD.setBlocking(false);
                break;

            // Upon attacking, this key release will set the image for SQUIDWARD depending on where he faces
            case P:
                if (SQUIDWARD.isRunningLeft() || !SQUIDWARD.getFacingRight()) {
                    SQUIDWARD.standLeft();
                } else {
                    SQUIDWARD.standRight();
                }
                endCondition();//for high score keeping
                break;
        }
    }


    /***************************************************************************
     * * Condition Checking/ Continuous Stage Updating * *
     **************************************************************************/
    public void checkConditions(Character player) {//continuously checks conditions on the tick of a timer
        //Checking conditions to move a player along the x axis of the stage--------------------------------------------
        if (player.isRunningRight()) {
            player.setLayoutX(player.getLayoutX() + player.getSpeed());
        }

        if (player.isRunningLeft()) {
            player.setLayoutX(player.getLayoutX() - player.getSpeed());
        }
        //--------------------------------------------------------------------------------------------------------------
        //Checking conditions for falling/ landing, motion across the y axis of the stage-------------------------------
        if (player.getLayoutY() <= ground) {//when the player is falling
            //player.jump();
            player.fall();

            //check to see if player has changed to hit ground
            if (player.getLayoutY() >= ground) {
                player.land();
            }
        }
        //--------------------------------------------------------------------------------------------------------------
        //boundaries of the stage and ensuring player does not exceed them----------------------------------------------
        if (player.getLayoutX() > 750) {
            player.setLayoutX(750);
        }
        if (player.getLayoutX() < 0) {
            player.setLayoutX(0);
        }
        //--------------------------------------------------------------------------------------------------------------
    }


    /***************************************************************************
     * * Database * *
     **************************************************************************/
    public static void main(String[] args) {
        // database instantiation
        Database sql = new Database();

        // connects to db.
        sql.getConnection();
        // creates a table in db, if not already created.
        sql.createTable();
        // can probable placed in end/winning condition method instead.
        sql.updateTotalCoins();
        // Displays the top 5 highscores stored in database
        sql.displayTopFive();
        // Displays the total amount of coins in wallet from database.
        sql.displayTotalCoins();
        // starts program
        launch(args);
    }

    // With an end condition that will calculate score and winner.
    public void endCondition(){
        Database endSQL = new Database();

        if (SHAGGY.health<=0){
            System.out.println("\nShaggy's Score: "+SHAGGY.getScore());
            System.out.println("Squidward's Score: "+SQUIDWARD.getScore()+"\nSQUIDWARD WINS! by "+(SQUIDWARD.getScore()-SHAGGY.getScore()));
            endSQL.insertScore(SQUIDWARD.getScore());
            endSQL.insertCoins(1);
            endSQL.updateTotalCoins();


        } else if (SQUIDWARD.health<=0){
            System.out.println("\nSquidward's Score: "+SQUIDWARD.getScore());
            System.out.println("Shaggy's Score: "+SHAGGY.getScore()+"\nSHAGGY WINS! by "+(SHAGGY.getScore()-SQUIDWARD.getScore()));
            endSQL.insertScore(SHAGGY.getScore());
            endSQL.insertCoins(1);
            endSQL.updateTotalCoins();

        }
    }
}