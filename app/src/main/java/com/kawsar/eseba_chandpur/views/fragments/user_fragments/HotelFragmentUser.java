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
import com.kawsar.eseba_chandpur.databinding.FragmentHotelUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.List;


public class HotelFragmentUser extends Fragment {

    FragmentHotelUserBinding hotelUserBinding;
    DatabaseReference databaseReference_hotelUser;
    List<CommonModel> hotelUserModelList = new ArrayList<>();
    AdapterCommonUser adapterCommon_hotelUser;

    public HotelFragmentUser() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hotelUserBinding = FragmentHotelUserBinding.inflate(inflater,
                container, false);
        databaseReference_hotelUser = FirebaseDatabase.getInstance().getReference("Admin").child("Hotels");
        databaseReference_hotelUser.keepSynced(true);
        adapterCommon_hotelUser = new AdapterCommonUser(getContext(), hotelUserModelList);
        adapterCommon_hotelUser.notifyDataSetChanged();
        getDataFromFirebase();
        hotelUserBinding.hotelUserSearchV.clearFocus();
        hotelUserBinding.hotelUserSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        hotelUserBinding.layNonetHotelUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotelUserBinding.layNonetHotelUser.setVisibility(View.GONE);
            }
        });
        hotelUserBinding.backBtnHotelUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        hotelUserBinding.swipeHotelUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    hotelUserBinding.layNonetHotelUser.setVisibility(View.VISIBLE);
                } else {
                    hotelUserBinding.layNonetHotelUser.setVisibility(View.GONE);
                }
                getDataFromFirebase();
                adapterCommon_hotelUser.notifyDataSetChanged();
                hotelUserBinding.swipeHotelUser.setRefreshing(false);
            }
        });

        return hotelUserBinding.getRoot();
    }

    private void setupOnBackPress() {
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()) {
                    Toast.makeText(requireContext(), "Go back", Toast.LENGTH_SHORT).show();
                    setEnabled(false);
                    requireActivity().onBackPressed();
                } else {

                }
            }
        });
    }

    private void filterList(String text) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : hotelUserModelList) {
            if (model.getSearch_data().toLowerCase().contains(text.toLowerCase())) {
                filterlist.add(model);
            }
        }
        if (filterlist.isEmpty()) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterCommon_hotelUser.setFilterList(filterlist);
        }
    }

    private void getDataFromFirebase() {
        hotelUserBinding.loadingProgressHotelUser.setVisibility(View.VISIBLE);
        databaseReference_hotelUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hotelUserModelList.clear();
                hotelUserBinding.loadingProgressHotelUser.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    hotelUserModelList.add(commonModel_hospital);
                }
                if (hotelUserModelList.isEmpty()) {
                    hotelUserBinding.loadingProgressHotelUser.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapterCommon_hotelUser.notifyDataSetChanged();
                hotelUserBinding.hotelUserRecyclerView.setHasFixedSize(true);
                hotelUserBinding.hotelUserRecyclerView.setAdapter(adapterCommon_hotelUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}