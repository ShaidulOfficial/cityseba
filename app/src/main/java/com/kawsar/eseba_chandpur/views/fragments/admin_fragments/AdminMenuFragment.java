package com.kawsar.eseba_chandpur.views.fragments.admin_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kawsar.eseba_chandpur.Constant;
import com.kawsar.eseba_chandpur.databinding.FragmentAdminMenuBinding;
import com.kawsar.eseba_chandpur.views.activities.MainActivity;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.views.fragments.admin_fragments.newspaper.JobsNewsFragment;

public class AdminMenuFragment extends Fragment {
    FragmentAdminMenuBinding adminMenuBinding;
DatabaseReference dbrefFeedBack;
long feedBackCountNumber;
    public AdminMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adminMenuBinding = FragmentAdminMenuBinding.inflate(inflater, container, false);
        dbrefFeedBack = FirebaseDatabase.getInstance().getReference(Constant.UserBucket).child("Feedback");
        dbrefFeedBack.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    feedBackCountNumber = snapshot.getChildrenCount();
                    adminMenuBinding.feedbackCountTv.setText("" + feedBackCountNumber);
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        adminMenuBinding.backBtnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));

            }
        });

        //            ********** ambulance fragment ************
        adminMenuBinding.adminBtnAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment ambulance_fragment = new AmbulanceFragment();
                FragmentTransaction ambulance_fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                ambulance_fragmentTransaction.replace(R.id.framlayout_admin_dashboard, ambulance_fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
        //            ********** blood donner fragment ************
        adminMenuBinding.adminBtnBloodDonner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment blood_donner_fragment = new BloodDonnerFragment();
                FragmentTransaction blood_donner_fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                blood_donner_fragmentTransaction.replace(R.id.framlayout_admin_dashboard, blood_donner_fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
        //            ********** doctor donner fragment ************
        adminMenuBinding.adminBtnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment doctorFragment = new DoctorFragment();
                FragmentTransaction doctor_ft = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                doctor_ft.replace(R.id.framlayout_admin_dashboard, doctorFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
        //            ********** emergency fragment ************
        adminMenuBinding.emergencyContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment emergencyFragment = new EmergencyFragment();
                FragmentTransaction emergency_ft = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                emergency_ft.replace(R.id.framlayout_admin_dashboard, emergencyFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

//            ********** fire service fragment ************
        adminMenuBinding.fireServiceAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fireServiceFragment = new FireServiceFragment();
                FragmentTransaction fireService_fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                fireService_fragmentTransaction.replace(R.id.framlayout_admin_dashboard, fireServiceFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        //            **********  feedback fragment ************
        adminMenuBinding.adminBtnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment checkFeedBackFragment = new CheckFeedBackFragment();
                FragmentTransaction checkFeedBackFt = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                checkFeedBackFt.replace(R.id.framlayout_admin_dashboard, checkFeedBackFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        // *************************** hotel fragment ********************************
        adminMenuBinding.hotelAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment hotelFragment = new HotelFragment();
                FragmentTransaction hotel_fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                hotel_fragmentTransaction.replace(R.id.framlayout_admin_dashboard, hotelFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
//            ********** hospital fragment ************
        adminMenuBinding.adminBtnHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment hospital_fragment = new HospitalFragment();
                FragmentTransaction hospital_fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                hospital_fragmentTransaction.replace(R.id.framlayout_admin_dashboard, hospital_fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        // *************************** journal fragment ********************************
        adminMenuBinding.journalAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment journal_fragment = new JournalFragment();
                FragmentTransaction journal_fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                journal_fragmentTransaction.replace(R.id.framlayout_admin_dashboard, journal_fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        // *************************** jobs fragment ********************************
        adminMenuBinding.adminBtnJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment jobs_fragment = new JobsNewsFragment();
                FragmentTransaction jobs_fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                jobs_fragmentTransaction.replace(R.id.framlayout_admin_dashboard, jobs_fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // *************************** lawer fragment ********************************
        adminMenuBinding.adminBtnLawyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fireService_fragment = new LawerFragment();
                FragmentTransaction fireService_fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                fireService_fragmentTransaction.replace(R.id.framlayout_admin_dashboard, fireService_fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        // *************************** police fragment ********************************
        adminMenuBinding.adminBtnPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fireService_fragment = new PoliceFragment();
                FragmentTransaction fireService_fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                fireService_fragmentTransaction.replace(R.id.framlayout_admin_dashboard, fireService_fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        // *************************** paribahan fragment ********************************
        adminMenuBinding.adminBtnParibahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment paribahanFragment = new ParibahanFragment();
                FragmentTransaction paribahanFt = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                paribahanFt.replace(R.id.framlayout_admin_dashboard, paribahanFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // *************************** news paper fragment ********************************
        adminMenuBinding.adminBtnNewspaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newspaperFragment = new NewspaperFragment();
                FragmentTransaction newspaperFt = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                newspaperFt.replace(R.id.framlayout_admin_dashboard, newspaperFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        // *************************** notice fragment ********************************
        adminMenuBinding.adminBtnNoticeAlern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment noticeFragment = new NoticeFragment();
                FragmentTransaction noticeFragmentFt = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                noticeFragmentFt.replace(R.id.framlayout_admin_dashboard, noticeFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        // *************************** polli office  fragment ********************************
        adminMenuBinding.polliAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment journal_fragment = new PolliFragment();
                FragmentTransaction journal_fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                journal_fragmentTransaction.replace(R.id.framlayout_admin_dashboard, journal_fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // *************************** resturent fragment ********************************
        adminMenuBinding.resturentAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment resturentFragment = new ResturentFragment();
                FragmentTransaction Resturent_fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                Resturent_fragmentTransaction.replace(R.id.framlayout_admin_dashboard, resturentFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        // *************************** shopping fragment ********************************
        adminMenuBinding.shoppingAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment shoppingFragment = new ShoppingFragment();
                FragmentTransaction shopping_ft = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                shopping_ft.replace(R.id.framlayout_admin_dashboard, shoppingFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        // *************************** tourist fragment ********************************
        adminMenuBinding.touristAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment touristPlaceFragment = new TouristPlaceFragment();
                FragmentTransaction tourist_fragmentTransaction = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                tourist_fragmentTransaction.replace(R.id.framlayout_admin_dashboard, touristPlaceFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        return adminMenuBinding.getRoot();
    }
}