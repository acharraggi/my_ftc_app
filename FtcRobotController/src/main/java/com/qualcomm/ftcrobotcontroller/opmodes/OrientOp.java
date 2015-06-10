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
 * An empty op mode serving as a template for custom OpModes
 */
public class OrientOp extends OpMode implements SensorEventListener {
    private String startDate;
    private SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;

    private float azimut = 0.0f;
    private float pitch = 0.0f;
    private float roll = 0.0f;

    private float[] mGravity;
    private float[] mGeomagnetic;
    private float[] mOrientation;

    /*
    * Constructor
    */
    public OrientOp() {

    }

    /*
    * Code to run when the op mode is first enabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
    */
    @Override
    public void start() {
        startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

        mSensorManager = (SensorManager) FtcConfig.context.getSystemService(Context.SENSOR_SERVICE);
        // Log.d("OrientOp","just called getSystemService");
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);

    }

    /*
    * This method will be called repeatedly in a loop
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
    */
    @Override
    public void loop() {
        telemetry.addData("1 Start", "OrientOp started at " + startDate);
        if (mGravity == null) {
            telemetry.addData("2 Gravity", "Gravity sensor values null ");
        } else {
            telemetry.addData("2 Gravity", "Gravity sensor returning values " );
        }
        if (mGravity == null) {
            telemetry.addData("3 Geomagnetic", "Geomagnetic sensor values null ");
        } else {
            telemetry.addData("3 Geomagnetic", "Geomagnetic sensor returning values " );
        }
        telemetry.addData("4 azimut", "azimut = "+Math.round(Math.toDegrees(azimut)));
        telemetry.addData("5 pitch", "pitch = "+Math.round(Math.toDegrees(pitch)));
        telemetry.addData("6 pitch", "roll = "+Math.round(Math.toDegrees(roll)));
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
        //Log.i("DisplayOrientation", "onAccuracyChanged called");
    }


    public void onSensorChanged(SensorEvent event) {
        //Log.i("DisplayOrientation", "onSensorChanged called");
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagnetic = event.values;
        }
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut = orientation[0]; // orientation contains: azimut, pitch and roll
                pitch = orientation[1];
                roll = orientation[2];
            }
        }
    }
}
