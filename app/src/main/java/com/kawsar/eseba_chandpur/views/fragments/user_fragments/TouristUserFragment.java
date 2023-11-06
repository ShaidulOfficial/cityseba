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
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.adapters.users_adapter.AdapterResturentUser;
import com.kawsar.eseba_chandpur.adapters.users_adapter.AdapterTouristUser;
import com.kawsar.eseba_chandpur.databinding.FragmentResturentUserBinding;
import com.kawsar.eseba_chandpur.databinding.FragmentTouristUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.List;


public class TouristUserFragment extends Fragment {

    FragmentTouristUserBinding touristUserBinding;
    AdapterTouristUser adapterTouristUser;
    DatabaseReference dbrefTouristUser;
    List<CommonModel> modelListTouristUser = new ArrayList<>();

    public TouristUserFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        touristUserBinding = FragmentTouristUserBinding.inflate(inflater,
                container, false);
        dbrefTouristUser = FirebaseDatabase.getInstance()
                .getReference("Admin").child("TouristPlace");
        dbrefTouristUser.keepSynced(true);
        adapterTouristUser = new AdapterTouristUser(getContext(), modelListTouristUser);
        adapterTouristUser.notifyDataSetChanged();
        getDataFromFirebase();
        touristUserBinding.backBtnTouristUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        touristUserBinding.touristUserSearchV.clearFocus();
        touristUserBinding.touristUserSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        touristUserBinding.touristUserSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    touristUserBinding.layNonetTouristUser.setVisibility(View.VISIBLE);
                } else {
                    touristUserBinding.layNonetTouristUser.setVisibility(View.GONE);
                }
                getDataFromFirebase();
                adapterTouristUser.notifyDataSetChanged();
                touristUserBinding.touristUserSwip.setRefreshing(false);
            }
        });
        touristUserBinding.layNonetTouristUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touristUserBinding.layNonetTouristUser.setVisibility(View.GONE);
            }
        });
        return touristUserBinding.getRoot();
    }

    private void filterList(String newText) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : modelListTouristUser) {
            if (model.getSearch_data().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(model);
            }
        }
        if (filterlist.isEmpty()) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterTouristUser.setFilterList_TouristUser(filterlist);
        }
    }

    private void getDataFromFirebase() {
        touristUserBinding.loadingTouristUser.setVisibility(View.VISIBLE);
        dbrefTouristUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelListTouristUser.clear();
                touristUserBinding.loadingTouristUser.setVisibility(View.GONE);
                modelListTouristUser.clear();
                touristUserBinding.loadingTouristUser.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelListTouristUser.add(commonModel_hospital);
                }
                if (modelListTouristUser.isEmpty()) {
                    touristUserBinding.loadingTouristUser.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                } else {

                }

                touristUserBinding.touristUserRcv.setAdapter(adapterTouristUser);
                touristUserBinding.touristUserRcv.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}