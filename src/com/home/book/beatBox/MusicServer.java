package com.home.book.beatBox;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class MusicServer {
    private ArrayList<ObjectOutputStream> clientOutputStreams;

    public static void main(String[] args) {
        new MusicServer().go();
    }

    private void go() {
        clientOutputStreams = new ArrayList<>();
        try {
            ServerSocket serverSocket = new ServerSocket(4242);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientOutputStreams.add(new ObjectOutputStream(clientSocket.getOutputStream()));
                new Thread(new ClientHandler(clientSocket)).start();
                System.out.println("got a connection");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ClientHandler implements Runnable {
        ObjectInputStream in;
        Socket clientSocket;

        public ClientHandler(Socket socket) {
            try {
                clientSocket = socket;
                in = new ObjectInputStream(clientSocket.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
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
                e.printStackTrace();
            }
        }
    }

    private void tellEveryOne(Object one, Object two) {
        Iterator<ObjectOutputStream> iterator = clientOutputStreams.iterator();
        while (iterator.hasNext()){
            try {
                ObjectOutputStream out = iterator.next();
                out.writeObject(one);
                out.writeObject(two);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
