package dutytimetable.controller;


import dutytimetable.model.FileModel;
import dutytimetable.model.GlobalStatus;
import dutytimetable.view.FileView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TitledPane;
import myutils.debug.Debug;

import java.net.URL;
import java.util.ResourceBundle;


public class MainController {
    private static MainController instance = null;

    public static MainController getInstance() {
        assert instance != null : "MainController is not initialized before requested";
        return instance;
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TitledPane x1;

    @FXML
    private Label statusLabel;
    @FXML
    private ProgressIndicator progressIndicator;

    public Label getStatusLabel() {
        return statusLabel;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }


    @FXML
    void openMenuItemOnAction(ActionEvent event) {
        FileView.getUrl();
    }

    @FXML
    void closeMenuItemOnAction(ActionEvent event) {
        FileModel.saveFile();
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
        instance = this;

        GlobalStatus.bindMeOnStatusView();

        GlobalStatus.task().updateMessage("Ready to import timetable");
        GlobalStatus.task().updateProgress(1, 1);

        Debug.showMessage("finish initialize");
    }

}
