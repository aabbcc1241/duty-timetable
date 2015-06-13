package dutytimetable.gui.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by beenotung on 6/8/15.
 */
public class MainLoader extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Auto Duty-Timetable Application");
        stage.setScene(scene);
        stage.show();
    }
}
