/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatizacionavanzada;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import pistonccontrol.PistonControl;

/**
 *
 * @author NORE
 */
public class WritableCommands {

    public static final int START = 0;
    public static final int COLOR1 = 1;
    public static final int COLOR2 = 2;
    public static final int END_READ = 3;
    
    private SimpleBooleanProperty start;
    public SimpleBooleanProperty stop;
    public SimpleBooleanProperty color1;
    public SimpleBooleanProperty color2;

    public WritableCommands() {
        start = new SimpleBooleanProperty(false);
        start.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                // get modbus instance from crixus and write
                System.out.println("ME LLAMASTE?");
                Crixus.getInstance().getModbus().writeCommandThread(WritableCommands.START, newValue);

            }

        });

        stop = new SimpleBooleanProperty(false);
        stop.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //.getInstance().getModbus().writeCommand(WritableCommands.STOP, newValue);
                Crixus.getInstance().getModbus().writeCommandThread(4, newValue);
            }

        });
        
        color1 = new SimpleBooleanProperty(false);
        color1.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //.getInstance().getModbus().writeCommand(WritableCommands.STOP, newValue);
                Crixus.getInstance().getModbus().writeCommandThread(4, newValue);
            }

        });
        
        color2 = new SimpleBooleanProperty(false);
        color2.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //.getInstance().getModbus().writeCommand(WritableCommands.STOP, newValue);
                Crixus.getInstance().getModbus().writeCommandThread(4, newValue);
            }

        });
      
    }

    public void setStart(boolean value) {
        start.set(value);
    }

    public boolean getStart() {
        return start.get();
    }

   
}
