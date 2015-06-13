package dutytimetable.gui.fx;

import java.net.URL;
import java.util.ResourceBundle;

import dutytimetable.gui.fx.mainmodel.GDoc;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;



public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TitledPane x1;


    @FXML
    void openMenuItemOnAction(ActionEvent event) {
        GDoc.getUrl();
    }

    @FXML
    void initialize() {
        assert x1 != null : "fx:id=\"x1\" was not injected: check your FXML file 'MainView.fxml'.";


    }

}
