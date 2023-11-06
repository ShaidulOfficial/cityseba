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
import com.kawsar.eseba_chandpur.Constant;
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.adapters.users_adapter.AdapterCommonUser;
import com.kawsar.eseba_chandpur.databinding.FragmentAmbulanceUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.List;

public class AmbulanceUserFragment extends Fragment {
    FragmentAmbulanceUserBinding ambulanceUserBinding;
    DatabaseReference databaseReference_ambulanceUser;
    List<CommonModel> ambulanceUserModelList = new ArrayList<>();
    AdapterCommonUser adapterCommon_ambulanceUser;

    public AmbulanceUserFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ambulanceUserBinding = FragmentAmbulanceUserBinding.inflate(inflater,
                container, false);
        databaseReference_ambulanceUser = FirebaseDatabase.getInstance().getReference(Constant.ServiceBucket).child("Ambulances");
        databaseReference_ambulanceUser.keepSynced(true);
        adapterCommon_ambulanceUser = new AdapterCommonUser(getContext(), ambulanceUserModelList);
        adapterCommon_ambulanceUser.notifyDataSetChanged();
        getDataFromFirebase();
        ambulanceUserBinding.layNonetAmbulanceUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambulanceUserBinding.layNonetAmbulanceUser.setVisibility(View.GONE);
            }
        });
        ambulanceUserBinding.backBtnAmbulanceUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        ambulanceUserBinding.ambulanceUserSearchV.clearFocus();
        ambulanceUserBinding.ambulanceUserSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        ambulanceUserBinding.swipeAmbulanceUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    ambulanceUserBinding.layNonetAmbulanceUser.setVisibility(View.VISIBLE);
                } else {
                    ambulanceUserBinding.layNonetAmbulanceUser.setVisibility(View.GONE);
                }
                getDataFromFirebase();
                adapterCommon_ambulanceUser.notifyDataSetChanged();
                ambulanceUserBinding.swipeAmbulanceUser.setRefreshing(false);
            }
        });

        return ambulanceUserBinding.getRoot();

    }

    private void filterList(String text) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : ambulanceUserModelList) {
            if (model.getSearch_data().toLowerCase().contains(text.toLowerCase())) {
                filterlist.add(model);
            }
        }
        if (filterlist.isEmpty()) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterCommon_ambulanceUser.setFilterList(filterlist);
        }
    }

    private void getDataFromFirebase() {
        ambulanceUserBinding.loadingProgressAmbulanceUser.setVisibility(View.VISIBLE);
        databaseReference_ambulanceUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ambulanceUserModelList.clear();

                ambulanceUserBinding.loadingProgressAmbulanceUser.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {

                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    ambulanceUserModelList.add(commonModel_hospital);

                }
                if (ambulanceUserModelList.isEmpty()) {
                    ambulanceUserBinding.loadingProgressAmbulanceUser.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapterCommon_ambulanceUser.notifyDataSetChanged();
                ambulanceUserBinding.ambulanceUserRecyclerView.setHasFixedSize(true);
                ambulanceUserBinding.ambulanceUserRecyclerView.setAdapter(adapterCommon_ambulanceUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}