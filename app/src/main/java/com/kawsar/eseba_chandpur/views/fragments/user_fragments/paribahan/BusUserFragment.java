package com.kawsar.eseba_chandpur.views.fragments.user_fragments.paribahan;

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
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterParibahan;
import com.kawsar.eseba_chandpur.adapters.users_adapter.AdapterParibahanUser;
import com.kawsar.eseba_chandpur.databinding.FragmentBusUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.List;


public class BusUserFragment extends Fragment {
    FragmentBusUserBinding busUserBinding;
    AdapterParibahanUser adapterParibahanUser;
    DatabaseReference dbRef_paribahan;
    List<CommonModel> modelList_paribahan = new ArrayList<>();

    public BusUserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        busUserBinding = FragmentBusUserBinding.inflate(inflater, container, false);
        dbRef_paribahan = FirebaseDatabase.getInstance()
                .getReference("Admin").child("Buses");
        dbRef_paribahan.keepSynced(true);
        getDataFromFirebase();
        adapterParibahanUser = new AdapterParibahanUser(getContext(), modelList_paribahan);
        adapterParibahanUser.notifyDataSetChanged();
        busUserBinding.busSearchVUser.clearFocus();
        busUserBinding.busSearchVUser.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<CommonModel> filterlist = new ArrayList<>();
                for (CommonModel model : modelList_paribahan) {
                    if (model.getSearch_data().toLowerCase().contains(newText.toLowerCase())) {
                        filterlist.add(model);
                    } else {
                    }
                }
                if (filterlist.isEmpty()) {

                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {
                    adapterParibahanUser.setFilterList_userParibahan(filterlist);
                }
                return true;
            }
        });

        return busUserBinding.getRoot();
    }

    private void getDataFromFirebase() {
        dbRef_paribahan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList_paribahan.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelList_paribahan.add(commonModel_hospital);
                }
                if (modelList_paribahan.isEmpty()) {
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {
                }
                adapterParibahanUser.notifyDataSetChanged();
                busUserBinding.rcvBusUser.setHasFixedSize(true);
                busUserBinding.rcvBusUser.setAdapter(adapterParibahanUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}