package friarLib3.Utils;

import edu.wpi.first.math.filter.SlewRateLimiter;

/**
 * Just like WPILib's SlewRateLimiter, but with different limits for
 * acceleration and deceleration.
 */
public class DoubleSlewRateLimiter
{
    private final SlewRateLimiter accelLimiter;
    private final SlewRateLimiter decelLimiter;

    private double lastSpeed;

    public DoubleSlewRateLimiter(double accelLimit, double decelLimit)
    {
        accelLimiter = new SlewRateLimiter(accelLimit);
        decelLimiter = new SlewRateLimiter(decelLimit);
    }

    public double calculate(double speed)
    {
        boolean isVelocityPositive = lastSpeed >= 0;
        boolean isAccelPositive = speed - lastSpeed >= 0;

        double out;
        if ((isAccelPositive && isVelocityPositive) || (!isAccelPositive && !isVelocityPositive))
        {
            out = accelLimiter.calculate(speed);
            decelLimiter.reset(lastSpeed);
        }
        else
        {
            out = decelLimiter.calculate(speed);
            accelLimiter.reset(lastSpeed);
        }
        lastSpeed = out;

        return out;
    }

    public void reset(double value)
    {
        accelLimiter.reset(value);
        decelLimiter.reset(value);
    }
}
