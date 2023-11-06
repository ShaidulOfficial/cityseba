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
import com.kawsar.eseba_chandpur.adapters.users_adapter.AdapterCommonUser;
import com.kawsar.eseba_chandpur.databinding.FragmentLawerUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.List;


public class LawerUserFragment extends Fragment {

    FragmentLawerUserBinding lawerUserBinding;
    DatabaseReference databaseReference_lawyerUser,dbRef_lawer;
    List<CommonModel> lawyerUserModelList = new ArrayList<>();
    AdapterCommonUser adapterCommon_lawyerUser;

    public LawerUserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lawerUserBinding = FragmentLawerUserBinding.inflate(inflater,
                container, false);

        databaseReference_lawyerUser = FirebaseDatabase.getInstance().getReference("Admin").child("Lawyers");
        databaseReference_lawyerUser.keepSynced(true);
        adapterCommon_lawyerUser = new AdapterCommonUser(getContext(), lawyerUserModelList);
        adapterCommon_lawyerUser.notifyDataSetChanged();
        dbRef_lawer = FirebaseDatabase.getInstance().getReference("Admin").child("LawyerWebLink");
        dbRef_lawer.keepSynced(true);
        dbRef_lawer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    lawerUserBinding.webLinkLawyerUser.setText(snapshot.child("webUrl")
                            .getValue().toString().trim());
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        getDataFromFirebase();
        lawerUserBinding.backBtnUserLawyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        lawerUserBinding.webLinkLawyerUser.setSelected(true);
        lawerUserBinding.lawyerUserSearchV.clearFocus();
        lawerUserBinding.lawyerUserSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        lawerUserBinding.swipeLawyerUser.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    lawerUserBinding.layNonetLawyerUser.setVisibility(View.VISIBLE);
                } else {
                    lawerUserBinding.layNonetLawyerUser.setVisibility(View.GONE);
                }
                getDataFromFirebase();
                adapterCommon_lawyerUser.notifyDataSetChanged();
                lawerUserBinding.swipeLawyerUser.setRefreshing(false);
            }
        });

        return lawerUserBinding.getRoot();

    }

    private void filterList(String text) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel modelfilter : lawyerUserModelList) {
            if (modelfilter.getSearch_data().toLowerCase().contains(text.toLowerCase())) {
                filterlist.add(modelfilter);
            }
        }
        if (filterlist.isEmpty()) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterCommon_lawyerUser.setFilterList(filterlist);
        }
    }

    private void getDataFromFirebase() {
        lawerUserBinding.loadingProgressLawyerUser.setVisibility(View.VISIBLE);
        databaseReference_lawyerUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lawyerUserModelList.clear();
                             lawerUserBinding.loadingProgressLawyerUser.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {

                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    lawyerUserModelList.add(commonModel_hospital);

                }
                if (lawyerUserModelList.isEmpty()) {
                    lawerUserBinding.loadingProgressLawyerUser.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapterCommon_lawyerUser.notifyDataSetChanged();
                lawerUserBinding.lawyerUserRecyclerView.setHasFixedSize(true);
                lawerUserBinding.lawyerUserRecyclerView.setAdapter(adapterCommon_lawyerUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}