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
import com.kawsar.eseba_chandpur.adapters.users_adapter.AdapterNewsUser;
import com.kawsar.eseba_chandpur.adapters.users_adapter.AdapterShoppingUser;
import com.kawsar.eseba_chandpur.databinding.FragmentShoppingUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.List;

public class ShoppingUserFragment extends Fragment {

    FragmentShoppingUserBinding shoppingUserBinding;
    AdapterShoppingUser adapterShoppingUser;
    DatabaseReference dbRefShoppingUser;
    List<CommonModel> modelListShoppingUser = new ArrayList<>();
    public ShoppingUserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shoppingUserBinding = FragmentShoppingUserBinding.inflate(inflater, container, false);
        dbRefShoppingUser = FirebaseDatabase.getInstance()
                .getReference("Admin").child("Shopping");
        dbRefShoppingUser.keepSynced(true);
        adapterShoppingUser = new AdapterShoppingUser(getContext(), modelListShoppingUser);
        adapterShoppingUser.notifyDataSetChanged();
        getDataFromFirebase();

        shoppingUserBinding.backBtnShoppingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        shoppingUserBinding.shoppingUserSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    shoppingUserBinding.layNonetShoppingUser.setVisibility(View.VISIBLE);
                } else {
                    shoppingUserBinding.layNonetShoppingUser.setVisibility(View.GONE);
                }
                getDataFromFirebase();
                adapterShoppingUser.notifyDataSetChanged();
                shoppingUserBinding.shoppingUserSwip.setRefreshing(false);
            }
        });
        shoppingUserBinding.layNonetShoppingUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingUserBinding.layNonetShoppingUser.setVisibility(View.GONE);
            }
        });
        shoppingUserBinding.shoppingUserSearchV.clearFocus();
        shoppingUserBinding.shoppingUserSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        return shoppingUserBinding.getRoot();
    }
    private void filterList(String newText) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : modelListShoppingUser) {
            if (model.getName().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(model);
            }
        }
        if (filterlist.isEmpty()) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterShoppingUser.setFilterList_shoppingUser(filterlist);
        }
    }

    private void getDataFromFirebase() {
        shoppingUserBinding.loadingShoppingUser.setVisibility(View.VISIBLE);
        dbRefShoppingUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelListShoppingUser.clear();
                shoppingUserBinding.loadingShoppingUser.setVisibility(View.GONE);
                modelListShoppingUser.clear();
                shoppingUserBinding.loadingShoppingUser.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelListShoppingUser.add(commonModel_hospital);
                }
                if (modelListShoppingUser.isEmpty()) {
                    shoppingUserBinding.loadingShoppingUser.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                } else {

                }
                shoppingUserBinding.shoppingUserRcv.setHasFixedSize(true);
                shoppingUserBinding.shoppingUserRcv.setAdapter(adapterShoppingUser);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}