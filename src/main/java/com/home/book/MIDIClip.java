package com.home.book;

import javax.sound.midi.*;
import javax.swing.*;

public class MIDIClip {

    public static final int COMMAND_BEGIN_PLAY_NOTE = 144;
    public static final int COMMAND_END_PLAY_NOTE = 144;
    public static final int COMMAND_CONTROLLER_EVENT = 176;

    MyDrawPanel panel;

    public static void main(String[] args) {
        MIDIClip program = new MIDIClip();
        program.init();
        program.run();
    }

    private void init(){
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new MyDrawPanel();
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    private void run() {
        try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();

            int[] events = {127};
            sequencer.addControllerEventListener(panel, events);

            Sequence sequence = new Sequence(Sequence.PPQ, 4);
            Track track = sequence.createTrack();
            for (int i = 5; i < 61; i += 4) {
                int note = (int)(50*Math.random() + 1);
                track.add(makeEvent(COMMAND_BEGIN_PLAY_NOTE, 1, note, 100, i));
                track.add(makeEvent(COMMAND_CONTROLLER_EVENT, 1, events[0], 0, i));
                track.add(makeEvent(COMMAND_END_PLAY_NOTE, 1, note, 100, i + 2));
            }

            sequencer.setSequence(sequence);
            sequencer.setTempoInBPM(120);
            sequencer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }
}
