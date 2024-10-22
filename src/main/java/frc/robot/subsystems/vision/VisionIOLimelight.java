package frc.robot.subsystems.vision;

import frc.robot.limelight.Limelight;

public class VisionIOLimelight implements VisionIO
{
    public VisionIOLimelight() {}

    @Override
    public void updateInputs(VisionIOInputs inputs)
    {
        inputs.limelightX = Limelight.getTX(VisionConstants.noteDetectionLimelight);
        inputs.limelightY = Limelight.getTY(VisionConstants.noteDetectionLimelight);

        inputs.LatestResults = null;
        inputs.ResultsAreStale = false;
    }
}
