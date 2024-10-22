package frc.robot.subsystems.vision;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;

public final class VisionConstants {
    public static final String limelight = "limelight";
    public static final String driverCam = "dash-cam";

    public static final double limelightAngleDegrees = 1; // TODO: idk tune
    public static final double limelightHeightInches = 1; // TODO: idk tune
    public static final double noteHeightInches = 2; // TODO: idk tune

    public static final Transform2d robotToCam =
        new Transform2d(
            new Translation2d(0, 0), new Rotation2d(0)); // pretty sure its too small to matter
}d