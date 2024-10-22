package frc.robot.subsystems.swerve;

import edu.wpi.first.math.geometry.Rotation2d;

public class AzimuthConstants
{
    /* Angles (raw) */

    // TODO: Tune all on practice day
    // Front of robot is intake

    public static final Rotation2d aziAmpRed = Rotation2d.fromDegrees(90);

    public static final Rotation2d aziAmpBlue = Rotation2d.fromDegrees(-aziAmpRed.getDegrees());

    public static final Rotation2d aziSubwooferFront = Rotation2d.fromDegrees(0);

    public static final Rotation2d aziSubwooferLeft = Rotation2d.fromDegrees(-30);

    public static final Rotation2d aziSubwooferRight =
        Rotation2d.fromDegrees(aziSubwooferLeft.getDegrees());

    public static final Rotation2d aziSourceRed = Rotation2d.fromDegrees(60);

    public static final Rotation2d aziSourceBlue = Rotation2d.fromDegrees(-aziSourceRed.getDegrees());

    public static final Rotation2d aziFeederRed = Rotation2d.fromDegrees(45);

    public static final Rotation2d aziFeederBlue = Rotation2d.fromDegrees(-aziFeederRed.getDegrees());

    public static final Rotation2d aziCleanUp = Rotation2d.fromDegrees(180);

    /* Timeout */
    public static final double aziCommandTimeOut = 1.5;
}
