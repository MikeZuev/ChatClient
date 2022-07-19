module com.mishazuev.chat_client {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mishazuev.chat_client to javafx.fxml;
    exports com.mishazuev.chat_client;
    exports com.mishazuev.chat_client.controllers;
    opens com.mishazuev.chat_client.controllers to javafx.fxml;
}