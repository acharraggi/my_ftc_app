package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.ftcrobotcontroller.FtcConfig;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An Op Mode that returns the rotation vector sensor values as telemetry
 */
public class RotationOp extends OpMode implements SensorEventListener {
    private String startDate;
    private SensorManager mSensorManager;
    Sensor rotationSensor;

    private float[] rotationVector = {0.0f,0.0f,0.0f,0.0f,0.0f};
    // see http://developer.android.com/reference/android/hardware/SensorEvent.html#values
    //values[0]: x*sin(θ/2)
    //values[1]: y*sin(θ/2)
    //values[2]: z*sin(θ/2)
    //values[3]: cos(θ/2)
    //values[4]: estimated heading Accuracy (in radians) (-1 if unavailable)

    private float[] mRotationVector;       // latest sensor values

    /*
    * Constructor
    */
    public RotationOp() {

    }

    /*
    * Code to run when the op mode is first enabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
    */
    @Override
    public void start() {
        startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

        // needed FtcConfig to get context for getSystemService
        // but this required change to FtcRobotControllerActivity to set the context for us
        mSensorManager = (SensorManager) FtcConfig.context.getSystemService(Context.SENSOR_SERVICE);
        rotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        // delay value is SENSOR_DELAY_UI which is ok for telemetry, maybe not for actual robot use
        mSensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_UI);
    }

    /*
    * This method will be called repeatedly in a loop
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
    */
    @Override
    public void loop() {
        telemetry.addData("1 Start", "RotationOp started at " + startDate);
        telemetry.addData("2 x", "x*sin(θ/2) = " + rotationVector[0]);
        telemetry.addData("3 y", "y*sin(θ/2) = " + rotationVector[1]);
        telemetry.addData("4 z", "z*sin(θ/2) = " + rotationVector[2]);
        telemetry.addData("5 cos", "cos(θ/2) = " + rotationVector[3]);
        if (rotationVector[4] == -1.0f) {
            telemetry.addData("6 Accuracy", "Accuracy value unavailable");
        }
        else {
            telemetry.addData("6 Accuracy", "Accuracy = " + rotationVector[4] + " radians");
        }
    }

    /*
    * Code to run when the op mode is first disabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
    */
    @Override
    public void stop() {
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not sure if needed, placeholder just in case
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            mRotationVector = event.values;
            if (mRotationVector != null) {
                rotationVector[0] = mRotationVector[0];
                rotationVector[1] = mRotationVector[1];
                rotationVector[2] = mRotationVector[2];
                rotationVector[3] = mRotationVector[3];
                rotationVector[4] = mRotationVector[4];
            }
        }
    }
}
