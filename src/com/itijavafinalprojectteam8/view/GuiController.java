package com.itijavafinalprojectteam8.view;

import com.itijavafinalprojectteam8.controller.operations.Props;
import com.itijavafinalprojectteam8.controller.operations.log.GuiLogger;
import com.itijavafinalprojectteam8.controller.server.GameServer;
import com.itijavafinalprojectteam8.interfaces.View;
import com.itijavafinalprojectteam8.model.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable, View {

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private TextArea logTA;

    @FXML
    private Button btnStopServer;

    @FXML
    private Button btnStartServer;

    @FXML
    private TableView<Player> table;

    @FXML
    private TableColumn<ImageView, String> imagesCol;

    @FXML
    private TableColumn<Player, String> playersCol;

    @FXML
    private TableColumn<Player, String> pointsCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GuiLogger.init(this);

        Props.allPlayersJson.addListener((observableValue, oldValue, newValue) -> {
            GuiLogger.log("oldValue: " + oldValue);
            GuiLogger.log("newValue: " + newValue);

            if (!newValue.equals(oldValue)) {
//                if (mView != null) {
//                    //mView.onPlayerListChanged();
//                }
            }
        });
    }

    @FXML
    public void onBtnStartServerClicked(ActionEvent event) {
        logTA.clear();
        toggleStopBtn();
        toggleStartBtn();
        GameServer.startServer();
    }

    @FXML
    public void onBtnStopServerClicked(ActionEvent event) {
        toggleStopBtn();
        toggleStartBtn();
        GameServer.stopServer();
    }

    /*======================================================================================*/
    @Override
    public void toggleStartBtn() {
        btnStartServer.setDisable(!btnStartServer.isDisabled());
    }

    @Override
    public void toggleStopBtn() {
        btnStopServer.setDisable(!btnStopServer.isDisabled());
    }

    @Override
    public void displayLog(String text) {
        Platform.runLater(() -> {
            logTA.appendText(text + "\n");
            scrollpane.setVvalue(1.0);
        });
    }
}
