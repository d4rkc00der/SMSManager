package com.example.user.smsmanager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends Activity  {

    final String LOG_TAG ="MyLog";
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    int count = 0;
    SmsManager smsManager = SmsManager.getDefault();
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(SMS_RECEIVED)) {
                Bundle bundle = intent.getExtras();
                Object[] pdus = (Object[])bundle.get("pdus");

                SmsMessage[] messages = new SmsMessage[pdus.length];
                for(int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    for(SmsMessage message : messages) {
                        Log.d(LOG_TAG,"SMS Received from " + message.getOriginatingAddress());


                    }
                }

            }
        }
    };
    ListView smslist;
    ExpandableListAdapter adapter;
    SimpleCursorAdapter simpleCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smslist = (ListView)findViewById(R.id.smsList);

        IntentFilter filter = new IntentFilter(SMS_RECEIVED);
        registerReceiver(broadcastReceiver,filter);
        Uri inboxUri = Uri.parse("content://sms/inbox");
        String[] columns = {"_id", "address", "body"};
        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(inboxUri,columns,null,null,null);
        simpleCursorAdapter = new SimpleCursorAdapter(this,R.layout.raw,cursor,new String[]{"address"},new int[] {R.id.lblMsg});
        smslist.setAdapter(simpleCursorAdapter);
    }


}
