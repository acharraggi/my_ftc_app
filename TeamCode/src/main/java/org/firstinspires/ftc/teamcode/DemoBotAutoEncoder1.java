package org.firstinspires.ftc.teamcode;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


/**
 * An op mode that uses the geomagnetic and accelerometer values to calculate device
 * orientation and attempts to drive in a square.
 *
 * It makes use of getRotationMatrix() and getOrientation(), but does not use
 * remapCoordinateSystem() which one might want.
 * see: http://developer.android.com/reference/android/hardware/SensorManager.html#remapCoordinateSystem(float[], int, int, float[])
 */
@Autonomous(name = "DemoBotAutoEncoder1", group = "DemoBot")
public class DemoBotAutoEncoder1 extends OpMode {

    private long timeTick;          // compute time between loops
    MyHardwarePushbot robot       = new MyHardwarePushbot(); // use the class created to define a Pushbot's hardware

    /*
    * Constructor
    */
    public DemoBotAutoEncoder1() {

    }

    /*
    * Code to run when the op mode is first enabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
    */
    @Override
    public void init() {
        timeTick = 0;
        robot.init(hardwareMap);
    }

    /*
* Code to run when the op mode is first enabled goes here
* @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
*/
    @Override
    public void start() {
        timeTick = System.currentTimeMillis();

        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.rightMotor.setTargetPosition(1000);
        robot.leftMotor.setTargetPosition(1000);

        robot.rightMotor.setPower(0.15);
        robot.leftMotor.setPower(0.15);
    }

    /*
    * This method will be called repeatedly in a loop
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
    */
    @Override
    public void loop() {
        long newTick = System.currentTimeMillis();

        if(robot.leftMotor.isBusy()) { ; }
        else {
            robot.leftMotor.setPower(0);
        }
        if(robot.rightMotor.isBusy()) { ; }
        else {
            robot.rightMotor.setPower(0);
        }
        telemetry.addData("getMaxSpeed", robot.rightMotor.getMaxSpeed());
        telemetry.addData("loop ms", newTick - timeTick);
        timeTick = newTick;

        telemetry.update();
    }

    /*
    * Code to run when the op mode is first disabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
    */
    @Override
    public void stop() {
        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftMotor.setPower(0);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setPower(0);
    }


}
