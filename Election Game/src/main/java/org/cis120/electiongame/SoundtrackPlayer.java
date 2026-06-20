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
    private volatile boolean shouldPlay = true;  // Single flag for both mute and playback control
    private volatile boolean isPlaying = false;

    public SoundtrackPlayer(List<String> tracks) {
        this.tracks = new ArrayList<>(tracks);
        Collections.shuffle(this.tracks);
    }

    // Play the current track, if not muted and should play
    public synchronized void play() {
        if (!shouldPlay) {
            return;  // If muted, don't play
        }

        if (isPlaying) {
            return;  // Avoid starting a new thread if the track is already playing
        }

        if (tracks.isEmpty()) return;

        String trackPath = tracks.get(currentTrackIndex);

        playbackThread = new Thread(() -> {
            isPlaying = true;
            try (InputStream audioStream = getClass().getClassLoader().getResourceAsStream(trackPath)) {
                if (audioStream != null) {
                    player = new AdvancedPlayer(audioStream);
                    player.play();
                } else {
                    System.err.println("Could not find file: " + trackPath);
                }
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            } finally {
                isPlaying = false;  // Track finished
                nextTrack();  // Automatically play the next track
            }
        });

        playbackThread.start();
    }

    // Play the next track after current one finishes
    private void nextTrack() {
        currentTrackIndex = (currentTrackIndex + 1) % tracks.size();
        if (shouldPlay) {
            play();
        }
    }

 // Stop playback
    public synchronized void stop() {
        if (isPlaying) {
            if (playbackThread != null && playbackThread.isAlive()) {
                playbackThread.interrupt();  // Interrupt the playback thread
                try {
                    playbackThread.join(1);  // Wait for the thread to properly stop with timeout
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            if (player != null) {
                player.close();  // This will stop playback
                player = null;
            }
            isPlaying = false;
        }
    }


    // Play a sound effect
    public void playSoundEffect(String soundFile) {
        if (!shouldPlay) return;  // Do not play sound effects if muted
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

    // Toggle mute
    public void toggleMute() {
        shouldPlay = !shouldPlay;  // Flip the play state
        if (!shouldPlay) {
            stop();  // Stop all audio if muted
        } else {
            play();  // Resume playing if unmuted
        }
    }

    // Check if the player is muted (based on the shouldPlay flag)
    public boolean isMuted() {
        return !shouldPlay;  // If shouldPlay is false, it means we're muted
    }

    // Get the name of the current track
    public String getCurrentTrackName() {
        return tracks.get(currentTrackIndex);
    }
    
    // Play the next track
    public void playNextTrack() {
        // Move to the next track, looping back to the first if we're at the end
        currentTrackIndex = (currentTrackIndex + 1) % tracks.size();
        stop();
    }


    // Play the previous track
    public void playPreviousTrack() {
        currentTrackIndex = (currentTrackIndex - 1 + tracks.size()) % tracks.size()-1;  // Move to previous track
        stop();  // Stop the current track
    }
}
