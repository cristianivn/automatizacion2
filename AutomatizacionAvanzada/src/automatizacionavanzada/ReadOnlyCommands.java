/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatizacionavanzada;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 *
 * @author NORE
 */
public class ReadOnlyCommands {

    public SimpleBooleanProperty[] states = new SimpleBooleanProperty[4];
    private SimpleDoubleProperty time = new SimpleDoubleProperty(5);

    public ReadOnlyCommands() {
        for (int i = 0; i < states.length; i++) {
            int lis = i;
            states[i] = new SimpleBooleanProperty(false);
            states[i].addListener(new ChangeListener<Boolean>() {

                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    switch (lis) {
                        case 0:
                            //   System.out.println("LISTENER " + lis);
                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        /* if (newValue) {
                                         System.out.println("activado");
                                         } else {
                                         System.out.println("desactivado");
                                         }*/

                                        if (!oldValue && newValue) { // cambiar a property
                                            System.out.println("increase box count");
                                            Crixus.getInstance().getDashBoiardInstance().setCurrentAmount(Crixus.getInstance().getDashBoiardInstance().getCurrentAmount() + 1);
                                            Crixus.getInstance().getDashBoiardInstance().getBoxCount().setText("Procesadas: " + String.valueOf(Crixus.getInstance().getDashBoiardInstance().getCurrentAmount()));
                                            if (Crixus.getInstance().getDashBoiardInstance().getCurrentAmount() == Crixus.getInstance().getDashBoiardInstance().getNumber()) {
                                                Crixus.getInstance().getDashBoiardInstance().stopProcces();
                                            }
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            });

                            break;
                        case 1:
                            //     System.out.println("LISTENER " + lis);
                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        if (!oldValue && newValue) { // cambiar a property
                                           
                                            Crixus.getInstance().getDashBoiardInstance().
                                                    updateStatistics(
                                                            25);
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            });
                            break;
                        case 2:
                            //     System.out.println("LISTENER " + lis);
                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        if (!oldValue && newValue) { // cambiar a property

                                            System.out.println("lado b");
                                            Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(time.getValue()), new EventHandler<ActionEvent>() {

                                                @Override
                                                public void handle(ActionEvent event) {
                                                    System.out.println("activar transicion!!: " + time.getValue());
                                                    // LEER SENSOR Y GRAFICAR
                                                    Crixus.getInstance().getModbus().writeCommandThread(WritableCommands.END_READ, Boolean.TRUE);
                                                }
                                            }));
                                            fiveSecondsWonder.setCycleCount(1);
                                            fiveSecondsWonder.play();
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            });
                            break;
                        case 3:
                            //     System.out.println("LISTENER " + lis);
                            Platform.runLater(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        if (!oldValue && newValue) { // cambiar a property

                                            System.out.println("lado c");
                                            Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(time.getValue()), new EventHandler<ActionEvent>() {

                                                @Override
                                                public void handle(ActionEvent event) {
                                                    System.out.println("activar transicion!!: " + time.getValue());
                                                    Crixus.getInstance().getModbus().writeCommandThread(WritableCommands.END_READ, Boolean.TRUE);
                                                }
                                            }));
                                            fiveSecondsWonder.setCycleCount(1);
                                            fiveSecondsWonder.play();
                                        }
                                    } catch (Exception e) {

                                    }
                                }
                            });
                            break;
                    }
                }

            });
        }
    }

    public void readRegisters() {

        try {
            if (Crixus.getInstance().getModbus().getMaster().isInitialized()) {
                boolean[] vals = Crixus.getInstance().getModbus().readDiscrete(
                        ModBus.SLAVE_ADDRESS, 0, 3);
                if (vals.length > 0) {
                    for (int i = 0; i < states.length; i++) {
                        if (!(vals[i] && states[i].getValue())) {
                            states[i].set(vals[i]);
                        }
                    }
                } else {
                    System.out.println("ES NULO EL ARREGLO");
                }
            } else {
                System.out.println("CONEXION NO ESTABLECIDA");
            }
        } catch (Exception e) {

        }

    }

    public SimpleDoubleProperty getTime() {
        return time;
    }

}
