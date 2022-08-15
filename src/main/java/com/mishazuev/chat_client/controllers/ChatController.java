package com.mishazuev.chat_client.controllers;

import java.net.URL;
import java.text.DateFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.ResourceBundle;

import com.mishazuev.chat_client.StartClient;
import com.mishazuev.chat_client.models.Network;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class ChatController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea chatHistory;

    @FXML
    private Label usernameTitle;

    @FXML
    private TextField inputField;

    @FXML
    private Button sendButton;

    @FXML
    private ListView<String> usersList;



    private Network network;

    private StartClient startClient;

    

    private String selectedRecipient;
    @FXML
    void initialize() {
        usersList.setItems(FXCollections.observableArrayList());



        sendButton.setOnAction(event -> sendMessage());
        inputField.setOnAction(event -> sendMessage());

        usersList.setCellFactory(lv -> {
            MultipleSelectionModel<String> selectionModel = usersList.getSelectionModel();
            ListCell<String> cell = new ListCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                usersList.requestFocus();
                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    if (selectionModel.getSelectedIndices().contains(index)) {
                        selectionModel.clearSelection(index);
                        selectedRecipient = null;
                    } else {
                        selectionModel.select(index);
                        selectedRecipient = cell.getItem();
                    }
                    event.consume();
                }
            });
            return cell;
        });


    }

    private void sendMessage(){
        String message = inputField.getText().trim();
        inputField.clear();

        if(message.isEmpty()){
            return;
        }

   //     appendMessage(message);
    //    network.sendMessage(message);

        if(selectedRecipient != null) {
            network.sendPrivateMessage(selectedRecipient, message);
        } else if (selectedRecipient == null) {
            network.sendMessage(message);
        }




    }

    public void appendMessage(String sender, String message) {
        String timeStamp = DateFormat.getInstance().format(new Date());
        chatHistory.appendText(timeStamp);
        chatHistory.appendText(System.lineSeparator());
        chatHistory.appendText(String.format("%s %s", sender, message));
        chatHistory.appendText(System.lineSeparator());
        chatHistory.appendText(System.lineSeparator());


        message += System.lineSeparator();
  //     chatHistory.setText(new StringBuilder(chatHistory.getText()).insert(0, message).toString());


    }

    public void appendChatHistory(String message) {
        chatHistory.appendText(message);


    }


    public void appendServerMessage(String message) {

        chatHistory.appendText(String.format("Server: %s", message));
        chatHistory.appendText(System.lineSeparator());
        chatHistory.appendText(System.lineSeparator());


        message += System.lineSeparator();
        //     chatHistory.setText(new StringBuilder(chatHistory.getText()).insert(0, message).toString());


    }

    public void addOnlineUsersListToUsersList (ArrayList<String> user) {
        //usersList.setItems(FXCollections.observableArrayList(users));
        usersList.getItems().removeAll();

        usersList.setItems(FXCollections.observableArrayList(user));





    }

    public void cleanUserList(){
        usersList.getItems().removeAll();

    }




    public void setNetwork(Network network) {

        this.network = network;
    }


    public void setStartClient(StartClient startClient) {
        this.startClient = startClient;
    }

    public void setUsernameTitle(String usernameTitleStr) {
        this.usernameTitle.setText(usernameTitleStr);
    }

    public ListView<String> getUsersList() {
        return usersList;
    }
}
