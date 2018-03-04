package com.home.book.beatBox;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

public class BeatBox {
    private JTextField messageField;
    private JList<String> incomingList;
    private ArrayList<JCheckBox> checkboxList;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    private int nextNum;
    private String serverAddress;
    private String userName;
    private Vector<String> listVector;
    private HashMap<String, boolean[]> otherSeqMap;
    private MusicController music;


    public static void main(String[] args) {
        BeatBox beatBox = new BeatBox();
        beatBox.userName = args.length > 0 ? args[0] : "undefined";
        beatBox.serverAddress = args.length > 1? args[1] : ApplicationContext.SERVER_ADDRESS;
        beatBox.init();
    }

    private void init() {
        listVector = new Vector<>();
        otherSeqMap = new HashMap<>();
        music = new MusicController();

        connectToServer();
        music.initMIDI();
        buildGUI();
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket(serverAddress, ApplicationContext.SERVER_PORT);
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            new Thread(new RemoteReader()).start();
        } catch (Exception e) {
            System.out.println("Can't connect with server");
        }
    }

    private void buildGUI() {
        JFrame theFrame = new JFrame("Cyber BeatBox");
        theFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        BorderLayout layout = new BorderLayout();
        JPanel background = new JPanel(layout);
        background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Box buttonBox = new Box(BoxLayout.Y_AXIS);
        buttonBox.add(createButton("Start", new MyStartListener()));
        buttonBox.add(createButton("Stop", new MyStopListener()));
        buttonBox.add(createButton("Tempo Up", new MyUpTempoListener()));
        buttonBox.add(createButton("Tempo Down", new MyDownTempoListener()));
        buttonBox.add(createButton("sendIt", new MySendListener()));
        buttonBox.add(messageField = new JTextField("write your message here"));
        buttonBox.add(createButton("random", e -> {
            generateRandomTrackAndPlay();
        }));

        incomingList = new JList<>();
        incomingList.addListSelectionListener(new MyListSelectionListener());
        incomingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        incomingList.setListData(listVector);
        buttonBox.add(new JScrollPane(incomingList));

        Box nameBox = new Box(BoxLayout.Y_AXIS);
        String[] names = music.getInstrumentsNames();
        for(String name: names){
            nameBox.add(new Label(name));
        }

        background.add(BorderLayout.EAST, buttonBox);
        background.add(BorderLayout.WEST, nameBox);
        theFrame.getContentPane().add(background);

        GridLayout grid = new GridLayout(16, 16);
        grid.setVgap(1);
        grid.setHgap(2);

        JPanel mainPanel = new JPanel(grid);
        checkboxList = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(false);
            checkboxList.add(checkBox);
            mainPanel.add(checkBox);
        }
        background.add(BorderLayout.CENTER, mainPanel);

        theFrame.setBounds(50, 50, 300, 300);
        theFrame.pack();
        theFrame.setVisible(true);
    }

    private JButton createButton(String name, ActionListener listener){
        JButton button = new JButton(name);
        button.addActionListener(listener);
        return button;
    }

    private void buildTrackAndPlay() {
        music.resetTrack();
        String[] instruments = music.getInstrumentsNames();
        boolean[] rhythm = new boolean[16];
        for (int i = 0; i < instruments.length; i++) {
            for (int j = 0; j < rhythm.length; j++) {
                rhythm[j] = checkboxList.get(16 * i + j).isSelected();
            }
            music.addToTrack(instruments[i], rhythm);
        }
        music.play();
    }

    private void generateRandomTrackAndPlay() {
        music.resetTrack();
        String[] instruments = music.getInstrumentsNames();
        boolean[] rhythm = new boolean[16];
        Random generator = new Random();
        for (int i = 0; i < instruments.length; i++) {
            for (int j = 0; j < rhythm.length; j++) {
                boolean value = generator.nextInt(10) == 2;
                rhythm[j] = value;
                checkboxList.get(16 * i + j).setSelected(value);
            }
            music.addToTrack(instruments[i], rhythm);
        }
        music.play();
    }

    private void changeSequence(boolean[] checkboxState) {
        for (int i = 0; i < 256; i++) {
            checkboxList.get(i).setSelected(checkboxState[i]);
        }
    }


    private class MyStartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            buildTrackAndPlay();
        }
    }

    private class MyStopListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            music.stop();
        }
    }

    private class MyUpTempoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            music.rollVolume(1.03f);
        }
    }

    private class MyDownTempoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            music.rollVolume(0.97f);
        }
    }

    private class MySendListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean[] checkboxState = new boolean[256];
            for (int i = 0; i < 256; i++) {
                checkboxState[i] = checkboxList.get(i).isSelected();
            }

            try {
                out.writeObject(userName + nextNum++ + ": " + messageField.getText());
                out.writeObject(checkboxState);
            } catch (Exception error) {
                System.out.println("Ошибка при отправке композиции");
            }
            messageField.setText("");
        }
    }

    private class RemoteReader implements Runnable {
        @Override
        public void run() {
            try {
                Object buffer;
                while ((buffer = in.readObject()) != null) {
                    String nameToShow = (String) buffer;
                    otherSeqMap.put(nameToShow, (boolean[]) in.readObject());
                    listVector.add(nameToShow);
                    incomingList.setListData(listVector);
                }
            } catch (Exception e) {
                System.out.println("Ошибка при получении сообщения");
            }
        }
    }

    private class MyListSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                String selected = incomingList.getSelectedValue();
                if (selected != null) {
                    changeSequence(otherSeqMap.get(selected));
                    music.stop();
                    buildTrackAndPlay();
                }
            }
        }
    }
}