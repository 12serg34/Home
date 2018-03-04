package com.home.book.beatbox;

import java.io.FileInputStream;
import java.util.Scanner;

public final class ApplicationContext {

    public static final String SETTINGS_FILE = "settings.txt";

    private static int serverPort;
    private static String serverAddress;

    static {
        serverPort = 4242;
        serverAddress = "127.0.0.1";
        tryLoadFromFile();
    }

    private static void tryLoadFromFile(){
        try {
            Scanner scanner = new Scanner(new FileInputStream(SETTINGS_FILE));
            serverPort = scanner.nextInt();
            System.out.println("Read server port: " + serverPort);
            serverAddress = scanner.next();
            System.out.println("Read server address: " + serverAddress);
            scanner.close();
        } catch (Exception e) {
            System.out.println("Error while loading settings file.");
        }
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static String getServerAddress() {
        return serverAddress;
    }
}
