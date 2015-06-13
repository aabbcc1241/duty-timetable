package dutytimetable.gui.fx;

import dutytimetable.debug.Debug;
import dutytimetable.gui.fx.mainmodel.FileHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TitledPane;

import java.net.URL;
import java.util.ResourceBundle;


public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TitledPane x1;

    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Label statusLabel;


    @FXML
    void openMenuItemOnAction(ActionEvent event) {
        FileHandler.openFile();
    }

    @FXML
    void closeMenuItemOnAction(ActionEvent event) {
        FileHandler.saveFile();
        System.exit(0);
    }

    @FXML
    void startMenuItemOnAction(ActionEvent event) {

    }

    @FXML
    void pauseMenuItemOnAction(ActionEvent event) {

    }

    @FXML
    void initialize() {
        Debug.showMessage("start initialize");
        assert x1 != null : "fx:id=\"x1\" was not injected: check your FXML file 'MainView.fxml'.";

        System.out.println(statusLabel);
        statusLabel.setText("Ready to import timetable");
        progressIndicator.setProgress(1);

        Debug.showMessage("finish initialize");
    }

}
