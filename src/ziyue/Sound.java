package ziyue;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	public URL url;
	public AudioInputStream audioIn;
	public Clip clip;
	
	private String fileName;
	
	public Sound(String fileName) {
		this.fileName = fileName;
	}
	
	public void play() {
		try {
	         url = this.getClass().getClassLoader().getResource(fileName);
	         audioIn = AudioSystem.getAudioInputStream(url);
	         clip = AudioSystem.getClip();
	         clip.open(audioIn);
	         clip.start();
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
	}
	
	public void playLoop() {
		try {
	         url = Sound.class.getClassLoader().getResource(fileName);
	         audioIn = AudioSystem.getAudioInputStream(url);
	         clip = AudioSystem.getClip();
	         clip.open(audioIn);
	         
	         clip.loop(Clip.LOOP_CONTINUOUSLY);
	         
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
	}
}
