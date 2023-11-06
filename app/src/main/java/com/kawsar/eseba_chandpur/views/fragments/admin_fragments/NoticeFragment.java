package com.kawsar.eseba_chandpur.views.fragments.admin_fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.kawsar.eseba_chandpur.databinding.FragmentNoticeBinding;
import com.kawsar.eseba_chandpur.views.activities.AdminDashboardActivity;

import java.util.HashMap;

public class NoticeFragment extends Fragment {

    FragmentNoticeBinding noticeBinding;
    DatabaseReference dbRefNotice;

    public NoticeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        noticeBinding = FragmentNoticeBinding.inflate(inflater, container, false);

        dbRefNotice = FirebaseDatabase.getInstance().getReference("Admin").child("Notice");
        dbRefNotice.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    noticeBinding.messageEdtNotice.setText(snapshot.child("message").getValue().toString().trim());
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        noticeBinding.sendBtnNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    MyApplication.noNetdialogShow(getContext());
                } else {
                    String noticeMsg = noticeBinding.messageEdtNotice.getText().toString().trim();
                    HashMap<String, Object> noticeMap = new HashMap<>();
                    noticeMap.put("message", noticeMsg);
                    dbRefNotice.setValue(noticeMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    AlertDialog.Builder noticeUpdateDialog = new AlertDialog.Builder(getContext());
                                    noticeUpdateDialog.setIcon(R.drawable.check_circle_24)
                                            .setTitle("Congratulations").setMessage("Your Notice successfully Sent!");
                                    noticeUpdateDialog.setCancelable(false);
                                    noticeUpdateDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            startActivity(new Intent(getContext(), AdminDashboardActivity.class));
                                            getActivity().finish();
                                        }
                                    });
                                    AlertDialog noticeDialog = noticeUpdateDialog.create();
                                    noticeDialog.show();
                                    Toast.makeText(getContext(), "Notice Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });
        return noticeBinding.getRoot();
    }
}