/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatizacionavanzada;

import com.serotonin.io.serial.SerialParameters;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.modbus4j.msg.ReadCoilsResponse;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.WriteCoilRequest;
import com.serotonin.modbus4j.msg.WriteCoilResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;

/**
 *
 * @author NORE
 */
public class ModBus implements Runnable {

    private ModbusMaster master;
// MODBUS network slave address  paquete  windows EmbeddedInteliggence
    public final static int SLAVE_ADDRESS = 1;

    // Serial port baud rate
    private final static int BAUD_RATE = 19200;

    private Crixus instance = null;

    public static boolean writeOrRead = false;

    public static boolean alive = true;

    static boolean valor;

    static int direccion;

    private boolean[] estados;

    private int i;
    private int i0;
    //q6 , q11
    public ModBus() {
        SerialParameters serialParameters = new SerialParameters();
        // Set the serial port of the MODBUS communication serialParameters
        serialParameters.
                setCommPortId("COM10");
        // Set no parity

        serialParameters.setParity(0);
        // Set the data bits is 8 bits serialParameters
        serialParameters.
                setDataBits(8);
        // Set to 1 stop bit

        serialParameters.setStopBits(1);

        serialParameters.setPortOwnerName("Numb nuts");
        // Serial port baud rate serialParameters
        serialParameters.
                setBaudRate(BAUD_RATE);

        ModbusFactory modbusFactory = new ModbusFactory();

        master = modbusFactory.createRtuMaster(serialParameters);
        master.setTimeout(50000);

        try {
            master.init();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * read digital inputs
     */
    private boolean[] readDiscreteInputTest(int slaveId, int start, int len) {

        boolean[] vals = new boolean[7];
        for (int i = start; i < len + 1; i++) {
            try {
                //i is the register number
                vals[i] = (boolean) master.getValue(1, RegisterRange.INPUT_STATUS, i, DataType.BINARY);
                //  System.out.println(i + " VALOR REGISTRO : " + vals[i]); // read coil
            } catch (ModbusTransportException ex) {
                Logger.getLogger(ModBus.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ErrorResponseException ex) {
                Logger.getLogger(ModBus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return vals;
    }

    /**
     *
     * Read Holding register
     *
     */
    private static void readHoldingRegistersTest(ModbusMaster master, int slaveId, int start, int len) {

        try {

            ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, start, len);

            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
            if (response.isException()) {
                System.out.println("Exception response: message =" + response.getExceptionMessage());
            } else {
                System.out.println(Arrays.toString(response.getShortData()));
            }

        } catch (ModbusTransportException e) {

            e.printStackTrace();

        }

    }

    /**
     *
     * write data to the holding register
     *
     */
    public static void writeRegistersTest(ModbusMaster master, int slaveId, int start, short[] values) {

        try {

            WriteRegistersRequest request = new WriteRegistersRequest(slaveId, start, values);

            WriteRegistersResponse response = (WriteRegistersResponse) master.send(request);
            if (response.isException()) {
                System.out.println("Exception response: message =" + response.getExceptionMessage());
            } else {
                System.out.println("success");
            }

        } catch (ModbusTransportException e) {

            e.printStackTrace();

        }

    }

    /*
     read coils
     */
    public static void readCoils(ModbusMaster master, int slaveId, int start, int len) {
        try {
            ReadCoilsRequest request = new ReadCoilsRequest(slaveId, start, start);

            ReadCoilsResponse response = (ReadCoilsResponse) master.send(request);

            if (response.isException()) {
                System.out.println("Exception response: message =" + response.getExceptionMessage());
            } else {
                System.out.println("SIZE: " + response.getShortData().length);
                System.out.println(Arrays.toString(response.getBooleanData()));
            }

        } catch (ModbusTransportException e) {

        }
    }

    /**
     *
     * write coils state
     *
     */
    public static void writeCoils(ModbusMaster master, int slaveId, int position, boolean value) {

        try {

            WriteCoilRequest request = new WriteCoilRequest(slaveId, position, value);

            WriteCoilResponse response = (WriteCoilResponse) master.send(request);
            if (response.isException()) {
                System.out.println("Exception response: message =" + response.getExceptionMessage());
            } else {
                System.out.println("success");
            }

        } catch (ModbusTransportException e) {

            e.printStackTrace();

        }

    }

    public void close() {
        master.destroy();
    }

    private void writeCommand(int registro, boolean value) {
        System.out.println("escribiendo registro al plc");
        try {
            master.setValue(SLAVE_ADDRESS, RegisterRange.COIL_STATUS, registro, DataType.BINARY, value); // change coils state
        } catch (ModbusTransportException ex) {
            Logger.getLogger(ModBus.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErrorResponseException ex) {
            Logger.getLogger(ModBus.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ModbusMaster getMaster() {
        return master;
    }

    private void writeContador(int intValue) {
        try {
            master.setValue(SLAVE_ADDRESS, RegisterRange.HOLDING_REGISTER, 0, DataType.TWO_BYTE_INT_UNSIGNED, intValue); // change Holding register value
        } catch (ModbusTransportException ex) {
            Logger.getLogger(ModBus.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErrorResponseException ex) {
            Logger.getLogger(ModBus.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void writeCommandStart(int registro, Boolean newValue) {
        System.out.println("escribiendo registro al plc");
        try {
            master.setValue(SLAVE_ADDRESS, RegisterRange.COIL_STATUS, registro, DataType.BINARY, newValue); // change coils state
        } catch (ModbusTransportException ex) {
            Logger.getLogger(ModBus.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ErrorResponseException ex) {
            Logger.getLogger(ModBus.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        while (alive) {
            if (writeOrRead) {
                //escribe comandos
                System.out.println("BLOQUE QUE ESCRIBE AL PLC");
                System.out.println("la direccion: " + direccion);
                System.out.println("el valor: " + valor);
                writeCommand(direccion, valor);
                ModBus.writeOrRead = false;
                //   System.out.println("YA ESCRIBI EN EL PLC");
            } else {
                //lee comandos
                Crixus.getInstance().getRead().readRegisters();
                estados = readDiscreteInputTest(SLAVE_ADDRESS, i, i0);
                System.out.println("BLOQUE QUE LEE ESTADOS DE LOS PISTONES");
            }
        }
    }

    public void writeCommandThread(int START, Boolean newValue) {
        direccion = START;
        valor = newValue;
        ModBus.writeOrRead = true;

    }

    public boolean[] readDiscrete(int SLAVE_ADDRESS, int i3, int i5) {
        i = i3;
        i0 = i5;
        return estados;
    }
}
