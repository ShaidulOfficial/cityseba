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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterResturent;
import com.kawsar.eseba_chandpur.adapters.users_adapter.AdapterResturentUser;
import com.kawsar.eseba_chandpur.databinding.FragmentResturentUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.List;


public class ResturentUserFragment extends Fragment {
    FragmentResturentUserBinding resturentUserBinding;
    AdapterResturentUser adapterResturentUser;
    DatabaseReference databaseReference_resturentUser;
    List<CommonModel> modelList_resturentUser = new ArrayList<>();

    public ResturentUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        resturentUserBinding = FragmentResturentUserBinding.inflate(inflater,
                container, false);
        databaseReference_resturentUser = FirebaseDatabase.getInstance()
                .getReference("Admin").child("Resturents");
        databaseReference_resturentUser.keepSynced(true);
        adapterResturentUser = new AdapterResturentUser(getContext(), modelList_resturentUser);
        adapterResturentUser.notifyDataSetChanged();
        getDataFromFirebase();
        resturentUserBinding.backBtnResturentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        resturentUserBinding.resturentUserAdminSearchV.clearFocus();
        resturentUserBinding.resturentUserAdminSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        return resturentUserBinding.getRoot();

    }
    private void filterList(String newText) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : modelList_resturentUser) {
            if (model.getSearch_data().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(model);
            }
        }
        if (filterlist.isEmpty()) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterResturentUser.setFilterList_ResturentUser(filterlist);
        }
    }

    private void getDataFromFirebase() {
        resturentUserBinding.loadingResturentUser.setVisibility(View.VISIBLE);
        databaseReference_resturentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList_resturentUser.clear();
                resturentUserBinding.loadingResturentUser.setVisibility(View.GONE);
                modelList_resturentUser.clear();
                resturentUserBinding.loadingResturentUser.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelList_resturentUser.add(commonModel_hospital);
                }
                if (modelList_resturentUser.isEmpty()) {
                    resturentUserBinding.loadingResturentUser.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                } else {

                }

                resturentUserBinding.resturentUserAdminRcv.setAdapter(adapterResturentUser);
                resturentUserBinding.resturentUserAdminRcv.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}