package com.legacy.apppolicia.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.legacy.apppolicia.R;

public class NoEmergenciesFragment extends Fragment {
    //
    private OnNoEmergenciesFragmentInteractionListener mListener;
    private boolean shift;

    public enum Shift{
        START,
        END
    }
    public NoEmergenciesFragment() {
        // Required empty public constructor
    }

    public static NoEmergenciesFragment newInstance() {
        NoEmergenciesFragment fragment = new NoEmergenciesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        shift = sharedPreferences.getBoolean("shift", false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_no_emergencies, container, false);
        final Button toggleBtn = (Button) v.findViewById(R.id.toggle_shift);
        if(shift){
            toggleBtn.setText(R.string.stop);
        }else{
            toggleBtn.setText(R.string.start);
        }
        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shift){
                    mListener.onNoEmergenciesFragmentInteraction(Shift.END);
                    toggleBtn.setText(R.string.start);
                    shift = false;
                }else{
                    mListener.onNoEmergenciesFragmentInteraction(Shift.START);
                    toggleBtn.setText(R.string.stop);
                    shift = true;
                }
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNoEmergenciesFragmentInteractionListener) {
            mListener = (OnNoEmergenciesFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNoEmergenciesFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnNoEmergenciesFragmentInteractionListener {
        void onNoEmergenciesFragmentInteraction(Shift toDo);
    }
}
