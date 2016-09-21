package org.firstinspires.ftc.teamcode;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An Op Mode that returns the raw gravity sensor values as telemetry
 */
//TODO check if Moto G 3 supports a raw gravity sensor, apparently not.
@Autonomous(name = "GravityOp", group = "Demo")
public class GravityOp extends OpMode implements SensorEventListener {
    private String startDate;
    private SensorManager mSensorManager;
    private Sensor gravitySensor;

    private float[] gravityValues = {0.0f,0.0f,0.0f};    // SI units (m/s^2)

    private float[] mGravitySensor;       // latest sensor values
    // see http://developer.android.com/reference/android/hardware/SensorEvent.html#values

    /*
    * Constructor
    */
    public GravityOp() {

    }

    /*
    * Code to run when the op mode is first enabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
    */
    @Override
    public void init() {
        mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        gravityValues[0] = 0.0f;
        gravityValues[1] = 0.0f;
        gravityValues[2] = 0.0f;
    }

    /*
    * Code to run when the op mode is first enabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
    */
    @Override
    public void start() {
        startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

        // delay value is SENSOR_DELAY_UI which is ok for telemetry, maybe not for actual robot use
        mSensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_UI);
    }

    /*
    * This method will be called repeatedly in a loop
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
    */
    @Override
    public void loop() {
//        telemetry.addData("1 Start", "GravityOp started at " + startDate);
//        telemetry.addData("2 units", "values in SI units (m/s^2)");
        telemetry.addData("x-axis", gravityValues[0]);
        telemetry.addData("y-axis", gravityValues[1]);
        telemetry.addData("z-axis", gravityValues[2]);
//        telemetry.addData("3 z-axis", gravityValues[2]+" SI units (m/s^2)");
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
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            mGravitySensor = event.values;
            if (mGravitySensor != null) {
                gravityValues[0] = mGravitySensor[0]; // x-axis
                gravityValues[1] = mGravitySensor[1]; // y-axis
                gravityValues[2] = mGravitySensor[2]; // z-axis
            }
        }
    }
}
