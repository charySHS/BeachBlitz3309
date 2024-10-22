package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.SignalLogger;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import friarLib3.Utils.DisableSubsystem;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class Intake extends DisableSubsystem
{
    private final IntakeIO intakeIO;
    private final IntakeIO.IntakeIOInputs intakeIOAutoLogged = new IntakeIO.IntakeIOInputs();
    private final SysIdRoutine intakeSysIDRoutine;
    private final SysIdRoutine feederSysIDRoutine;

    double lastCurrent = 0;
    int currentSpikeCount = 0;

    boolean isFeeding = false;
    boolean hasGotten = false;

    public enum EOuttakeType
    {
        Amp(-0.5),
        Speaker(-1),
        None(0),
        Trap(0);

        public final double DutyCycle;

        EOuttakeType(double dutyCycle)
        {
            DutyCycle = dutyCycle;
        }
    }

    public enum EFeedType
    {
        IntakeGround(0.6),
        IntakeSource(0.4),
        IntakeToFeeder(0.3),
        FeederTake(-0.075),
        FeederGive(0.5);

        public final double DutyCycle;

        EFeedType(double dutyCycle)
        {
            DutyCycle = dutyCycle;
        }
    }

    public Intake(boolean disabled, IntakeIO intakeIO)
    {
        super(disabled);

        this.intakeIO = intakeIO;
        intakeSysIDRoutine =
            new SysIdRoutine(
                new SysIdRoutine.Config(
                    Volts.of(0.2).per(Seconds.of(1)),
                    Volts.of(6),
                    null,
                    (state) -> SignalLogger.writeString("state", state.toString())
                ),
                new SysIdRoutine.Mechanism(
                    (volts) ->
                        intakeIO
                            .getIntakeMotor()
                            .setControl(intakeIO.getIntakeVoltageRequest().withOutput(volts.in(Volts))),
                    null,
                    this
                )
            );

        feederSysIDRoutine =
            new SysIdRoutine(
                new SysIdRoutine.Config(
                    Volts.of(0.2).per(Seconds.of(1)), // Use default ramp rate (1 V/s)
                    Volts.of(6), // Reduce dynamic step voltage to 4
                    null,
                    // Log state with Phoenix SignalLogger class
                    (state) -> SignalLogger.writeString("state", state.toString())
                ),
                new SysIdRoutine.Mechanism(
                    (volts) ->
                        intakeIO
                            .getFeederMotor()
                            .setVoltage(
                                volts.in(Volts)
                            ),
                    null,
                    this
                )
            );
    }

    @Override
    public void periodic()
    {
        super.periodic();
        intakeIO.updateInputs(intakeIOAutoLogged);
        Logger.processInputs(this.getClass().getSimpleName(), (LoggableInputs) intakeIOAutoLogged);
    }

    public Command setVoltage(double voltage, double feederVoltage)
    {
        return this.run(
            () ->
            {
                intakeIO.setIntakeVoltage(voltage);
                intakeIO.setFeederVoltage(feederVoltage);
            }
        )
            .finallyDo(intakeIO::off);
    }

    public Command setVelocity(double velocity, double feederVelocity)
    {
        return this.run(
            () ->
            {
                intakeIO.setIntakeVelocity(velocity);
                intakeIO.setFeederVelocity(feederVelocity);
            }
        )
            .finallyDo(intakeIO::off);
    }

    public Command setIntakeVoltage(double voltage)
    {
        return this.run(() -> intakeIO.setIntakeVoltage(voltage)).finallyDo(intakeIO::off);
    }

    public Command setIntakeVelocity(double velocity)
    {
        return this.run(() -> intakeIO.setIntakeVelocity(velocity)).finallyDo(intakeIO::off);
    }

    public Command setFeederVoltage(double voltage)
    {
        return this.run(() -> intakeIO.setFeederVoltage(voltage)).finallyDo(intakeIO::off);
    }

    public Command setFeederVelocity(double velocity)
    {
        return this.run(() -> intakeIO.setFeederVelocity(velocity)).finallyDo(intakeIO::off);
    }

    public Command off() { return this.runOnce(intakeIO::off); }

    public Command setIntakeVelocityFeederVoltage(double velocity, double voltage)
    {
        return setIntakeVelocity(velocity).andThen(setFeederVoltage(voltage));
    }

    public Command stopIntake() { return runOnce(this::stopMotors); }

    public void stopMotors()
    {
        intakeIO.getIntakeMotor().stopMotor();
        intakeIO.getFeederMotor().stopMotor();
    }

    public void requestCancelIntake() {}

    public Command outtake(EOuttakeType outtakeType)
    {
        return Commands.sequence(
            runOnce(() -> intakeIO.getIntakeMotor().setControl(intakeIO.getDutyCycleRequest().withOutput(outtakeType.DutyCycle))),
            Commands.waitSeconds(0.25),
            runOnce(() -> intakeIO.getFeederMotor().set(0.5)),
            Commands.waitSeconds(0.5),
            stopIntake()
        );
    }

    public Command moveNote(boolean forward)
    {
        return startEnd(
            () ->
            {
                intakeIO.getIntakeMotor().setControl(intakeIO.getDutyCycleRequest().withOutput(forward ? -0.5 : 0.5));
                intakeIO.getFeederMotor().set(forward ? 0.3 : -0.3);
            },
            this::stopMotors
        );
    }

    public Command feederTakeNote(boolean skipWaitForSpinup)
    {
        return Commands.sequence(
            runOnce(() -> intakeIO.getFeederMotor().set(EFeedType.FeederTake.DutyCycle)),

            Commands.waitSeconds(0.25).unless(() -> skipWaitForSpinup),

            runOnce(() ->
                    {
                        currentSpikeCount = 0;
                        lastCurrent = intakeIO.getFeederMotor().getOutputCurrent();
                        intakeIO.getIntakeMotor().setControl(intakeIO.getDutyCycleRequest().withOutput(EFeedType.IntakeToFeeder.DutyCycle));
                    }),

            Commands.waitUntil(() ->
                               {
                                   double curCurrent = intakeIO.getFeederMotor().getOutputCurrent();
                                   Logger.recordOutput("Intake.CurrentDelta", curCurrent - lastCurrent);

                                   if (curCurrent - lastCurrent > 5) { currentSpikeCount++; }
                                   lastCurrent = curCurrent;

                                   return currentSpikeCount >= 1;
                               })
                .withTimeout(1),

            Commands.waitSeconds(0.25)
        )
            .finallyDo(() ->
                       {
                           stopMotors();
                           isFeeding = false;
                       });
    }

    public Command intakeSysIDQuasistatic(SysIdRoutine.Direction direction) { return intakeSysIDRoutine.quasistatic(direction); }

    public Command intakeSysIDDynamic(SysIdRoutine.Direction direction) { return intakeSysIDRoutine.dynamic(direction); }

    public Command feederSysIDQuasistatic(SysIdRoutine.Direction direction) { return feederSysIDRoutine.quasistatic(direction); }

    public Command feederSysIDDynamic(SysIdRoutine.Direction direction) { return feederSysIDRoutine.dynamic(direction); }

    public boolean isCurrentSpiked() { return intakeIO.hasCurrentSpiked(); }

    public boolean getIsFeedingNote() { return isFeeding; }
}
