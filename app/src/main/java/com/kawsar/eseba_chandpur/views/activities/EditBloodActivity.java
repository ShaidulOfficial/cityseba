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
import com.kawsar.eseba_chandpur.databinding.ActivityEditBloodBinding;

import java.util.HashMap;

public class EditBloodActivity extends AppCompatActivity {

    ActivityEditBloodBinding editBloodBinding;
    Intent intent;
    String userId = "", dataType = "";
    String name, fbUrl, webUrl;
    DatabaseReference databaseReferenceBloodEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editBloodBinding = ActivityEditBloodBinding.inflate(getLayoutInflater());
        setContentView(editBloodBinding.getRoot());

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("Updating...");
        progressDialog.setCanceledOnTouchOutside(false);
        intent = getIntent();
        userId = intent.getStringExtra("userId");
        dataType = intent.getStringExtra("dataType");
        editBloodBinding.headTitleBloodTvEdit.setText(dataType);
        databaseReferenceBloodEdit = FirebaseDatabase.getInstance().getReference("Admin").child(dataType)
                .child(userId);
        editBloodBinding.backBtnEditBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditBloodActivity.super.onBackPressed();
            }
        });
        editBloodBinding.cancelBtnBloodEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditBloodActivity.super.onBackPressed();
                Toast.makeText(EditBloodActivity.this, "no update", Toast.LENGTH_SHORT).show();
             }
        });
        editBloodBinding.submitBtnBloodEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                databaseReferenceBloodEdit.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressDialog.dismiss();
                        name = editBloodBinding.nameEdtBloodEdit.getText().toString().trim();
                        fbUrl = editBloodBinding.fbPageEdtBloodEdit.getText().toString().trim();
                        webUrl = editBloodBinding.webUrlEdtBloodEdit.getText().toString().trim();
                        if (name.isEmpty()) {
                            editBloodBinding.nameEdtBloodEdit.setError("name required");
                        } else if (fbUrl.isEmpty()) {
                            editBloodBinding.fbPageEdtBloodEdit.setError("Facebook Link required");
                        } else if (webUrl.isEmpty()) {
                            editBloodBinding.webUrlEdtBloodEdit.setError("Web Link required");
                        } else {
                            HashMap<String, Object> updateBlood = new HashMap<>();
                            updateBlood.put("name", name);
                            updateBlood.put("fbPageUrl", fbUrl);
                            updateBlood.put("webUrl", webUrl);
                            databaseReferenceBloodEdit.updateChildren(updateBlood).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        updateDialog();
                                    } else {
                                        progressDialog.show();
                                        Toast.makeText(EditBloodActivity.this, "no update", Toast.LENGTH_SHORT).show();
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

        databaseReferenceBloodEdit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    editBloodBinding.nameEdtBloodEdit.setText(snapshot.child("name").getValue().toString().trim());
                    editBloodBinding.fbPageEdtBloodEdit.setText(snapshot.child("fbPageUrl").getValue().toString().trim());
                    editBloodBinding.webUrlEdtBloodEdit.setText(snapshot.child("webUrl").getValue().toString().trim());
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditBloodActivity.this,
                        "failed due to" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDialog() {
        Dialog dialog = new Dialog(EditBloodActivity.this);
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