import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Musics {
    private MediaPlayer titleMusic;
    private MediaPlayer introMusic;
    private MediaPlayer gunShot;
    private MediaPlayer duckFalls;
    private MediaPlayer gameOver;
    private MediaPlayer gameCompleted;
    private MediaPlayer levelCompleted;

    public Musics() {
        initializeMediaPlayers();
    }

    private void initializeMediaPlayers() {
        titleMusic = createMediaPlayer("assets/effects/Title.mp3");
        introMusic = createMediaPlayer("assets/effects/Intro.mp3");
        gunShot = createMediaPlayer("assets/effects/GunShot.mp3");
        duckFalls = createMediaPlayer("assets/effects/DuckFalls.mp3");
        gameOver = createMediaPlayer("assets/effects/GameOver.mp3");
        gameCompleted = createMediaPlayer("assets/effects/GameCompleted.mp3");
        levelCompleted = createMediaPlayer("assets/effects/LevelCompleted.mp3");
    }

    private MediaPlayer createMediaPlayer(String mediaPath) {
        Media media = new Media(getClass().getResource(mediaPath).toString());
        return new MediaPlayer(media);
    }

    public void playTitleMusic(double volume) {
        titleMusic.setVolume(volume);
        titleMusic.play();
    }


    public void playTitleMusicIndefinitely(double volume) {
        titleMusic.setVolume(volume);
        titleMusic.setCycleCount(MediaPlayer.INDEFINITE);
        titleMusic.play();
    }


    public void stopTitleMusic() {
        titleMusic.stop();
    }

    public void playIntroMusic(double volume) {
        introMusic.setVolume(volume);
        introMusic.play();
    }

    public void stopIntroMusic() {
        introMusic.stop();
    }

    public void playGunshotSound(double volume) {
        if (gunShot != null) {
            gunShot.stop();
            gunShot.seek(gunShot.getStartTime()); // Reset the playback position
            gunShot.setVolume(volume);
            gunShot.play();
        }
    }
    public void playDuckFallsSound(double volume) {
        duckFalls.setVolume(volume);
        duckFalls.play();
    }

    public void stopDuckFallsSound() {
        duckFalls.stop();
    }

    public void playGameOverSound(double volume) {
        gameOver.setVolume(volume);
        gameOver.play();
    }

    public void stopGameOverSound() {
        gameOver.stop();
    }

    public void playGameCompletedSound(double volume) {
        gameCompleted.setVolume(volume);
        gameCompleted.play();
    }

    public void stopGameCompletedSound() {
        gameCompleted.stop();
    }

    public void playLevelCompletedSound(double volume) {
        levelCompleted.setVolume(volume);
        levelCompleted.play();
    }

    public void stopLevelCompletedSound() {
        levelCompleted.stop();
    }

    public void setIntroMusicEndHandler(Runnable handler) {
        introMusic.setOnEndOfMedia(() -> {
            handler.run();
        });
    }
}
