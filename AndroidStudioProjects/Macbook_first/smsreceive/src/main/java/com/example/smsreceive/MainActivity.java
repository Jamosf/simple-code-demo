package com.example.smsreceive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TextView textView1;

    private IntentFilter receiveFilter;
    private MessageReceiver messageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text_send);
        textView1 = (TextView) findViewById(R.id.text_re);

        receiveFilter = new IntentFilter();
        receiveFilter.addAction("android.provider.Telephone.SMS_RECEIVED");
        messageReceiver = new MessageReceiver();
        registerReceiver(messageReceiver,receiveFilter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(messageReceiver);
    }

    class MessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Object[] messageText = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[messageText.length];
            String format = bundle.getString("format");
            for (int i = 0; i < messages.length;i++){
                messages[i] = SmsMessage.createFromPdu((byte[])messageText[i],format);
            }
            String adress = messages[0].getOriginatingAddress();
            String fullMessage = "";
            for(SmsMessage message : messages){
                fullMessage += message.getMessageBody();
            }

            textView.setText(adress);
            textView1.setText(fullMessage);

        }

    }
}
