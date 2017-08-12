package com.example.notificationtest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.btn_noti);
        button.setOnClickListener(this);
    }
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btn_noti:
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification.Builder builder = new Notification.Builder(this);
                        builder.setContentInfo("这是什么？");
                        builder.setContentText("不知道");
                        builder.setContentTitle("我表示不知道");
                        builder.setTicker("news");
                        builder.setSmallIcon(R.drawable.icon);
                        builder.setAutoCancel(true);
                        builder.setWhen(System.currentTimeMillis());
                        Intent intent = new Intent(MainActivity.this,notatall.class);
                        PendingIntent pendingintent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                        builder.setContentIntent(pendingintent);
                        Notification notified = builder.build();
                        notificationManager.notify(1,notified);
                        break;
                    default:
                        break;
                }
            }

    }

