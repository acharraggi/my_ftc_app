package org.firstinspires.ftc.teamcode;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An op mode that returns the ambient light sensor value as telemetry
 */
@Autonomous(name = "LightOp", group = "Demo")
public class LightOp extends OpMode implements SensorEventListener {
    private String startDate;
    private SensorManager mSensorManager;
    private Sensor light;

    private float lightLevel = 0.0f;       // Ambient light level in SI lux units

    private float[] mLight;       // latest sensor values

    /*
    * Constructor
    */
    public LightOp() {

    }

    /*
    * Code to run when the op mode is first enabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
    */
    @Override
    public void init() {
        Log.d("LightOp", "init() entered");
        mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        light = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        lightLevel = 0.0f;
   }

    /*
* Code to run when the op mode is first enabled goes here
* @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
*/
    @Override
    public void start() {
        Log.d("LightOp", "start() entered");
        startDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

        // delay value is SENSOR_DELAY_UI which is ok for telemetry, maybe not for actual robot use
        mSensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_UI);
    }

    /*
    * This method will be called repeatedly in a loop
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#loop()
    */
    @Override
    public void loop() {
        //telemetry.addData("1 Start", "LightOp started at " + startDate);
        telemetry.addData("Light Level", Math.round(lightLevel) + " SI lux");
    }

    /*
    * Code to run when the op mode is first disabled goes here
    * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
    */
    @Override
    public void stop() {
        Log.d("LightOp", "stop() entered");
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("LightOp", "onAccuracyChanged() entered");
        // not sure if needed, placeholder just in case
    }

    public void onSensorChanged(SensorEvent event) {
        Log.d("LightOp", "onSensorChanged() entered");
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            mLight = event.values;
        }
        else mLight = null;
        if (mLight != null) {
            lightLevel = mLight[0]; // only one value from this sensor
        }
    }
}
