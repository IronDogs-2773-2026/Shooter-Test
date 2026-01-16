// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.filter.SlewRateLimiter;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  // ------------------------------------------------------------------------------------------------------------------------------------------------------------------
  private final PWMSparkMax driveMotorLeft1 = new PWMSparkMax(0);
  private final PWMSparkMax driveMotorLeft2 = new PWMSparkMax(1);
  private final PWMSparkMax driveMotorRight1 = new PWMSparkMax(2);
  private final PWMSparkMax driveMotorRight2 = new PWMSparkMax(3);

  private DifferentialDrive driveTrain =
      new DifferentialDrive(driveMotorLeft1, driveMotorRight1);
  // ------------------------------------------------------------------------------------------------------------------------------------------------------------------

  private final PWMSparkMax flywheelMotor = new PWMSparkMax(6);
  private final PWMSparkMax feedMotor = new PWMSparkMax(7);

  private final SlewRateLimiter filterAcc = new SlewRateLimiter(Constants.accelerationLimit);
  private final SlewRateLimiter filterTurn = new SlewRateLimiter(Constants.turnLimit);

  private final XboxController controller = new XboxController(0);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------
    driveMotorLeft1.setInverted(false);
    driveMotorLeft2.setInverted(false);
    driveMotorLeft1.addFollower(driveMotorLeft2);
    driveMotorRight1.addFollower(driveMotorRight2);
    // ------------------------------------------------------------------------------------------------------------------------------------------------------------------
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  private double leftTrig;
  @Override
  public void teleopPeriodic() {
    if (!controller.getLeftBumperButton()) {
      leftTrig = ((controller.getLeftTriggerAxis())*(controller.getLeftTriggerAxis()));
    } else {
      leftTrig = controller.getLeftBumperButton() ? -1 : 0;
    }
    flywheelMotor.set(leftTrig*Constants.maxFlywheelSpeed);
    feed();
   

    driveTrain.curvatureDrive(controller.getRightX(), controller.getLeftY(), true);
  }

  private void feed() {
    double rightTrig;
    if (!controller.getRightBumperButton()) {
      rightTrig = -controller.getRightTriggerAxis();
    } else {
      rightTrig = controller.getRightBumperButton() ? 1 : 0;
    }
    feedMotor.set(rightTrig);
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
