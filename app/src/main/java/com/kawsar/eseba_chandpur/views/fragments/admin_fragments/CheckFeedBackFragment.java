package com.kawsar.eseba_chandpur.views.fragments.admin_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.kawsar.eseba_chandpur.Constant;
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterFeedBack;
import com.kawsar.eseba_chandpur.adapters.users_adapter.AdapterResturentUser;
import com.kawsar.eseba_chandpur.databinding.FragmentCheckFeedBackBinding;
import com.kawsar.eseba_chandpur.databinding.FragmentFeedBackBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.List;


public class CheckFeedBackFragment extends Fragment {

    FragmentCheckFeedBackBinding checkFeedBackBinding;
    DatabaseReference dbRefCheckFeedback;
    AdapterFeedBack adapterFeedback;
    List<CommonModel> modelListFeedback = new ArrayList<>();

    public CheckFeedBackFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        checkFeedBackBinding = FragmentCheckFeedBackBinding.inflate(inflater, container, false);

        dbRefCheckFeedback = FirebaseDatabase.getInstance().getReference(Constant.UserBucket).child("Feedback");
        dbRefCheckFeedback.keepSynced(true);
        adapterFeedback = new AdapterFeedBack(getContext(), modelListFeedback);
        adapterFeedback.notifyDataSetChanged();
        checkFeedBackBinding.backFeedbackCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        getDataFromFirebase();
        return checkFeedBackBinding.getRoot();
    }

    private void getDataFromFirebase() {
        checkFeedBackBinding.loadingFeedbackCheck.setVisibility(View.VISIBLE);
        dbRefCheckFeedback.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelListFeedback.clear();
                checkFeedBackBinding.loadingFeedbackCheck.setVisibility(View.GONE);
                modelListFeedback.clear();
                checkFeedBackBinding.loadingFeedbackCheck.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelListFeedback.add(commonModel_hospital);
                }
                if (modelListFeedback.isEmpty()) {
                    checkFeedBackBinding.loadingFeedbackCheck.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                } else {

                }

                checkFeedBackBinding.feedbackAdminRcv.setAdapter(adapterFeedback);
                checkFeedBackBinding.feedbackAdminRcv.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}