import javafx.animation.*;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Level1{
    int ammo = 3;
    ImageView foregroundImageView;
    DuckHunt obj = new DuckHunt();
    boolean levelFinished = false;
    private Musics obj1;   // Declare obj1 as a member variable

    /**
     * Sets up and displays level 1 of the game.
     *
     * @param primaryStage The primary stage of the JavaFX application.
     */
    public void level1(Stage primaryStage) {

        obj1 = new Musics();   // Instantiate obj1
        Group gameRoot = new Group();

        Methods.setupBackground(gameRoot);

        Bird bird = new Bird(gameRoot, "assets/duck_black/", obj.getScale(), 4);
        bird.getImageView().setX(0);
        bird.getImageView().setY(70*obj.getScale());

        setupBirdAnimation(bird);

        setupForeground(gameRoot);
        setupTextElements(gameRoot);
        Methods.setupGameScene(primaryStage, gameRoot);
        setupMouseEvents(primaryStage, gameRoot, bird);

        primaryStage.show();
    }



    /**
     * Sets up the foreground image for the game and adds it to the provided Group.
     *
     * @param gameRoot The Group to which the foreground image will be added.
     */
    public void setupForeground(Group gameRoot) {
        foregroundImageView = new ImageView(DuckHunt.foregroundImages[DuckHunt.selectedBackgroundIndex]);
        double w = foregroundImageView.getImage().getWidth();
        double h = foregroundImageView.getImage().getHeight();
        foregroundImageView.setFitWidth(obj.getScale() * w);
        foregroundImageView.setFitHeight(obj.getScale() * h);
        gameRoot.getChildren().add(foregroundImageView);
    }

    /**
     * Sets up the animation for the specified Bird object.
     *
     * @param bird The Bird object for which the animation will be set up.
     */
    public void setupBirdAnimation(Bird bird) {
        // Define the start and end positions for the bird's animation
        double END_X = DuckHunt.widthScene * obj.getScale();
        double START_X = 0;
        // Define the duration of each frame in the bird's animation
        int FRAME_DURATION = 1800;
        // Define the number of frames used for interpolation
        int INTERPOLATION_FRAMES = 10;
        // Calculate the speed
        double speed = (END_X - START_X) / (double) (bird.getWingFlappingImages().length * (INTERPOLATION_FRAMES + 1))*3 / obj.getScale();

        // Create a Timeline for the bird's animation
        Timeline birdAnimation = new Timeline(
                new KeyFrame(Duration.millis(FRAME_DURATION / (INTERPOLATION_FRAMES + 1)), event -> {
                    double interpolationStep = 1.0 / (INTERPOLATION_FRAMES + 1);

                    // Update the current frame and scale of the bird's ImageView
                    if (bird.getImageView().getScaleX() > 0) {
                        bird.setCurrentFrame(bird.getCurrentFrame() + 1);
                        if (bird.getCurrentFrame() >= bird.getWingFlappingImages().length) {
                            bird.setCurrentFrame(0);
                        }
                        bird.getImageView().setScaleX(obj.getScale());
                    } else {
                        bird.setCurrentFrame(bird.getCurrentFrame() - 1);
                        if (bird.getCurrentFrame() < 0) {
                            bird.setCurrentFrame(bird.getWingFlappingImages().length - 1);
                        }
                        bird.getImageView().setScaleX(-obj.getScale());
                    }

                    // Update the bird's position and check if it exceeds the boundaries
                    bird.update();

                    double newX = bird.getImageView().getX();
                    if (bird.getImageView().getScaleX() > 0) {
                        newX += (END_X - START_X) / speed;
                        // Check if it has exceeded the boundaries
                        if (newX >= END_X - bird.getWidth()) {
                            newX = END_X - bird.getWidth(); // To prevent the bird from going out of the screen momentarily
                            bird.getImageView().setScaleX(-obj.getScale());
                        }
                    } else {
                        // Check if it has exceeded the boundaries
                        newX -= (END_X - START_X) / speed;
                        if (newX <= START_X) {
                            newX = START_X; // To prevent the bird from going out of the screen momentarily
                            bird.getImageView().setScaleX(obj.getScale());
                        }
                    }
                    // Set the new position of the bird's ImageView
                    bird.getImageView().setX(newX);
                })
        );
        // Set the cycle count of the bird's animation to indefinite and start it
        birdAnimation.setCycleCount(Animation.INDEFINITE);
        birdAnimation.play();
    }




    public void setupTextElements(Group gameRoot) {
        // Remove the existing ammo text element
        gameRoot.getChildren().removeIf(node -> node instanceof Text && ((Text) node).getText().startsWith("Ammo Left"));

        // Create the new ammo text element
        Text messageText = new Text();
        messageText.setText("Ammo Left " + ammo);
        messageText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        messageText.setFill(Color.YELLOW);
        messageText.setX(DuckHunt.widthScene * obj.getScale() - messageText.getLayoutBounds().getWidth() - 10);
        messageText.setY(20);

        // Add the new ammo text element
        gameRoot.getChildren().add(messageText);

        Text messageText1 = new Text("Level 1/6");
        messageText1.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        messageText1.setFill(Color.YELLOW);
        messageText1.setX(DuckHunt.widthScene * obj.getScale() / 2 - messageText1.getLayoutBounds().getWidth() / 2);
        messageText1.setY(20);
        gameRoot.getChildren().add(messageText1);
    }

    public void handleKeyEvents(Scene scene, Stage primaryStage) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && levelFinished) {
                obj1.stopDuckFallsSound();
                obj1.stopLevelCompletedSound();
                obj1.stopGameOverSound();
                obj1.stopDuckFallsSound();

                // Switch to the second level
                Level2 level2Game = new Level2();
                level2Game.level2(primaryStage);
            }
        });
    }

    /**
     * Sets up the mouse events for the game.
     *
     * @param primaryStage The primary stage of the JavaFX application.
     * @param gameRoot The root Group of the game scene.
     * @param bird The Bird object in the game.
     */
    public void setupMouseEvents(Stage primaryStage, Group gameRoot, Bird bird) {

        Scene gameScene = primaryStage.getScene();

        // Event handler for mouse enter event
        gameScene.setOnMouseEntered(event -> {
            if (primaryStage.getScene() == gameScene) {
                // Set custom crosshair cursor when the mouse enters the game scene
                Image crosshairImage0 = new Image( "assets/crosshair/"+(DuckHunt.selectedCrosshairIndex+1)+".png");
                double width = obj.getScale() * crosshairImage0.getWidth();
                double height = obj.getScale() * crosshairImage0.getHeight();

                Image crosshairImage = new Image( "assets/crosshair/"+(DuckHunt.selectedCrosshairIndex+1)+".png",
                        width, height, true, true);

                Cursor crosshairCursor = new ImageCursor(crosshairImage);
                primaryStage.getScene().setCursor(crosshairCursor);
            }
        });
        gameScene.setOnMouseExited(event -> {
            // Set default cursor when the mouse exits the game scene
            if (primaryStage.getScene() == gameScene) {
                primaryStage.getScene().setCursor(Cursor.DEFAULT);
            }
        });



        gameScene.setOnMouseClicked(event -> {

            // game over if the last shot is missed
            if (!levelFinished && ammo==1 && !bird.getImageView().getBoundsInParent().contains(event.getX(), event.getY()))  {

                obj1.playGunshotSound(obj.getVolume());
                ammo--;
                setupTextElements(gameRoot);
                Methods.displayGameOver(gameRoot, primaryStage, obj.getScale(), gameScene, true);
            }

            // If there is ammo left and bird is shot, game over
            else if (ammo > 0 && !levelFinished){

                obj1.playGunshotSound(obj.getVolume());
                ammo--;
                setupTextElements(gameRoot);

                if (bird.getImageView().getBoundsInParent().contains(event.getX(), event.getY()))  {

                    gameRoot.getChildren().remove(bird.getImageView());
                    levelFinished = true;
                    obj1.playLevelCompletedSound(obj.getVolume());

                    Text messageText2 = new Text("YOU WIN!");
                    messageText2.setFont(Font.font("Arial", FontWeight.BOLD, 15*obj.getScale()));
                    messageText2.setFill(Color.YELLOW);
                    messageText2.setX(DuckHunt.widthScene * obj.getScale() / 2 - messageText2.getLayoutBounds().getWidth()/2 - 20);
                    messageText2.setY(DuckHunt.heightScene * obj.getScale()/2 - 6*obj.getScale());

                    gameRoot.getChildren().add(messageText2);

                    Text Text = new Text("Press ENTER to play next level");
                    Text.setFill(Color.YELLOW);
                    Text.setFont(Font.font(15*obj.getScale()));
                    Text.setX((DuckHunt.widthScene * obj.getScale() - Text.getLayoutBounds().getWidth()) / 2);
                    Text.setY(DuckHunt.heightScene * obj.getScale() / 2 + 6*obj.getScale());

                    FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), Text);
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0.0);
                    fadeTransition.setCycleCount(Animation.INDEFINITE);
                    fadeTransition.setAutoReverse(true);
                    fadeTransition.play();


                    gameRoot.getChildren().add(Text);


                    Image shouttedBird = new Image("assets/duck_black/7.png");
                    ImageView shotBirdImageView = new ImageView(shouttedBird);
                    shotBirdImageView.setX(bird.getImageView().getX());
                    shotBirdImageView.setY(bird.getImageView().getY());
                    shotBirdImageView.setScaleX(bird.getImageView().getScaleX());
                    shotBirdImageView.setScaleY(bird.getImageView().getScaleY());
                    gameRoot.getChildren().add(gameRoot.getChildren().indexOf(foregroundImageView), shotBirdImageView); // Add the shot bird image

                    // Create the falling animation
                    ImageView fallingBirdImageView = new ImageView("assets/duck_black/8.png");
                    fallingBirdImageView.setX(bird.getImageView().getX());
                    fallingBirdImageView.setY(bird.getImageView().getY());
                    fallingBirdImageView.setScaleX(bird.getImageView().getScaleX());
                    fallingBirdImageView.setScaleY(bird.getImageView().getScaleY());

                    double targetY = DuckHunt.heightScene * obj.getScale();  // Y-coordinate where the bird will fall

                    // Animation to make the bird fall
                    TranslateTransition fallAnimation = new TranslateTransition(Duration.seconds(2), fallingBirdImageView);
                    fallAnimation.setToY(targetY);
                    fallAnimation.setOnFinished(animationEvent -> {
                        // Remove the falling bird from the scene when the animation is finished
                        gameRoot.getChildren().remove(fallingBirdImageView);
                    });

                    PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
                    pauseTransition.setOnFinished(pauseEvent -> {
                        // Remove the shot bird image right before the falling animation starts
                        gameRoot.getChildren().remove(shotBirdImageView);
                        gameRoot.getChildren().add(gameRoot.getChildren().indexOf(foregroundImageView), fallingBirdImageView);
                        obj1.playDuckFallsSound(obj.getVolume());
                        fallAnimation.play();
                    });

                    pauseTransition.play();

                    handleKeyEvents(gameScene, primaryStage);

                }
            }
        });
    }
}