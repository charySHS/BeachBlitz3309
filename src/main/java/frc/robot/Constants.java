package frc.robot;

public class Constants
{

    // We do not include the AdvKit in the main FeatureFlags class - since this is
    // in Robot.java, and we'd prefer not to
    // have things break.

    /* FeatureFlags used in Robot.java */
    public static final boolean EnableAdvKit = true;
    public static final boolean EnableMonologue = false;
    public static final boolean DisableSubsystemsOnDisableInit = true;

    // Careful!
    public static final boolean OverrideBrownOutVoltage = true;
    public static final double OverridenBrownOutVoltage = 5.6; // We ball

    /* Drive configuration */
    public static final double StickDeadband = 0.1;
    public static final double RotationalDeadband = 0.12;
    public static final double AzimuthStickDeadband = 0.3;
    // Logging
    public static final int LogLinesBeforeFlush = 100;
    public static final boolean MonologueFileOnly = false;
    public static final boolean MonologueLazyLogging = false;

    public static final class FeatureFlags {

        // subsystems

        public static final boolean EasterEggEnabled = false;

        public static final boolean IntakeEnabled = true;

        public static final boolean ShooterEnabled = true;

        public static final boolean SwerveEnabled = false;

        public static final boolean ClimbEnabled = true;

        public static final boolean ArmEnabled = true;

        public static final boolean IgnoringDisabled = true;

        // logging
        public static final boolean TuningMode = false;
        public static final boolean DebugEnabled = false;
        public static final boolean DebugCommandEnabled = false;
        //public static final boolean RobotVizEnabled = true && !Robot.isReal();

        // features
        public static final boolean AutoAlignEnabled = false;
        public static final boolean RumbleEnabled = true;
        public static final boolean UsePrefs = true;
        public static final boolean ShuffleboardLayoutEnabled = true;
    }

    public static final class ShuffleboardConstants {
        public static final String DriverTabName = "Driver";
        public static final String OperatorTabName = "Operator";
        public static final String IntakeLayoutName = "Intake";
        public static final String SwerveLayoutName = "Swerve";
        public static final String ArmLayoutName = "Arm";
        public static final String ShooterLayoutName = "Shooter";
    }
}
