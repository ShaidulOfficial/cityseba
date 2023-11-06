package com.kawsar.eseba_chandpur.views.fragments.user_fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kawsar.eseba_chandpur.Constant;
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.databinding.FragmentFeedBackBinding;
import com.kawsar.eseba_chandpur.views.activities.MainActivity;

import java.util.HashMap;


public class FeedBackFragment extends Fragment {
    FragmentFeedBackBinding feedBackBinding;
    DatabaseReference dbRef_feedBack;
    long timeStamp;

    public FeedBackFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        feedBackBinding = FragmentFeedBackBinding.inflate(inflater, container, false);
        feedBackBinding.backFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        dbRef_feedBack = FirebaseDatabase.getInstance().getReference(Constant.UserBucket).child("Feedback");
        feedBackBinding.sendBtnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    MyApplication.noNetdialogShow(getContext());
                } else {
                    feedBackSend();
                }

            }
        });
        return feedBackBinding.getRoot();

    }

    private void feedBackSend() {
        timeStamp = System.currentTimeMillis();
        String userKey = dbRef_feedBack.push().getKey();
        String name = feedBackBinding.nameEdtFeedback.getText().toString().trim();
        String email = feedBackBinding.emailEdtFeedback.getText().toString().trim();
        String mobile = feedBackBinding.mobileEdtFeedback.getText().toString().trim();
        String message = feedBackBinding.messageEdtFeedback.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            feedBackBinding.nameEdtFeedback.setError("Name is required");
        } else if (TextUtils.isEmpty(email)) {
            feedBackBinding.emailEdtFeedback.setError("Email required");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            feedBackBinding.emailEdtFeedback.setError("Email invalid, pattern not match");
        } else if (TextUtils.isEmpty(mobile)) {
            feedBackBinding.mobileEdtFeedback.setError("Mobile required");
        } else if (mobile.length() != 11) {
            feedBackBinding.mobileEdtFeedback.setError("11 Digit must be required");
        } else if (TextUtils.isEmpty(message)) {
            feedBackBinding.messageEdtFeedback.setError("Message required");
        } else {
            // ***===============================( ||||||||||||||||||||||| )============================***
// ********************************** Data Insert here........ **********************************
            HashMap<String, Object> dataSetMap = new HashMap<>();
            dataSetMap.put("name", name);
            dataSetMap.put("mobile", mobile);
            dataSetMap.put("email", email);
            dataSetMap.put("message", message);
            dataSetMap.put("userId", userKey);
            dataSetMap.put("timestamp", timeStamp);
            dbRef_feedBack.child(userKey).setValue(dataSetMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    AlertDialog.Builder feedBackUpdateDialog = new AlertDialog.Builder(getContext());
                    feedBackUpdateDialog.setIcon(R.drawable.check_circle_24)
                            .setTitle("Congratulations").setMessage("Your Feedback successfully Sent!");
                    feedBackUpdateDialog.setCancelable(false);
                    feedBackUpdateDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        }
                    });
                    AlertDialog fbackDialog = feedBackUpdateDialog.create();
                    fbackDialog.show();
                    Toast.makeText(getContext(), "Your Feedback sending done!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


}