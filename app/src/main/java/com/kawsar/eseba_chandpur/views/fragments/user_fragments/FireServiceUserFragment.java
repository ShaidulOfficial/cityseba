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
import com.kawsar.eseba_chandpur.adapters.users_adapter.AdapterCommonUser;
import com.kawsar.eseba_chandpur.databinding.FragmentFireServiceUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.List;

public class FireServiceUserFragment extends Fragment {

    FragmentFireServiceUserBinding fireServiceUserBinding;

    DatabaseReference databaseReference_fireServiceUser;
    List<CommonModel> fireServiceUserModelList = new ArrayList<>();
    AdapterCommonUser adapterCommon_fireServiceUser;

    public FireServiceUserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fireServiceUserBinding = FragmentFireServiceUserBinding.inflate(inflater,
                container, false);
        databaseReference_fireServiceUser = FirebaseDatabase.getInstance().getReference("Admin").child("FireServices");
        databaseReference_fireServiceUser.keepSynced(true);
        adapterCommon_fireServiceUser = new AdapterCommonUser(getContext(), fireServiceUserModelList);
        adapterCommon_fireServiceUser.notifyDataSetChanged();
        getDataFromFirebase();
        fireServiceUserBinding.backBtnUserFs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        fireServiceUserBinding.fireServiceUserSearchV.clearFocus();
        fireServiceUserBinding.fireServiceUserSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        fireServiceUserBinding.swipeFireServiceUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    fireServiceUserBinding.layNonetFireServiceUser.setVisibility(View.VISIBLE);
                } else {
                    fireServiceUserBinding.layNonetFireServiceUser.setVisibility(View.GONE);
                }
                getDataFromFirebase();
                adapterCommon_fireServiceUser.notifyDataSetChanged();
                fireServiceUserBinding.swipeFireServiceUser.setRefreshing(false);
            }
        });

        return fireServiceUserBinding.getRoot();

    }

    private void filterList(String text) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : fireServiceUserModelList) {
            if (model.getSearch_data().toLowerCase().contains(text.toLowerCase())) {
                filterlist.add(model);
            }
        }
        if (filterlist.isEmpty()) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterCommon_fireServiceUser.setFilterList(filterlist);
        }
    }


    private void getDataFromFirebase() {
        fireServiceUserBinding.loadingProgressFireServiceUser.setVisibility(View.VISIBLE);
        databaseReference_fireServiceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fireServiceUserModelList.clear();

                fireServiceUserBinding.loadingProgressFireServiceUser.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {

                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    fireServiceUserModelList.add(commonModel_hospital);

                }
                if (fireServiceUserModelList.isEmpty()) {
                    fireServiceUserBinding.loadingProgressFireServiceUser.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapterCommon_fireServiceUser.notifyDataSetChanged();
                fireServiceUserBinding.fireServiceUserRecyclerView.setHasFixedSize(true);
                fireServiceUserBinding.fireServiceUserRecyclerView.setAdapter(adapterCommon_fireServiceUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}