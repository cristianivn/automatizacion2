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
import javafx.application.Platform;
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
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;

/**
 * FXML Controller class
 *
 * @author NORE
 */
public class DashBoardController implements Initializable {

    @FXML
    private AnchorPane paneTabel;

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
    @FXML
    private VBox chartContainer;
    @FXML
    private Slider silverSurfer;

    final ToggleGroup group = new ToggleGroup();
    private int number = 0;
    private int currentAmount = 0;
    private boolean estadoStart = false;

    private ObservableList<XYChart.Series<Integer, Integer>> lineChartData;

    private XYChart.Series<Integer, Integer> series1;

    private Stopwatch stopwatch;
    LineChart chart;
    private static int x = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        silverSurfer.valueProperty().bindBidirectional(Crixus.getInstance().getRead().getTime());
        setUpGraph(Screen.getPrimary().getVisualBounds());
        Crixus.getInstance().setDashBoardInstance(this);
        boxCount.setText("Procesadas: " + String.valueOf(currentAmount));
        // TODO
        radioUno.setToggleGroup(group);
        radioUno.setUserData("color1");
        radioDos.setToggleGroup(group);
        radioDos.setUserData("color2");
        //roup.selectToggle(radioUno);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                //System.out.println(newValue.getUserData());
                System.out.println("listener activated");
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
        persistColor();
    }

    public void persistColor() {
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
        cualcomm();
    }

    public void cualcomm() {
        try {
            int amount = Integer.parseInt(cantidad.getText());
            number = amount;
            currentAmount = 0;
            boxCount.setText("Procesadas: " + String.valueOf(currentAmount));
            System.out.println(amount);
        } catch (Exception e) {

        }
    }

    public void updateText(String can) {
        cantidad.setText(can);
    }

    @FXML
    private void start(ActionEvent event) {
        startProccess();

    }

    public void startProccess() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                if (number != 0 && group.getSelectedToggle() != null) {
                    if (estadoStart == false) {
                        estadoStart = true;
                        stopwatch = Stopwatch.createStarted();
                        System.out.println("start proccess");
                        Crixus.getInstance().getModbus().writeCommandThread(WritableCommands.START, Boolean.TRUE);
                        startBtn.setText("STOP");
                        //series1.getData().clear();
                    } else {
                        stopProcces();
                    }
                } else {
                    System.out.println("inserta datos");
                }
            }
        });

    }

    public void stopProcces() {
        stopwatch.stop();
        stopwatch.reset();
        estadoStart = false;
        Crixus.getInstance().getModbus().writeCommandThread(WritableCommands.START, Boolean.FALSE);
        startBtn.setText("START");
        number = 0;
        cantidad.setText(String.valueOf("0"));
        boxCount.setText("Procesados: 0");
    }

    @FXML
    private void testUltrasonic(ActionEvent event) {
        testUltra();
    }

    public void testUltra() {
        try {
            Crixus.getInstance().getModbus().writeCommandThread(WritableCommands.END_READ, Boolean.TRUE);
        } catch (Exception e) {

        }
    }

    private void setUpGraph(Rectangle2D scrsetUpGrapheen) {
        // new NumberAxis(label,inicio,maximoValor,intervalos)
        NumberAxis xAxis = new NumberAxis("#Numero", 0, 50, 5);
        NumberAxis yAxis = new NumberAxis("Cantidad", 1, 60, 10);

        series1 = new XYChart.Series<>();

        lineChartData = FXCollections.observableArrayList(
                series1
        );

        chart = new LineChart(xAxis, yAxis, lineChartData);
        /*chart.setTranslateX(2 * screen.getWidth() / 4);
         chart.setTranslateY(screen.getHeight() / 2);
         chart.setPrefWidth(screen.getWidth() - (2 * screen.getWidth() / 4));
         chart.setPrefHeight((screen.getHeight() / 2) - 10);*/
        chartContainer.getChildren().add(chart);
    }

    public void updateStatistics(double value) {
        // timeline.pause();
        //System.out.println("UPDATE CHART: " + stopwatch.elapsed(TimeUnit.SECONDS) + " VALOR: " + value);
        // series1.getData().add(new LineChart.Data<Integer, Integer>(dataCollected.size(), (int) (stopwatch.elapsed(TimeUnit.SECONDS))));
        x = x + 2;
        series1.getData().add(new LineChart.Data<Integer, Integer>(x, (int) value));

        //stopwatch.start();
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

    public Stopwatch getStopwatch() {
        return stopwatch;
    }

    public RadioButton getRadioUno() {
        return radioUno;
    }

    public void setRadioUno(RadioButton radioUno) {
        this.radioUno = radioUno;
    }

    public RadioButton getRadioDos() {
        return radioDos;
    }

    public void setRadioDos(RadioButton radioDos) {
        this.radioDos = radioDos;
    }

    public ToggleGroup getGroup() {
        return group;
    }

}
