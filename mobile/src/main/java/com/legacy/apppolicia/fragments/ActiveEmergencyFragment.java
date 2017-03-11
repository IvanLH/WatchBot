package com.legacy.apppolicia.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legacy.apppolicia.R;
import com.legacy.apppolicia.activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.legacy.apppolicia.activities.MainActivity.FIRE;
import static com.legacy.apppolicia.activities.MainActivity.MEDICAL;
import static com.legacy.apppolicia.activities.MainActivity.POLICE;

public class ActiveEmergencyFragment extends Fragment {

    private static final String PROFILE_DATA = "profile_data";

    private String mProfileData;
    private boolean responding = false;
    private int type = POLICE;
    private double lat;
    private double lon;
    private String name;
    private String details;
    private int id;

    public enum Command{
        NAVIGATE,
        SOLVED,
        RESPONDING
    }

    private OnActiveEmergencyFragmentInteractionListener mListener;

    public ActiveEmergencyFragment() {
        // Required empty public constructor
    }

    public static ActiveEmergencyFragment newInstance(String profile_data) {
        ActiveEmergencyFragment fragment = new ActiveEmergencyFragment();
        Bundle args = new Bundle();
        args.putString(PROFILE_DATA, profile_data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mProfileData = getArguments().getString(PROFILE_DATA);
            try {
                JSONObject object = new JSONObject(mProfileData);
                lat = object.getDouble("ubicacionX");
                lon = object.getDouble("ubicacionY");
                name = object.getString("nombre");
                if(object.has("detalles")) {
                    details = object.getString("detalles");
                }
                type = object.getInt("clase");
                id = object.getInt("idEmergencia");
                int status = object.getInt("estado");
                responding = status == 1;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_active_emergency, container, false);
        LinearLayout navigateLL = (LinearLayout) v.findViewById(R.id.navigate_ll);
        navigateLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAciveEmergencyFragmentInteraction(Command.NAVIGATE, lat, lon, 0);
            }
        });
        LinearLayout titleLL = (LinearLayout) v.findViewById(R.id.title_ll);
        TextView emergencyTV = (TextView) v.findViewById(R.id.emergency_tv);
        switch (type){
            case FIRE:
                emergencyTV.setText(R.string.fire);
                titleLL.setBackground(getResources().getDrawable(R.drawable.fire_card_background));
                break;
            case MEDICAL:
                emergencyTV.setText(R.string.medical);
                titleLL.setBackground(getResources().getDrawable(R.drawable.medical_card_background));
                break;
            case POLICE:
                emergencyTV.setText(R.string.police);
                titleLL.setBackground(getResources().getDrawable(R.drawable.police_card_background));
                break;
            default:
                emergencyTV.setText(R.string.police);
                titleLL.setBackground(getResources().getDrawable(R.drawable.police_card_background));
                break;
        }
        TextView nameTV = (TextView) v.findViewById(R.id.name_tv);
        TextView detailsTV = (TextView)v.findViewById(R.id.details_tv);

        nameTV.setText(name);
        detailsTV.setText(details);

        final Button mainBtn = (Button) v.findViewById(R.id.main_btn);
        if (responding) {
            mainBtn.setText(R.string.solve);
        }else{
            mainBtn.setText(R.string.respond);
        }
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(responding){
                    mListener.onAciveEmergencyFragmentInteraction(Command.SOLVED, 0, 0, id);
                }else{
                    mListener.onAciveEmergencyFragmentInteraction(Command.RESPONDING, 0, 0, id);
                    mainBtn.setText(R.string.solve);
                    responding = true;
                }
            }
        });
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnActiveEmergencyFragmentInteractionListener) {
            mListener = (OnActiveEmergencyFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnActiveEmergencyFragmentInteractionListener {
        void onAciveEmergencyFragmentInteraction(Command command, double lat, double lon, int id);
    }
}
