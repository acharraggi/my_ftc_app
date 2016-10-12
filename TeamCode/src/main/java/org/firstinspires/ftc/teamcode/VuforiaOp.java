package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by mikes on 2016-10-04.
 */

@Autonomous(name = "Demo: VuforiaOp", group = "Demo")
public class VuforiaOp extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
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

        waitForStart();

        beacons.activate();  // start tracking beacons

        while(opModeIsActive()) {
            for(VuforiaTrackable beacon : beacons) {
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) beacon.getListener()).getPose();

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
                       It's that offset that can be used to calculation the angle from the phone to the target.
                     */

                    double angle = Math.atan2(translation.get(2), translation.get(0)); // in radians
                    double degreesToTurn = Math.toDegrees(angle) + 90;                 // adjust for vertical orientation of phone
                    telemetry.addData(beacon.getName() + "-Degrees", degreesToTurn);

                    /*     ConceptVuforiaNavigation
                           <li>If you want to break open the black box of a transformation matrix to understand
                            *     what it's doing inside, use {@link MatrixF#getTranslation()} to fetch how much the
                            *     transform will move you in x, y, and z, and use {@link Orientation#getOrientation(MatrixF,
                            *     AxesReference, AxesOrder, AngleUnit)} to determine the rotational motion that the transform
                           will impart. See {@link #format(OpenGLMatrix)} below for an example.</li> */
//                    Orientation o = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
//                    telemetry.addData("orient", o);  // same numbers as format(pose)
//                    telemetry.addData("orient-1,2,3", o.firstAngle+","+o.secondAngle+","+o.thirdAngle);  // same numbers as format(pose)
                }
            }
            telemetry.update();
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
