package frc.robot.subsystems.swerve;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.mechanisms.swerve.*;

import com.choreo.lib.Choreo;
import com.choreo.lib.ChoreoTrajectory;
import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.mechanisms.swerve.*;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

import frc.robot.limelight.LimelightHelpers;
import frc.robot.subsystems.vision.Vision;

import java.util.Optional;
import java.util.function.Supplier;

public class FriarSwerveDrivetrain extends SwerveDrivetrain implements Subsystem
{
    private static final double SimLoopPeriod = 0.005; // 5ms
    private Notifier simNotifier = null;
    private double lastSimTime;

    public final SwerveRequest.ApplyChassisSpeeds AutoRequest =
        new SwerveRequest.ApplyChassisSpeeds()
            .withDriveRequestType(
                SwerveModule.DriveRequestType.Velocity
            ); // TODO: Very important tune

    public final SwerveRequest.RobotCentric NoteRequest =
        new SwerveRequest.RobotCentric()
            .withDriveRequestType(
                SwerveModule.DriveRequestType.Velocity
            ); // TODO: VERY IMPORTANT; TUNE!!

    /* Blue alliance sees forward as 0 deg */
    private final Rotation2d BlueAlliancePerspectiveRotation = Rotation2d.fromDegrees(0);
    /* Red alliance sees forward as 180 deg */
    private final Rotation2d RedAlliancePerspectiveRotation = Rotation2d.fromDegrees(180);

    // Check if applied operator perspective
    private boolean hasAppliedOperatorPerspective = false;

    public FriarSwerveDrivetrain( SwerveDrivetrainConstants drivetrainConstants, double OdemetryUpdateFrequency, SwerveModuleConstants... modules )
    {
        super(drivetrainConstants, OdemetryUpdateFrequency, modules);
        if (Utils.isSimulation()) { StartSimThread(); }

        ConfigurePathplanner();
    }

    public FriarSwerveDrivetrain( SwerveDrivetrainConstants drivetrainConstants, SwerveModuleConstants... modules )
    {
        super(drivetrainConstants, modules);
        if (Utils.isSimulation()) { StartSimThread(); }

        ConfigurePathplanner();
    }

    public Command PIDtoNote(Vision vision)
    {
        PIDController xController = new PIDController(0.1, 0, 0);
        PIDController yController = new PIDController(0.1, 0, 0);

        yController.setTolerance(1);

        return this.run(
            () -> {
                var xSpeed = xController.calculate(-22, vision.getLimelightX());
                var ySpeed = yController.calculate(0, vision.getLimelightY());
                Logger.recordOutput(this.getClass().getSimpleName() + "ySpeed", ySpeed);

                this.setControl(
                    NoteRequest.withVelocityX(-xSpeed).withVelocityY(ySpeed).withRotationalRate(0)
                );
            }
        );
    }

    public Command ApplyRequest(Supplier<SwerveRequest> requestSupplier) { return run(() -> this.setControl(requestSupplier.get())); }

    private void StartSimThread()
    {
        lastSimTime = Utils.getCurrentTimeSeconds();

        /* Run sim at faster rate so PID behaves */
        simNotifier =
            new Notifier(
                () ->
                {
                    final double currentTime = Utils.getCurrentTimeSeconds();
                    double deltaTime = currentTime - lastSimTime;
                    lastSimTime = currentTime;

                    /* use the measured time delta, get battery voltage */
                    updateSimState(deltaTime, RobotController.getBatteryVoltage());
                }
            );

        simNotifier.startPeriodic(SimLoopPeriod);
    }

