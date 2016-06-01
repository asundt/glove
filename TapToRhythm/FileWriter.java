package TapToRhythm;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Alex on 5/30/16.
 */
public class FileWriter {
    private String fileName;

    public FileWriter(String fileName) {
        this.fileName = fileName + ".h";
    }

    public void writeToFile(double tempo, LinkedList<Tap> taps) throws IOException {
        java.io.FileWriter writer = new java.io.FileWriter(fileName);
        writer.write(Integer.toString((int) Math.round(tempo)) + "\n");
        for (Tap t : taps) {
            if (t.hit) {
                writer.write("hit ");
            } else {
                writer.write("rest ");
            }
            writer.write(Long.toString(t.elapsedTime) + "\n");
        }
        writer.close();
    }
}
