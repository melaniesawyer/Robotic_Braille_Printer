import javax.sound.sampled.*;
import java.io.*;

/**
 * A sample program is to demonstrate how to record sound in Java
 * author: www.codejava.net
 */
public class JavaSoundRecorder {
	// record duration, in milliseconds
	static final long RECORD_TIME = 3000;  // 1 minute

	// path of the wav file
	File wavFile = new File("RecordAudio1.wav");

	// format of audio file
	AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

	// the line from which audio data is captured
	TargetDataLine line;

	/**
	 * Defines an audio format
	 */
	AudioFormat getAudioFormat() {
		float sampleRate = 16000;
		int sampleSizeInBits = 8;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = true;
		AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
				channels, signed, bigEndian);
		return format;
	}

	/**
	 * Captures the sound and record into a WAV file
	 */
	void start() {
		try {
			AudioFormat format = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

			// checks if system supports the data line
			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("Line not supported");
				System.exit(0);
			}
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();   // start capturing

			AudioInputStream ais = new AudioInputStream(line);

			System.out.println("Start recording...");

			// start recording
			AudioSystem.write(ais, fileType, wavFile);

		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Closes the target data line to finish capturing and recording
	 */
	void finish() {
		line.stop();
		line.close();
		System.out.println("Finished");
	}

	/**
	 * Entry to run the program
	 */

	public static void play(String filename)
	{
		try
		{
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File(filename)));
			clip.start();
			while (!clip.isRunning()){
				Thread.sleep(10);
			}
			while (clip.isRunning()) {
				Thread.sleep(10);
			}
			clip.close();
		}
		catch (Exception exc)
		{
			exc.printStackTrace(System.out);
		}
	}

	public static void main(String[] args) {

		boolean firstTime = true;

		while(true) {

			int index = (int) (Math.floor(Math.random()*26));
			char letter = (char) (index + 65);
			String str = "python serial1.py " + Integer.toString(index);	
			try {
				Process k = Runtime.getRuntime().exec(str);
				k.waitFor();
			}
			catch (Exception e){
				e.printStackTrace();
			}
			System.out.println(letter);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			if(firstTime) {
				play("speak_beep.wav");
				firstTime = false;
			}
			boolean incorrect = true;
			while(incorrect) {
				play("beep.wav");

				boolean start = true;
				while(start) {
					final JavaSoundRecorder recorder = new JavaSoundRecorder();

					// creates a new thread that waits for a specified
					// of time before stopping
					Thread stopper = new Thread(new Runnable() {
							public void run() {
							try {
							Thread.sleep(RECORD_TIME);
							} catch (InterruptedException ex) {
							ex.printStackTrace();
							}
							recorder.finish();
							}
							});
					stopper.start();

					// start recording
					recorder.start();
					try {
						Process p = Runtime.getRuntime().exec("python test.py");
						p.waitFor();
						BufferedReader br = new BufferedReader(new FileReader("data.json"));
						String res = br.readLine();
						if(res != null && res.length() >= 8) {
							start = false;
							System.out.println(res.charAt(7));
							if(res.charAt(7) != letter) {
								play("incorrect.wav");
								incorrect = true;
							}
							if(res.charAt(7) == letter) {
								play("correct.wav");
								incorrect = false;
							}
						}
						else {
							play("invalid.wav");
							play("beep.wav");
						}
					} catch (Exception e){
						e.printStackTrace();
					}
				}

			}
		}
	}
}
