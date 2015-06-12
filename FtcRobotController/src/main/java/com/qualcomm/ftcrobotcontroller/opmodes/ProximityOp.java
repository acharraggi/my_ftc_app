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
 * An op mode that returns the proximity sensor values as telemetry.
 * Note: the ZTE Speed phone appears to have a binary near/far type sensor.
 *    Perhaps based on the front facing camera, only seems sensitive in that area.
 *    It reports 5cm when nothing nearer than 5cm, and 0cm when something is closer.
 */
public class ProximityOp extends OpMode implements SensorEventListener {
    private String startDate;
    private SensorManager mSensorManager;
    Sensor proximity;

    private float proximityValue = 0.0f;       // proximity value in cm

    private float[] mProximity;       // latest sensor values

    /*
    * Constructor
    */
    public ProximityOp() {

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
        proximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // delay value is SENSOR_DELAY_UI which is ok for telemetry, maybe not for actual robot use
        mSensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_UI);
    }

    /*
    * This method will be called repeatedly in a loop
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
    */
    @Override
    public void loop() {
        telemetry.addData("1 Start", "LightOp started at " + startDate);
        telemetry.addData("2 proximity", "proximity distance = " + proximityValue + " cm");
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
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            mProximity = event.values;
            if (mProximity != null) {
                proximityValue = mProximity[0]; // only one value from this sensor
            }
        }
    }
}
