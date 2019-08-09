package com.shemaroo.radiosdklib.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;

import com.shemaroo.radiosdklib.mediaplayer.Controls;

/**
 * Created by Shweta Basu on 2/9/2016.
 */
public class PhoneStateReceiver extends BroadcastReceiver {
    Tag TAG;
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String str = intent.getAction();
        if ("android.intent.action.PHONE_STATE".equals(str))
            inComing(context, intent);

        if ("android.intent.action.NEW_OUTGOING_CALL".equals(str))
            trueCallerOutgoing(context, intent);
    }

    private void inComing(Context context, Intent intent){
        String callState = intent.getStringExtra("state");
        if ("RINGING".equals(callState)){
           // Log.i(TAG, "RINGING SENDS BUSY");
            Controls.pauseControl(context);
        }else if ("OFFHOOK".equals(callState)){
            Controls.pauseControl(context);
        }else if("IDLE".equals(callState)){
            Controls.playControl(context);
        }
    }

    private void trueCallerOutgoing(Context context, Intent intent)
    {
        String callState = intent.getStringExtra("state");
        if ("RINGING".equals(callState)){
           // Log.i(TAG, "RINGING SENDS BUSY");
            Controls.pauseControl(context);
        }else if ("OFFHOOK".equals(callState)){
           // Log.i(TAG, "OFFHOOK SENDS BUSY");
            Controls.pauseControl(context);
        }else if("IDLE".equals(callState)){
            //Log.i(TAG, "IDLE SENDS AVAILABLE");
            Controls.playControl(context);
        }
    }
}
