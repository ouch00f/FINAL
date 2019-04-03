package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {
    int ground = 300;
    Squidward SQUIDWARD = new Squidward(1000,ground);
    public Shaggy SHAGGY = new Shaggy(25,ground);
    KeyCode previousKeycode;
    Timer clock = new Timer();

    public Main() throws IOException {

    }
    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();

        primaryStage.setTitle("");
        Scene scene = new Scene(root, 800, 450);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        //primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnHiding( event -> {Runtime.getRuntime().exit(0);} );//Ends all processes of application on stage close
        root.getChildren().add(SHAGGY);



        //Key event filters
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {//key press controls
            this.keyPress(keyEvent.getCode());
        });

        scene.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {//key press controls
            this.keyRelease(keyEvent.getCode());
        });


        //All timing and motion tools
        clock = new Timer();//for tracking motion and conditions on the stage continuously
        TimerTask stageTick = new TimerTask(){
            @Override
            public void run() {//all motion and conditions continuously checked here
                checkConditions();
            }
        };
        clock.scheduleAtFixedRate(stageTick,1000,10);



    }

    public void keyPress(KeyCode keycode){//when keys are pressed

        switch(keycode){
            case W:
                SHAGGY.jump();
                break;
            case D:
                SHAGGY.runRight();
                break;
            case A:
                SHAGGY.runLeft();
                break;
        }
        // System.out.println(SHAGGY.getLayoutX());

    }

    public void keyRelease(KeyCode keycode){//when keys are released

        switch (keycode){
            case D:
                SHAGGY.standRight();
                break;
            case A:
                SHAGGY.standLeft();
                break;
            case W:
                SHAGGY.setFalling(true);
                break;
        }
    }

    public void checkConditions(){//continuously checks conditions on the tick of a timer

        if(SHAGGY.isRunningRight()){
            SHAGGY.setLayoutX(SHAGGY.getLayoutX()+SHAGGY.getSpeed());
        }

        if(SHAGGY.isRunningLeft()){
            SHAGGY.setLayoutX(SHAGGY.getLayoutX()-SHAGGY.getSpeed());
        }

        if(SHAGGY.isFalling() && SHAGGY.getLayoutY() == ground){
            SHAGGY.land();
        }
    }

    public static void main(String[] args) {
        launch(args);
        connect();
        createTable();
        insert('5');
    }

    // final fields used for sql string syntax
    private static final String insertSQL = "INSERT INTO HighScores(dmgDealt) VALUES(?)";
    //private static final int newScore = 0;
    private static final String url = "jdbc:sqlite:/Users/rain/Desktop/FINAL/master.db";

    // Connection method, perhaps replaced with built in database tool(?)
    private static void connect() {
        Connection conn = null;
        try {

            //String url = "jdbc:sqlite:/Users/rain/Desktop/Squidward_VS_Shaggy/master.db";;

            conn = DriverManager.getConnection(url);

            System.out.println("Connection established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Creates a new table in the db
    private static void createTable(){
        String createT = "CREATE TABLE IF NOT EXISTS HighScores(dmgDealt int)";
        try (Connection conn = DriverManager.getConnection(url);
             Statement statement = conn.createStatement()) {
            statement.execute(createT);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    // insert method that currently does not work
    private static void insert(int score){
        try{
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement statement = conn.prepareStatement(insertSQL);

            // tasting for parameter index, unsure what it is.
            statement.setInt('a', score);

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private void newScore(int oldScore, int newScore){
        if (newScore > oldScore) {
            insert(newScore);
        }
    }

    // Suppose to select all row in the HighScore DB & print them out.
    private void selectAll(){
        String sql = "SELECT dmgDealt FROM HighScores";

        // not sure if SQL syntax is correct
        String minToMax = "SELECT MAX(dmgDealt) AS max FROM HighScores";

        // Might have conn = this.connect(); (?)**
        try(Connection conn = DriverManager.getConnection(url);
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql)){

            while (rs.next()){
                System.out.println(rs.getInt("dmgDealt"));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}