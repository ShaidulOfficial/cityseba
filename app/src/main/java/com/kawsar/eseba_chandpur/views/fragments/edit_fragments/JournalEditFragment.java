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
import com.kawsar.eseba_chandpur.databinding.FragmentJournalEditBinding;

import java.util.HashMap;

public class JournalEditFragment extends Fragment {

    FragmentJournalEditBinding journalEditBinding;
    String userId = "", dataType = "";
    DatabaseReference dbReferenceEdit_journal;
    String name, mobile, channelName, channelLink;

    public JournalEditFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        journalEditBinding = FragmentJournalEditBinding.inflate(inflater
                , container, false);
        Bundle bundle = this.getArguments();
        userId = bundle.getString("userId");
        dataType = bundle.getString("dataType");
        journalEditBinding.headTitleEditJournal.setText(dataType);
        dbReferenceEdit_journal = FirebaseDatabase.getInstance().getReference("Admin").child(dataType)
                .child(userId);

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Updating...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);

        journalEditBinding.backBtnEditJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        journalEditBinding.cancelBtnEditJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        dbReferenceEdit_journal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    journalEditBinding.nameEdtEditJournal.setText(snapshot.child("name").getValue().toString().trim());
                    journalEditBinding.mobileEdtEditJournal.setText(snapshot.child("mobile").getValue().toString().trim());
                    journalEditBinding.channelNameEdtEditJournal.setText(snapshot.child("channelName").getValue().toString().trim());
                    journalEditBinding.channelLinkEdtEditJournal.setText(snapshot.child("webUrl").getValue().toString().trim());
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),
                        "failed due to" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        journalEditBinding.submitBtnEditJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                dbReferenceEdit_journal.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressDialog.dismiss();
                        name = journalEditBinding.nameEdtEditJournal.getText().toString().trim();
                        mobile = journalEditBinding.mobileEdtEditJournal.getText().toString().trim();
                        channelName = journalEditBinding.channelNameEdtEditJournal.getText().toString().trim();
                        channelLink = journalEditBinding.channelLinkEdtEditJournal.getText().toString().trim();
                        if (name.isEmpty()) {
                            journalEditBinding.nameEdtEditJournal.setError("name required");
                        } else if (channelName.isEmpty()) {
                            journalEditBinding.channelNameEdtEditJournal.setError("Channel Name required");
                        } else if (mobile.isEmpty()) {
                            journalEditBinding.mobileEdtEditJournal.setError("mobile required");
                        } else if (mobile.length() != 11) {
                            journalEditBinding.mobileEdtEditJournal.setError("Digit must be 11!");
                        } else if (channelLink.isEmpty()) {
                            journalEditBinding.channelLinkEdtEditJournal.setError("Channel link required");
                        } else {
                            HashMap<String, Object> updateMAP = new HashMap<>();
                            updateMAP.put("name", name);
                            updateMAP.put("mobile", mobile);
                            updateMAP.put("channelName", channelName);
                            updateMAP.put("webUrl", channelLink);
                            dbReferenceEdit_journal.updateChildren(updateMAP).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        return journalEditBinding.getRoot();
    }
}