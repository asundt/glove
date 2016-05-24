package TapToRhythm;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

import java.util.LinkedList;

/**
 * Created by Alex on 5/22/16.
 */
public class TapListener {
    public class TapEventHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent keyEvent) {
            if (listenerActive) {
                if (taps.isEmpty()) {
                    taps.add(new Tap(true));
                } else if (!taps.getLast().hit) {
                    taps.getLast().end();
                    taps.add(new Tap(true));
                }
            }
        }
    }

    public class ReleaseEventHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent keyEvent) {
            if (listenerActive) {
                if (!taps.isEmpty()) {
                    taps.getLast().end();
                }
                taps.add(new Tap(false));
            }
        }
    }

    private class Tap {
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

    private boolean listenerActive = false;
    LinkedList<Tap> taps;
    long listenStartTime;

    public TapListener(Scene scene) {
        taps = new LinkedList<>();
        scene.setOnKeyPressed(new TapEventHandler());
        scene.setOnKeyReleased(new ReleaseEventHandler());
    }

    public void setActive(boolean state) {
        listenerActive = state;
    }

    public void start() {
        setActive(true);
        listenStartTime = System.nanoTime();
        taps = new LinkedList<>();
    }

    public LinkedList<Tap> stop() {
        setActive(false);
        if (taps.isEmpty()) {
            Tap rest = new Tap(false);
            rest.startTime = listenStartTime;
            taps.addFirst(rest);
        }
        taps.getLast().end();
        if (taps.getFirst().hit) {
            Tap first = new Tap(false);
            first.startTime = listenStartTime;
            first.endTime = taps.getFirst().startTime;
            first.elapsedTime = first.endTime - first.startTime;
            taps.addFirst(first);
        }
        return taps;
    }

    public void analyze(double tempo, double precision) {
        long baseUnit = (long) ((60 / tempo) * 4 * precision * Math.pow(10, 9)); //in nanoseconds
        for (Tap tap: taps) {
            if (tap.hit) {
                int length = Math.round(tap.elapsedTime / baseUnit);
                if (length == 0) {
                    length = 1;
                }
                System.out.println("Hold " + length);
            } else if (Math.round(tap.elapsedTime / baseUnit) > 0) {
                System.out.println("Rest " + Math.round(tap.elapsedTime / baseUnit));
            }
        }
    }

}
