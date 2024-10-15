package friarLib3.Drivers;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.CANcoder;
import friarLib3.Logging.FBLogger;

public class MonitoredCANCoder extends CANcoder {
    private int canDeviceID;
    private String canDeviceBus;

    public MonitoredCANCoder(int deviceId, String canbus) {
        super(deviceId, canbus);
        this.canDeviceID = deviceId;
        this.canDeviceBus = canbus;
    }

    public MonitoredCANCoder(int deviceId) {
        super(deviceId);
        // Our DriveTrain can is on the "mani" bus.
        System.out.println(
            "MonitoredCANCoder: Using default canDeviceBus of 'rio' on device " + deviceId);
        this.canDeviceID = deviceId;
        this.canDeviceBus = "rio";
    }

    @Override
    public StatusSignal<Double> getAbsolutePosition() {
        return FBLogger.getInstance()
                       .moniterStatusSignal(
                           super.getAbsolutePosition(),
                           canDeviceID,
                           canDeviceBus,
                           "MonitoredCANCoder",
                           "MonitoredCANCoder.getAbsolutePosition()");
    }
}
