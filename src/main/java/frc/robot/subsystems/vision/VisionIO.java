package frc.robot.subsystems.vision;

import frc.robot.limelight.LimelightHelpers;
import org.littletonrobotics.junction.AutoLog;

public interface VisionIO
{
    @AutoLog
    public static class VisionIOInputs
    {
        public double limelightX = 0.0;
        public double limelightY = 0.0;

        public boolean ResultsAreStale = false;

        public LimelightHelpers.Results LatestResults = null;
    }

    public default void updateInputs(VisionIOInputs inputs) {}
}
