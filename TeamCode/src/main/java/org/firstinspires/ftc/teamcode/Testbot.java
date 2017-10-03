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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

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

@TeleOp(name="Testbot: drive and lift", group="DemoBot")
public class Testbot extends OpMode{

    /* Declare OpMode members. */
    public DcMotor leftMotor   = null;
    public DcMotor  rightMotor  = null;
    public DcMotor  liftMotor  = null;
                                                         // could also use HardwarePushbotMatrix class.
    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        leftMotor   = hardwareMap.dcMotor.get("drive right");
        rightMotor  = hardwareMap.dcMotor.get("drive left");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);

        liftMotor  = hardwareMap.dcMotor.get("Slide");
        //liftMotor.setDirection(DcMotor.Direction.REVERSE);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        updateTelemetry(telemetry);
    }


    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        double left;
        double right;
        double ly,ry;
        double pop;

        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        ly = gamepad1.left_stick_y;
        ry = gamepad1.right_stick_y;
        left = scalePower(ly);
        right = scalePower(ry);
        leftMotor.setPower(left);
        rightMotor.setPower(right);

        telemetry.addData("left  motor,joystick",  "%.2f, %.2f", left, ly);
        telemetry.addData("right motor,joystick", "%.2f, %.2f", right, ry);

        if(gamepad1.right_bumper) { // slide up
            liftMotor.setPower(0.5);
            telemetry.addData("Slide", "going up");

        }
        else if (gamepad1.right_trigger > 0.5) { // slide down
            liftMotor.setPower(-0.2);
            telemetry.addData("Slide", "going down");
        }
        else {
            liftMotor.setPower(0);
            telemetry.addData("Slide", "stay put");
        }

        updateTelemetry(telemetry);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

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
