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

import static java.lang.Math.abs;

public class Level6{
    int ammo = 9;
    int birdsShot = 0;
    ImageView foregroundImageView;
    public final double START_X = 0;
    public final double START_Y = 0;
    DuckHunt obj = new DuckHunt();
    boolean levelFinished = false;
    private Musics obj1;   // Declare obj1 as a member variable

    public void level6(Stage primaryStage) {

        obj1 = new Musics();   // Instantiate obj1
        Group gameRoot = new Group();

        Methods.setupBackground(gameRoot);

        Bird bird1 = new Bird(gameRoot, "assets/duck_black/", obj.getScale(), 1);
        Bird bird2 = new Bird(gameRoot, "assets/duck_black/", obj.getScale(), 1);
        Bird bird3 = new Bird(gameRoot, "assets/duck_black/", obj.getScale(), 1);

        bird1.getImageView().setX(0);
        bird1.getImageView().setY(70*obj.getScale());
        bird1.setMovingUp(false);

        bird2.getImageView().setX(DuckHunt.widthScene * obj.getScale() - bird2.getWidth());
        bird2.getImageView().setY(70*obj.getScale());
        bird2.getImageView().setScaleX(-obj.getScale());
        bird2.setMovingUp(false);

        bird3.getImageView().setX(0);
        bird3.getImageView().setY(70*obj.getScale());
        bird3.setMovingUp(true);

        setupBirdAnimation(bird1);
        setupBirdAnimation(bird2);
        setupBirdAnimation(bird3);

        setupForeground(gameRoot);
        setupTextElements(gameRoot);
        Methods.setupGameScene(primaryStage, gameRoot);

        ImageView bird1ImageView = bird1.getImageView();
        ImageView bird2ImageView = bird2.getImageView();
        ImageView bird3ImageView = bird3.getImageView();

        setupMouseEvents(primaryStage, gameRoot, bird1ImageView, bird2ImageView, bird3ImageView);

        primaryStage.show();
    }


    public void setupBirdAnimation(Bird bird) {
        double END_X = DuckHunt.widthScene * obj.getScale();
        double END_Y = DuckHunt.heightScene * obj.getScale();
        int FRAME_DURATION = 1800;
        int INTERPOLATION_FRAMES = 10;
        double speed = (END_X - START_X) / (double) (bird.getWingFlappingImages().length * (INTERPOLATION_FRAMES + 1))*obj.getScale()/2;
        double speed1 = (END_Y - START_Y) / (double) (bird.getWingFlappingImages().length * (INTERPOLATION_FRAMES + 1))*obj.getScale()/2;

        Timeline birdAnimation = new Timeline(
                new KeyFrame(Duration.millis(FRAME_DURATION / (INTERPOLATION_FRAMES + 1)), event -> {
                    double interpolationStep = 1.0 / (INTERPOLATION_FRAMES + 1);

                    double newX = bird.getImageView().getX();
                    double newY = bird.getImageView().getY();

                    if (bird.isMovingRight()) {
                        newX += speed;
                        // Check if it has exceeded the boundaries
                        if (newX >= END_X - bird.getWidth()) {
                            newX = END_X - bird.getWidth(); // To prevent the bird from going out of the screen momentarily
                            bird.setMovingRight(false);
                        }
                    } else {
                        newX -= speed;
                        // Check if it has exceeded the boundaries
                        if (newX <= START_X) {
                            newX = START_X; // To prevent the bird from going out of the screen momentarily
                            bird.setMovingRight(true);
                        }
                    }

                    if (bird.isMovingUp()) {
                        newY -= speed;
                        // Check if it has exceeded the boundaries
                        if (newY <= START_Y) {
                            newY = START_Y; // To prevent the bird from going out of the screen momentarily
                            bird.setMovingUp(false);
                        }
                    } else {
                        newY += speed;
                        // Check if it has exceeded the boundaries
                        if (newY >= END_Y - bird.getImageView().getImage().getHeight()) {
                            newY = END_Y - bird.getImageView().getImage().getHeight(); // To prevent the bird from going out of the screen momentarily
                            bird.setMovingUp(true);
                        }
                    }

                    bird.getImageView().setX(newX);
                    bird.getImageView().setY(newY);

                    if (!bird.isMovingRight()) {
                        bird.getImageView().setScaleX(-obj.getScale());
                    } else {
                        bird.getImageView().setScaleX(obj.getScale());
                    }
                    if (!bird.isMovingUp()) {
                        bird.getImageView().setScaleY(-obj.getScale());
                    } else {
                        bird.getImageView().setScaleY(obj.getScale());
                    }

                    bird.setCurrentFrame(bird.getCurrentFrame() + 1);
                    if (bird.getCurrentFrame() >= bird.getWingFlappingImages().length) {
                        bird.setCurrentFrame(0);
                    }
                    bird.update();
                })
        );
        birdAnimation.setCycleCount(Animation.INDEFINITE);
        birdAnimation.play();
    }



    public void setupTextElements(Group gameRoot) {
        DuckHunt obj = new DuckHunt();
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

        Text messageText1 = new Text("Level 6/6");
        messageText1.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        messageText1.setFill(Color.YELLOW);
        messageText1.setX(DuckHunt.widthScene * obj.getScale() / 2 - messageText1.getLayoutBounds().getWidth() / 2);
        messageText1.setY(20);
        gameRoot.getChildren().add(messageText1);
    }

