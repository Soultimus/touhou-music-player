import java.beans.EventHandler;
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
                m = new MusicHandler(tm);
                t = new Thread(() -> 
                    m.playTrack()
                );
                t.setDaemon(true);
                t.start();
            }
        );
    }

    private String filterGamePath() {
        String id = gameId < 10 ? "dir0" + gameId : "dir" + gameId;
        String path = null;
        try {
            List<String> allDirs = Files.readAllLines(Paths.get("info/directories.dat"));
            for (int i = 0; i < allDirs.size(); i++) {
                if (allDirs.get(i).contains(id)) {
                    path = allDirs.get(i).substring(6);
                }
            }
        }
        catch (IOException e) { // TODO: Make an error when an id block is missing!
            Alert alert = new Alert(
                AlertType.WARNING, 
                "Unable to find thbgm files in set directory", 
                ButtonType.OK
            );
            alert.showAndWait();
        }
        if (path.length() == 0) {
            return null;
        }
        return path;
    }
        
    private File findInfoFile (String ext) {
        return null;
    }
    
}
