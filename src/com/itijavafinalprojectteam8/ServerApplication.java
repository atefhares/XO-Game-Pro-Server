package com.itijavafinalprojectteam8;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerApplication extends Application {

    private Scene mScene;

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setResizable(false);

        if (mScene == null)
            mScene = new Scene(
                    FXMLLoader.load(getClass().getResource("/com/itijavafinalprojectteam8/view/servergui.fxml"))
            );

        stage.setOnCloseRequest(windowEvent -> {
            System.exit(0);
        });

        stage.setScene(mScene);
        stage.show();
    }


    /*======================================================================================*/
    public static void main(String[] args) {
        launch(args);
    }

}
