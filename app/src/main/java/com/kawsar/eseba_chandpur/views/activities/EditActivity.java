package com.kawsar.eseba_chandpur.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.databinding.ActivityEditBinding;

import java.util.HashMap;

public class EditActivity extends AppCompatActivity {

    ActivityEditBinding editBinding;
    Intent intent;
    String userId = "", dataType = "";
    String name, location, mobile;
    DatabaseReference databaseReferenceEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editBinding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(editBinding.getRoot());

        intent = getIntent();
        userId = intent.getStringExtra("userId");
        dataType = intent.getStringExtra("dataType");
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("Updating...");
        progressDialog.setCanceledOnTouchOutside(true);

        databaseReferenceEdit = FirebaseDatabase.getInstance().getReference("Admin").child(dataType)
                .child(userId);
        editBinding.headTitleEdit.setText(dataType);
        editBinding.backBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditActivity.super.onBackPressed();
            }
        });
        editBinding.cancelBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditActivity.super.onBackPressed();
                Toast.makeText(EditActivity.this, "no update", Toast.LENGTH_SHORT).show();
            }
        });
        editBinding.submitBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                databaseReferenceEdit.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressDialog.dismiss();
                        name = editBinding.nameEdtEdit.getText().toString().trim();
                        location = editBinding.locationEdtEdit.getText().toString().trim();
                        mobile = editBinding.mobileEdtEdit.getText().toString().trim();
                        if (name.isEmpty()) {
                            editBinding.nameEdtEdit.setError("name required");
                        } else if (location.isEmpty()) {
                            editBinding.locationEdtEdit.setError("location required");
                        } else if (mobile.isEmpty()) {
                            editBinding.mobileEdtEdit.setError("mobile required");
                        } else if (mobile.length() != 11) {
                            editBinding.mobileEdtEdit.setError("Digit must be 11!");
                        } else {
                            HashMap<String, Object> updateEdit = new HashMap<>();
                            updateEdit.put("name", name);
                            updateEdit.put("location", location);
                            updateEdit.put("mobile", mobile);
                            databaseReferenceEdit.updateChildren(updateEdit).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        updateDialog();

                                    } else {
                                        progressDialog.show();
                                        Toast.makeText(EditActivity.this, "no update", Toast.LENGTH_SHORT).show();
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

        databaseReferenceEdit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    editBinding.nameEdtEdit.setText(snapshot.child("name").getValue().toString().trim());
                    editBinding.locationEdtEdit.setText(snapshot.child("location").getValue().toString().trim());
                    editBinding.mobileEdtEdit.setText(snapshot.child("mobile").getValue().toString().trim());
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditActivity.this,
                        "failed due to" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDialog() {
        Dialog dialog = new Dialog(EditActivity.this);
        dialog.setContentView(R.layout.custom_dialog_update);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        TextView okBtnUpdate = dialog.findViewById(R.id.okBtnUpdate);
        okBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog.create();
        dialog.show();
    }
}