/**
 * Created by Alex on 5/19/16.
 */

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TapToRhythm extends Application {
    private GridPane root = new GridPane();
    private final static int STARTTIME = 4;
    private final Slider tempoSlider = new Slider(40, 160, 120);
    private final Label tempoLabel = new Label("Choose a tempo:");
    private final Label tempoValue = new Label(
            Integer.toString((int) Math.round(tempoSlider.getValue())));
    private final Label precisionLabel = new Label("Precision:");
    private ToggleGroup toggleGroup = new ToggleGroup();
    private int countdownTime = STARTTIME;
    private double period;
    private double tempo;
    private double precision;
    private Timeline timeline;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        setup();
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("TapToRhythm");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setup() {
        root.getChildren().clear();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setHgap(40);
        root.setVgap(10);
        root.setAlignment(Pos.CENTER);
        //root.setGridLinesVisible(true);
        root.add(tempoLabel, 0, 0, 3, 1);
        root.add(tempoSlider, 0, 1, 6, 1);
        root.add(tempoValue, 5, 0);
        tempoSlider.setMajorTickUnit(10);
        tempoSlider.setMinorTickCount(4);
        tempoSlider.setSnapToTicks(true);
        tempoSlider.showTickLabelsProperty().setValue(true);
        tempoSlider.showTickMarksProperty().setValue(true);
        tempoSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number newVal) {
                tempoValue.setText(Integer.toString(newVal.intValue()));
            }
        });
        Button btn = new Button();
        btn.setText("Start");
        btn.setOnAction(new StartEventHandler());
        root.add(btn, 5, 5);
        countdownTime = STARTTIME;
        RadioButton rb8 = new RadioButton("1/8");
        rb8.setUserData(1/8);
        rb8.setToggleGroup(toggleGroup);
        RadioButton rb16 = new RadioButton("1/16");
        rb16.setUserData(1/16);
        rb16.setToggleGroup(toggleGroup);
        rb16.setSelected(true);
        RadioButton rb32 = new RadioButton("1/32");
        rb32.setUserData(1/32);
        rb32.setToggleGroup(toggleGroup);
        HBox rb = new HBox(10, precisionLabel, rb8, rb16, rb32);
        root.add(rb, 0, 3, 6, 1);
    }

    private class StartEventHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            precision = (double) toggleGroup.getSelectedToggle().getUserData();
            tempo = Math.round(tempoSlider.getValue());
            period = 60 / tempo;
            root.getChildren().clear();
            ColumnConstraints column0 = new ColumnConstraints();
            column0.setHalignment(HPos.CENTER);
            root.getColumnConstraints().add(column0);
            countdown();
            Button btn = new Button();
            btn.setText("Reset");
            btn.setOnAction(new ResetEventHandler());
            root.add(btn, 0, 2);
            countdownTime = STARTTIME;
        }

        private void countdown() {
            Text txt = new Text();
            txt.setFont(new Font(20));
            root.add(txt, 0, 0);
            txt.setText(Integer.toString(countdownTime));
            timeline = new Timeline();
            timeline.setCycleCount(4);
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(period),
                    new CountdownEvent(txt)));
            timeline.playFromStart();
        }

        private class CountdownEvent implements EventHandler<ActionEvent> {
            private Text txt;
            public CountdownEvent(Text txt) {
                this.txt = txt;
            }

            @Override
            public void handle (ActionEvent event){
                countdownTime -= 1;
                txt.setText(Integer.toString(countdownTime));
                if (countdownTime == 0) {
                    txt.setText("Tap!");
                    timeline.stop();
                }
            }
        }
    }

    private class ResetEventHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            timeline.stop();
            setup();
        }
    }
}
