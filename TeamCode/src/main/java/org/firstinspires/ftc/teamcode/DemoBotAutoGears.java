package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by mikes on 2016-10-04.
 * OpMode to find the Gears target and drive towards it in autonomous.
 * stop when distance < 400mm.
 * Note: seems to require the bot to be within about 5 feet, any further and it can't find the target
 */

@Autonomous(name = "DemoBot: Gears", group = "DemoBot")
public class DemoBotAutoGears extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        MyHardwarePushbot robot       = new MyHardwarePushbot(); // use the class created to define a Pushbot's hardware
        double left  = 0;
        double right = 0;

        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = "Ad6yHYT/////AAAAGdvmcs1KfECKpMTU2qCaOVQWM3OYYVJAzmnAt7Fbwl6WshFiYXMaWNBqT5dkWPelfReDyziet598boIocDwk8MsPCsMAxNZoFyGdhSvPJlHmiMTINmiMs+1jk0r0YhlVwjhAV00F80rqdD1TgDJpadpnP0gASiVznlCEETId3LbJLccRaxt8vqkvjXI1dWSl93/0+Y7rzm1TxMVehZhbxoc5WENnnKHWmeXOHia0l5xqWsuJ8a9zprqCLGr6/Ii9QLEUmMXz9XeoG04bqqozmDO6bVPVMQwZHCfwq7ogJaH8D75wuMdlhwKGEdT7PEQ3y2yb3Dw9AS6NKG8f0iMXI+X5h+6MylxQuiyv8Bu8++Wa";
        params.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(params);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);   // four targets on the field

        VuforiaTrackables beacons = vuforia.loadTrackablesFromAsset("FTC_2016-17");
        beacons.get(0).setName("Wheels");
        beacons.get(1).setName("Tools");
        beacons.get(2).setName("Lego");
        beacons.get(3).setName("Gears");

        VuforiaTrackable beacon = beacons.get(3);   // gears
        robot.init(hardwareMap);

        waitForStart();

        beacons.activate();  // start tracking beacons

        while(opModeIsActive()) {

                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beacon.getListener()).getPose();  // get gears target position

                if(pose != null) {
                    if (((VuforiaTrackableDefaultListener) beacon.getListener()).isVisible()) {
                        telemetry.addData(beacon.getName() + "-visible", "Yes");
                    } else {
                        telemetry.addData(beacon.getName() + "-visible", "No");  // does not seem to be invokved once target found once.
                    }
                    VectorF translation = pose.getTranslation();
                    //telemetry.addData(beacon.getName() + "-Translation", translation); // format(pose) shows both orientation and translation
                    //telemetry.addData(beacon.getName()+"-vector", pose.toVector());    // dump entire matrix

                    telemetry.addData("Pose", format(pose));
                    /* based on observation of pose, it represents the position and orientation of the target image.
                       The position is in milimeters, the orientation is of the target, not the phone.
                       eg. if the phone and target are both parallel to the floor, but the phone is offset towards one edge of the target
                       and is above the floor,
                       then the orientation is (0,0,0) but the translation will contain an offset from the centre of the target,
                       in both the vertical axis and in the other axis.
                       It's that offset that can be used to calculate the angle from the phone to the target.
                     */

                    double angle = Math.atan2(translation.get(2), translation.get(0)); // in radians
                    double degreesToTurn = Math.toDegrees(angle) + 90;                 // adjust for vertical orientation of phone
                    telemetry.addData(beacon.getName() + "-Degrees", degreesToTurn);
                    double distance = Math.sqrt(translation.get(2)*translation.get(2) + translation.get(0)*translation.get(0));  // Pythagoras calc of hypotenuse
                    telemetry.addData(beacon.getName() + "-Distance", distance);

                    if(distance < 400) {
                        left = 0; right = 0;
                    }
                    else if (degreesToTurn > 10) {  //turn right
                        left = 0.2; right = 0.1;
                    }
                    else if (degreesToTurn < -10) { ///turn left
                        left = 0.1; right = 0.2;
                    }
                    else {  // go straight
                        left = 0.2; right = 0.2;
                    }
                    robot.leftMotor.setPower(left);
                    robot.rightMotor.setPower(right);
                }

            telemetry.update();
            idle(); // Always call idle() at the bottom of your while(opModeIsActive()) loop
        }
    }

    /**
     * A simple utility that extracts positioning information from a transformation matrix
     * and formats it in a form palatable to a human being.
     */
    String format(OpenGLMatrix transformationMatrix) {
        return transformationMatrix.formatAsTransform();
    }
}
