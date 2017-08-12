package com.lewisen.goodnight.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lewisen.goodnight.R;
import com.lewisen.goodnight.app.MyApplication;
import com.lewisen.goodnight.dataSrc.PageData;

import java.util.Random;

/**
 *
 */
public class ShakeRandomFragment extends Fragment implements SensorEventListener {
    private final int threshold = 17;
    private Context mContext;
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private int maxHomeID, maxPicID, maxArtID;
    private final int minID = 25;//最小ID
    private Random random;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        //获取现有最大ID
        maxHomeID = MyApplication.appConfig.getMaxId("homePageMaxId");
        maxPicID = MyApplication.appConfig.getMaxId("picturePageMaxId");
        maxArtID = MyApplication.appConfig.getMaxId("articlePageMaxId");
        random = new Random();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();

        //获取传感器
        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        //获取振动
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);


        return inflater.inflate(R.layout.fragment_shake_random, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        //注册监听器
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            if ((Math.abs(values[0]) > threshold || Math.abs(values[1]) > threshold || Math.abs(values[2]) > threshold)) {
                //摇动手机后，伴随震动提示
                vibrator.vibrate(200);
                openRandomPage();
            }
        }
    }

    /**
     * 生成随机ID  并且打开页面
     */
    private void openRandomPage() {
        int type = random.nextInt(3) + 1;
        int pageID = 0;

        if (type == PageData.TYPE_HOME) {
            pageID = random.nextInt(maxHomeID);
        } else if (type == PageData.TYPE_ARTICLE) {
            pageID = random.nextInt(maxArtID);
        } else if (type == PageData.TYPE_PICTURE) {
            pageID = random.nextInt(maxPicID);
        } else {
            type = PageData.TYPE_ARTICLE;//不会发生
        }

        if (pageID < minID) {
            pageID += minID;
        }

//        Util.printInfo("类型 = " + type);
//        Util.printInfo("ID = " + pageID);

        Intent intent = new Intent(mContext, DisplayDetailActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("pageID", pageID);
        startActivity(intent);
        ((Activity) mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPause() {
        super.onPause();
//        Util.printInfo("==onPause==");
        sensorManager.unregisterListener(this);
    }
}
