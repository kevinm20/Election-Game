package org.cis120.electiongame;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.BufferedInputStream;
import java.io.IOException;

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
            try (InputStream audioStream = getClass().getClassLoader().getResourceAsStream(trackPath)) {
                if (audioStream != null) {
                    player = new AdvancedPlayer(audioStream);
                    player.play();
                } else {
                    System.err.println("Could not find file: " + trackPath);
                }
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
        return tracks.get(currentTrackIndex);
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
        try (InputStream audioSrc = getClass().getClassLoader().getResourceAsStream(soundFile);
             BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc)) {

            if (bufferedIn != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
            } else {
                System.err.println("Could not find sound effect: " + soundFile);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
