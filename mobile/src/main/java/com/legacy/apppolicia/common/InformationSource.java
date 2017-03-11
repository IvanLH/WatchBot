package com.legacy.apppolicia.common;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ivanl on 11/03/2017.
 */

public class InformationSource {
    private static final int POST = 1;
    private static final int GET = 2;
    private static final String TAG = "Information Service";
    private static final boolean SUCCESS = true;
    private static final boolean FAILURE = false;

    private static String getData(String requestURL, HashMap<String, String> getDataParams) {
        URL url;
        String response = "";
        String createdURL = requestURL;
        if (getDataParams.size() > 0) {
            createdURL += "?";
            for (Map.Entry<String, String> entry : getDataParams.entrySet()) {
                createdURL += entry.getKey();
                createdURL += "=";
                createdURL += entry.getValue();
                createdURL += "&";
            }
            createdURL = createdURL.substring(0, createdURL.length() - 1);
        }
        try {
            url = new URL(createdURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            String line;
            if (conn.getErrorStream() != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static String postData(String requestURL, HashMap<String, String> postDataParams) {
        URL url;
        String response = "";
        Log.i("CALL", requestURL);
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            String line;
            if (conn.getErrorStream() != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            }


        } catch (MalformedURLException ex) {
            Log.e(TAG, "Invalid URL");
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e(TAG, "Error abriendo la conexion");
        }
        return response;
    }

    private static ServerResponse sendData(int type, String url, String canary, HashMap<String, String> data) {
        JSONObject jsonObject;
        ServerResponse serverResponse;
        String response;
        if (type == POST) {
            response = postData(url, data);
        } else {
            response = getData(url, data);
        }
        if (response.equals("")) {
            return null;
        } else {
            try {
                jsonObject = new JSONObject(response);
                jsonObject.getString(canary);
                serverResponse = new ServerResponse(true, 0);
                serverResponse.setData(jsonObject);
                return serverResponse;
            } catch (JSONException ex) {
                try {
                    jsonObject = new JSONObject(response);
                    int errorCode = jsonObject.getInt("code");
                    serverResponse = new ServerResponse(false, errorCode);
                    JSONArray messages = jsonObject.getJSONArray("message");
                    for (int i = 0; i < messages.length(); i++) {
                        JSONObject message = messages.getJSONObject(i);
                        serverResponse.addMessage(new Message(message.getInt("code"), message.getString("reference"), message.getString("verbose")));
                    }
                    return serverResponse;

                } catch (JSONException exc) {
                    Log.e(TAG, response);
                    Log.e(TAG, "Unknown error");
                    return null;
                }
            }
        }
    }

    private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    public static ServerResponse getEmergency(){
        HashMap<String, String> data = new HashMap<>();
        data.put("idUnidad", "9001");
        return sendData(POST, "https://echo-mx.herokuapp.com/services/showEmergencyDetails", "emergencia", data);
    }

    public static ServerResponse sendResponse(String toSend, int emergency_id) {
        HashMap<String, String> data = new HashMap<>();
        data.put("idUnidad", "9001");
        data.put("idEmergencia", "" + emergency_id);
        data.put("answer", toSend);
        return sendData(POST, "https://echo-mx.herokuapp.com/services/response", "success", data);
    }

    public static ServerResponse notifySolved(int emergency_id) {
        HashMap<String, String> data = new HashMap<>();
        data.put("idUnidad", "9001");
        data.put("idEmergencia", "" + emergency_id);
        return sendData(POST, "https://echo-mx.herokuapp.com/services/solved", "success", data);
    }

    public static ServerResponse notifyShiftStarting() {
        HashMap<String, String> data = new HashMap<>();
        data.put("idUnidad", "9001");
        return sendData(POST, "https://echo-mx.herokuapp.com/services/startShift", "success", data);
    }

    public static ServerResponse notifyShiftEnding() {
        HashMap<String, String> data = new HashMap<>();
        data.put("idUnidad", "9001");
        return sendData(POST, "https://echo-mx.herokuapp.com/services/endShift", "success", data);
    }

    public static ServerResponse sendLocationUpdate(double longitude, double latitude) {
        HashMap<String, String> data = new HashMap<>();
        data.put("numeroUnidad", "9001");
        data.put("x", "" + longitude);
        data.put("y", "" + latitude);
        return sendData(POST, "https://echo-mx.herokuapp.com/services/updateLocation", "success", data);
    }
}
