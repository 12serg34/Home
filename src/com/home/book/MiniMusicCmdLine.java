package com.home.book;

import javax.sound.midi.*;
import java.util.Random;

public class MiniMusicCmdLine {

    private final int TIME_PAUSE = 3000;
    private final int COMMAND_CHANGE_INSTRUMENT = 192;
    private final int COMMAND_BEGIN_PLAY_NOTE = 144;
    private final int COMMAND_END_PLAY_NOTE = 128;

    private int instrument, note;
    private Sequencer player;
    private Sequence sequence;

    public static void main(String[] args) {
        MiniMusicCmdLine miniMusic = new MiniMusicCmdLine();
        if (args.length >= 2){
            miniMusic.instrument = Integer.parseInt(args[0]);
            miniMusic.note = Integer.parseInt(args[1]);
        }
        else {
            System.out.println("Сыграю что-нибудь слйчайное." +
                    "\nВ следующий раз выбери инструмент и ноту: (0..127) (0..127)");
            Random random = new Random();
            miniMusic.instrument = random.nextInt(128);
            miniMusic.note = random.nextInt(128);
        }
        miniMusic.play();
    }

    private void play() {
        try {
            sequence = new Sequence(Sequence.PPQ, 4);
            initializeSequence();

            player = MidiSystem.getSequencer();
            player.open();
            player.setSequence(sequence);
            player.start();
            Thread.sleep(TIME_PAUSE);
            player.close();

        } catch (Exception e) {
            System.out.println("We have some problem((");
        }
    }

    private void initializeSequence() throws Exception{
        Track track = sequence.createTrack();
        track.add(new MidiEvent(
                new ShortMessage(COMMAND_CHANGE_INSTRUMENT, 1, instrument, 0), 1));
        track.add(new MidiEvent(
                new ShortMessage(COMMAND_BEGIN_PLAY_NOTE, 1, note, 100), 1));
        track.add(new MidiEvent(
                new ShortMessage(COMMAND_END_PLAY_NOTE, 1, note, 100), 16));
    }
}
