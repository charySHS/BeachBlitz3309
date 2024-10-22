package friarLib3.Drivers;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.Pigeon2;

import friarLib3.Logging.FBLogger;

public class MonitoredPigeon2 extends Pigeon2
{
    private int canDeviceID;
    private String canDeviceBus;

    public MonitoredPigeon2(int deviceID, String canbus)
    {
        super(deviceID, canbus);
        this.canDeviceID = deviceID;
        this.canDeviceBus = canbus;
    }

    public MonitoredPigeon2(int deviceID)
    {
        super(deviceID);
        // Drivetrain can is on the CANivore bus which starts with the Pigeon2
        System.out.println("MonitoredPigeon2: Using default canDeviceBus of 'rio on device " + deviceID);

        this.canDeviceID = deviceID;
        this.canDeviceBus = "rio";
    }

    @Override
    public StatusSignal<Double> getYaw()
    {
        return FBLogger.getInstance()
            .monitorStatusSignal(
                super.getYaw(),
                canDeviceID,
                canDeviceBus,
                "MonitoredPigeon2",
                "MonitoredPigeon2.getYaw()"
            );
    }
}
