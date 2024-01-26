import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;

public class FirstScene {
    private StackPane root;
    private double scale;
    static Musics music = new Musics();
    DuckHunt obj = new DuckHunt();

    public FirstScene(StackPane root, double scale) {
        this.root = root;
        this.scale = scale;
    }

    /**
     * Sets up the title scene with the specified scale and adds it to the provided StackPane.
     *
     * @param root The StackPane to which the title scene will be added.
     * @param scale The scale factor to be applied to the title scene.
     */
    public void setupTitleScene(StackPane root, double scale) {

        music.playTitleMusicIndefinitely(obj.getVolume());

        Image backgroundTitle = new Image("assets/welcome/1.png");
        ImagePattern backgroundPattern = new ImagePattern(backgroundTitle);
        BackgroundFill backgroundFill = new BackgroundFill(backgroundPattern, CornerRadii.EMPTY, null);
        Background background = new Background(backgroundFill);
        root.setBackground(background);

        Text titleText = Background23.createCenteredText("HUBBM Duck Hunt", 0, 12, scale);
        StackPane.setAlignment(titleText, Pos.CENTER);

        Text instructionsText1 = Background23.createCenteredText("PRESS ENTER TO PLAY", 30*scale,15 , scale);
        Text instructionsText2 = Background23.createCenteredText("PRESS ESC TO EXIT", 35*scale, 15 , scale);

        FadeTransition fadeTransition1 = Background23.createBlinkingFadeTransition(instructionsText1);
        FadeTransition fadeTransition2 = Background23.createBlinkingFadeTransition(instructionsText2);

        // Create a VBox to hold the instruction texts and center it within the StackPane
        VBox vbox = new VBox(5*scale);
        vbox.getChildren().addAll(instructionsText1, instructionsText2);
        vbox.setAlignment(Pos.CENTER);

        root.getChildren().add(vbox);
    }
}
