package com.mishazuev.chat_client.models;

import com.mishazuev.chat_client.StartClient;
import com.mishazuev.chat_client.controllers.ChatController;
import javafx.application.Platform;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Network {
    private static final String AUTH_CMD_PREFIX = "/auth"; // + login + password
    private static final String AUTHOK_CMD_PREFIX = "/authok"; // + username
    private static final String ONLINE_USER_PREFIX = "/online"; // + adding username name to ListView
    private static final String AUTHERR_CMD_PREFIX = "/autherr"; // + error message
    private static final String CLIENT_MSG_CMD_PREFIX = "/cMsg"; // + msg
    private static final String SERVER_MSG_CMD_PREFIX = "/sMsg"; // + msg
    private static final String PRIVATE_MSG_CMD_PREFIX = "/pm"; // + username + msg
    private static final String STOP_SERVER_CMD_PREFIX = "/stop";
    private static final String END_CLIENT_CMD_PREFIX = "/end";


    public static final String DEFAUlT_HOST = "localhost";
    public static final int DEFAULT_PORT = 8888;

    private final String host;
    private final int port;
    private DataOutputStream out;
    private DataInputStream in;

    private ChatController chatController;
    private String chatHistory;
    private String username;
    private StartClient startClient;
    private ArrayList<String> users = new ArrayList<>();


    public Network(String host, int port){
        this.host = host;
        this.port = port;
    }

    public Network(){

        this(DEFAUlT_HOST, DEFAULT_PORT);
    }

    public void connect() {
        try {
            Socket socket = new Socket(host, port);

            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Соединение не установлено");
            startClient.showErrorAlert("Ошибка подключения","Соединение не установлено" );

        }

    }


    public String sendAuthMessage(String login, String password) {
        try {
            out.writeUTF(String.format("%s %s %s", AUTH_CMD_PREFIX, login, password));
            String response = in.readUTF();


            if (response.startsWith(AUTHOK_CMD_PREFIX)) {
                this.username = response.split("\\s+", 3)[1];
                this.chatHistory = response.split("\\s+", 3)[2];



                return null;
            } else {
                return response.split("\\s+", 2)[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при отправке сообщение... Error during sending the message...");
            return e.getMessage();
        }

    }

    public void waitMessage(ChatController chatController){
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    String message = in.readUTF();

                    String typeMessage = message.split("\\s+")[0];
                    if (!typeMessage.startsWith("/")) {
                        System.out.println("Неверный запрос!");
                    }

                    switch (typeMessage) {
                        case CLIENT_MSG_CMD_PREFIX -> {
                            String[] parts = message.split("\\s+", 3);
                            String sender = parts[1];
                            String messageFromSender = parts[2];

                            if (sender.equals(username)) {
                                sender = "I: ";
                            }


                            String finalSender = sender;
                            Platform.runLater(() -> chatController.appendMessage(finalSender, messageFromSender));
                        }

                        case PRIVATE_MSG_CMD_PREFIX -> {
                            String[] parts = message.split("\\s+", 3);
                            String sender = parts[1];
                            String messageFromSender = parts[2];

                            Platform.runLater(() -> chatController.appendMessage("[pm] " + sender, messageFromSender));

                        }

                        case SERVER_MSG_CMD_PREFIX -> {
                            String[] parts = message.split("\\s+", 3);
                            String username = parts[1];
                            String serverMessage = parts[2];

                            Platform.runLater(() -> {
                                chatController.appendServerMessage(serverMessage);


                            });

                        }
                        case ONLINE_USER_PREFIX -> {
                            String[] parts = message.split("\\s+");

                            users.removeAll(users);

                            for(int i = 1; i < parts.length; i++) {
                                users.add(parts[i]);
                            }




                            Platform.runLater(() -> {

                                chatController.addOnlineUsersListToUsersList(users);

                            });

                        }
                    }
                }


            }catch(IOException e){
                e.printStackTrace();
            }
        });

        t.setDaemon(true);
        t.start();


    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(String.format("%s %s", CLIENT_MSG_CMD_PREFIX, message));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при отправке сообщение... Error during sending the message...");

        }

    }

    public void sendPrivateMessage(String recipient, String message) {
        try{
            out.writeUTF(String.format("%s %s %s", PRIVATE_MSG_CMD_PREFIX, recipient, message));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при отправке сообщение... Error during sending the message...");
        }
    }

    public DataOutputStream getOut() {

        return out;
    }

    public DataInputStream getIn() {

        return in;
    }


    public String getUsername() {
        return username;
    }

    public void setStartClient(StartClient startClient) {
        
        this.startClient = startClient;
    }

    public String getChatHistory() {
        return chatHistory;
    }
}
