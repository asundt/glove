package TapToRhythm;

/**
 * Created by Alex on 5/30/16.
 */
public class Tap {
    long startTime;
    long endTime;
    long elapsedTime;
    boolean hit;

    public Tap(boolean hit) {
        startTime = System.nanoTime();
        this.hit = hit;
    }

    public void end() {
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
    }
}
