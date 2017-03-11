/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.legacy.apppolicia.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.CarExtender;
import android.support.v4.app.NotificationCompat.CarExtender.UnreadConversation;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import com.legacy.apppolicia.R;

public class CopMessagingService extends Service {
    public static final String READ_ACTION =
            "com.legacy.apppolicia.ACTION_MESSAGE_READ";
    public static final String REPLY_ACTION =
            "com.legacy.apppolicia.ACTION_MESSAGE_REPLY";
    public static final String CONVERSATION_ID = "conversation_id";
    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";
    private static final String TAG = CopMessagingService.class.getSimpleName();
    private final Messenger mMessenger = new Messenger(new IncomingHandler());
    private NotificationManagerCompat mNotificationManager;

    @Override
    public void onCreate() {
        mNotificationManager = NotificationManagerCompat.from(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.getExtras() != null){
            String message = intent.getExtras().getString("message");
            sendNotification(1, message, "John Doe",
                    System.currentTimeMillis());
        }
        return START_STICKY;
    }


    private void sendNotification(int conversationId, String message,
                                  String participant, long timestamp) {
        Log.i(TAG, "Message: " + message);
        Intent msgHeardIntent = new Intent()
                .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction(READ_ACTION)
                .putExtra("conversation_id", conversationId);

        PendingIntent msgHeardPendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(),
                        conversationId,
                        msgHeardIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        // Build a RemoteInput for receiving voice input in a Car Notification
        RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY)
                .setLabel("Reply by voice")
                .build();

        // Building a Pending Intent for the reply action to trigger
        Intent msgReplyIntent = new Intent()
                .addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
                .setAction(REPLY_ACTION)
                .putExtra("conversation_id", conversationId);

        PendingIntent msgReplyPendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                conversationId,
                msgReplyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the UnreadConversation and populate it with the participant name,
        // read and reply intents.
        UnreadConversation.Builder unreadConvBuilder =
                new UnreadConversation.Builder(participant)
                        .setLatestTimestamp(timestamp)
                        .setReadPendingIntent(msgHeardPendingIntent)
                        .setReplyAction(msgReplyPendingIntent, remoteInput);

        unreadConvBuilder.addMessage(message)
                .setLatestTimestamp(timestamp);

        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.mipmap.ic_launcher);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setSmallIcon(R.drawable.ic_warning_black)
                        .setLargeIcon(icon)
                        .setContentText(message)
                        .setWhen(timestamp)
                        .setContentTitle(participant)
                        .setContentIntent(msgHeardPendingIntent)
                        .extend(new CarExtender()
                        .setUnreadConversation(unreadConvBuilder.build()));

        mNotificationManager.notify("COP", 150, notificationBuilder.build());
    }

    /**
     * Handler of incoming messages from clients.
     */
    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            String message = ((Bundle) msg.obj).getString("message");
            sendNotification(1, message, "John Doe",
                    System.currentTimeMillis());
        }
    }
}