    private void ConfigurePathplanner()
    {
        double driveBaseRadius = 0;
        for (var moduleLocation : m_moduleLocations) { driveBaseRadius = Math.max(driveBaseRadius, moduleLocation.getNorm()); }

        AutoBuilder.configureHolonomic(
            () -> this.getState().Pose,
            this::seedFieldRelative,
            () -> this.getState().speeds,
            (speeds) ->
                this.setControl(
                    AutoRequest.withSpeeds(speeds)),
            new HolonomicPathFollowerConfig(
                SwerveConstants.autoTranslationalController,
                SwerveConstants.autoRotationalController,
                TunerConstants.kSpeedAt12VoltsMps,
                driveBaseRadius,
                new ReplanningConfig()),
            () ->
                DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue) == Alliance.Red,
            this
        );
    }

    public Command RunChoreoTrajectory(ChoreoTrajectory trajectory)
    {
        return Choreo.choreoSwerveCommand(
            trajectory,
            () -> (this.getState().Pose),
            SwerveConstants.choreoTranslationController,
            SwerveConstants.choreoTranslationController,
            SwerveConstants.choreoRotationController,
            ((ChassisSpeeds speeds) -> {
                Logger.recordOutput("Auto Request X", speeds.vxMetersPerSecond);
                Logger.recordOutput("Auto Request Y", speeds.vyMetersPerSecond);
                Logger.recordOutput("Auto Request Omega", speeds.omegaRadiansPerSecond);
                this.setControl(
                    new SwerveRequest.ApplyChassisSpeeds()
                        .withSpeeds(speeds)
                        .withDriveRequestType(SwerveModule.DriveRequestType.Velocity));
            }),
            () -> {
                Optional<Alliance> alliance = DriverStation.getAlliance();
                return alliance.isPresent() && alliance.get() == DriverStation.Alliance.Red;
            },
            this);
    }

    public void updateVision() {
        boolean useMegaTag2 = true; // set to false to use MegaTag1
        boolean doRejectUpdate = false;
        if (useMegaTag2 == false) {
            LimelightHelpers.PoseEstimate mt1 = LimelightHelpers.getBotPoseEstimate_wpiBlue("limelight");

            if (mt1.tagCount == 1 && mt1.rawFiducials.length == 1) {
                if (mt1.rawFiducials[0].ambiguity > .7) {
                    doRejectUpdate = true;
                }
                if (mt1.rawFiducials[0].distToCamera > 3) {
                    doRejectUpdate = true;
                }
            }
            if (mt1.tagCount == 0) {
                doRejectUpdate = true;
            }

            if (!doRejectUpdate) {
                this.setVisionMeasurementStdDevs(VecBuilder.fill(.5, .5, 9999999));
                this.addVisionMeasurement(mt1.pose, mt1.timestampSeconds);
            }
        } else if (useMegaTag2 == true) {
            LimelightHelpers.SetRobotOrientation(
                "limelight",
                this.m_odometry.getEstimatedPosition().getRotation().getDegrees(),
                0,
                0,
                0,
                0,
                0);
            LimelightHelpers.PoseEstimate mt2 =
                LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2("limelight");
            if (Math.abs(this.getPigeon2().getRate())
                > 720) // if our angular velocity is greater than 720 degrees per
            // second, ignore vision
            // updates
            {
                doRejectUpdate = true;
            }
            if (mt2.tagCount == 0) {
                doRejectUpdate = true;
            }
            if (!doRejectUpdate) {
                this.setVisionMeasurementStdDevs(VecBuilder.fill(.7, .7, 9999999));
                this.addVisionMeasurement(mt2.pose, mt2.timestampSeconds);
            }
        }
    }

    @Override
    public void periodic()
    {
        /* Periodically try to apply the operator perspective */
        /*
         * If we haven't applied the operator perspective before, then we should apply
         * it regardless of DS state
         */
        /*
         * This allows us to correct the perspective in case the robot code restarts
         * mid-match
         */
        /*
         * Otherwise, only check and apply the operator perspective if the DS is
         * disabled
         */
        /*
         * This ensures driving behavior doesn't change until an explicit disable event
         * occurs during testing
         */
        if (!hasAppliedOperatorPerspective || DriverStation.isDisabled())
        {
            DriverStation.getAlliance()
                         .ifPresent(
                             (allianceColor) ->
                             {
                                 this.setOperatorPerspectiveForward(
                                     allianceColor == Alliance.Red
                                         ? RedAlliancePerspectiveRotation
                                         : BlueAlliancePerspectiveRotation);
                                 hasAppliedOperatorPerspective = true;
                             });
        }
        updateVision();
        Logger.recordOutput("SwervePose2D", this.getState().Pose);
        Logger.recordOutput("SwerveState", this.getState().ModuleStates);
        Logger.recordOutput("SwerveTargets", this.getState().ModuleTargets);
    }
}
