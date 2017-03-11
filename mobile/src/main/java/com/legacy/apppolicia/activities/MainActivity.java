package com.legacy.apppolicia.activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.legacy.apppolicia.common.InformationSource;
import com.legacy.apppolicia.common.ServerResponse;
import com.legacy.apppolicia.fragments.ActiveEmergencyFragment.Command;
import com.legacy.apppolicia.fragments.ActiveEmergencyFragment;
import com.legacy.apppolicia.fragments.NoEmergenciesFragment;
import com.legacy.apppolicia.services.CopMessagingService;
import com.legacy.apppolicia.recievers.CustomHandler;
import com.legacy.apppolicia.R;
import com.legacy.apppolicia.services.UpdateLocationService;
import com.pushbots.push.Pushbots;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements ActiveEmergencyFragment.OnActiveEmergencyFragmentInteractionListener,
        NoEmergenciesFragment.OnNoEmergenciesFragmentInteractionListener{

    private static final int MY_LOCATION_PERMISSION_REQUEST = 1;
    private double lat;
    private double lon;

    private String myId;

    private SharedPreferences emergencyPreferences;
    private SharedPreferences userPreferences;

    public static final int FIRE = 0;
    public static final int POLICE = 1;
    public static final int MEDICAL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Pushbots.sharedInstance().registerForRemoteNotifications();
        Pushbots.setAlias("Me caga pushbots");
        Pushbots.sharedInstance().setCustomHandler(CustomHandler.class);
        checkLocationPermission();
        emergencyPreferences = getSharedPreferences("active_emergency", MODE_PRIVATE);
        userPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(R.string.app_name);
        }
        GetEmergencyTask getEmergencyTask = new GetEmergencyTask();
        getEmergencyTask.execute((Void) null);

    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, CopMessagingService.class);
        startService(intent);
    }

    @Override
    public void onAciveEmergencyFragmentInteraction(Command command, double lat, double lon, int id) {
        switch (command){
            case NAVIGATE:
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q="+lat+"," + lon));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
                break;
            case SOLVED:
                NotifySolvedTask notifySolvedtask = new NotifySolvedTask(id);
                notifySolvedtask.execute((Void) null);
                break;
            case RESPONDING:
                SendResponseTask sendResponseTask = new SendResponseTask("Yes", id);
                sendResponseTask.execute((Void) null);
                break;
        }
    }

    @Override
    public void onNoEmergenciesFragmentInteraction(NoEmergenciesFragment.Shift toDo) {
        switch (toDo){
            case START:
                NotifyShiftStartingTask notifyShiftStartingTask = new NotifyShiftStartingTask();
                notifyShiftStartingTask.execute((Void) null);
                Intent intent = new Intent(this, UpdateLocationService.class);
                startService(intent);
                break;
            case END:
                NotifyShiftEndingTask notifyShiftEndingTask = new NotifyShiftEndingTask();
                notifyShiftEndingTask.execute((Void) null);
                intent = new Intent(this, UpdateLocationService.class);
                stopService(intent);
                break;
        }
    }

    private class GetEmergencyTask extends AsyncTask<Void, Void, Boolean>{
        private ServerResponse response;
        @Override
        protected Boolean doInBackground(Void... params) {
            response = InformationSource.getEmergency();
            if (response != null) {
                return response.isSuccess();
            }else{
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean result){
            Fragment toNav;
            if(result){
                try {
                    response.getData();
                    String array = response.getData().get("emergencia");
                    StringBuilder builder = new StringBuilder(array);
                    JSONArray array1 = new JSONArray(array);
                    JSONObject object = array1.getJSONObject(0);
                    toNav = ActiveEmergencyFragment.newInstance(object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    toNav = null;
                }

            }else{
                toNav = NoEmergenciesFragment.newInstance();
            }
            try{
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, toNav)
                        .commit();
            }catch(Exception e){

            }
        }
    }

    private class NotifySolvedTask extends AsyncTask<Void, Void, Boolean>{
        private final int emergencyId;
        private ServerResponse response;

        public NotifySolvedTask(int id) {
            emergencyId = id;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            response = InformationSource.notifySolved(emergencyId);
            if (response != null) {
                return response.isSuccess();
            }else{
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean response){
            GetEmergencyTask getEmergencyTask = new GetEmergencyTask();
            getEmergencyTask.execute((Void) null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_PERMISSION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    //TODO permission denied
                }
                break;
            }
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                //TODO permission rejected
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_PERMISSION_REQUEST);
            }
        }
    }

    private class NotifyShiftStartingTask extends AsyncTask<Void, Void, Boolean>{
        private ServerResponse response;

        @Override
        protected Boolean doInBackground(Void... params) {
            response = InformationSource.notifyShiftStarting();
            if(response != null){
                return  response.isSuccess();
            }else{
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result){

        }
    }

    private class NotifyShiftEndingTask extends AsyncTask<Void, Void, Boolean>{
        private ServerResponse response;


        @Override
        protected Boolean doInBackground(Void... params) {
            response = InformationSource.notifyShiftEnding();
            if(response != null){
                return response.isSuccess();
            }else{
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean result){

        }
    }

    private class SendResponseTask extends AsyncTask<Void, Void, Boolean>{

        private ServerResponse response;
        private String toSend;
        private int emergency_id;
        public SendResponseTask(String response, int emergencyId){
            toSend = response;
            emergency_id = emergencyId;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            response = InformationSource.sendResponse(toSend, emergency_id);
            if(response != null){
                return response.isSuccess();
            }else{
                return false;
            }
        }
    }
}
