package com.kawsar.eseba_chandpur.views.fragments.user_fragments;

import android.os.Bundle;

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
import com.kawsar.eseba_chandpur.adapters.users_adapter.AdapterBloodUser;
import com.kawsar.eseba_chandpur.databinding.FragmentBloodDonorUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.List;


public class BloodDonorUserFragment extends Fragment {

    FragmentBloodDonorUserBinding bloodDonorUserBinding;
    AdapterBloodUser adapterBlood;
    DatabaseReference dbref_bloodDonor;
    List<CommonModel> modelList_blood = new ArrayList<>();

    public BloodDonorUserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bloodDonorUserBinding = FragmentBloodDonorUserBinding.inflate(inflater, container, false);
        adapterBlood = new AdapterBloodUser(getContext(), modelList_blood);
        adapterBlood.notifyDataSetChanged();
        dbref_bloodDonor = FirebaseDatabase.getInstance().getReference("Admin").child("BloodDonors");
        dbref_bloodDonor.keepSynced(true);
        getDataFromFirebase();
        bloodDonorUserBinding.backBtnBloodUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        bloodDonorUserBinding.bloodUserSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<CommonModel> filterlist = new ArrayList<>();
                for (CommonModel model : modelList_blood) {
                    if (model.getSearch_data().toLowerCase().contains(newText.toLowerCase())) {
                        filterlist.add(model);
                    }
                }
                if (filterlist.isEmpty()) {
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {
                    adapterBlood.setFilterList_userblood(filterlist);
                }
                return true;
            }
        });
        bloodDonorUserBinding.swipeBloodUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    bloodDonorUserBinding.layNonetBloodUser.setVisibility(View.VISIBLE);
                } else {
                    bloodDonorUserBinding.layNonetBloodUser.setVisibility(View.GONE);
                }
                getDataFromFirebase();
                adapterBlood.notifyDataSetChanged();
                bloodDonorUserBinding.swipeBloodUser.setRefreshing(false);
            }
        });
        return bloodDonorUserBinding.getRoot();
    }

    private void getDataFromFirebase() {
        bloodDonorUserBinding.loadingProgressBloodUser.setVisibility(View.VISIBLE);
        dbref_bloodDonor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList_blood.clear();
                bloodDonorUserBinding.loadingProgressBloodUser.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {

                    CommonModel commonModel_blood = ds.getValue(CommonModel.class);
                    modelList_blood.add(commonModel_blood);

                }
                if (modelList_blood.isEmpty()) {
                    bloodDonorUserBinding.loadingProgressBloodUser.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapterBlood.notifyDataSetChanged();
                bloodDonorUserBinding.bloodUserRecyclerView.setHasFixedSize(true);
                bloodDonorUserBinding.bloodUserRecyclerView.setAdapter(adapterBlood);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}