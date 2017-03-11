package com.legacy.apppolicia.services;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.legacy.apppolicia.common.InformationSource;
import com.legacy.apppolicia.common.ServerResponse;

/**
 * Created by ivanl on 11/03/2017.
 */

public class BackgroundPostService extends Service {
    private SendResponseTask sendResponseTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.getExtras() != null && sendResponseTask == null){
            String message = intent.getExtras().getString("message");
            sendResponseTask = new SendResponseTask(message);
            sendResponseTask.execute((Void) null);
        }else if(intent != null && intent.getExtras() != null && sendResponseTask != null){
            sendResponseTask.cancel(true);
            String message = intent.getExtras().getString("message");
            sendResponseTask = new SendResponseTask(message);
            sendResponseTask.execute((Void) null);
        }
        return START_STICKY;
    }

    private class SendResponseTask extends AsyncTask<Void, Void, Boolean>{

        private ServerResponse response;
        private String toSend;
        public SendResponseTask(String response){
            toSend = response;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            response = InformationSource.sendResponse(toSend);
            if(response != null){
                return response.isSuccess();
            }else{
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean response){
            Intent intentMaps = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q=19.030769, -98.235876"));
            intentMaps.setPackage("com.google.android.apps.maps");
            intentMaps.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intentMaps);
            stopSelf();
        }
    }
}
