package com.home.book.beatBox;

import javax.sound.midi.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class MusicController {

    private Map<String, Integer> instruments;
    private Sequencer sequencer;
    private Sequence sequence;
    private Track track;

    {
        instruments = new LinkedHashMap<>();
        instruments.put("Bass Drum", 35);
        instruments.put("Closed Hi-Hat", 42);
        instruments.put("Open Hi-Fat", 46);
        instruments.put("Acoustic Snare", 38);
        instruments.put("Crash Cymbal", 49);
        instruments.put("Hand Clap", 39);
        instruments.put("High Tom", 50);
        instruments.put("Hi Bongo", 60);
        instruments.put("Maracas", 70);
        instruments.put("Whistle", 72);
        instruments.put("Low Conga", 64);
        instruments.put("Cowbell", 56);
        instruments.put("Vibraslap", 58);
        instruments.put("Low-mind Tom", 47);
        instruments.put("High Agogo", 67);
        instruments.put("Open Hi Conga", 63);
    }

    public String[] getInstrumentsNames() {
        return (String[]) instruments.keySet().toArray();
    }

    public void initMIDI() {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.setTempoInBPM(120);
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
        } catch (Exception e) {
            System.out.println("Can't using MIDI system");
        }
    }

    public void resetTrack(){
        sequence.deleteTrack(track);
        track = sequence.createTrack();
    }

    public void addToTrack(String instrument, boolean[] rhythm) {
        int key = instruments.get(instrument);
        for(int tick = 0; tick < rhythm.length; tick++){
            if (rhythm[tick]) {
                track.add(makeEvent(144, 9, key, 100, tick));
                track.add(makeEvent(128, 9, key, 100, tick + 1));
            }
        }
    }

    public void play(){
        track.add(makeEvent(192, 9, 1, 0, 15));
        try {
            sequencer.setSequence(sequence);
            sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
            sequencer.start();
            sequencer.setTempoInBPM(120);
        } catch (Exception e) {
            System.out.println("Что-то случилось с проигрывателем");
        }
    }

    public void stop(){
        sequencer.stop();
    }

    public void rollVolume(float percent){
        float tempoFactor = sequencer.getTempoFactor();
        sequencer.setTempoFactor(tempoFactor * percent);
    }

    private MidiEvent makeEvent(int command, int channel, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage message = new ShortMessage();
            message.setMessage(command, channel, one, two);
            event = new MidiEvent(message, tick);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }
}
