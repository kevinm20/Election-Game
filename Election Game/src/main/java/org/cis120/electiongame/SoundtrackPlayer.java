package org.cis120.electiongame;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class SoundtrackPlayer {

    private List<String> tracks;
    private AdvancedPlayer player;
    private int currentTrackIndex = 0;
    private Thread playbackThread;

    public SoundtrackPlayer(List<String> tracks) {
        this.tracks = new ArrayList<>(tracks);
        Collections.shuffle(this.tracks);
    }

    public synchronized void play() {
        stop(); // Ensure any previous playback is stopped

        if (tracks.isEmpty()) return;

        String trackPath = tracks.get(currentTrackIndex);
        playbackThread = new Thread(() -> {
            try (FileInputStream fis = new FileInputStream(trackPath)) {
                player = new AdvancedPlayer(fis);
                player.play();
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            }
        });
        playbackThread.start();

        try {
            playbackThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        currentTrackIndex = (currentTrackIndex + 1) % tracks.size();
        if (currentTrackIndex == 0) {
            Collections.shuffle(tracks); // Reshuffle after a full loop
        }
    }

    public synchronized void stop() {
        if (player != null) {
            player.close(); // This will stop playback
        }
        if (playbackThread != null && playbackThread.isAlive()) {
            playbackThread.interrupt(); // Interrupt the playback thread
        }
    }

    public String getCurrentTrackName() {
        return new File(tracks.get(currentTrackIndex)).getName();
    }

    public void playNext() {
        stop();
        currentTrackIndex = (currentTrackIndex + 1) % tracks.size();
        play();
    }

    public void playPrevious() {
        stop();
        currentTrackIndex = (currentTrackIndex - 1 + tracks.size()) % tracks.size();
        play();
    }

    public void playSoundEffect(String soundFile) {
        try {
            // Load the file as a resource
            InputStream audioSrc = getClass().getClassLoader().getResourceAsStream(soundFile);
            if (audioSrc == null) {
                throw new IOException("File not found: " + soundFile);
            }
            
            // Convert the resource into an AudioInputStream
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

}
