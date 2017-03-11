package com.legacy.apppolicia.common;

/**
 * Created by ivanl on 11/03/2017.
 */
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerResponse {

    private boolean success;
    private int code;
    private String token;
    private ArrayList<Message> messages = new ArrayList<>();
    private String dataString;

    public ServerResponse(boolean success, int code) {
        this.success = success;
        this.code = code;
    }

    public void addMessage(Message toAdd){
        messages.add(toAdd);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public ArrayList<Message> getMessages(){
        return messages;
    }

    public HashMap<String, String> getData() throws JSONException{
        JSONObject object = new JSONObject(dataString);
        return Common.toMap(object);
    }
    public String getStringData(){
        return this.dataString;
    }
    public void setStringData(String data){
        this.dataString = data;
    }
    public void setData(HashMap<String, String> data){
        JSONObject object = new JSONObject(data);
        this.dataString = object.toString();
    }
    public void setData(JSONObject data){
        this.dataString = data.toString();
    }

}
