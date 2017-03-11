package com.legacy.apppolicia.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legacy.apppolicia.R;
import com.legacy.apppolicia.activities.MainActivity;

import static com.legacy.apppolicia.activities.MainActivity.FIRE;
import static com.legacy.apppolicia.activities.MainActivity.MEDICAL;
import static com.legacy.apppolicia.activities.MainActivity.POLICE;

public class ActiveEmergencyFragment extends Fragment {

    private static final String PROFILE_DATA = "profile_data";

    private String mProfileData;
    private boolean responding = false;
    private int type = POLICE;

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
                mListener.onAciveEmergencyFragmentInteraction(Command.NAVIGATE);
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
        }
        Button mainBtn = (Button) v.findViewById(R.id.main_btn);
        if (responding) {
            mainBtn.setText(R.string.solve);
        }else{
            mainBtn.setText(R.string.respond);
        }
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(responding){
                    mListener.onAciveEmergencyFragmentInteraction(Command.SOLVED);
                    //TODO notify webservice
                }else{
                    mListener.onAciveEmergencyFragmentInteraction(Command.RESPONDING);
                    //TODO notify webservice
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
        void onAciveEmergencyFragmentInteraction(Command command);
    }
}
