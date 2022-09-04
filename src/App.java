import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane bp = new BorderPane();
        ScrollPane sp = null;
        String[] games = new String[] {
            "Perfect Cherry Blossom",
            "Imperishable Night",
            "Phantasmagoria of Flower View",
            "Mountain Of Faith",
            "Subterranean Animism",
            "Undefined Fantastic Object",
            "Ten Desires",
            "Double Dealing Character",
            "Legacy of Lunatic Kingdom",
            "Hidden Star in Four Seasons",
            "Wily Beast and Weakest Creature",
            "Unconnected Marketeers"
        };

        String[] lastInfo = readLast();

        ComboBox<String> cb = new ComboBox<String>();
        cb.getItems().addAll(games);
        cb.setValue(lastInfo[0]);
        int gameId = Integer.parseInt(lastInfo[1]);

        JSONParser parser = new JSONParser();
        JSONArray allTracks = (JSONArray)parser.parse(new FileReader("info/songNamesEn.json"));
        JSONArray songs = (JSONArray)parser.parse(allTracks.get(gameId).toString());
        sp = displaySongs(songs, gameId);
        // I hate doing this, but there's no way to change the ScrollPane like I want to in an instance method...
        ScrollPane[] jankSp = new ScrollPane[1];
        cb.valueProperty().addListener(e -> {
            int value = cb.getSelectionModel().getSelectedIndex();
            value++;
            String gameName = cb.getSelectionModel().getSelectedItem();
            try {
                jankSp[0] = displaySongs((JSONArray)parser.parse(allTracks.get(value).toString()), value);
                writeToLast(gameName, value);
            }
            catch (ParseException err) {
                err.printStackTrace();
            }
            bp.setLeft(jankSp[0]);
        });
        bp.setLeft(sp);
        bp.setRight(cb);


        Scene scene = new Scene(bp, 1030, 625);
        // scene.getStylesheets().add("file:styles/main.css");
        stage.setTitle("Touhou Music Player");
        stage.setScene(scene);
        // stage.getIcons().add(new Image("file:images/marysue.png"));
        stage.show();
    }

    private static ScrollPane displaySongs(JSONArray songs, int gameId) {
        VBox songBox = new VBox();

        Iterator<JSONArray> it = songs.iterator();
        List<Object> songNames = new ArrayList<>();
        while (it.hasNext()) {
            songNames.add(it.next());
        }

        ButtonAction ba = null;
        for (int i = 0; i < songNames.size(); i++) {
            Button b = new Button(songNames.get(i).toString());
            b.setFocusTraversable(false);
            ba = new ButtonAction(b, gameId + 6, i);
            ba.assignMusic();
            songBox.getChildren().add(b);
        }

        ScrollPane sp = new ScrollPane(songBox);
        return sp;
    }

    /**
     * Read last.dat to retrieve information about the last session
     * @return Array containing Game Name, JSON index, Game Executable Path
     */
    private String[] readLast() {
        try {
            String lastString = Files.readString(Paths.get("info/last.dat"));
            String[] last = lastString.split(", ");
            return last;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeToLast(String gameName, int index) {
        String toWrite = gameName + ", " + index;
        byte[] stringBytes = toWrite.getBytes();
        try {
            Files.write(Paths.get("info/last.dat"), stringBytes);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
