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
import com.kawsar.eseba_chandpur.databinding.FragmentBanglaNewsUserBinding;
import com.kawsar.eseba_chandpur.databinding.FragmentChandpurNewsUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.List;

public class ChandpurNewsUserFragment extends Fragment {

    FragmentChandpurNewsUserBinding chandpurNewsUserBinding;
    AdapterNewsUser adapterNewsUser;
    DatabaseReference dbRefNewsUser;
    List<CommonModel> modelListNewsUser = new ArrayList<>();
    public ChandpurNewsUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        chandpurNewsUserBinding = FragmentChandpurNewsUserBinding.inflate(inflater
                , container, false);
        dbRefNewsUser = FirebaseDatabase.getInstance()
                .getReference("NewsPapers").child("Chandpur_News");
        dbRefNewsUser.keepSynced(true);
        adapterNewsUser = new AdapterNewsUser(getContext(), modelListNewsUser);
        adapterNewsUser.notifyDataSetChanged();
        getDataFromFirebase();
        chandpurNewsUserBinding.chandpurNewsUserSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    chandpurNewsUserBinding.layNonetChandpurNewsUser.setVisibility(View.VISIBLE);
                } else {
                    chandpurNewsUserBinding.layNonetChandpurNewsUser.setVisibility(View.GONE);
                }
                getDataFromFirebase();
                adapterNewsUser.notifyDataSetChanged();
                chandpurNewsUserBinding.chandpurNewsUserSwip.setRefreshing(false);
            }
        });
        chandpurNewsUserBinding.layNonetChandpurNewsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chandpurNewsUserBinding.layNonetChandpurNewsUser.setVisibility(View.GONE);
            }
        });
        chandpurNewsUserBinding.chandpurNewsUserSearchV.clearFocus();
        chandpurNewsUserBinding.chandpurNewsUserSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        return chandpurNewsUserBinding.getRoot();

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
        chandpurNewsUserBinding.loadingChandpurNewsUser.setVisibility(View.VISIBLE);
        dbRefNewsUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelListNewsUser.clear();
                chandpurNewsUserBinding.loadingChandpurNewsUser.setVisibility(View.GONE);
                modelListNewsUser.clear();
                chandpurNewsUserBinding.loadingChandpurNewsUser.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelListNewsUser.add(commonModel_hospital);
                }
                if (modelListNewsUser.isEmpty()) {
                    chandpurNewsUserBinding.loadingChandpurNewsUser.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                } else {

                }
                chandpurNewsUserBinding.chandpurNewsUserRcv.setHasFixedSize(true);
                chandpurNewsUserBinding.chandpurNewsUserRcv.setAdapter(adapterNewsUser);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}