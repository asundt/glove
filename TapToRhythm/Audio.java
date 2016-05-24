package TapToRhythm;

import javax.sound.sampled.*;
/**
 * Created by Alex on 5/22/16.
 */
public class Audio {
    /**
     * This class is designed to take the filename of a .wav
     * inside of the com.Game package and turn it into a Clip with a playClip()
     * method
     * @author michaelsnowden
     * I've borrowed this class to use for playing a click sound for a metronome in
     * my TapToRhythm class
     */
    Clip clip;

    public Audio(String st)
    {
        clip = null;

        try{
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource(st));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void playClip()
    {
        if( clip.isRunning() )
        {
            clip.stop();
        }
        clip.setFramePosition( 0 );
        clip.start();
    }
}
