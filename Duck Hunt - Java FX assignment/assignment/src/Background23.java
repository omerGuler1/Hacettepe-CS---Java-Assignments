import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Background23 {
    private static Rectangle backgroundRect;
    private static Rectangle crosshairRect;
    private StackPane root;
    private double scale;
    public Background23(StackPane root, double scale){
        this.root = root;
        this.scale = scale;
    }

    /**
     * Sets up the background scene with the specified scale and adds it to the provided StackPane.
     *
     * @param root The StackPane to which the background scene will be added.
     * @param scale The scale is to set background scene's size.
     */
    public void setupBackgroundScene(StackPane root, double scale) {

        backgroundRect = new Rectangle(DuckHunt.widthScene * scale, DuckHunt.heightScene * scale);
        backgroundRect.setFill(new ImagePattern(DuckHunt.backgroundImages[DuckHunt.selectedBackgroundIndex]));
        root.getChildren().add(backgroundRect);

        VBox vbox = new VBox(5*scale);
        vbox.setAlignment(Pos.TOP_CENTER);

        Text instructionsText1 = createCenteredText("USE ARROW KEYS TO NAVIGATE", 3*scale, 8, scale);
        Text instructionsText2 = createCenteredText("PRESS ENTER TO START", 6*scale, 8, scale);
        Text instructionsText3 = createCenteredText("PRESS ESC TO EXIT", 10*scale, 8, scale);

        vbox.getChildren().addAll(instructionsText1, instructionsText2, instructionsText3);

        root.getChildren().add(vbox);

        crosshairRect = new Rectangle(DuckHunt.widthCross * scale, DuckHunt.heightCross * scale);
        crosshairRect.setFill(new ImagePattern(DuckHunt.crosshairs[DuckHunt.selectedCrosshairIndex]));
        root.getChildren().add(crosshairRect);
    }


    /**
     * Sets up the game scene with the scale and adds it to StackPane and Scene.
     *
     * @param root The StackPane to which the game scene will be added.
     * @param gameScene The Scene to which the root StackPane will be set.
     * @param scale The scale factor to be applied to the game scene.
     */
    static void setupGameScene(StackPane root, Scene gameScene, double scale) {

        backgroundRect = new Rectangle(DuckHunt.widthScene * scale, DuckHunt.heightScene * scale);
        backgroundRect.setFill(new ImagePattern(DuckHunt.backgroundImages[DuckHunt.selectedBackgroundIndex]));
        root.getChildren().add(backgroundRect);

        Image faviconImage = new Image("assets/favicon/1.png");
        ImagePattern backgroundPattern = new ImagePattern(faviconImage);
        BackgroundFill backgroundFill = new BackgroundFill(backgroundPattern, CornerRadii.EMPTY, null);
        Background background = new Background(backgroundFill);
        root.setBackground(background);
        gameScene.setRoot(root);
    }


    /**
     * Creates a Text object with the specified properties and returns it.
     *
     * @param text The text content of the Text object.
     * @param verticalOffset The vertical offset of the Text object.
     * @param size The font size of the Text object.
     * @param scale The scale factor to be applied to the Text object.
     * @return The Text object with the specified properties.
     */
    static Text createCenteredText(String text, double verticalOffset, int size, double scale) {
        Text centeredText = new Text(text);
        centeredText.setFont(Font.font("Arial", FontWeight.BOLD, size));
        centeredText.setFill(Color.YELLOW);
        centeredText.setTranslateY(verticalOffset);
        centeredText.setScaleX(scale);
        centeredText.setScaleY(scale);
        return centeredText;
    }

    /**
     * Creates a FadeTransition object that makes the specified Text object blink by fading in and out repeatedly.
     *
     * @param text The Text object to apply the blinking fade transition to.
     * @return The FadeTransition object that makes the Text object blink.
     */
    static FadeTransition createBlinkingFadeTransition(Text text) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), text);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
        return fadeTransition;
    }

    // updates background in background selection screen
    static void updateBackground() {
        backgroundRect.setFill(new ImagePattern(DuckHunt.backgroundImages[DuckHunt.selectedBackgroundIndex]));
    }
    // updates crosshair in background selection screen
    static void updateCrosshairImage() {
        crosshairRect.setFill(new ImagePattern(DuckHunt.crosshairs[DuckHunt.selectedCrosshairIndex]));
    }
}
