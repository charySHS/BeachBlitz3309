package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Volts;

import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.SignalLogger;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

import friarLib3.Utils.DisableSubsystem;

public class Intake extends DisableSubsystem
{
    private final IntakeIO intakeIO;
    private final IntakeIO.IntakeIOInputs intakeIOAutoLogged = new IntakeIO.IntakeIOInputs();
    private final SysIdRoutine intakeSysIDRoutine;
    private final SysIdRoutine feederSysIDRoutine;

    private boolean hasCurrentSpiked = false;

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
            new SysIdRoutine()
    }
}
