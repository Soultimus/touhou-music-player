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
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class ButtonAction {

    private Button b;
    private int gameId;
    private int songId;
    private Stage s;
    private Text text;

    private static Thread t;
    private static MusicHandler m;

    public ButtonAction(Button b) {
        this.b = b;
    }

    public ButtonAction(Button b, Text text) {
        this(b);
        this.text = text;
    }

    public ButtonAction(Button b, int gameId, Stage s) {
        this(b);
        this.gameId = gameId;
    }

    public ButtonAction(Button b, int gameId, Text text, int songId) {
        this(b);
        this.gameId = gameId;
        this.songId = songId;
        this.text = text;
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
                songId
            );

            String trackName = b.getText();
            // Ten Desires Trance tracks check
            if (gameId == 13) {
                if (trackName.substring(trackName.length() - 1).equals(")")) {
                    m = new MusicHandler(tm, true);
                }
                else {
                    m = new MusicHandler(tm, false);
                }
            }
            else {
                m = new MusicHandler(tm);
            }


            t = new Thread(() -> m.playTrack());
            t.setDaemon(true);
            t.start();
            if (trackName.length() > 40) {
                text.setStyle("-fx-font: bold 18pt fantasy;");
            }
            else {
                text.setStyle("-fx-font: bold 25pt fantasy;");
            }
            text.setText("Now playing: " + b.getText());
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
                    text.setText("Touhou Music Player");
                }
            } 
            catch (NullPointerException err) {
                err.printStackTrace();
            }
        });
    }

    /**
     * Save the directory of the game
     */
    public void assignDirectory() {
        b.setOnAction(e -> {
            DirectoryChooser c = new DirectoryChooser();
            c.setTitle("Select the directory where Touhou " + (gameId + 6) + " is located");
            c.setInitialDirectory(new File("."));
            File dir = c.showDialog(s);
            try {
                this.saveDirectory(dir, gameId);
                // assignGameExecutable();
            }
            catch (IOException err) {
                err.printStackTrace();
            }
            catch (NullPointerException err) {}
        });
    }


    ///#region Private methods

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

    /**
     * Save game directory to dirs.dat
     * @param file 
     * @param gameId The game's number
     * @throws IOException
     */
    private void saveDirectory(File file, int gameId) throws IOException {
        gameId += 6;
        String id = gameId < 10 ? "dir0" + gameId + ":" : "dir" + gameId + ":";
        String directoryPath = "info/dirs.dat";

        // Replace whatever was on the corresponding line
        List<String> lines = Files.readAllLines(Paths.get(directoryPath));
        lines.set(gameId - 6, id + file.getAbsolutePath().replace('\\', '/'));
        Files.write(Paths.get(directoryPath), lines);
    }

    ///#endregion Private Methods

}
