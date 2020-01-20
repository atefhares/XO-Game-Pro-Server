package com.itijavafinalprojectteam8.view;

import com.itijavafinalprojectteam8.controller.operations.Props;
import com.itijavafinalprojectteam8.controller.operations.log.GuiLogger;
import com.itijavafinalprojectteam8.controller.server.GameServer;
import com.itijavafinalprojectteam8.interfaces.View;
import com.itijavafinalprojectteam8.others.Constants;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.json.JSONArray;
import org.json.JSONObject;

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
    private GridPane playersGrid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GuiLogger.init(this);

        Props.allPlayersJson.addListener((observableValue, oldValue, newValue) -> {
            GuiLogger.log("oldValue: " + oldValue);
            GuiLogger.log("newValue: " + newValue);

            if (!newValue.equals(oldValue)) {
                Platform.runLater(() -> updatePlayersView(newValue));
            }
        });

        playersGrid.setHgap(10);
        playersGrid.setVgap(10);
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

    public void updatePlayersView(String jsonString) {
        playersGrid.getChildren().clear();

        JSONArray arr = new JSONArray(jsonString);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject jsonobject = arr.getJSONObject(i);
            String name = jsonobject.optString(Constants.JsonKeys.KEY_USER_NAME);
            int points = jsonobject.optInt(Constants.JsonKeys.KEY_USER_POINTS);
            int status = jsonobject.optInt(Constants.JsonKeys.KEY_USER_STATUS);

            Circle circle = new Circle(200, 200, 8);
            if (status == Constants.PlayerStatus.OFFLINE)
                circle.setFill(Color.RED);
            else
                circle.setFill(Color.GREEN);


            Label playerName = new Label();
            playerName.setText(name);

            Label playerPoints = new Label();
            playerPoints.setText(String.valueOf(points));


            playersGrid.add(circle, 0, i);
            playersGrid.add(playerName, 1, i);
            playersGrid.add(playerPoints, 2, i);

        }
    }
}