    public void setupForeground(Group gameRoot) {
        foregroundImageView = new ImageView(DuckHunt.foregroundImages[DuckHunt.selectedBackgroundIndex]);
        double w = foregroundImageView.getImage().getWidth();
        double h = foregroundImageView.getImage().getHeight();
        foregroundImageView.setFitWidth(obj.getScale() * w);
        foregroundImageView.setFitHeight(obj.getScale() * h);
        gameRoot.getChildren().add(foregroundImageView);
    }


    public void setupMouseEvents(Stage primaryStage, Group gameRoot, ImageView bird1ImageView, ImageView bird2ImageView, ImageView bird3ImageView) {

        Scene gameScene = primaryStage.getScene();


        gameScene.setOnMouseEntered(event -> {
            if (primaryStage.getScene() == gameScene) {

                Image crosshairImage0 = new Image( "assets/crosshair/"+(DuckHunt.selectedCrosshairIndex+1)+".png");
                double widht = obj.getScale() * crosshairImage0.getWidth();
                double height = obj.getScale() * crosshairImage0.getHeight();

                Image crosshairImage = new Image( "assets/crosshair/"+(DuckHunt.selectedCrosshairIndex+1)+".png",
                        widht, height, true, true);

                Cursor crosshairCursor = new ImageCursor(crosshairImage);
                primaryStage.getScene().setCursor(crosshairCursor);
            }
        });
        gameScene.setOnMouseExited(event -> {
            if (primaryStage.getScene() == gameScene) {
                primaryStage.getScene().setCursor(Cursor.DEFAULT);
            }
        });



        gameScene.setOnMouseClicked(event -> {

            // son atış ıskalanmışsa oyun biter
            if (!levelFinished && ammo==1)  {

                ammo--;
                obj1.playGunshotSound(obj.getVolume());
                setupTextElements(gameRoot);
                Methods.displayGameOver(gameRoot, primaryStage , obj.getScale(), gameScene, true);
            }

            else if (ammo > 0 && !levelFinished){

                ammo--;
                obj1.playGunshotSound(obj.getVolume());
                setupTextElements(gameRoot);

                ImageView[] birdImageViews = {bird1ImageView, bird2ImageView, bird3ImageView};

                for (ImageView birdImageView : birdImageViews) {
                    if (birdImageView.getBoundsInParent().contains(event.getX(), event.getY()))  {

                        gameRoot.getChildren().remove(birdImageView);
                        birdsShot++;
                        Image shouttedBird = new Image("assets/duck_black/7.png");
                        ImageView shotBirdImageView = new ImageView(shouttedBird);
                        shotBirdImageView.setX(birdImageView.getX());
                        shotBirdImageView.setY(birdImageView.getY());
                        shotBirdImageView.setScaleX(birdImageView.getScaleX());
                        shotBirdImageView.setScaleY(birdImageView.getScaleY());
                        gameRoot.getChildren().add(gameRoot.getChildren().indexOf(foregroundImageView), shotBirdImageView);

                        // Create the falling animation
                        ImageView fallingBirdImageView = new ImageView("assets/duck_black/8.png");
                        fallingBirdImageView.setX(birdImageView.getX());
                        fallingBirdImageView.setY(birdImageView.getY());
                        fallingBirdImageView.setScaleX(birdImageView.getScaleX());
                        fallingBirdImageView.setScaleY(abs(birdImageView.getScaleY()));

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


                        if (birdsShot == birdImageViews.length) {
                            // All birds have been shot, end the game

                            levelFinished = true;
                            obj1.playGameCompletedSound(obj.getVolume());

                            Text messageText2 = new Text("You have completed the game!");
                            messageText2.setFont(Font.font("Arial", FontWeight.BOLD, 15* obj.getScale()));
                            messageText2.setFill(Color.YELLOW);
                            messageText2.setX(DuckHunt.widthScene * obj.getScale() / 2 - messageText2.getLayoutBounds().getWidth()/2 - 20);
                            messageText2.setY(DuckHunt.heightScene * obj.getScale()/2 - 6*obj.getScale());

                            gameRoot.getChildren().add(messageText2);

                            Text Text = new Text("Press ENTER to play again");
                            Text.setFill(Color.YELLOW);
                            Text.setFont(Font.font(15* obj.getScale()));
                            Text.setX((DuckHunt.widthScene * obj.getScale() - Text.getLayoutBounds().getWidth()) / 2);
                            Text.setY(DuckHunt.heightScene * obj.getScale() / 2 + 7*obj.getScale());

                            Text Text1 = new Text("Press ESC to exit");
                            Text1.setFill(Color.YELLOW);
                            Text1.setFont(Font.font(15* obj.getScale()));
                            Text1.setX((DuckHunt.widthScene * obj.getScale() - Text.getLayoutBounds().getWidth()) / 2 + 8*obj.getScale());
                            Text1.setY(DuckHunt.heightScene * obj.getScale() / 2 + 19*obj.getScale());

                            gameRoot.getChildren().addAll(Text, Text1);

                            FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), Text);
                            fadeTransition.setFromValue(1.0);
                            fadeTransition.setToValue(0.0);
                            fadeTransition.setCycleCount(Animation.INDEFINITE);
                            fadeTransition.setAutoReverse(true);
                            fadeTransition.play();

                            FadeTransition fadeTransition1 = new FadeTransition(Duration.millis(1000), Text1);
                            fadeTransition1.setFromValue(1.0);
                            fadeTransition1.setToValue(0.0);
                            fadeTransition1.setCycleCount(Animation.INDEFINITE);
                            fadeTransition1.setAutoReverse(true);
                            fadeTransition1.play();

                            Methods.displayGameOver(gameRoot, primaryStage, obj.getScale(), gameScene, false);
                            break; // Exit the loop since we found the bird that was clicked
                        }
                    }
                }
            }
        });
    }
}