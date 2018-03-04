package com.beatbox;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MusicServer {
    private ArrayList<ObjectOutputStream> clientOutputStreams;
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        new MusicServer().run();
    }

    private void run() {
        clientOutputStreams = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(ApplicationContext.getServerPort());
            waitNewClient();
        } catch (Exception e) {
            System.out.println("Error create socket or wait client");
        }
    }

    private void waitNewClient(){
        System.out.println("Waiting a new client...");
        try {
            Socket clientSocket = serverSocket.accept();
            clientOutputStreams.add(new ObjectOutputStream(clientSocket.getOutputStream()));
            new Thread(new ClientHandler(clientSocket)).start();
            System.out.println("got new connection");
            waitNewClient();
        }catch (Exception e){
            System.out.println("Error waiting client");
        }
    }

    private void tellEveryOne(Object one, Object two) {
        for (ObjectOutputStream clientOutputStream : clientOutputStreams) {
            try {
                clientOutputStream.writeObject(one);
                clientOutputStream.writeObject(two);
            } catch (Exception e) {
                System.out.println("Error broadcast sending");
            }
        }
    }

    public class ClientHandler implements Runnable {
        ObjectInputStream in;

        private ClientHandler(Socket socket) {
            try {
                in = new ObjectInputStream(socket.getInputStream());
            } catch (Exception e) {
                System.out.println("Error getting input stream");
            }
        }

        public void run() {
            Object o1, o2;
            try {
                while ((o1 = in.readObject()) != null) {
                    o2 = in.readObject();
                    System.out.println("Read two objects");
                    tellEveryOne(o1, o2);
                }
            } catch (Exception e) {
                System.out.println("Error reading input");
            }
        }
    }
}
