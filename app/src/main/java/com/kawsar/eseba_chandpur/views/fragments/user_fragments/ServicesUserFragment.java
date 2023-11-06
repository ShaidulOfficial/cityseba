package com.kawsar.eseba_chandpur.views.fragments.user_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.kawsar.eseba_chandpur.Constant;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.databinding.FragmentServicesUserBinding;


public class ServicesUserFragment extends Fragment {

    FragmentServicesUserBinding servicesUserBinding;
    AdRequest adRequest;
    private InterstitialAd mInterstitialAd;

    public ServicesUserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        servicesUserBinding = FragmentServicesUserBinding.inflate(inflater, container
                , false);
        MobileAds.initialize(getContext(), initializationStatus -> {
        });
        adRequest = new AdRequest.Builder().build();
        servicesUserBinding.backBtnUserService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        // *************************** ambulance fragment ********************************
        servicesUserBinding.ambulanceUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullScreen();
                Fragment ambulanceUserFragment = new AmbulanceUserFragment();
                FragmentTransaction ambulanceUser_ft = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                ambulanceUser_ft.replace(R.id.framlayout_user_dashboard, ambulanceUserFragment)
                        .addToBackStack(null).commit();
            }
        });
        // *************************** doctor fragment ********************************
        servicesUserBinding.doctorUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment doctorUserFragment = new DoctorUserFragment();
                FragmentTransaction doctorUser_ft = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                doctorUser_ft.replace(R.id.framlayout_user_dashboard, doctorUserFragment)
                        .addToBackStack(null).commit();
            }
        });

        // *************************** emergency fragment ********************************
        servicesUserBinding.emergencyContactUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment emergencyUserFragment = new EmergencyUserFragment();
                FragmentTransaction emergencyUser_ft = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                emergencyUser_ft.replace(R.id.framlayout_user_dashboard, emergencyUserFragment)
                        .addToBackStack(null).commit();
            }
        });
        // *************************** fire service fragment ********************************
        servicesUserBinding.fireServiceUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fireServiceUserFragment = new FireServiceUserFragment();
                FragmentTransaction fireServiceUser_ft = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fireServiceUser_ft.replace(R.id.framlayout_user_dashboard, fireServiceUserFragment)
                        .addToBackStack(null).commit();
            }
        });
        // *************************** journal fragment ********************************
        servicesUserBinding.journalUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullScreen();
                Fragment journalUserFragment = new JournalUserFragment();
                FragmentTransaction journalUser_ft = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                journalUser_ft.replace(R.id.framlayout_user_dashboard, journalUserFragment)
                        .addToBackStack(null).commit();
            }
        });
        // *************************** hospital fragment ********************************
        servicesUserBinding.hospitalUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment hospitalUserFragment = new HospitaUserFragment();
                FragmentTransaction hospitalUser_ft = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                hospitalUser_ft.replace(R.id.framlayout_user_dashboard, hospitalUserFragment)
                        .addToBackStack(null).commit();
            }
        });
        // *************************** lawyer fragment ********************************
        servicesUserBinding.lawerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullScreen();
                Fragment lawerUserFragment = new LawerUserFragment();
                FragmentTransaction lawer_ft = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                lawer_ft.replace(R.id.framlayout_user_dashboard, lawerUserFragment)
                        .addToBackStack(null).commit();
            }
        });

        // *************************** police fragment ********************************
        servicesUserBinding.policeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment policeFragment = new PoliceUserFragment();
                FragmentTransaction fragmentTransaction_police = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction_police.replace(R.id.framlayout_user_dashboard, policeFragment)
                        .addToBackStack(null).commit();
            }
        });

//        // *************************** journal fragment ********************************
        servicesUserBinding.journalUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullScreen();
                Fragment journalUserFragment = new JournalUserFragment();
                FragmentTransaction fragmentTransaction_journal = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction_journal.replace(R.id.framlayout_user_dashboard, journalUserFragment)
                        .addToBackStack(null).commit();
            }
        });


//        // *************************** polli fragment ********************************
        servicesUserBinding.polliUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullScreen();
                Fragment polliUserFragment = new PolliUserFragment();
                FragmentTransaction fragmentTransaction_polliUser = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction_polliUser.replace(R.id.framlayout_user_dashboard, polliUserFragment)
                        .addToBackStack(null).commit();
            }
        });


        return servicesUserBinding.getRoot();

    }

    @Override
    public void onResume() {
        loadInterstitial();
        super.onResume();
    }

    private void showFullScreen() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(getActivity());
        } else {
            Toast.makeText(getContext(), "no load fullScreen ads", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadInterstitial() {
        InterstitialAd.load(getContext(), Constant.admob_INTERSTITIAL_UNIT_ID, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {


                        mInterstitialAd = null;
                    }
                });
    }
}