package com.kawsar.eseba_chandpur.views.fragments.edit_fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.databinding.FragmentParibahanEditBinding;

import java.util.HashMap;


public class ParibahanEditFragment extends Fragment {

    FragmentParibahanEditBinding paribahanEditBinding;
    String userId = "", dataType = "";
    DatabaseReference dbReferenceEdit_paribahan;
    String name, mobile, journey, station;

    public ParibahanEditFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        paribahanEditBinding = FragmentParibahanEditBinding.inflate(inflater, container, false);
        Bundle bundle = this.getArguments();
        userId = bundle.getString("userId_Paribahan");
        dataType = bundle.getString("data_Type_paribahan");
        dbReferenceEdit_paribahan = FirebaseDatabase.getInstance().getReference("Admin").child(dataType)
                .child(userId);

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Updating...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);

        paribahanEditBinding.backBtnEditParibahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        paribahanEditBinding.cancelBtnEditParibahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        dbReferenceEdit_paribahan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    paribahanEditBinding.nameEdtEditParibahan.setText(snapshot.child("name").getValue().toString().trim());
                    paribahanEditBinding.mobileEdtEditParibahan.setText(snapshot.child("mobile").getValue().toString().trim());
                    paribahanEditBinding.journeyEdtEditParibahan.setText(snapshot.child("message").getValue().toString().trim());
                    paribahanEditBinding.stationNameEdtEditParibahan.setText(snapshot.child("location").getValue().toString().trim());
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),
                        "failed due to" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        paribahanEditBinding.submitBtnEditParibahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                dbReferenceEdit_paribahan.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        progressDialog.dismiss();
                        name = paribahanEditBinding.nameEdtEditParibahan.getText().toString().trim();
                        mobile = paribahanEditBinding.mobileEdtEditParibahan.getText().toString().trim();
                        station = paribahanEditBinding.stationNameEdtEditParibahan.getText().toString().trim();
                        journey = paribahanEditBinding.journeyEdtEditParibahan.getText().toString().trim();
                        if (name.isEmpty()) {
                            paribahanEditBinding.nameEdtEditParibahan.setError("Paribahan Name required");
                        } else if (mobile.isEmpty()) {
                            paribahanEditBinding.mobileEdtEditParibahan.setError("mobile required");
                        } else if (mobile.length() != 11) {
                            paribahanEditBinding.mobileEdtEditParibahan.setError("Digit must be 11!");
                        } else if (station.isEmpty()) {
                            paribahanEditBinding.stationNameEdtEditParibahan.setError("Location required");
                        } else if (journey.isEmpty()) {
                            paribahanEditBinding.journeyEdtEditParibahan.setError("Journey is required");
                        } else {
                            HashMap<String, Object> updateMAP = new HashMap<>();
                            updateMAP.put("name", name);
                            updateMAP.put("mobile", mobile);
                            updateMAP.put("location", station);
                            updateMAP.put("message", journey);
                            dbReferenceEdit_paribahan.updateChildren(updateMAP).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        MyApplication.updateDialogShow(getContext());

                                    } else {
                                        progressDialog.show();
                                        Toast.makeText(getContext(), "no update", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        return paribahanEditBinding.getRoot();
    }
}