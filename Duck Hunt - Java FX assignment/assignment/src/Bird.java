import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Bird {

    private Image[] wingFlappingImages;
    private ImageView birdImageView;
    private double birdWidth;
    private int currentFrame;
    public boolean movingRight;
    public boolean movingUp;
    public boolean diagonal;

    /**
     * Constructs a Bird object with the specified parameters and adds it to the provided Group.
     *
     * @param gameRoot The Group to which the Bird object will be added.
     * @param imagePath The path to the image files for the bird's wing flapping animation.
     * @param scale The scale factor to be applied to the bird's image.
     * @param j The offset value used to select the appropriate image files for the wing flapping animation.(1 for diagonal, 4 for horizontal)
     */
    public Bird(Group gameRoot, String imagePath, double scale, int j) {
        // Create an array to store the wing flapping images
        wingFlappingImages = new Image[3];
        // Load the wing flapping images using the specified imagePath and j offset
        for (int i = 0; i < 3; i++) {
            wingFlappingImages[i] = new Image(imagePath + (i + j) + ".png");
        }

        birdImageView = new ImageView();
        birdImageView.setImage(wingFlappingImages[currentFrame]);
        // Set the scale factors for the bird's image
        birdImageView.setScaleX(scale);
        birdImageView.setScaleY(scale);

        gameRoot.getChildren().add(birdImageView);
        birdWidth = birdImageView.getImage().getWidth();
        // Set the initial movement direction of the bird
        this.movingRight = true;
        this.movingUp = true;
    }

    public void update() {
        birdImageView.setImage(wingFlappingImages[currentFrame]);
    }
    public double getWidth() {
        return birdWidth;
    }
    public Image[] getWingFlappingImages(){
        return wingFlappingImages;
    }
    public void setCurrentFrame(int currentFrame){
        this.currentFrame = currentFrame;
    }
    public int getCurrentFrame(){
        return currentFrame;
    }
    public ImageView getImageView() {
        return birdImageView;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public boolean isMovingUp() {
        return movingUp;
    }

    public void setMovingUp(boolean movingUp) {
        this.movingUp = movingUp;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public void setDiagonal(boolean diagonal) {
        this.diagonal = diagonal;
    }

    public boolean isDiagonal() {
        return diagonal;
    }
}
