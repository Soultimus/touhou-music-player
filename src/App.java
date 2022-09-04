import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        VBox bp = new VBox();
        Scene scene = new Scene(bp, 1030, 625);
        // scene.getStylesheets().add("file:styles/main.css");
        stage.setTitle("Touhou Music Player");
        stage.setScene(scene);
        // stage.getIcons().add(new Image("file:images/marysue.png"));
        stage.show();
    }
}
