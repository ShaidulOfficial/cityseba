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
import com.kawsar.eseba_chandpur.databinding.FragmentPolliUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.List;


public class PolliUserFragment extends Fragment {

    FragmentPolliUserBinding polliUserBinding;
    DatabaseReference databaseReference_polliUser;
    List<CommonModel> polliUserModelList = new ArrayList<>();
    AdapterCommonUser adapterCommon_polliUser;

    public PolliUserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        polliUserBinding = FragmentPolliUserBinding.inflate(inflater,
                container, false);
        databaseReference_polliUser = FirebaseDatabase.getInstance().getReference("Admin").child("PolliBiddut");
        databaseReference_polliUser.keepSynced(true);
        adapterCommon_polliUser = new AdapterCommonUser(getContext(), polliUserModelList);
        adapterCommon_polliUser.notifyDataSetChanged();
         getDataFromFirebase();
         polliUserBinding.backBtnPolliUser.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 getActivity().onBackPressed();
             }
         });
        polliUserBinding.polliUserSearchV.clearFocus();
        polliUserBinding.polliUserSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        polliUserBinding.swipePolliUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    polliUserBinding.layNonetPolliUser.setVisibility(View.VISIBLE);
                } else {
                    polliUserBinding.layNonetPolliUser.setVisibility(View.GONE);
                }
                 getDataFromFirebase();
                adapterCommon_polliUser.notifyDataSetChanged();
                polliUserBinding.swipePolliUser.setRefreshing(false);
            }
        });

        return polliUserBinding.getRoot();

    }

    private void getDataFromFirebase() {
        polliUserBinding.loadingProgressPolliUser.setVisibility(View.VISIBLE);
        databaseReference_polliUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                polliUserModelList.clear();
                              polliUserBinding.loadingProgressPolliUser.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {

                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    polliUserModelList.add(commonModel_hospital);

                }
                if (polliUserModelList.isEmpty()) {
                    polliUserBinding.loadingProgressPolliUser.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapterCommon_polliUser.notifyDataSetChanged();
                polliUserBinding.polliUserRecyclerView.setHasFixedSize(true);
                polliUserBinding.polliUserRecyclerView.setAdapter(adapterCommon_polliUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterList(String text) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : polliUserModelList) {
            if (model.getSearch_data().toLowerCase().contains(text.toLowerCase())) {
                filterlist.add(model);
            }
        }
        if (filterlist.isEmpty()) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterCommon_polliUser.setFilterList(filterlist);
        }
    }


}