package com.kawsar.eseba_chandpur.views.fragments.user_fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.kawsar.eseba_chandpur.adapters.users_adapter.AdapterCommonUser;

import com.kawsar.eseba_chandpur.databinding.FragmentHospitalUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;


import java.util.ArrayList;
import java.util.List;


public class HospitaUserFragment extends Fragment {

    FragmentHospitalUserBinding hospitalUserBinding;
    DatabaseReference databaseReference_hospital_user;
    List<CommonModel> hospitalUserModelList = new ArrayList<>();
    AdapterCommonUser adapterCommonUser;
    public HospitaUserFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hospitalUserBinding = FragmentHospitalUserBinding.inflate(inflater, container, false);

        databaseReference_hospital_user = FirebaseDatabase.getInstance().getReference("Admin").child("Hospitals");
        databaseReference_hospital_user.keepSynced(true);
        adapterCommonUser = new AdapterCommonUser(getContext(), hospitalUserModelList);
        adapterCommonUser.notifyDataSetChanged();

        getDataFromFirebase();
        hospitalUserBinding.backBtnUserHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        hospitalUserBinding.hospitalUserSearchV.clearFocus();
        hospitalUserBinding.hospitalUserSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        hospitalUserBinding.swipeHospitalUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    hospitalUserBinding.layNonetHospitaluser.setVisibility(View.VISIBLE);
                } else {
                    hospitalUserBinding.layNonetHospitaluser.setVisibility(View.GONE);
                }
                getDataFromFirebase();
                adapterCommonUser.notifyDataSetChanged();
                hospitalUserBinding.swipeHospitalUser.setRefreshing(false);
            }
        });

        return hospitalUserBinding.getRoot();
    }

    private void filterList(String text) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : hospitalUserModelList) {
            if (model.getSearch_data().toLowerCase().contains(text.toLowerCase())) {
                filterlist.add(model);
            }
        }
        if (filterlist.isEmpty()) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterCommonUser.setFilterList(filterlist);
        }
    }


    private void getDataFromFirebase() {
        hospitalUserBinding.loadingProgressUserHospital.setVisibility(View.VISIBLE);
        databaseReference_hospital_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hospitalUserModelList.clear();
                hospitalUserBinding.loadingProgressUserHospital.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {

                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    hospitalUserModelList.add(commonModel_hospital);

                }
                if (hospitalUserModelList.isEmpty()) {
                    hospitalUserBinding.loadingProgressUserHospital.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapterCommonUser.notifyDataSetChanged();
                hospitalUserBinding.hospitalRecyclerView.setAdapter(adapterCommonUser);
                hospitalUserBinding.hospitalRecyclerView.setHasFixedSize(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setuponBackpressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()) {
                    Toast.makeText(getContext(), "go back", Toast.LENGTH_SHORT).show();
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });
    }
}