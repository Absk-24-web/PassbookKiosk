package Main;
// Java program to play an Audio 
// file using Clip Object 

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class PlaySound
{ 

	// to store current position 
	 private static  Long currentFrame;
	 private static Clip clip;

	
	// current status of clip 
	private static String status;
	
	AudioInputStream audioInputStream; 
	public static String filePath; 

	// constructor to initialize streams and clip 
	public PlaySound()
	{ 
		try
		{
			// create AudioInputStream object 
			audioInputStream = 
					AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile()); 
			
			// create clip reference 
			clip = AudioSystem.getClip(); 
			
			// open audioInputStream to the clip 
			clip.open(audioInputStream); 
			
			//clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		} 
	} 

	
	
	// Method to play the audio 
	public static void play()
	{ 
		//start the clip 
		clip.start(); 
		
		status = "play"; 
	} 
	
	// Method to pause the audio 
	public void pause() 
	{ 
		if (status.equals("paused")) 
		{ 
			System.out.println("audio is already paused"); 
			return; 
		} 
		currentFrame =
		clip.getMicrosecondPosition();
		clip.stop(); 
		status = "paused"; 
	} 
	
	// Method to resume the audio 
	public void resumeAudio() throws UnsupportedAudioFileException, 
								IOException, LineUnavailableException 
	{ 
		if (status.equals("play")) 
		{ 
			System.out.println("Audio is already "+ 
			"being played"); 
			return; 
		} 
		clip.close(); 
		resetAudioStream(); 
		clip.setMicrosecondPosition(currentFrame); 
		this.play(); 
	} 
	
	// Method to restart the audio 
	public void restart() throws IOException, LineUnavailableException, 
											UnsupportedAudioFileException 
	{ 
		clip.stop(); 
		clip.close(); 
		resetAudioStream(); 
		currentFrame = 0L; 
		clip.setMicrosecondPosition(0); 
		this.play(); 
	} 
	
	// Method to stop the audio 
	public void stop() throws UnsupportedAudioFileException, 
	IOException, LineUnavailableException 
	{ 
		currentFrame = 0L; 
		clip.stop(); 
		clip.close(); 
	} 
	
	// Method to jump over a specific part 
	public void jump(long c) throws UnsupportedAudioFileException, IOException, 
														LineUnavailableException 
	{ 
		if (c > 0 && c < clip.getMicrosecondLength()) 
		{ 
			clip.stop(); 
			clip.close(); 
			resetAudioStream(); 
			currentFrame = c; 
			clip.setMicrosecondPosition(c); 
			this.play(); 
		} 
	} 
	
	// Method to reset audio stream 
	public void resetAudioStream() throws UnsupportedAudioFileException, IOException, 
											LineUnavailableException 
	{ 
		audioInputStream = AudioSystem.getAudioInputStream( 
		new File(filePath).getAbsoluteFile()); 
		clip.open(audioInputStream); 
		clip.loop(Clip.LOOP_CONTINUOUSLY); 
	} 

} 
