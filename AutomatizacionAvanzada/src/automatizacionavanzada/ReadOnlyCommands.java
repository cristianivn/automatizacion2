/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatizacionavanzada;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author NORE
 */
public class ReadOnlyCommands {

    public SimpleBooleanProperty[] states = new SimpleBooleanProperty[2];

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
                                        //Crixus.getInstance().getGemma().piston2.setVastagoState(newValue);
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
                        ModBus.SLAVE_ADDRESS, 0, 1);
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

}
