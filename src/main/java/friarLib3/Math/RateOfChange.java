package friarLib3.Math;

import edu.wpi.first.wpilibj.Timer;

public class RateOfChange {

    private double previousValue = 0;
    private double latestValue = 0;

    private double previousTime = 0;
    private double latestTime = 0;

    private Timer timer = new Timer();

    public RateOfChange()
    {
        timer.reset();
        timer.start();
    }

    public double update(double newValue)
    {
        previousValue = latestValue;
        latestValue = newValue;

        previousTime = latestTime;
        latestTime = timer.get();

        return getRoC();
    }

    public double getRoC()
    {
        return (latestValue - previousValue) / (latestTime - previousTime);
    }

}
