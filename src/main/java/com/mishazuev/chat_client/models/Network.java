package com.mishazuev.chat_client.models;

import com.mishazuev.chat_client.controllers.ChatController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {
    public static final String DEFAUlT_HOST = "localhost";
    public static final int DEFAULT_PORT = 8888;

    private final String host;
    private final int port;
    private DataOutputStream out;
    private DataInputStream in;

    private ChatController chatController;


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
            System.out.println("--------------------------");
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


}
