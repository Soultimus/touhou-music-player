import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

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

        ComboBox<String> cb = new ComboBox<String>();
        cb.getItems().addAll(games);
        cb.setValue("Mountain of Faith"); // Default to MoF for now, will add a last played file later

        JSONParser parser = new JSONParser();
        JSONArray allTracks = (JSONArray)parser.parse(new FileReader("info/songNamesEn.json"));
        ScrollPane sp = displaySongs((JSONArray)parser.parse(allTracks.get(4).toString()), 4); // same default, to change later
        bp.setLeft(sp);


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
            ba = new ButtonAction(b, gameId, i);
            songBox.getChildren().add(b);
        }

        ScrollPane sp = new ScrollPane(songBox);
        return sp;
    }
}
