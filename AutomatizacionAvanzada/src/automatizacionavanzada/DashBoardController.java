/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatizacionavanzada;

import com.google.common.base.Stopwatch;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author NORE
 */
public class DashBoardController implements Initializable {

    @FXML
    private AnchorPane paneTabel;
    @FXML
    private LineChart<?, ?> chart;
    @FXML
    private RadioButton radioUno;
    @FXML
    private RadioButton radioDos;
    @FXML
    private Button changeColor;
    @FXML
    private TextField cantidad;
    @FXML
    private Button set;
    @FXML
    private Button btnNew;
    @FXML
    private Button startBtn;
    @FXML
    private ImageView imgLoad;
    @FXML
    private ProgressBar bar;
    @FXML
    private Text boxCount;

    final ToggleGroup group = new ToggleGroup();
    private int number = 0;
    private int currentAmount = 0;
    private boolean estadoStart = false;

    private XYChart.Series<Double, Long> series1;
    private ObservableList<XYChart.Series<Double, Long>> lineChartData;

    private Stopwatch stopwatch;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Crixus.getInstance().setDashBoardInstance(this);
        boxCount.setText("Procesadas: " + String.valueOf(currentAmount));
        // TODO
        radioUno.setToggleGroup(group);
        radioUno.setUserData("color1");
        radioDos.setToggleGroup(group);
        radioDos.setUserData("color2");
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                System.out.println(newValue.getUserData());
                if (oldValue != null) {
                    switch (oldValue.getUserData().toString()) {
                        case "color1":
                            System.out.println("oldvalue color1");
                            Crixus.getInstance().getModbus().writeCommandThread(WritableCommands.COLOR1, Boolean.FALSE);
                            break;
                        case "color2":
                            System.out.println("oldvalue color2");
                            Crixus.getInstance().getModbus().writeCommandThread(WritableCommands.COLOR2, Boolean.FALSE);
                            break;
                    }
                }
            }

        });
    }

    @FXML
    private void aksiNew(ActionEvent event) {
        System.out.println("hola!!!!!!!!!!!!!!!!!!!");
    }

    @FXML
    private void setColor(ActionEvent event) {
        Toggle toggle = group.getSelectedToggle();
        if (toggle != null) {
            System.out.println(toggle.getUserData());
            switch (toggle.getUserData().toString()) {
                case "color1":
                    System.out.println("poner color1");
                    Crixus.getInstance().getModbus().writeCommandThread(WritableCommands.COLOR1, Boolean.TRUE);
                    break;
                case "color2":
                    System.out.println("poner color2");
                    Crixus.getInstance().getModbus().writeCommandThread(WritableCommands.COLOR2, Boolean.TRUE);
                    break;
            }
        }
    }

    @FXML
    private void setQuantity(ActionEvent event) {
        try {
            int amount = Integer.parseInt(cantidad.getText());
            number = amount;
            currentAmount = 0;
            boxCount.setText("Procesadas: " + String.valueOf(currentAmount));
            System.out.println(amount);
        } catch (Exception e) {

        }
    }

    @FXML
    private void start(ActionEvent event) {
        if (number != 0 && group.getSelectedToggle() != null) {
            if (estadoStart == false) {
                estadoStart = true;
                stopwatch = Stopwatch.createStarted();
                System.out.println("start proccess");
                Crixus.getInstance().getModbus().writeCommandThread(WritableCommands.START, Boolean.TRUE);
                startBtn.setText("STOP");
            } else {
                stopProcces();
            }
        } else {
            System.out.println("inserta datos");
        }

    }

    public void stopProcces() {
        stopwatch.stop();
        estadoStart = false;
        Crixus.getInstance().getModbus().writeCommandThread(WritableCommands.START, Boolean.FALSE);
        startBtn.setText("START");
    }

    @FXML
    private void testUltrasonic(ActionEvent event) {
        try {
            Crixus.getInstance().getModbus().writeCommandThread(WritableCommands.END_READ, Boolean.TRUE);
        } catch (Exception e) {

        }
    }

    private void setUpGraph(Rectangle2D screen) {
        NumberAxis xAxis = new NumberAxis("#Lote", 0, 50, 5);
        NumberAxis yAxis = new NumberAxis("Tiempo", 1, 60, 10);

        series1 = new XYChart.Series<>();

        lineChartData = FXCollections.observableArrayList(
                series1
        );

        chart = new LineChart(xAxis, yAxis, lineChartData);
        chart.setTranslateX(2 * screen.getWidth() / 4);
        chart.setTranslateY(screen.getHeight() / 2);
        chart.setPrefWidth(screen.getWidth() - (2 * screen.getWidth() / 4));
        chart.setPrefHeight((screen.getHeight() / 2) - 10);
        //group.getChildren().add(chart);
    }

    private void updateStatistics(long timePass, double value) {
        // timeline.pause();

        // series1.getData().add(new LineChart.Data<Integer, Integer>(dataCollected.size(), (int) (stopwatch.elapsed(TimeUnit.SECONDS))));
        series1.getData().add(new LineChart.Data<>(value, (stopwatch.elapsed(TimeUnit.SECONDS))));
        stopwatch.reset();
        stopwatch.start();
    }

    public Text getBoxCount() {
        return boxCount;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setBoxCount(Text boxCount) {
        this.boxCount = boxCount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
