package org.firstinspires.ftc.teamcode;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * An op mode that uses the geomagnetic and accelerometer values to calculate device
 * orientation and attempts to drive in a square.
 *
 * It makes use of getRotationMatrix() and getOrientation(), but does not use
 * remapCoordinateSystem() which one might want.
 * see: http://developer.android.com/reference/android/hardware/SensorManager.html#remapCoordinateSystem(float[], int, int, float[])
 */
@Autonomous(name = "DemoBotAutoSquare1", group = "DemoBot")
@Disabled    // not finished
public class DemoBotAutoSquare1 extends OpMode implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    // orientation values
    private float azimuth = 0.0f;      // value in radians
    private float pitch = 0.0f;        // value in radians
    private float roll = 0.0f;         // value in radians

    private float[] mGravity;       // latest sensor values
    private float[] mGeomagnetic;   // latest sensor values

    private long timeTick;          // compute time between loops
    private long heading;           // target heading

    /*
    * Constructor
    */
    public DemoBotAutoSquare1() {

    }

    /*
    * Code to run when the op mode is first enabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
    */
    @Override
    public void init() {
        mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        azimuth = 0.0f;      // value in radians
        pitch = 0.0f;        // value in radians
        roll = 0.0f;

        timeTick = 0;
        heading = 0;
    }

    /*
* Code to run when the op mode is first enabled goes here
* @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
*/
    @Override
    public void start() {
        // delay value is SENSOR_DELAY_UI which is ok for telemetry, maybe not for actual robot use
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        timeTick = System.currentTimeMillis();
    }

    /*
    * This method will be called repeatedly in a loop
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
    */
    @Override
    public void loop() {
        long newTick = System.currentTimeMillis();

        long  currentHeading = Math.round(Math.toDegrees(azimuth));
        if(heading == 0) {  // at start set heading the way robot is facing
            heading = currentHeading;
        }

        if (mGravity != null && mGeomagnetic != null) {
            telemetry.addData("heading", currentHeading);
            telemetry.addData("target", heading);
        }
        else {
            if (mGravity != null) {
                telemetry.addData("note1", "no default accelerometer sensor on phone");
            }
            if (mGeomagnetic != null) {
                telemetry.addData("note2", "no default magnetometer sensor on phone");
            }
        }
        telemetry.addData("loop ms", newTick - timeTick);
        timeTick = newTick;
    }

    /*
    * Code to run when the op mode is first disabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
    */
    @Override
    public void stop() {
        mSensorManager.unregisterListener(this);
        heading = 0;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not sure if needed, placeholder just in case
    }

    public void onSensorChanged(SensorEvent event) {
        // we need both sensor values to calculate orientation
        // only one value will have changed when this method called, we assume we can still use the other value.
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagnetic = event.values;
        }
        if (mGravity != null && mGeomagnetic != null) {  //make sure we have both before calling getRotationMatrix
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimuth = orientation[0]; // orientation contains: azimuth, pitch and roll
                pitch = orientation[1];
                roll = orientation[2];
            }
        }
    }
}
