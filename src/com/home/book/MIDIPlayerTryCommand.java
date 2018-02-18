package com.home.book;

import javax.sound.midi.*;

public class MIDIPlayerTryCommand {

    public static void main(String[] args) {
        new MIDIPlayerTryCommand().play();
    }

    private void play() {
        try {
            Sequencer player = MidiSystem.getSequencer();
            player.open();

            Sequence sequence = new Sequence(Sequence.PPQ, 4);
            Track track = sequence.createTrack();

            ShortMessage a = new ShortMessage();
            a.setMessage(144, 1, 24, 100);
            MidiEvent noteOn = new MidiEvent(a, 3);
            track.add(noteOn);

            ShortMessage b = new ShortMessage();
            b.setMessage(128, 1, 24, 100);
            MidiEvent noteOff = new MidiEvent(a, 24);
            track.add(noteOff);

            player.setSequence(sequence);
            player.start();
            Thread.sleep(5000);

            player.close();

        } catch (Exception e) {
            System.out.println("We have some problem((");
        }
    }
}
