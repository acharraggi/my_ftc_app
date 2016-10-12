/* Copyright (c) 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

/*
 * This is an example LinearOpMode that shows how to use
 * the Modern Robotics ITR Seeker
 *
 * The op mode assumes that the IR Seeker
 * is configured with a name of "ir seeker".
 *
 * Set the switch on the Modern Robotics IR beacon to 1200 at 180.  <br>
 * Turn on the IR beacon.
 * Make sure the side of the beacon with the LED on is facing the robot. <br>
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */
@Autonomous(name = "DemoBotAutoIR", group = "DemoBot")
public class DemoBotAutoIrSeeker extends LinearOpMode {

  MyHardwarePushbot robot       = new MyHardwarePushbot(); // use the class created to define a Pushbot's hardware

  @Override
  public void runOpMode() throws InterruptedException {

    double left;
    double right;
    double angle;
    double strength;


            /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
    robot.init(hardwareMap);

    IrSeekerSensor irSeeker;    // Hardware Device Object

    // get a reference to our GyroSensor object.
    irSeeker = hardwareMap.irSeekerSensor.get("ir_seeker");

    // wait for the start button to be pressed.
    waitForStart();

    while (opModeIsActive())  {

      angle = irSeeker.getAngle();
      strength = irSeeker.getStrength();

      if (strength > 0.5) { // we're close so stop
        left = 0; right = 0;
      }
      else if (angle > 20) {  //turn right
        left = 0.2; right = 0;
      }
      else if (angle < -20) { ///turn left
        left = 0; right = 0.2;
      }
      else {  // go straight
        left = 0.2; right = 0.2;
      }

      // Ensure we have a IR signal
      if (irSeeker.signalDetected())
      {
        // Display angle and strength
        telemetry.addData("Angle",    angle);
        telemetry.addData("Strength", strength);
        telemetry.addData("Left Motor", left);
        telemetry.addData("RightMotor", right);
      }
      else
      {
        // Display loss of signal
        telemetry.addData("Seeker", "Signal Lost");
      }

      telemetry.update();

      robot.leftMotor.setPower(left);
      robot.rightMotor.setPower(right);

      idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
    }
  }
}
