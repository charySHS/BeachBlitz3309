package frc.robot.subsystems.vision;

import frc.robot.limelight.LimelightHelpers;
import frc.robot.subsystems.vision.VisionIO.VisionIOInputs;
import org.littletonrobotics.junction.AutoLogOutput;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.Set;

public class Vision extends SubsystemBase
{
    private final static Set<Integer> ValidTags = Set.of(
        1, 2     // Blue Source
        , 4          // Red Speaker (center)
        , 5          // Red Amp
        , 6          // Blue Amp
        , 7          // Blue Speaker (center)
        , 9, 10      // Red Source
        , 11, 12, 13 // Red Stage
        , 14, 15, 16 // Red Stage
    );

    private final VisionIO VisionIO;
    private final VisionIOInputs visionIOAutoLogged = new VisionIOInputs();

    private static double limelightX = 0.0;
    private static double limelightY = 0.0;

    private static boolean ResultsAreStale = true;

    private static LimelightHelpers.Results LatestResults = null;

    public Vision(VisionIO visionIO) { this.VisionIO = visionIO; }

    public static void UpdateResults()
    {
        if (ResultsAreStale)
        {
            limelightX = LimelightHelpers.getTX(VisionConstants.limelight);
            limelightY = LimelightHelpers.getTY(VisionConstants.limelight);

            LatestResults = LimelightHelpers.getLatestResults(VisionConstants.limelight).targetingResults;

            ResultsAreStale = false;
        }
    }

    @Override
    public void periodic()
    {
        ResultsAreStale = true;
        UpdateResults();

        VisionIO.updateInputs(visionIOAutoLogged);
    }

    @AutoLogOutput
    public double GetDistanceToNote()
    {
        return -(VisionConstants.limelightHeightInches - VisionConstants.noteHeightInches)
            / Math.tan(
                Units.degreesToRadians(
                    visionIOAutoLogged.limelightY + VisionConstants.limelightAngleDegrees
                )
        );
    }

    public Pose2d GetNotePose(Pose2d robotPose)
    {
        return robotPose
            .transformBy(VisionConstants.robotToCam)
            .transformBy(
                new Transform2d(
                    new Translation2d(
                        Units.inchesToMeters(GetDistanceToNote()),
                        Rotation2d.fromDegrees(visionIOAutoLogged.limelightX)),
                    Rotation2d.fromDegrees(visionIOAutoLogged.limelightX)
                )
            );
    }

    @AutoLogOutput
    public double getCompensatedLimelightX() { return limelightX; }

    @AutoLogOutput
    public double getCompensatedLimelightY() { return limelightY; }

    @AutoLogOutput
    public static LimelightHelpers.LimelightTarget_Fiducial GetBestTarget()
    {
        UpdateResults();

        LimelightHelpers.LimelightTarget_Fiducial bestTarget = null;

        for (LimelightHelpers.LimelightTarget_Fiducial target : LatestResults.targets_Fiducials)
        {
            if (ValidTags.contains((int)target.fiducialID))
            {
                if (bestTarget == null || Math.abs(target.tx) < Math.abs(bestTarget.tx))
                {
                    bestTarget = target;
                }
            }
        }

        return bestTarget;
    }

    public double getLimelightX() { return limelightX; }

    public double getLimelightY() { return limelightY; }

}
