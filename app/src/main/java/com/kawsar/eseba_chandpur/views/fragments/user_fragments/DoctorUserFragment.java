package com.kawsar.eseba_chandpur.views.fragments.user_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.adapters.users_adapter.AdapterDoctorUser;
import com.kawsar.eseba_chandpur.databinding.FragmentDoctorUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.List;


public class DoctorUserFragment extends Fragment {

    FragmentDoctorUserBinding doctorUserBinding;
    AdapterDoctorUser adapterDoctorUser;
    DatabaseReference databaseReference_doctor;
    List<CommonModel> modelList_doctorUser = new ArrayList<>();

    public DoctorUserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        doctorUserBinding = FragmentDoctorUserBinding.inflate(inflater, container, false);


        databaseReference_doctor = FirebaseDatabase.getInstance().getReference("Admin").child("Doctors");

        getDataFromFirebase();
        doctorUserBinding.backBtnDoctoruser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().onBackPressed();
            }
        });
        adapterDoctorUser = new AdapterDoctorUser(getContext(), modelList_doctorUser);
        adapterDoctorUser.notifyDataSetChanged();

        doctorUserBinding.doctorUserSearchV.clearFocus();
        doctorUserBinding.doctorUserSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        return doctorUserBinding.getRoot();
    }

    private void filterList(String newText) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : modelList_doctorUser) {
            if (model.getSearch_data().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(model);

            } else {

            }
        }
        if (filterlist.isEmpty()) {

            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterDoctorUser.setFilterDoctorUser(filterlist);
        }
    }

    private void getDataFromFirebase() {
        doctorUserBinding.loadingProgressDoctoruser.setVisibility(View.VISIBLE);
        databaseReference_doctor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList_doctorUser.clear();
                doctorUserBinding.loadingProgressDoctoruser.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_doctorUser = ds.getValue(CommonModel.class);
                    modelList_doctorUser.add(commonModel_doctorUser);
                }
                if (modelList_doctorUser.isEmpty()) {
                    doctorUserBinding.loadingProgressDoctoruser.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                } else {
                }
                adapterDoctorUser.notifyDataSetChanged();
                doctorUserBinding.doctorUserRecyclerView.setAdapter(adapterDoctorUser);
                doctorUserBinding.doctorUserRecyclerView.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}