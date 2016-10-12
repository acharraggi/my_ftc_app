/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;

/**
 * This file provides basic Telop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 * Copied from sample PushbotTeleopTank_Iterative
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the MyHardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="DemoBot: Tank Drive", group="DemoBot")
public class DemoBotTeleopTank extends OpMode{

    /* Declare OpMode members. */
    MyHardwarePushbot robot       = new MyHardwarePushbot(); // use the class created to define a Pushbot's hardware
                                                         // could also use HardwarePushbotMatrix class.
    double          clawOffset  = 0.0 ;                  // Servo mid position
    final double    CLAW_SPEED  = 0.02 ;                 // sets rate to move servo
    OpticalDistanceSensor odsSensor;  // Hardware Device Object
    TouchSensor touchSensor;  // Hardware Device Object

    private long timeTick;              // last timeTick
    private long timeTickLastSecond;    // timeTick of last second
    private int  ticksPerSecond;        // number of ticks/sec
    private int  ticksThisSecond;       // ticks so far this second

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        odsSensor = hardwareMap.opticalDistanceSensor.get("ods sensor");
        touchSensor = hardwareMap.touchSensor.get("touch sensor");

        timeTick = 0;
        timeTickLastSecond = 0;
        ticksPerSecond = 0;
        ticksThisSecond = 0;

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        updateTelemetry(telemetry);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
//    @Override
//    public void init_loop() {
//        timeTick = 0;
//    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        timeTick = System.currentTimeMillis();
        timeTickLastSecond = timeTick;
        ticksPerSecond = 0;
        ticksThisSecond = 0;
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        double left;
        double right;
        double ly,ry;
        double rawLight;

        long newTick = System.currentTimeMillis();
        ticksThisSecond = ticksThisSecond + 1;
        if(newTick - timeTickLastSecond > 1000) {  // we've started a new second
            timeTickLastSecond = newTick;
            ticksPerSecond = ticksThisSecond;
            ticksThisSecond = 0;
        }

        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        ly = -gamepad1.left_stick_y;
        ry = -gamepad1.right_stick_y;
        left = scalePower(ly);
        right = scalePower(ry);
        robot.leftMotor.setPower(left);
        robot.rightMotor.setPower(right);

        rawLight = odsSensor.getRawLightDetected();

        telemetry.addData("left  motor,joystick",  "%.2f, %.2f", left, ly);
        telemetry.addData("right motor,joystick", "%.2f, %.2f", right, ry);

        telemetry.addData("loop ms", newTick - timeTick);
        telemetry.addData("loops/sec", ticksPerSecond);
        timeTick = newTick;

        telemetry.addData("rawLight", "%.2f", rawLight);

        if (touchSensor.isPressed())
            telemetry.addData("Touch", "Is Pressed");
        else
            telemetry.addData("Touch", "Is Not Pressed");
        updateTelemetry(telemetry);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
        timeTick = 0;
    }

    // scale joystick so small joystick values have even smaller motor values
    // anything above 0.5 is close to full motor power
    private double scalePower(double p) {
        double newP;
        double sign = (p < 0) ? -1 : 1;

        if (Math.abs(p) < 0.05) {
            newP = 0;
        }
        else if(Math.abs(p) < 0.25) {
            newP = 0.1;
        }
        else if(Math.abs(p) < 0.4) {
            newP = 0.15;
        }
        else if(Math.abs(p) < 0.5) {
            newP = 0.2;
        }
        else if(Math.abs(p) < 0.6) {
            newP = 0.3;
        }
        else if(Math.abs(p) < 0.8) {
            newP = 0.4;
        }
        else if(Math.abs(p) < 0.9) {
            newP = 0.5;
        }
        else newP = 1.0;

        return newP * sign;
    }

}
