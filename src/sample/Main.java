/* Ultimate Fighter: Shaggy at 0.05% Power Level VS Squidward
Class Main: Bulk of program used for loading forms, and calculating game mechanics.
Authors: Sandy Le, Devon Gulley.
Last updated: April 5th, 2019.*/


package sample;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;

public class Main extends Application {

    // Character/object instantiation
    int ground = 200;
    Squidward SQUIDWARD = new Squidward(675, ground);
    Shaggy SHAGGY = new Shaggy(25, ground);
    Timer timer;


    public Main() throws IOException {

    }

    // This stage is called upon whenever purchase button is activated.
    // This stage will contain upgrade information along with the current coin balance in the database
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

        // Instantiation and settings for the back to main menu button if user decides not to
        // purchase the upgrade, it will take you back to main menu on activation.
        Button backBtn = new Button("Back to Main Menu");
        backBtn.setLayoutX(20);
        backBtn.setLayoutY(390);
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainMenu(primaryStage);
            }
        });



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
        root.getChildren().add(backBtn);
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
    // This is the main menu stage, it will at as the main homepage for our game.
    // This is the core index of Shaggy vs Squidward and will contain various types of options.
    public void mainMenu(Stage primaryStage) {
        //Display for openning game-------------------------------------------------------------------------------------

        // Header for the main menu.
        Text txtMenu = new Text("Ultimate Fighter:\nSHAGGY AT 0.05% POWER LEVEL VS SQUIDWARD EDITION");
        txtMenu.setLayoutY(50);
        txtMenu.setTextAlignment(TextAlignment.CENTER);
        txtMenu.setLayoutX(170);
        txtMenu.setFill(Color.DARKRED);
        txtMenu.setFont(new Font("Georgia", 18));

        // variable to keep main menu buttons alinged.
        double xAlignment = 40;

        // Instantiation and field of the standard fight option button
        Button btnStandardGame = new Button("Standard Match");
        btnStandardGame.setLayoutX(xAlignment);
        btnStandardGame.setLayoutY(100);
        btnStandardGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadGame(primaryStage, "Standard Match");
            }
        });

        // Instantiation and field for the time based match.
        Button btnTimedGame = new Button("Timed Match");
        btnTimedGame.setLayoutY(150);
        btnTimedGame.setLayoutX(xAlignment);
        btnTimedGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadGame(primaryStage, "Timed Match");
                PauseTransition delay = new PauseTransition(Duration.seconds(3));
                delay.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        mainMenu(primaryStage);
                    }
                });
            }
        });

        // Instantiation and field of the game mode shaggy @ 0.10% Power Level, by increasing
        // shaggy's damage x2.
        Button btnSpecial = new Button("Shaggy @ 0.10% Power Level");
        btnSpecial.setLayoutX(xAlignment);
        btnSpecial.setLayoutY(200);
        btnSpecial.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SHAGGY.damage *= 2;
                loadGame(primaryStage, "Timed Match");
            }
        });

        // Instantiation and field of the help button which will change scene and provide
        // instructions on how to play the game.
        Button btnHelp = new Button("Help");
        btnHelp.setLayoutX(xAlignment);
        btnHelp.setLayoutY(250);
        btnHelp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadHelp(primaryStage);
            }
        });

        // Instantiation and field of the upgrade button. When activated it will open another stage
        // that will have options for upgrading your damage.
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

        // Adding the buttons,labels and text created for the main menu.
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

    // This method will be used to allow the key press of 'esc', it will be a shortcut to go back
    // to the menu mid-fight.
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
    // This stage loads the tutorial/instructions stage that will have information on how to
    // play the game.
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

        Pane root = new Pane();
        primaryStage.setTitle("Shaggy VS Squidward");
        Scene scene = new Scene(root, 800, 450);

        String url = "background.png" ;
        Image img = new Image(url);
        BackgroundImage bgImg = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
        root.setBackground(new Background(bgImg));



        primaryStage.setResizable(false);

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnHiding(event -> {
            Runtime.getRuntime().exit(0);
        });//Ends all processes of application on stage close


        // Adding object characters to scene.
        root.getChildren().add(SHAGGY);
        root.getChildren().add(SQUIDWARD);


        enableEscape(scene,primaryStage);

        //Key event filters
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {//key press controls
            this.keyPress(keyEvent.getCode());
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {//key press controls
            this.keyRelease(keyEvent.getCode(),primaryStage);
        });

        //All timing and motion tools
        timer = new Timer(45, e -> {
            checkConditions(SHAGGY);
            checkConditions(SQUIDWARD);
        });
        if(!timer.isRunning()) {
            timer.start();
        }
        timer.setDelay(45);

    }

    /***************************************************************************
     * * Winning Screen * *
     **************************************************************************/
    // A winning screen that will prompt whenever a user wins, it will display the top score
    // along with a image of the winner.
    public void loadWinningScreen(Character winner, Stage primaryStage){
        Database db = new Database();

        // Instantiation and field of title.
        Text txtCongratulations = new Text("WHAT A GAME, BEHOLD THE GLORIOUS WINNER!");
        txtCongratulations.setLayoutY(50);
        txtCongratulations.setLayoutX(25);
        txtCongratulations.setFont(Font.font("Georgia", FontWeight.BOLD, 13));

        // Instantiation and field of a button that will allow to enter the main menu.
        Button backBtn = new Button("Back to main menu");
        backBtn.setLayoutX(20);
        backBtn.setLayoutY(350);

        // Instantiation and field of a text to highlight highscore.
        Text highScore = new Text("Highscore:");
        highScore.setLayoutY(80);
        highScore.setLayoutX(100);
        highScore.setFont(Font.font("Georgia", FontWeight.BOLD, 15));


        // Instantiation and field of test for highscore printing from the database.
        Text txtHighScore = new Text(db.displayRank1());
        txtHighScore.setLayoutY(100);
        txtHighScore.setLayoutX(100);
        txtHighScore.setFont(Font.font("Georgia", FontWeight.BOLD, 15));

        // // Instantiation and field of root of the winning screen
        Group root = new Group();
        root.getChildren().add(winner); //teleports the winner to this new screen
        root.getChildren().add(txtCongratulations);
        root.getChildren().add(txtHighScore);
        root.getChildren().add(backBtn);
        root.getChildren().add(highScore);


        Scene scene = new Scene(root,400,400);
        Stage stage = new Stage();

        stage.setOnHiding(event -> {//restarts the programs when the stage of this popup is hidden
                    this.mainMenu(stage);
                });

        stage.setScene(scene);
        winner.setLayoutY(200);
        winner.setLayoutX(200);
        stage.show();
        backBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainMenu(primaryStage);
                stage.close();
            }
        });
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

    public void keyRelease(KeyCode keycode, Stage primaryStage) {//when keys are released


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
                SHAGGY.setBlocking(false); //SHAGGY is no longer blocking upon release of the block button
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
                endCondition(primaryStage);//for high score keeping
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
                SQUIDWARD.setBlocking(false);//SQUIDWARD is no longer blocking upon release of the block button
                break;

            // Upon attacking, this key release will set the image for SQUIDWARD depending on where he faces
            case P:
                if (SQUIDWARD.isRunningLeft() || !SQUIDWARD.getFacingRight()) {
                    SQUIDWARD.standLeft();
                } else {
                    SQUIDWARD.standRight();
                }
                endCondition(primaryStage);//for high score keeping
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
    public void endCondition(Stage primaryStage){
        Database endSQL = new Database();

        if (SHAGGY.getHealth()<=0){ //Events when SQUIDWARD wins the game
            System.out.println("\nShaggy's Score: "+SHAGGY.getScore());
            System.out.println("Squidward's Score: "+SQUIDWARD.getScore()+"\nSQUIDWARD WINS! by "+(SQUIDWARD.getScore()-SHAGGY.getScore()));
            endSQL.insertScore(SQUIDWARD.getScore());
            endSQL.insertCoins(1);
            endSQL.updateTotalCoins();
            loadWinningScreen(SQUIDWARD,primaryStage);
            // This will reload the health allowing for a refresh when playing again, resetting health.
            SHAGGY.setHealth(100);
            SQUIDWARD.setHealth(100);

        } else if (SQUIDWARD.getHealth()<=0){//Events when SHAGGY wins the game
            System.out.println("\nSquidward's Score: "+SQUIDWARD.getScore());
            System.out.println("Shaggy's Score: "+SHAGGY.getScore()+"\nSHAGGY WINS! by "+(SHAGGY.getScore()-SQUIDWARD.getScore()));
            endSQL.insertScore(SHAGGY.getScore());
            endSQL.insertCoins(1);
            endSQL.updateTotalCoins();
            loadWinningScreen(SHAGGY, primaryStage);
            // This will reload the health allowing for a refresh when playing again, resetting health.
            SHAGGY.setHealth(100);
            SQUIDWARD.setHealth(100);
        }
    }
}