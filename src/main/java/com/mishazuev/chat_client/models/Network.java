package com.mishazuev.chat_client.models;

import com.mishazuev.chat_client.controllers.ChatController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {
    private static final String AUTH_CMD_PREFIX = "/auth"; // + login + password
    private static final String AUTHOK_CMD_PREFIX = "/authok"; // + username
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
    private String username;


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
        }

    }


    public String sendAuthMessage(String login, String password) {
        try {
            out.writeUTF(String.format("%s %s %s", AUTH_CMD_PREFIX, login, password));
            String response = in.readUTF();


            if (response.startsWith(AUTHOK_CMD_PREFIX)) {
                this.username = response.split("\\s+", 2)[1];
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
        Thread t = new Thread(() ->{
            try {
                while (true) {
                    String message = in.readUTF();
                    chatController.appendMessage("I: " + message);
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
            out.writeUTF(message);
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
}
