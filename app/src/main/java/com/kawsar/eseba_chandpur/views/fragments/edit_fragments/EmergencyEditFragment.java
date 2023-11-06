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
import com.kawsar.eseba_chandpur.databinding.FragmentEmergencyEditBinding;

import java.util.HashMap;


public class EmergencyEditFragment extends Fragment {

    FragmentEmergencyEditBinding emergencyEditBinding;
    String userId = "", dataType = "";
    DatabaseReference dbReferenceEdit_emergency;
    String name, mobile;

    public EmergencyEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        emergencyEditBinding = FragmentEmergencyEditBinding.inflate(inflater, container
                , false);
        Bundle bundle = this.getArguments();
        userId = bundle.getString("userId");
        dataType = bundle.getString("dataType");
        emergencyEditBinding.headTitleEditEmergency.setText(dataType);
        dbReferenceEdit_emergency = FirebaseDatabase.getInstance().getReference("Admin").child(dataType)
                .child(userId);
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Updating...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        emergencyEditBinding.backBtnEditEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        emergencyEditBinding.cancelBtnEditEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        dbReferenceEdit_emergency.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    emergencyEditBinding.nameEdtEditEmergency.setText(snapshot.child("name").getValue().toString().trim());
                    emergencyEditBinding.mobileEdtEditEmergency.setText(snapshot.child("mobile").getValue().toString().trim());
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),
                        "failed due to" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        emergencyEditBinding.submitBtnEditEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                dbReferenceEdit_emergency.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressDialog.dismiss();
                        name = emergencyEditBinding.nameEdtEditEmergency.getText().toString().trim();
                        mobile = emergencyEditBinding.mobileEdtEditEmergency.getText().toString().trim();
                        if (name.isEmpty()) {
                            emergencyEditBinding.nameEdtEditEmergency.setError("name required");
                        } else if (mobile.isEmpty()) {
                            emergencyEditBinding.mobileEdtEditEmergency.setError("mobile required");
                        } else {
                            HashMap<String, Object> updateMAP = new HashMap<>();
                            updateMAP.put("name", name);
                            updateMAP.put("mobile", mobile);
                            dbReferenceEdit_emergency.updateChildren(updateMAP).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        return emergencyEditBinding.getRoot();
    }
}