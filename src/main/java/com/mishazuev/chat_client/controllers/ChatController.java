package com.mishazuev.chat_client.controllers;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.mishazuev.chat_client.models.Network;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea chatHistory;

    @FXML
    private TextField inputField;

    @FXML
    private Button sendButton;

    @FXML
    private ListView<String> usersList;

    private Network network;

    @FXML
    void initialize() {
        usersList.setItems(FXCollections.observableArrayList("Kate", "Bob", "Misha", "Vas"));

        sendButton.setOnAction(event -> sendMessage());
        inputField.setOnAction(event -> sendMessage());


    }

    private void sendMessage(){
        String message = inputField.getText().trim();
        inputField.clear();

        if(message.isEmpty()){
            return;
        }

   //     appendMessage(message);
        network.sendMessage(message);
    }

    public void appendMessage(String message) {
  //      chatHistory.appendText(message);
  //      chatHistory.appendText(System.lineSeparator());
        message += System.lineSeparator();
       chatHistory.setText(new StringBuilder(chatHistory.getText()).insert(0, message).toString());


    }


    public void setNetwork(Network network) {

        this.network = network;
    }


}
