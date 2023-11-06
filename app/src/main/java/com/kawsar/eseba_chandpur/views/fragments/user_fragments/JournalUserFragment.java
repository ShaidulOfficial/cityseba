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
import com.kawsar.eseba_chandpur.adapters.users_adapter.AdapterJournalUser;
import com.kawsar.eseba_chandpur.databinding.FragmentJournalUserBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;


import java.util.ArrayList;
import java.util.List;


public class JournalUserFragment extends Fragment {
    FragmentJournalUserBinding journalUserBinding;
    DatabaseReference dbRef_journal_user;
    List<CommonModel> journal_user_ModelList = new ArrayList<>();
    AdapterJournalUser adapterCommonUser_journal_user;

    public JournalUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        journalUserBinding = FragmentJournalUserBinding.inflate(inflater, container, false);
        dbRef_journal_user = FirebaseDatabase.getInstance().getReference("Admin").child("Journals");
        adapterCommonUser_journal_user = new AdapterJournalUser(getContext(), journal_user_ModelList);
        adapterCommonUser_journal_user.notifyDataSetChanged();
        getDataFromFirebase();
        journalUserBinding.backJournalUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        journalUserBinding.journalUserSearchV.clearFocus();
        journalUserBinding.journalUserSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        journalUserBinding.journalUserSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    journalUserBinding.layNonetJournalUser.setVisibility(View.VISIBLE);
                } else {
                    journalUserBinding.layNonetJournalUser.setVisibility(View.GONE);
                }
                getDataFromFirebase();
                adapterCommonUser_journal_user.notifyDataSetChanged();
                journalUserBinding.journalUserSwip.setRefreshing(false);
            }
        });
        journalUserBinding.layNonetJournalUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                journalUserBinding.layNonetJournalUser.setVisibility(View.GONE);
            }
        });

        return journalUserBinding.getRoot();
    }

    private void getDataFromFirebase() {
        journalUserBinding.loadingJournalUser.setVisibility(View.VISIBLE);
        dbRef_journal_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                journal_user_ModelList.clear();
                journalUserBinding.loadingJournalUser.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    journal_user_ModelList.add(commonModel_hospital);

                }
                if (journal_user_ModelList.isEmpty()) {
                    journalUserBinding.loadingJournalUser.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapterCommonUser_journal_user.notifyDataSetChanged();
                journalUserBinding.journalUserRcv.setHasFixedSize(true);
                journalUserBinding.journalUserRcv.setAdapter(adapterCommonUser_journal_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterList(String text) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : journal_user_ModelList) {
            if (model.getSearch_data().toLowerCase().contains(text.toLowerCase())) {
                filterlist.add(model);
            }
        }
        if (filterlist.isEmpty()) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterCommonUser_journal_user.setFilterList_journalUser(filterlist);
        }
    }


}