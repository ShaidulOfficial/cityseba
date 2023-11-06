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
import com.kawsar.eseba_chandpur.databinding.FragmentPoliceEditBinding;

import java.util.HashMap;


public class PoliceEditFragment extends Fragment {

    FragmentPoliceEditBinding policeEditBinding;
    String userId = "", dataType = "";
    DatabaseReference dbReferenceEdit_police;
    String name, mobile, postType, location;

    public PoliceEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        policeEditBinding = FragmentPoliceEditBinding.inflate(inflater
                , container, false);
        Bundle bundle = this.getArguments();
        userId = bundle.getString("userId");
        dataType = bundle.getString("dataType");
        policeEditBinding.headTitleEditPolice.setText(dataType);
        dbReferenceEdit_police = FirebaseDatabase.getInstance().getReference("Admin").child(dataType)
                .child(userId);

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Updating...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);

        policeEditBinding.backBtnEditPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        policeEditBinding.cancelBtnEditPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        dbReferenceEdit_police.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    policeEditBinding.nameEdtEditPolice.setText(snapshot.child("name").getValue().toString().trim());
                    policeEditBinding.mobileEdtEditPolice.setText(snapshot.child("mobile").getValue().toString().trim());
                    policeEditBinding.locationEdtEditPolice.setText(snapshot.child("location").getValue().toString().trim());
                    policeEditBinding.postTypeEdtEditPolice.setText(snapshot.child("post_type").getValue().toString().trim());
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),
                        "failed due to" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        policeEditBinding.submitBtnEditPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                dbReferenceEdit_police.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        progressDialog.dismiss();
                        name = policeEditBinding.nameEdtEditPolice.getText().toString().trim();
                        mobile = policeEditBinding.mobileEdtEditPolice.getText().toString().trim();
                        location = policeEditBinding.locationEdtEditPolice.getText().toString().trim();
                        postType = policeEditBinding.postTypeEdtEditPolice.getText().toString().trim();
                        if (name.isEmpty()) {
                            policeEditBinding.nameEdtEditPolice.setError("name required");
                        } else if (mobile.isEmpty()) {
                            policeEditBinding.mobileEdtEditPolice.setError("mobile required");
                        } else if (mobile.length() != 11) {
                            policeEditBinding.mobileEdtEditPolice.setError("Digit must be 11!");
                        } else if (location.isEmpty()) {
                            policeEditBinding.locationEdtEditPolice.setError("Location required");
                        } else if (postType.isEmpty()) {
                            policeEditBinding.postTypeEdtEditPolice.setError("Post is required");
                        } else {
                            HashMap<String, Object> updateMAP = new HashMap<>();
                            updateMAP.put("name", name);
                            updateMAP.put("mobile", mobile);
                            updateMAP.put("location", location);
                            updateMAP.put("post_type", postType);
                            dbReferenceEdit_police.updateChildren(updateMAP).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        return policeEditBinding.getRoot();
    }
}