package com.mishazuev.chat_client.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.mishazuev.chat_client.StartClient;
import com.mishazuev.chat_client.models.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AuthController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginField;

    @FXML
    private Button signUp;

    @FXML
    private TextField passwordField;

    private Network network;

    private StartClient startClient;

    private ChatController chatController;

    private String username;

    @FXML
    void initialize() {


    }

    @FXML
    void checkAuth(ActionEvent event) {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();

        if(login.length() == 0 || password.length() == 0) {
            System.out.println("Ошибка ввода при аутинфикации");
            System.out.println("Поля не должны быть пустые");
            startClient.showErrorAlert("Ошибка ввода при аутинфикации", "Поля не должны быть пустые");
            return;
        }

        if(login.length() > 32 || password.length() > 32) {
            System.out.println("Ошибка ввода при аутинфикации");
            System.out.println("Длина логина или пороля должны быть не более 32 символов");
            startClient.showErrorAlert("Ошибка ввода при аутинфикации", "Длина логина или пороля должны быть не более 32 символов");
            return;
        }

        System.out.println("Auth");

        String authErrorMessage = network.sendAuthMessage(login, password);

        if(authErrorMessage == null) {
            // call the second chat view window

            startClient.openChatDialog();


        } else {
            startClient.showErrorAlert("Ошибка аутентификации", authErrorMessage);
            System.out.println(authErrorMessage);
        }




    }

    public void setNetwork(Network network) {
        this.network = network;
    }


    public Network getNetwork() {
        return network;
    }

    public void setStartClient(StartClient startClient) {
        this.startClient = startClient;
    }

    public StartClient getStartClient() {
        return startClient;
    }
}
