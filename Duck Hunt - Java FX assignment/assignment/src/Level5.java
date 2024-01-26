import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import static java.lang.Math.abs;

public class Level5{
    int ammo = 9;
    int birdsShot = 0;
    ImageView foregroundImageView;
    DuckHunt obj = new DuckHunt();
    boolean levelFinished = false;
    private Musics obj1;   // Declare obj1 as a member variable

    public void level5(Stage primaryStage) {

        obj1 = new Musics();   // Instantiate obj1
        Group gameRoot = new Group();

        Methods.setupBackground(gameRoot);

        Bird bird1 = new Bird(gameRoot, "assets/duck_blue/", obj.getScale(), 4);
        Bird bird2 = new Bird(gameRoot, "assets/duck_blue/", obj.getScale(), 4);
        Bird bird3 = new Bird(gameRoot, "assets/duck_blue/", obj.getScale(), 1);

        bird1.getImageView().setX(0);
        bird1.getImageView().setY(100*obj.getScale());

        bird2.getImageView().setX(DuckHunt.widthScene * obj.getScale() - bird2.getWidth());
        bird2.getImageView().setY(70*obj.getScale());

        bird3.getImageView().setX(0);
        bird3.getImageView().setY(70*obj.getScale());
        bird3.setDiagonal(true);

        ImageView birdImageView1 = bird1.getImageView();
        ImageView birdImageView2 = bird2.getImageView();
        ImageView birdImageView3 = bird3.getImageView();

        setupBirdAnimation(bird1);
        setupBirdAnimation(bird2);
        setupBirdAnimation(bird3);

        setupForeground(gameRoot);
        setupTextElements(gameRoot);
        Methods.setupGameScene(primaryStage, gameRoot);
        setupMouseEvents(primaryStage, gameRoot, birdImageView1, birdImageView2, birdImageView3);

        primaryStage.show();
    }



    public void setupForeground(Group gameRoot) {
        foregroundImageView = new ImageView(DuckHunt.foregroundImages[DuckHunt.selectedBackgroundIndex]);
        double w = foregroundImageView.getImage().getWidth();
        double h = foregroundImageView.getImage().getHeight();
        foregroundImageView.setFitWidth(obj.getScale() * w);
        foregroundImageView.setFitHeight(obj.getScale() * h);
        gameRoot.getChildren().add(foregroundImageView);
    }




    public void setupBirdAnimation(Bird bird) {
        double END_X = DuckHunt.widthScene * obj.getScale();
        double START_X = 0;
        double END_Y = DuckHunt.heightScene * obj.getScale();
        double START_Y = 0;
        int FRAME_DURATION = 1800;
        int INTERPOLATION_FRAMES = 10;
        double speed = (END_X - START_X) / (double) (bird.getWingFlappingImages().length * (INTERPOLATION_FRAMES + 1))*3 / obj.getScale();
        double speed1 = (END_Y - START_Y) / (double) (bird.getWingFlappingImages().length * (INTERPOLATION_FRAMES + 1))*3 / obj.getScale();

        Timeline birdAnimation = new Timeline(
                new KeyFrame(Duration.millis(FRAME_DURATION / (INTERPOLATION_FRAMES + 1)), event -> {
                    double interpolationStep = 1.0 / (INTERPOLATION_FRAMES + 1);

                    double newX = bird.getImageView().getX();
                    double newY = bird.getImageView().getY();

                    if (bird.isDiagonal()) {
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
                            newY -= speed1;
                            // Check if it has exceeded the boundaries
                            if (newY <= START_Y) {
                                newY = START_Y; // To prevent the bird from going out of the screen momentarily
                                bird.setMovingUp(false);
                            }
                        } else {
                            newY += speed1;
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
                    }

                    else {
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

                        bird.update();

                        double new1X = bird.getImageView().getX();

                        if (bird.getImageView().getScaleX() > 0) {
                            new1X += (END_X - START_X) / (double) (bird.getWingFlappingImages().length * (INTERPOLATION_FRAMES + 1));
                            // Check if it has exceeded the boundaries
                            if (new1X >= END_X - bird.getWidth()) {
                                new1X = END_X - bird.getWidth(); // To prevent the bird from going out of the screen momentarily
                                bird.getImageView().setScaleX(-obj.getScale());
                            }
                        } else {
                            // Check if it has exceeded the boundaries
                            new1X -= (END_X - START_X) / (double) (bird.getWingFlappingImages().length * (INTERPOLATION_FRAMES + 1));
                            if (new1X <= START_X) {
                                new1X = START_X; // To prevent the bird from going out of the screen momentarily
                                bird.getImageView().setScaleX(obj.getScale());
                            }
                        }

                        bird.getImageView().setX(new1X);
                    }
                })
        );
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

        Text messageText1 = new Text("Level 5/6");
        messageText1.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        messageText1.setFill(Color.YELLOW);
        messageText1.setX(DuckHunt.widthScene * obj.getScale() / 2 - messageText1.getLayoutBounds().getWidth() / 2);
        messageText1.setY(20);
        gameRoot.getChildren().add(messageText1);
    }


    public void handleKeyEvents(Scene scene, Stage primaryStage) {
        Musics obj1 = new Musics();
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && levelFinished) {
                obj1.stopDuckFallsSound();
                obj1.stopLevelCompletedSound();
                obj1.stopGameOverSound();

                Level6 level6Game = new Level6();
                level6Game.level6(primaryStage);
            }
        });
    }



    public void setupMouseEvents(Stage primaryStage, Group gameRoot,
                                 ImageView bird1ImageView, ImageView bird2ImageView, ImageView bird3ImageView) {

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

                obj1.playGunshotSound(obj.getVolume());
                ammo--;

                setupTextElements(gameRoot);
                Methods.displayGameOver(gameRoot, primaryStage, obj.getScale(), gameScene, true);
            }

            else if (ammo > 0 && !levelFinished){

                ammo--;
                obj1.playGunshotSound(obj.getVolume());
                setupTextElements(gameRoot);

                ImageView[] birdImageViews = {bird1ImageView, bird2ImageView, bird3ImageView};

                for (ImageView birdImageView : birdImageViews) {
                    if (birdImageView.getBoundsInParent().contains(event.getX(), event.getY(),1,1))  {

                        gameRoot.getChildren().remove(birdImageView);
                        birdsShot++;
                        Image shouttedBird = new Image("assets/duck_blue/7.png");
                        ImageView shotBirdImageView = new ImageView(shouttedBird);
                        shotBirdImageView.setX(birdImageView.getX());
                        shotBirdImageView.setY(birdImageView.getY());
                        shotBirdImageView.setScaleX(birdImageView.getScaleX());
                        shotBirdImageView.setScaleY(birdImageView.getScaleY());
                        gameRoot.getChildren().add(gameRoot.getChildren().indexOf(foregroundImageView), shotBirdImageView);

                        // Create the falling animation
                        ImageView fallingBirdImageView = new ImageView("assets/duck_blue/8.png");
                        fallingBirdImageView.setX(birdImageView.getX());
                        fallingBirdImageView.setY(birdImageView.getY());
                        fallingBirdImageView.setScaleX(birdImageView.getScaleX());
                        fallingBirdImageView.setScaleY(abs(birdImageView.getScaleY())
                        );

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


                            handleKeyEvents(gameScene, primaryStage);
                            break; // Exit the loop since we found the bird that was clicked
                        }
                    }
                }
            }
        });
    }
}