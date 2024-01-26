import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Methods {

    /**
     * Sets up the background image for the game and adds it to the provided Group.
     *
     * @param gameRoot The Group to which the background image will be added.
     */
    static void setupBackground(Group gameRoot) {
        DuckHunt obj = new DuckHunt();
        ImageView backgroundImageView = new ImageView(DuckHunt.backgroundImages[DuckHunt.selectedBackgroundIndex]);
        double w = backgroundImageView.getImage().getWidth();
        double h = backgroundImageView.getImage().getHeight();
        backgroundImageView.setFitWidth(obj.scale * w);
        backgroundImageView.setFitHeight(obj.scale * h);
        gameRoot.getChildren().add(backgroundImageView);
    }

    static void setupGameScene(Stage primaryStage, Group gameRoot) {
        DuckHunt obj = new DuckHunt();
        Scene gameScene = new Scene(gameRoot, DuckHunt.widthScene * obj.getScale(), DuckHunt.heightScene * obj.getScale());
        primaryStage.setScene(gameScene);
    }

    static void displayGameOver(Group gameRoot, Stage primaryStage , double scale, Scene scene, boolean boo) {
        DuckHunt obj = new DuckHunt();
        Musics obj1 = new Musics();

        if(boo) {
            Text text = Background23.createCenteredText("GAME OVER", DuckHunt.heightScene * scale / 2, 10, scale);
            Text text1 = Background23.createCenteredText("Press ENTER to play again", DuckHunt.heightScene * scale / 2 + 10, 10, scale);
            FadeTransition fadeTransition1 = Background23.createBlinkingFadeTransition(text1);
            Text text2 = Background23.createCenteredText("Press ESC to exit", DuckHunt.heightScene * scale / 2 + 20, 10, scale);
            FadeTransition fadeTransition2 = Background23.createBlinkingFadeTransition(text2);

            VBox vbox = new VBox(4 * scale);
            vbox.setAlignment(Pos.CENTER);
            vbox.getChildren().addAll(text, text1, text2);
            vbox.setAlignment(Pos.CENTER);
            vbox.setTranslateX((DuckHunt.widthScene * scale) / 2 - 30 * scale);
            gameRoot.getChildren().add(vbox);

            obj1.playGameOverSound(obj.getVolume());
        }
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                obj1.stopGameCompletedSound();
                obj1.stopLevelCompletedSound();
                obj1.stopGameOverSound();
                obj1.stopDuckFallsSound();

                Level1 level1Game = new Level1();
                level1Game.level1(primaryStage);

            } else if (event.getCode() == KeyCode.ESCAPE) {
                obj1.stopGameCompletedSound();
                obj1.stopLevelCompletedSound();
                obj1.stopGameOverSound();
                obj1.stopDuckFallsSound();
                // Switch back to the background scene
                primaryStage.setScene(DuckHunt.titleScene);
                DuckHunt.selectedBackgroundIndex = 0;
                DuckHunt.selectedCrosshairIndex = 0;
                Background23.updateBackground();
                Background23.updateCrosshairImage();
                FirstScene.music.playTitleMusicIndefinitely(obj.getVolume());
                primaryStage.show();
            }
        });

    }
}
