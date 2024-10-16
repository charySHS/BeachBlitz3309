package friarLib3.Logging;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix.ErrorCode;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj.DriverStation;

/// It's an enum as it's the easiest way to
/// make a singleton in Java.
/// Please also note that this class does not contain
/// Monologue initialization code or initialization code
/// for any other logging outlets

public enum FBLogger
{
    INSTANCE;

    private Kattio io;
    private int linesPrinted = 0;

    private FBLogger()
    {
        this.io = new Kattio(System.in, System.out);
    }

    public static FBLogger getInstance()
    {
        return INSTANCE;
    }

    public <T> StatusSignal<T> monitorStatusSignal
        (
            StatusSignal<T> signal,
            int canDeviceID,
            String canDeviceBus,
            String itemName,
            String methodName
        )
    {
        Logger.recordOutput(
            itemName + " | " + canDeviceID + " | " + canDeviceBus,
            "Signal in "
                + signal.getName()
                + "( "
                + methodName
                + " ): "
                + signal.getStatus()
                        .toString()
                + " canDeviceID: "
                + canDeviceID
                + " canDeviceBus: "
                + canDeviceBus
                + " Device Name: "
                + signal.getName());
        if (signal.getStatus()
                  .isError())
        {
            println("---- ERROR ----");
            println(
                "Error in "
                    + signal.getName()
                    + "( "
                    + methodName
                    + " )"
                    + ": "
                    + signal.getStatus()
                            .toString());
            println("canDeviceID: " + canDeviceID + " canDeviceBus: " + canDeviceBus);
            println("Device Name: " + signal.getName());
            println("---- ERROR ----");
        }

        return signal;
    }

    public void info(String message)
    {
        println("[INFO]: " + message);
    }

    public void warn(String message)
    {
        println("[WARNING: " + message);
        DriverStation.reportWarning("[WARNING: " + message, false);
    }

    public void error(String message)
    {
        println("[ERROR]: " + message);
        DriverStation.reportError("[ERROR]: " + message, false);
    }

    public void error(String message, ErrorCode errorCode)
    {
        println("[ERROR]: " + message + " | Error Code: " + errorCode);
        DriverStation.reportError("[ERROR]: " + message, false);
        println("Traceback (most recent call last)");
        for (StackTraceElement ste : Thread.currentThread()
                                           .getStackTrace())
        {
            println(ste + "\n");
        }
    }

    private void println (String message)
    {
        // io.println(message);
        // linesPrinted += 1;
        // if (linesPrinted >= Constants.kLogLinesBeforeFlush) {
        // io.flush();
        // linesPrinted = 0;
        // }
    }


}
