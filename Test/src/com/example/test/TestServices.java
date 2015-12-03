package com.hieptran.onlineproject;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

/**
 * HiepTran
 */
public class TestServices extends Service implements SensorEventListener{
    private SensorManager mSensorManager;
    private float mThresholdMin, mThresholdMax;
    public static final String KEY_SENSOR_TYPE = "sensor_type";

    public static final String KEY_THRESHOLD_MIN_VALUE = "threshold_min_value";

    public static final String KEY_THRESHOLD_MAX_VALUE = "threshold_max_value";


    private static float previousValue;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d("HiepTHb","onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("HiepTHb","onStartCommand");
        // get sensor manager on starting the service
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // have a default sensor configured
        int sensorType = Sensor.TYPE_PROXIMITY;

        Bundle args = intent.getExtras();

        // get some properties from the intent
        if (args != null) {

            // set sensortype from bundle
            if (args.containsKey(KEY_SENSOR_TYPE))
                sensorType = args.getInt(KEY_SENSOR_TYPE);


            // treshold values
            // since we want to take them into account only when configured use min and max
            // values for the type to disable
            mThresholdMin = args.containsKey(KEY_THRESHOLD_MIN_VALUE) ? args.getFloat(KEY_THRESHOLD_MIN_VALUE) : Float.MIN_VALUE;
            mThresholdMax = args.containsKey(KEY_THRESHOLD_MAX_VALUE) ? args.getFloat(KEY_THRESHOLD_MAX_VALUE) : Float.MAX_VALUE;
        }

        // we need the light sensor
        Sensor sensor = mSensorManager.getDefaultSensor(sensorType);

        // TODO we could have the sensor reading delay configurable also though that won't do much
        // in this use case since we work with the alarm manager
        mSensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("HiepTHb","onAccuracyChanged");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float sensorValue = event.values[0];
        Log.d("HiepTHb","onSensorChanged");
        // if first value is below min or above max threshold but only when configured
        // we need to enable the screen
        if ((previousValue > mThresholdMin && sensorValue < mThresholdMin)
                || (previousValue < mThresholdMax && sensorValue > mThresholdMax)) {

            // and a check in between that there should have been a non triggering value before
            // we can mark a given value as trigger. This is to overcome unneeded wakeups during
            // night for instance where the sensor readings for a light sensor would always be below
            // the threshold needed for day time use.

            // TODO we could even make the actions configurable...

            // wake screen here
            PowerManager pm = (PowerManager) getApplicationContext().getSystemService(getApplicationContext().POWER_SERVICE);
            PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK|PowerManager.ACQUIRE_CAUSES_WAKEUP), "ScreenOnOff-HiepTHb");
            wakeLock.acquire();

            //and release again
            wakeLock.release();

            // optional to release screen lock
            //KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(getApplicationContext().KEYGUARD_SERVICE);
            //KeyguardManager.KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock(TAG);
            //keyguardLock.disableKeyguard();
        }

        previousValue = sensorValue;

        // stop the sensor and service
        mSensorManager.unregisterListener(this);
    }
}
