package com.mishazuev.chat_client;

import com.mishazuev.chat_client.controllers.ChatController;
import com.mishazuev.chat_client.models.Network;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartClient extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartClient.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 480);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();


        Network network = new Network();

        ChatController chatController = fxmlLoader.getController();
        chatController.setNetwork(network);
        network.connect();
        network.waitMessage(chatController);






    }

    public static void main(String[] args) {
        launch();
    }
}