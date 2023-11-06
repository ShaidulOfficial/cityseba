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
import com.kawsar.eseba_chandpur.databinding.FragmentDoctorEditBinding;

import java.util.HashMap;


public class DoctorEditFragment extends Fragment {
    FragmentDoctorEditBinding doctorEditBinding;
    String userId = "", dataType = "";
    DatabaseReference dbReferenceEdit_doctor;
    String name, mobile, hospitalName, qualification, chamberTime;

    public DoctorEditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        doctorEditBinding = FragmentDoctorEditBinding.inflate(inflater
                , container, false);
        Bundle bundle = this.getArguments();
        userId = bundle.getString("userId");
        dataType = bundle.getString("dataType");
        doctorEditBinding.headTitleDoctorEdit.setText(dataType);
        dbReferenceEdit_doctor = FirebaseDatabase.getInstance().getReference("Admin").child(dataType)
                .child(userId);
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Updating...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        doctorEditBinding.backBtnEditDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        dbReferenceEdit_doctor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    doctorEditBinding.nameEdtDoctorEdit.setText(snapshot.child("name").getValue().toString().trim());
                    doctorEditBinding.qualificationEdtDoctorEdit.setText(snapshot.child("qualification").getValue().toString().trim());
                    doctorEditBinding.hospitalnameEdtDoctorEdit.setText(snapshot.child("hospit_name").getValue().toString().trim());
                    doctorEditBinding.chamberTimeEdtDoctorEdit.setText(snapshot.child("chemberTime").getValue().toString().trim());
                    doctorEditBinding.mobileEditDoctor.setText(snapshot.child("mobile").getValue().toString().trim());
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),
                        "failed due to" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        doctorEditBinding.submitBtnEditDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                dbReferenceEdit_doctor.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressDialog.dismiss();
                        name = doctorEditBinding.nameEdtDoctorEdit.getText().toString().trim();
                        mobile = doctorEditBinding.mobileEditDoctor.getText().toString().trim();
                        qualification = doctorEditBinding.qualificationEdtDoctorEdit.getText().toString().trim();
                        hospitalName = doctorEditBinding.hospitalnameEdtDoctorEdit.getText().toString().trim();
                        chamberTime = doctorEditBinding.chamberTimeEdtDoctorEdit.getText().toString().trim();
                        if (name.isEmpty()) {
                            doctorEditBinding.nameEdtDoctorEdit.setError("name required");
                        } else if (qualification.isEmpty()) {
                            doctorEditBinding.qualificationEdtDoctorEdit.setError("Channel Name required");
                        } else if (hospitalName.isEmpty()) {
                            doctorEditBinding.hospitalnameEdtDoctorEdit.setError("mobile required");
                        } else if (chamberTime.isEmpty()) {
                            doctorEditBinding.chamberTimeEdtDoctorEdit.setError("mobile required");
                        } else if (mobile.isEmpty()) {
                            doctorEditBinding.mobileEditDoctor.setError("mobile required");
                        } else if (mobile.length() != 11) {
                            doctorEditBinding.mobileEditDoctor.setError("Digit must be 11!");
                        } else {
                            HashMap<String, Object> updateMAP = new HashMap<>();
                            updateMAP.put("name", name);
                            updateMAP.put("qualification", qualification);
                            updateMAP.put("hospit_name", hospitalName);
                            updateMAP.put("chemberTime", chamberTime);
                            updateMAP.put("mobile", mobile);
                            dbReferenceEdit_doctor.updateChildren(updateMAP).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        MyApplication.updateDialogShow(getContext());
                                         Toast.makeText(getContext(), "Notice Uploaded", Toast.LENGTH_SHORT).show();

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
        doctorEditBinding.cancelBtnEditDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return doctorEditBinding.getRoot();
    }

}