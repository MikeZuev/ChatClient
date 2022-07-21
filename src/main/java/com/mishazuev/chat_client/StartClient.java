package com.mishazuev.chat_client;

import com.mishazuev.chat_client.controllers.AuthController;
import com.mishazuev.chat_client.controllers.ChatController;
import com.mishazuev.chat_client.models.Network;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StartClient extends Application {
    private Network network;
    private Stage primaryStage;
    private Stage authStage;
    private ChatController chatController;

    @Override
    public void start(Stage stage) throws IOException {

        primaryStage = stage;
        network = new Network();
        network.connect();

        openAuthDialog();
        createChatDialog();



    }

    private void openAuthDialog() throws IOException {
        FXMLLoader authLoader = new FXMLLoader(StartClient.class.getResource("auth-view.fxml"));
        authStage = new Stage();
        Scene scene = new Scene(authLoader.load(), 640, 480);
        authStage.setScene(scene);
        authStage.setTitle("Authentication");
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.initOwner(primaryStage);
        authStage.setAlwaysOnTop(true);
        authStage.show();


        AuthController authController = authLoader.getController();



        authController.setNetwork(network);
        authController.setStartClient(this);



    }

    private void createChatDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("hello-view.fxml"));
        primaryStage = new Stage();
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);
    //    primaryStage.show();


        chatController = fxmlLoader.getController();
        chatController.setNetwork(network);
        chatController.setStartClient(this);



    }

    public static void main(String[] args) {
        launch();
    }

    public void openChatDialog() {
        authStage.close();
        primaryStage.show();
        primaryStage.setTitle(network.getUsername());

        network.waitMessage(chatController);
        chatController.setUsernameTitle(network.getUsername());



    }
}