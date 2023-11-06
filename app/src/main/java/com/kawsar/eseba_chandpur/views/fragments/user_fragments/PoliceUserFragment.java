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
import com.kawsar.eseba_chandpur.adapters.users_adapter.AdapterPoliceUser;
import com.kawsar.eseba_chandpur.databinding.FragmentPoliceUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;


import java.util.ArrayList;
import java.util.List;


public class PoliceUserFragment extends Fragment {

    FragmentPoliceUserBinding policeUserBinding;
    DatabaseReference databaseReference_policeUser;
    List<CommonModel> policeUserModelList = new ArrayList<>();
    AdapterPoliceUser adapterCommon_policeUser;

    public PoliceUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        policeUserBinding = FragmentPoliceUserBinding.inflate(inflater,
                container, false);
        databaseReference_policeUser = FirebaseDatabase.getInstance().getReference("Admin").child("Polices");
        databaseReference_policeUser.keepSynced(true);
        adapterCommon_policeUser = new AdapterPoliceUser(getContext(), policeUserModelList);
        adapterCommon_policeUser.notifyDataSetChanged();
        getDataFromFirebase();
        policeUserBinding.backBtnUserPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().onBackPressed();
            }
        });
        policeUserBinding.policeUserSearchV.clearFocus();
        policeUserBinding.policeUserSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        policeUserBinding.swipePoliceUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    policeUserBinding.layNonetPoliceUser.setVisibility(View.VISIBLE);
                } else {
                    policeUserBinding.layNonetPoliceUser.setVisibility(View.GONE);
                }
                getDataFromFirebase();
                adapterCommon_policeUser.notifyDataSetChanged();
                policeUserBinding.swipePoliceUser.setRefreshing(false);
            }
        });

        return policeUserBinding.getRoot();

    }

    private void filterList(String text) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel filtermodel : policeUserModelList) {
            if (filtermodel.getSearch_data().toLowerCase().contains(text.toLowerCase())) {
                filterlist.add(filtermodel);
            }
        }
        if (filterlist.isEmpty()) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterCommon_policeUser.setFilterList_policeUser(filterlist);
        }
    }

    private void getDataFromFirebase() {
        policeUserBinding.loadingProgressPoliceUser.setVisibility(View.VISIBLE);
        databaseReference_policeUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                policeUserModelList.clear();
                policeUserBinding.loadingProgressPoliceUser.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    policeUserModelList.add(commonModel_hospital);
                }
                if (policeUserModelList.isEmpty()) {
                    policeUserBinding.loadingProgressPoliceUser.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapterCommon_policeUser.notifyDataSetChanged();
                policeUserBinding.policeUserRecyclerView.setHasFixedSize(true);
                policeUserBinding.policeUserRecyclerView.setAdapter(adapterCommon_policeUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}