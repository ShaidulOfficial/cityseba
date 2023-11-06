package com.kawsar.eseba_chandpur.views.fragments.user_fragments.newspaper_user;

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
import com.kawsar.eseba_chandpur.databinding.FragmentJobsNewsUserBinding;
import com.kawsar.eseba_chandpur.databinding.FragmentSportsNewsUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.List;


public class SportsNewsUserFragment extends Fragment {

    FragmentSportsNewsUserBinding sportsNewsUserBinding;
    AdapterNewsUser adapterNewsUser;
    DatabaseReference dbRefNewsUser;
    List<CommonModel> modelListNewsUser = new ArrayList<>();

    public SportsNewsUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sportsNewsUserBinding = FragmentSportsNewsUserBinding.inflate(inflater
                , container, false);
        dbRefNewsUser = FirebaseDatabase.getInstance()
                .getReference("NewsPapers").child("Sports");
        dbRefNewsUser.keepSynced(true);
        adapterNewsUser = new AdapterNewsUser(getContext(), modelListNewsUser);
        adapterNewsUser.notifyDataSetChanged();
        getDataFromFirebase();
        sportsNewsUserBinding.sportsUserSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    sportsNewsUserBinding.layNonetSportsNewsUser.setVisibility(View.VISIBLE);
                } else {
                    sportsNewsUserBinding.layNonetSportsNewsUser.setVisibility(View.GONE);
                }
                getDataFromFirebase();
                adapterNewsUser.notifyDataSetChanged();
                sportsNewsUserBinding.sportsUserSwip.setRefreshing(false);
            }
        });
        sportsNewsUserBinding.layNonetSportsNewsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sportsNewsUserBinding.layNonetSportsNewsUser.setVisibility(View.GONE);
            }
        });
        sportsNewsUserBinding.sportsNewsUserSearchV.clearFocus();
        sportsNewsUserBinding.sportsNewsUserSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        return sportsNewsUserBinding.getRoot();

    }

    private void filterList(String newText) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : modelListNewsUser) {
            if (model.getName().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(model);
            }
        }
        if (filterlist.isEmpty()) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterNewsUser.setFilterList_newsUser(filterlist);
        }
    }

    private void getDataFromFirebase() {
        sportsNewsUserBinding.loadingSportsNewsUser.setVisibility(View.VISIBLE);
        dbRefNewsUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelListNewsUser.clear();
                sportsNewsUserBinding.loadingSportsNewsUser.setVisibility(View.GONE);
                modelListNewsUser.clear();
                sportsNewsUserBinding.loadingSportsNewsUser.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelListNewsUser.add(commonModel_hospital);
                }
                if (modelListNewsUser.isEmpty()) {
                    sportsNewsUserBinding.loadingSportsNewsUser.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                } else {

                }
                sportsNewsUserBinding.sportsNewsUserRcv.setHasFixedSize(true);
                sportsNewsUserBinding.sportsNewsUserRcv.setAdapter(adapterNewsUser);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}