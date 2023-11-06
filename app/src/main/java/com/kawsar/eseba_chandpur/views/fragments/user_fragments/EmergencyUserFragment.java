package com.kawsar.eseba_chandpur.views.fragments.user_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterEmergency;
import com.kawsar.eseba_chandpur.adapters.users_adapter.AdapterEmergencyUser;
import com.kawsar.eseba_chandpur.databinding.FragmentEmergencyUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.List;


public class EmergencyUserFragment extends Fragment {

    FragmentEmergencyUserBinding emergencyUserBinding;
    AdapterEmergencyUser adapterEmergencyUser;
    DatabaseReference dbRefEmergencyUser;
    List<CommonModel> modelListEmergencyUser = new ArrayList<>();
    public EmergencyUserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        emergencyUserBinding = FragmentEmergencyUserBinding.inflate(inflater
                , container, false);
        adapterEmergencyUser = new AdapterEmergencyUser(getContext(), modelListEmergencyUser);
        adapterEmergencyUser.notifyDataSetChanged();
        dbRefEmergencyUser = FirebaseDatabase.getInstance().getReference("Admin").child("Emergency");
        dbRefEmergencyUser.keepSynced(true);
        getDataFromFirebase();
        emergencyUserBinding.backBtnEmergencyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return emergencyUserBinding.getRoot();
    }

    private void getDataFromFirebase() {
        emergencyUserBinding.loadingEmergencyUser.setVisibility(View.VISIBLE);
        dbRefEmergencyUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelListEmergencyUser.clear();

                emergencyUserBinding.loadingEmergencyUser.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {

                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelListEmergencyUser.add(commonModel_hospital);

                }
                if (modelListEmergencyUser.isEmpty()) {
                    emergencyUserBinding.loadingEmergencyUser.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapterEmergencyUser.notifyDataSetChanged();
                emergencyUserBinding.emergencyUserAdminRcv.setHasFixedSize(true);
                emergencyUserBinding.emergencyUserAdminRcv.setAdapter(adapterEmergencyUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}