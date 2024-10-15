package friarLib3.Logging;

import org.littletonrobotics.junction.networktables.LoggedDashboardNumber;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;

import frc.robot.Constants;

/**
 * Class for a tunable number. Gets value from dashboard in tuning mode, returns default if not or
 * value not in dashboard.
 */

public class LoggedTunableNumber implements DoubleSupplier
{
    private static final String tableKey = "TunableNumbers";

    private final String key;
    private boolean hasDefault = false;
    private double defaultValue;
    private LoggedDashboardNumber dashboardNumber;
    private Map<Integer, Double> lastHasChangedValues = new HashMap<>();

    /**
     * Create a new LoggedTunableNumber
     *
     * @param dashboardKey Key on dashboard
     */
    public LoggedTunableNumber(String dashboardKey)
    {
        this.key = tableKey + "/" + dashboardKey;
        this.dashboardNumber = new LoggedDashboardNumber(key);
    }

    /**
     * Create a new LoggedTunableNumber with default value
     *
     * @param dashboardKey Key on dashboard
     * @param defaultValue Default value
     */
    public LoggedTunableNumber(String dashboardKey, double defaultValue)
    {
        this(dashboardKey);
        initDefault(defaultValue);
    }

    /**
     * Set default value of number. Can only be set once
     *
     * @param defaultValue Default value
     */
    public void initDefault(double defaultValue)
    {
        if (!hasDefault)
        {
            hasDefault = true;
            this.defaultValue = defaultValue;
            if (Constants.FeatureFlags.TuningMode)
            {
                dashboardNumber = new LoggedDashboardNumber(key, defaultValue);
            }
        }
    }

    /**
     * Get the current value from dashboard and in tuning mode
     *
     * @return The current value
     */
    public double get()
    {
        if (!hasDefault) { return 0.0; }
        else { return Constants.FeatureFlags.TuningMode ? dashboardNumber.get() : defaultValue; }
    }

    /**
     * Get current value, from dashboard and in tuning mode
     *
     * @param fallbackValue Default value to return to if no number in dashboard
     *
     * @return The current value
     */
    public double getOrUse(double fallbackValue)
    {
        dashboardNumber.setDefault(fallbackValue);
        return Constants.FeatureFlags.TuningMode ? dashboardNumber.get() : defaultValue;
    }

    /**
     * Checks whether the number has changed since our last check
     *
     * @param id Unique identifier for the caller to avoid conflicts when shared between multiple
     *     objects. Recommended approach is to pass the result of "hashCode()"
     * @return True if the number has changed since the last time this method was called, false
     *     otherwise.
     */
    public boolean hasChanged(int id)
    {
        double currentValue = get();
        Double lastValue = lastHasChangedValues.get(id);
        if (lastValue == null || currentValue != lastValue)
        {
            lastHasChangedValues.put(id, currentValue);
            return true;
        }

        return false;
    }

    /**
     * Runs action if any of the tunableNumbers have changed
     *
     * @param id Unique identifier for the caller to avoid conflicts when shared between multiple *
     *     objects. Recommended approach is to pass the result of "hashCode()"
     * @param action Callback to run when any of the tunable numbers have changed. Access tunable
     *     numbers in order inputted in method
     * @param tunableNumbers All tunable numbers to check
     */
    public static void ifChanged(
        int id, Consumer<double[]> action, LoggedTunableNumber... tunableNumbers)
    {
        if (Arrays.stream(tunableNumbers).anyMatch(tunableNumber -> tunableNumber.hasChanged(id)))
        {
            action.accept(Arrays.stream(tunableNumbers).mapToDouble(LoggedTunableNumber::get).toArray());
        }
    }

    /** Runs action if any tunableNumbers changed */
    public static void ifChanged(int id, Runnable action, LoggedTunableNumber... tunableNumbers){ ifChanged(id, values -> action.run(), tunableNumbers); }

    @Override
    public double getAsDouble() { return get(); }

}
