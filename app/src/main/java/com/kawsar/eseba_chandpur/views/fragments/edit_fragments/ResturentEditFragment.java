package com.kawsar.eseba_chandpur.views.fragments.edit_fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.databinding.FragmentResturentEditBinding;
import com.kawsar.eseba_chandpur.views.activities.AdminDashboardActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ResturentEditFragment extends Fragment {

    FragmentResturentEditBinding resturentEditBinding;
    String userId = "", dataType = "";
    DatabaseReference dbReferenceEdit_resturent;
    FirebaseStorage storage;
    StorageReference storageReferenceRestu;
    String name = "", mobile = "", description = "", location = "", websiteUrl = "";
    String picUrl;
    final int RESTURENT_PIC = 100;
    Uri imageUri;

    public ResturentEditFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        resturentEditBinding = FragmentResturentEditBinding.inflate(inflater,
                container, false);
        Bundle bundle = this.getArguments();
        userId = bundle.getString("userId");
        dataType = bundle.getString("dataType");
        resturentEditBinding.headTitleEditResturent.setText(dataType);
        dbReferenceEdit_resturent = FirebaseDatabase.getInstance().getReference("Admin").child(dataType)
                .child(userId);
        dbReferenceEdit_resturent.keepSynced(true);
        storage = FirebaseStorage.getInstance();
        storageReferenceRestu = storage.getReference("Resturent_Photo");
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Updating...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);

        resturentEditBinding.backBtnEditResturent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        resturentEditBinding.resturentPhotoImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    MyApplication.noNetdialogShow(getContext());
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, RESTURENT_PIC);
                }

            }
        });

        dbReferenceEdit_resturent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    resturentEditBinding.nameEdtResturentEdit.setText(snapshot.child("name").getValue().toString().trim());
                    resturentEditBinding.mobileEdtResturentEdit.setText(snapshot.child("mobile").getValue().toString().trim());
                    resturentEditBinding.locationEdtResturentEdit.setText(snapshot.child("location").getValue().toString().trim());
                    resturentEditBinding.descriptionEdtResturentEdit.setText(snapshot.child("message").getValue().toString().trim());
                    resturentEditBinding.webUrlEdtResturentEdit.setText(snapshot.child("webUrl").getValue().toString().trim());
                    picUrl = snapshot.child("picUrl").getValue().toString().trim();
                    Glide.with(getContext()).load(picUrl).placeholder(R.drawable.photo_camera_24)
                            .into(resturentEditBinding.resturentPhotoImv);
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),
                        "failed due to" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        resturentEditBinding.cancelBtnResturentEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdminDashboardActivity.class));
            }
        });
        resturentEditBinding.submitBtnResturentEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                dbReferenceEdit_resturent.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        progressDialog.dismiss();
                        name = resturentEditBinding.nameEdtResturentEdit.getText().toString().trim();
                        mobile = resturentEditBinding.mobileEdtResturentEdit.getText().toString().trim();
                        location = resturentEditBinding.locationEdtResturentEdit.getText().toString().trim();
                        description = resturentEditBinding.descriptionEdtResturentEdit.getText().toString().trim();
                        websiteUrl = resturentEditBinding.webUrlEdtResturentEdit.getText().toString().trim();
                        if (name.isEmpty()) {
                            resturentEditBinding.nameEdtResturentEdit.setError("name required");
                        } else if (websiteUrl.isEmpty()) {
                            resturentEditBinding.webUrlEdtResturentEdit.setError("Web link is required");
                        } else if (location.isEmpty()) {
                            resturentEditBinding.locationEdtResturentEdit.setError("Location is required");
                        } else if (description.isEmpty()) {
                            resturentEditBinding.descriptionEdtResturentEdit.setError("Description is required");
                        } else if (mobile.isEmpty()) {
                            resturentEditBinding.mobileEdtResturentEdit.setError("mobile required");
                        } else if (mobile.length() != 11) {
                            resturentEditBinding.mobileEdtResturentEdit.setError("Digit must be 11!");
                        } else if (picUrl == null) {
                            MyApplication.dialogShowPic(getContext());
                        } else {
                            HashMap<String, Object> updateMAP = new HashMap<>();
                            updateMAP.put("name", name);
                            updateMAP.put("mobile", mobile);
                            updateMAP.put("location", location);
                            updateMAP.put("webUrl", websiteUrl);
                            updateMAP.put("message", description);
                            updateMAP.put("picUrl", picUrl);
                            dbReferenceEdit_resturent.updateChildren(updateMAP).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    try {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            MyApplication.updateDialogShow(getContext());
                                        } else {
                                            progressDialog.show();
                                            Toast.makeText(getContext(), "no update", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
        return resturentEditBinding.getRoot();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESTURENT_PIC && data != null && data.getData() != null) {
            imageUri = data.getData();
            resturentEditBinding.resturentPhotoImv.setImageURI(imageUri);
            sendImagetoStorage(imageUri);
        } else {
            Toast.makeText(getContext(), "not pic get", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendImagetoStorage(Uri imageUri) {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Please wait");
        pd.setIcon(R.drawable.danger);
        pd.show();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM-dd-yyyy_hh:mm-aa", Locale.US);
        Date now = new Date();
        String fileName = formatter.format(now);
        StorageReference imagename = storageReferenceRestu.child("restu_pic" + fileName);
        imagename.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                pd.dismiss();
                Toast.makeText(getContext(), "img uploaded", Toast.LENGTH_SHORT).show();


                imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        picUrl = String.valueOf(uri);

                        Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "failed", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                int count = (int) progressPercent;
                pd.setMessage("Progress: " + (count++) + " %");
            }
        });

    }
}