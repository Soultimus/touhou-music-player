// TODO: FIX JSON FILE BECAUSE SOME EXTRA SONGS ARE AFTER THE CREDITS SONGS
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class ButtonAction {

    private Button b;
    private int gameId;
    private int songId;

    private static Thread t;
    private static MusicHandler m;

    public ButtonAction(Button b, int gameId, int songId) {
        this.b = b;
        this.gameId = gameId;
        this.songId = songId;
    }

    /**
     * Assign specified song to button
     */
    public void assignMusic() {
        b.setOnAction(e -> {
            if (t != null && t.isAlive()) {
                t.interrupt();
                m.getLineIn().stop();
            }

            MusicInfo tm = new MusicInfo(
                    this.findInfoFile(".dat"),
                    this.findInfoFile(".fmt"),
                    songId);
            m = new MusicHandler(tm);
            t = new Thread(() -> m.playTrack());
            t.setDaemon(true);
            t.start();
        });
    }

    /**
     * Stop song that is currently playing
     */
    public void stopMusic() {
        b.setOnAction(e -> {
            try {
                if (t.isAlive()) {
                    m.getLineIn().stop();
                }
            } 
            catch (NullPointerException err) {
                System.out.println("The app tried to pause music despite not having anything to pause");
            }
        });
    }

    /**
     * Read the dirs.dat file to get the game's path for information
     * @return Game path based on constructor gameId parameter
     */
    private String filterGamePath() {
        String id = gameId < 10 ? "dir0" + gameId : "dir" + gameId;
        String path = null;
        try {
            List<String> allDirs = Files.readAllLines(Paths.get("info/dirs.dat"));
            for (int i = 0; i < allDirs.size(); i++) {
                if (allDirs.get(i).contains(id)) {
                    path = allDirs.get(i).substring(6);
                }
            }
        }
        catch (IOException e) {
            Alert alert = new Alert(
                    AlertType.WARNING,
                    "Directory not found. You might have not set it",
                    ButtonType.OK);
            alert.showAndWait();
        }
        if (path.length() == 0) {
            return null;
        }
        return path;
    }

    /**
     * Retrive thbgm file of specified extension (.fmt or .dat)
     * @param ext File extension to look up (.fmt or .dat)
     * @return FMT or PCM thbgm file
     */
    private File findInfoFile(String ext) {
        if (gameId < 7) {
            throw new IllegalArgumentException(
                    "The application attempted to find a thbgm.dat file in a game before PCB");
        }
        return new File(this.filterGamePath() + "/thbgm" + ext);
    }

    private void saveLastPlayed() {

    }

}
