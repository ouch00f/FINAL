package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
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
        primaryStage.setScene(scene);
        //primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnHiding( event -> {Runtime.getRuntime().exit(0);;} );//Ends all processes of application on stage close
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

        if(SHAGGY.isFalling() && SHAGGY.getLayoutX() == ground){
            SHAGGY.land();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
