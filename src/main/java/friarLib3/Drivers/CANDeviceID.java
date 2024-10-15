package friarLib3.Drivers;

import java.util.Objects;

public class CANDeviceID
{
    private final int DeviceNumber;
    private final String Bus;

    public CANDeviceID(int deviceNumber, String bus)
    {
        DeviceNumber = deviceNumber;
        Bus = bus;
    }

    // Use default bus name
    public CANDeviceID(int deviceNumber) { this(deviceNumber, ""); }

    public int getDeviceNumber() { return DeviceNumber; }

    public String getBus() { return Bus; }

    public boolean equals(CANDeviceID other) { return other.DeviceNumber == DeviceNumber && Objects.equals(other.Bus, Bus); }

}
