package com.example.sensorapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * Created by fengshangren on 16/8/14.
 */
public class compass extends AppCompatActivity {

    ImageView compass;
    SensorManager sensorManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compass);
        compass = (ImageView) findViewById(R.id.compass);
//        arrow = (ImageView) findViewById(R.id.arrow);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magensensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(sensorEventListener,magensensor,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(sensorEventListener,accelsensor,SensorManager.SENSOR_DELAY_GAME);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(sensorManager!=null){
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {

        float[] accl = new float[3];
        float[] magen = new float[3];

        private float lastrotatedegree;

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                accl = sensorEvent.values.clone();
            }else if(sensorEvent.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
                magen = sensorEvent.values.clone();
            }
            float[] values = new float[3];
            float[] R = new float[9];
            SensorManager.getRotationMatrix(R,null,accl,magen);
            SensorManager.getOrientation(R,values);
            float rotateDegree = -(float) Math.toDegrees(values[0]);

            if(Math.abs(rotateDegree-lastrotatedegree)>1){
                RotateAnimation animation = new RotateAnimation(lastrotatedegree,rotateDegree, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                animation.setFillAfter(true);
                compass.startAnimation(animation);
                lastrotatedegree = rotateDegree;
            }

        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

}
