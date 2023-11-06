package com.kawsar.eseba_chandpur.views.fragments.user_fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.kawsar.eseba_chandpur.databinding.FragmentQiblaBinding;


public class QiblaFragment extends Fragment implements SensorEventListener {

    FragmentQiblaBinding qiblaBinding;
    private static SensorManager sensorManager;
    private float current_degrees;

    public QiblaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        qiblaBinding = FragmentQiblaBinding.inflate(inflater, container, false);
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        qiblaBinding.salamTv.setSelected(true);
        qiblaBinding.backBtnQibla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return qiblaBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        sensorManager.registerListener(this, sensorManager
                .getDefaultSensor(Sensor.TYPE_ORIENTATION), sensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);
        RotateAnimation rotateAnimation = new RotateAnimation(current_degrees, -degree,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(120);
        rotateAnimation.setFillAfter(true);
        qiblaBinding.compass.startAnimation(rotateAnimation);
        current_degrees = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}