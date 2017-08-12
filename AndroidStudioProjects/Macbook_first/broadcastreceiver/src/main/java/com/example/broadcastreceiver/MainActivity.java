package com.example.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private IntentFilter intentFilter;
    private LocalReceiver localReceiver;
//
//    private NetworkChangeReceiver networkChangeReceiver;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        intentFilter = new IntentFilter();
//        intentFilter.addAction("android.net.com.conn.CONNETYIVITY_CHANGE");
//        networkChangeReceiver = new NetworkChangeReceiver();
//        registerReceiver(networkChangeReceiver,intentFilter);
//
//    }
//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//        unregisterReceiver(networkChangeReceiver);
//    }
//    class NetworkChangeReceiver extends BroadcastReceiver{
//
//        @Override
//        public void onReceive(Context context, Intent intent){
//            Toast.makeText(context,"network changed",Toast.LENGTH_SHORT).show();
//        }
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.broadcast1.MY_BROAD");

        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);
        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localBroadcastManager.sendBroadcast(new Intent("com.example.broadcast1.MY_BROAD"));
            }
        });
    }
    class LocalReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent){
            Toast.makeText(context,"接收了自己的本地广播",Toast.LENGTH_SHORT).show();
        }
    }
}


