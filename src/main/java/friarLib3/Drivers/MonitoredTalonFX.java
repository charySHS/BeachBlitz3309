package friarLib3.Drivers;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.ControlModeValue;
import friarLib3.Logging.FBLogger;

public class MonitoredTalonFX extends TalonFX {
    private final int canDeviceID;
    private final String canDeviceBus;

    public MonitoredTalonFX(int canDeviceID, String canDeviceBus) {
        super(canDeviceID, canDeviceBus);
        this.canDeviceID = canDeviceID;
        this.canDeviceBus = canDeviceBus;
    }

    public MonitoredTalonFX(int canDeviceID) {
        super(canDeviceID);
        this.canDeviceID = canDeviceID;
        this.canDeviceBus = "rio"; // defaults to "rio" if not specified
    }

    // Override all the methods that we use in TalonFX to monitor the status of the
    // signals

    @Override
    public StatusSignal<Double> getMotorVoltage() {
        return FBLogger.getInstance()
                       .monitorStatusSignal(
                           super.getMotorVoltage(),
                           canDeviceID,
                           canDeviceBus,
                           "MonitoredTalonFX",
                           "MonitoredTalonFX.getIntakeMotorVoltage()");
    }

    @Override
    public StatusSignal<Double> getRotorVelocity() {
        return FBLogger.getInstance()
                       .monitorStatusSignal(
                           super.getRotorVelocity(),
                           canDeviceID,
                           canDeviceBus,
                           "MonitoredTalonFX",
                           "MonitoredTalonFX.getRotorVelocity()");
    }

    @Override
    public StatusSignal<Double> getRotorPosition() {
        return FBLogger.getInstance()
                       .monitorStatusSignal(
                           super.getRotorPosition(),
                           canDeviceID,
                           canDeviceBus,
                           "MonitoredTalonFX",
                           "MonitoredTalonFX.getRotorPosition()");
    }

    @Override
    public StatusSignal<Double> getStatorCurrent() {
        return FBLogger.getInstance()
                       .monitorStatusSignal(
                           super.getStatorCurrent(),
                           canDeviceID,
                           canDeviceBus,
                           "MonitoredTalonFX",
                           "MonitoredTalonFX.getStatorCurrent()");
    }

    @Override
    public StatusSignal<Double> getVelocity() {
        return FBLogger.getInstance()
                       .monitorStatusSignal(
                           super.getVelocity(),
                           canDeviceID,
                           canDeviceBus,
                           "MonitoredTalonFX",
                           "MonitoredTalonFX.getVelocity()");
    }

    @Override
    public StatusSignal<Double> getSupplyCurrent() {
        return FBLogger.getInstance()
                       .monitorStatusSignal(
                           super.getSupplyCurrent(),
                           canDeviceID,
                           canDeviceBus,
                           "MonitoredTalonFX",
                           "MonitoredTalonFX.getSupplyCurrent()");
    }

    @Override
    public StatusSignal<Double> getPosition() {
        return FBLogger.getInstance()
                       .monitorStatusSignal(
                           super.getPosition(),
                           canDeviceID,
                           canDeviceBus,
                           "MonitoredTalonFX",
                           "MonitoredTalonFX.getPosition()");
    }

    @Override
    public StatusSignal<ControlModeValue> getControlMode() {
        return FBLogger.getInstance()
                       .monitorStatusSignal(
                           super.getControlMode(),
                           canDeviceID,
                           canDeviceBus,
                           "MonitoredTalonFX",
                           "MonitoredTalonFX.getControlMode()");
    }
}