import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DuckHunt extends Application{
    double scale;
    double volume;
    static int selectedBackgroundIndex = 0;
    static int selectedCrosshairIndex = 0;
    static Image[] backgroundImages = {
            new Image("assets/background/1.png"),
            new Image("assets/background/2.png"),
            new Image("assets/background/3.png"),
            new Image("assets/background/4.png"),
            new Image("assets/background/5.png"),
            new Image("assets/background/6.png")
    };
    static double widthScene = backgroundImages[0].getWidth();
    static double heightScene = backgroundImages[0].getHeight();

     static Image[] foregroundImages = {
            new Image("assets/foreground/1.png"),
            new Image("assets/foreground/2.png"),
            new Image("assets/foreground/3.png"),
            new Image("assets/foreground/4.png"),
            new Image("assets/foreground/5.png"),
            new Image("assets/foreground/6.png")
    };

     static Image[] crosshairs = {
            new Image("assets/crosshair/1.png"),
            new Image("assets/crosshair/2.png"),
            new Image("assets/crosshair/3.png"),
            new Image("assets/crosshair/4.png"),
            new Image("assets/crosshair/5.png"),
            new Image("assets/crosshair/6.png"),
            new Image("assets/crosshair/7.png")
    };

    static double widthCross = crosshairs[0].getWidth();
    static double heightCross = crosshairs[0].getHeight();
    static Scene titleScene;
    static Scene backgroundScene;
    Musics obj1 = new Musics();

    public DuckHunt(){
        this.scale = 2;
        this.volume = 0.025;
    }

    public double getScale() {
        return scale;
    }
    // we can change the scale  dynamically
    public void setScale(double scale) {
        this.scale = scale;
    }
    public double getVolume() {
        return volume;
    }
    // we can change the volume  dynamically
    public void setVolume(double volume) {
        this.volume = volume;
    }


    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("HUBBM Duck Hunt");
        primaryStage.getIcons().add(new Image("assets/favicon/1.png"));

        // Create title scene
        StackPane titleRoot = new StackPane();
        titleScene = new Scene(titleRoot, widthScene * getScale(), heightScene * getScale(), Color.BLACK);
        FirstScene firstScene = new FirstScene(titleRoot, getScale());
        firstScene.setupTitleScene(titleRoot, getScale());

        // Create background selection scene
        StackPane backgroundRoot = new StackPane();
        backgroundScene = new Scene(backgroundRoot, widthScene * getScale(), heightScene * getScale(), Color.BLACK);
        Background23 background = new Background23(backgroundRoot, getScale());
        background.setupBackgroundScene(backgroundRoot, getScale());

        // create game scene
        StackPane gameRoot = new StackPane();
        Scene gameScene = new Scene(gameRoot, widthScene * getScale(), heightScene * getScale(), Color.BLACK);

        primaryStage.setScene(titleScene);
        primaryStage.show();



        // Handle keyboard events
        titleScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                selectedBackgroundIndex = 0;
                selectedCrosshairIndex = 0;
                Background23.updateBackground();
                Background23.updateCrosshairImage();

                background.setupBackgroundScene(backgroundRoot, getScale());
                primaryStage.setScene(backgroundScene);
            }
            else if (event.getCode() == KeyCode.ESCAPE) {
                obj1.stopTitleMusic();
                primaryStage.close();
            }
        });

        // start the game and stop the title music after playing intro music once
        backgroundScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (primaryStage.getScene() == backgroundScene) {
                    FirstScene.music.stopTitleMusic();

                    obj1.setIntroMusicEndHandler(() -> {
                        obj1.stopIntroMusic();

                        Background23.setupGameScene(gameRoot, gameScene, getScale());
                        primaryStage.setScene(gameScene);

                        Level1 level1Game = new Level1();
                        level1Game.level1(primaryStage);

                        primaryStage.show();

                    });
                    obj1.playIntroMusic(getVolume());
                }

            } else if (event.getCode() == KeyCode.ESCAPE) {
                if (primaryStage.getScene() == backgroundScene) {
                    primaryStage.setScene(titleScene);
                    selectedBackgroundIndex = 0;
                    selectedCrosshairIndex = 0;
                    Background23.updateBackground();
                    Background23.updateCrosshairImage();
                }
                // backgroundImages has 6 elements
                // cross-hair images has 7 elements
            } else if (event.getCode() == KeyCode.LEFT) {
                selectedBackgroundIndex = (selectedBackgroundIndex - 1 + 6) % 6;
                Background23.updateBackground();
            } else if (event.getCode() == KeyCode.RIGHT) {
                selectedBackgroundIndex = (selectedBackgroundIndex + 1) % 6;
                Background23.updateBackground();
            } else if (event.getCode() == KeyCode.UP) {
                selectedCrosshairIndex = (selectedCrosshairIndex + 1) % 7;
                Background23.updateCrosshairImage();
            } else if (event.getCode() == KeyCode.DOWN) {
                selectedCrosshairIndex = (selectedCrosshairIndex - 1 + 7) % 7;
                Background23.updateCrosshairImage();
            }
        });

    }
}