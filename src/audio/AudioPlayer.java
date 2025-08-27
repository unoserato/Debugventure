package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

	public static int MENU = 0;
	public static int LEVEL_1 = 1;
	public static int LEVEL_2 = 2;
	public static int LEVEL_3 = 3;

	public static int DIE = 0;
	public static int JUMP = 1;
	public static int ATK_1 = 2;
	public static int ATK_2 = 3;
	public static int ATK_3 = 4;
	public static int BOW = 5;
	public static int ATK_TV = 6;
	public static int ATK_SCOUT = 7;

	private Clip[] songs, effects;
	private int currentSongID;
	private float songVolume = 1f, effectsVolume = 0.8f;
	private boolean songMute, effectMute;
	private Random rand = new Random();

	public AudioPlayer() {
		loadSongs();
		loadEffects();
		playSong(MENU);
	}

	private void loadSongs() {
		String[] names = { "MENU", "LEVEL_1", "LEVEL_2", "LEVEL_3" };
		songs = new Clip[names.length];

		for (int i = 0; i < songs.length; i++)
			songs[i] = getClip(names[i]);

	}

	private void loadEffects() {
		String[] effectsnames = { "die", "jump", "attack1", "attack2", "attack3", "bow", "attack TV", "attack Scout"};
		effects = new Clip[effectsnames.length];

		for (int i = 0; i < effects.length; i++)
			effects[i] = getClip(effectsnames[i]);

		updateEffectsVolume();
	}

	private Clip getClip(String name) {
		URL url = getClass().getResource("/audio/" + name + ".wav");
		AudioInputStream audio;

		try {
			audio = AudioSystem.getAudioInputStream(url);
			Clip c;
			c = AudioSystem.getClip();
			c.open(audio);
			return c;
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void setVolume(float volume) {
		this.songVolume = volume;
		updateSongVolume();
		updateEffectsVolume();
	}

	public void stopSong() {
		if (songs[currentSongID].isActive())
			songs[currentSongID].stop();
	}

	public void setLevelSong(int levelIndex) {
		switch (levelIndex) {
		case 0:
		case 1:
			playSong(LEVEL_1);
			break;
		case 2:
			playSong(LEVEL_2);
			break;
		case 3:
			playSong(LEVEL_3);
			break;
		}
	}

	public void playAttackSound() {
		int start = 2;
		start += rand.nextInt(3);
		playEffect(start);
	}

	public void playEffect(int effect) {
		effects[effect].setMicrosecondPosition(0);
		effects[effect].start();
	}

	public void playSong(int song) {
		stopSong();

		currentSongID = song;
		updateSongVolume();
		songs[currentSongID].setMicrosecondPosition(0);
		songs[currentSongID].loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void toggleSongMute() {
		this.songMute = !songMute;
		for (Clip c : songs) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(songMute);
		}
	}

	public void toggleEffectMute() {
		this.effectMute = !effectMute;
		for (Clip c : effects) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(effectMute);
		}

		if (!effectMute) {
			playEffect(JUMP);
		}
	}

	private void updateSongVolume() {

		FloatControl gainControl = (FloatControl) songs[currentSongID].getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain = (range * songVolume) + gainControl.getMinimum();
		gainControl.setValue(gain);

	}

	private void updateEffectsVolume() {
		for (Clip c : effects) {
			FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			float range = gainControl.getMaximum() - gainControl.getMinimum();
			float gain = (range * effectsVolume) + gainControl.getMinimum();
			gainControl.setValue(gain);
		}
	}

}
