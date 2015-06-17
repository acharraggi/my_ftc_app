package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A test op mode that uses mock hardware and the gamepad to enable a fake teleop mode
 * Basic code copied from K9TeleOp with motors/servers renamed to match the mock hardware names.
 *
 * However, it behaves strangely, a single button press moves he server values for the arm or
 * claw from min to max or vice versa.
 *
 * I suspect the loop is too fast, so that it increments from in to max in one key press.
 * It was noticed that sometimes an intermediate telemetry value was displayed as the value
 * went from in to max.
 *
 * A quarter second delay was introduced after a button press before checking again for a button press.
 * This allows the user time to release the key if they want.
 *
 MockHardware set to true in FtcRobotControllerActivity.java - which set up:

 DcMotorController mc = dm.createUsbDcMotorController(new SerialNumber("MC"));
 DcMotorController mc2 = dm.createUsbDcMotorController(new SerialNumber("MC2"));
 ServoController sc = dm.createUsbServoController(new SerialNumber("SC"));

 HardwareMap hwMap = new HardwareMap();
 hwMap.dcMotor.put("left", new DcMotor(mc, 1));
 hwMap.dcMotor.put("right", new DcMotor(mc, 2));
 hwMap.dcMotor.put("flag", new DcMotor(mc2, 1));
 hwMap.dcMotor.put("arm", new DcMotor(mc2, 2));
 hwMap.servo.put("a", new Servo(sc, 1));
 hwMap.servo.put("b", new Servo(sc, 6));
  */
public class MockTeleOp extends OpMode {
    private String startDate;

    /*
	 * Note: the configuration of the servos is such that
	 * as the arm servo approaches 0, the arm position moves up (away from the floor).
	 * Also, as the claw servo approaches 0, the claw opens up (drops the game element).
	 */
    // TETRIX VALUES.
    final static double ARM_MIN_RANGE  = 0.20;
    final static double ARM_MAX_RANGE  = 0.90;
    final static double CLAW_MIN_RANGE  = 0.20;
    final static double CLAW_MAX_RANGE  = 0.7;

    // position of the arm servo.
    double armPosition;

    // amount to change the arm servo position.
    double armDelta = 0.1;

    // position of the claw servo
    double clawPosition;

    // amount to change the claw servo position by
    double clawDelta = 0.1;

    DcMotor motorRight;
    DcMotor motorLeft;
    Servo claw;
    Servo arm;

    // stuff needed to manage the loop/button presses
    boolean buttonPressed = false;
    double pressedTime = 0.0;
    final static double PRESS_DELAY = 0.25;

    /*
    * Constructor
    */
    public MockTeleOp() {

    }

    /*
    * Code to run when the op mode is first enabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
    */
    @Override
    public void start() {
        startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
        	/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */

		/*
		 * For the demo Tetrix K9 bot we assume the following,
		 *   There are two motors "motor_1" and "motor_2"
		 *   "motor_1" is on the right side of the bot.
		 *   "motor_2" is on the left side of the bot and reversed.
		 *
		 * We also assume that there are two servos "servo_1" and "servo_6"
		 *    "servo_1" controls the arm joint of the manipulator.
		 *    "servo_6" controls the claw joint of the manipulator.
		 */
        motorRight = hardwareMap.dcMotor.get("right");
        motorLeft = hardwareMap.dcMotor.get("left");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);

        arm = hardwareMap.servo.get("a");
        claw = hardwareMap.servo.get("b");

        // assign the starting position of the wrist and claw
        armPosition = 0.2;
        clawPosition = 0.2;
    }


    /*
    * This method will be called repeatedly in a loop
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
    */
    @Override
    public void loop() {
        /*
		 * Gamepad 1
		 *
		 * Gamepad 1 controls the motors via the left stick, and it controls the
		 * wrist/claw via the a,b, x, y buttons
		 */

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.left_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

        // write the values to the motors
        motorRight.setPower(right);
        motorLeft.setPower(left);

        if (buttonPressed) {
            // allow time for button key press to be released
            if (pressedTime + PRESS_DELAY < this.getRuntime() ) {
                buttonPressed = false;
            }
        }
        else {
            // update the position of the arm.
            if (gamepad1.a) {
                // if the A button is pushed on gamepad1, increment the position of
                // the arm servo.
                armPosition += armDelta;
                buttonPressed = true;
                pressedTime = this.getRuntime();
            }

            if (gamepad1.y) {
                // if the Y button is pushed on gamepad1, decrease the position of
                // the arm servo.
                armPosition -= armDelta;
                buttonPressed = true;
                pressedTime = this.getRuntime();
            }

            // update the position of the claw
            if (gamepad1.x) {
                clawPosition += clawDelta;
                buttonPressed = true;
                pressedTime = this.getRuntime();
            }

            if (gamepad1.b) {
                clawPosition -= clawDelta;
                buttonPressed = true;
                pressedTime = this.getRuntime();
            }
        }

        // clip the position values so that they never exceed their allowed range.
        armPosition = Range.clip(armPosition, ARM_MIN_RANGE, ARM_MAX_RANGE);
        clawPosition = Range.clip(clawPosition, CLAW_MIN_RANGE, CLAW_MAX_RANGE);

        // write position values to the wrist and claw servo
        arm.setPosition(armPosition);
        claw.setPosition(clawPosition);


        telemetry.addData("1 Start", "GamePadOp started at " + startDate);
        telemetry.addData("arm", "arm:  " + String.format("%.2f", armPosition));
        telemetry.addData("claw", "claw:  " + String.format("%.2f", clawPosition));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
    }

    /*
    * Code to run when the op mode is first disabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
    */
    @Override
    public void stop() {

    }
    /*
	 * This method scales the joystick input so for low joystick values, the
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }

}
