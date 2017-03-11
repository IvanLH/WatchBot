package com.legacy.apppolicia.recievers;

/**
 * Created by ivanl on 10/03/2017.
 */

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;
import android.util.Log;

import com.legacy.apppolicia.services.CopMessagingService;
import com.pushbots.push.utils.PBConstants;


public class CustomHandler extends BroadcastReceiver
{
    private boolean mServiceBound;
    private String TAG = "PB3";
    private Messenger msngr;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String action = intent.getAction();
        Log.d(TAG, "action=" + action);

        // Handle Push Message when opened
        if (action.equals(PBConstants.EVENT_MSG_OPEN)) {
        }else if(action.equals(PBConstants.EVENT_MSG_RECEIVE)){
            //Bundle containing all fields of the notification
            Bundle bundle = intent.getExtras().getBundle(PBConstants.EVENT_MSG_RECEIVE);
            Intent service = new Intent(context, CopMessagingService.class);
            service.putExtras(bundle);
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            context.startService(service);
            Log.i(TAG, "User received notification with Message: " + bundle.get("message"));

        }

    }

}