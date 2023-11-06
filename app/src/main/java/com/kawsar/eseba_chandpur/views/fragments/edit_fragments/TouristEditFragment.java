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
import com.kawsar.eseba_chandpur.databinding.FragmentTouristEditBinding;
import com.kawsar.eseba_chandpur.views.activities.AdminDashboardActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class TouristEditFragment extends Fragment {

    FragmentTouristEditBinding touristEditBinding;
    String userId = "", dataType = "";
    DatabaseReference dbReferenceEdit_tourist;
    FirebaseStorage storage;
    StorageReference storageReference_tourist;
    String name = "", description = "", location = "", websiteUrl = "";
    String picUrl;
    final int TOURIST_IMG_PIC = 100;
    Uri imageUri;

    public TouristEditFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        touristEditBinding = FragmentTouristEditBinding.inflate(inflater,
                container, false);
        Bundle bundle = this.getArguments();
        userId = bundle.getString("userId");
        dataType = bundle.getString("dataType");
        touristEditBinding.headTitleEditTourist.setText(dataType);
        dbReferenceEdit_tourist = FirebaseDatabase.getInstance().getReference("Admin").child(dataType)
                .child(userId);
        dbReferenceEdit_tourist.keepSynced(true);
        storage = FirebaseStorage.getInstance();
        storageReference_tourist = storage.getReference("Tourist_Photo");
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Updating...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);

        touristEditBinding.backBtnEditTourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        touristEditBinding.touristPhotoImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    MyApplication.noNetdialogShow(getContext());
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, TOURIST_IMG_PIC);
                }
            }
        });

        dbReferenceEdit_tourist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    touristEditBinding.nameEdtTouristEdit.setText(snapshot.child("name").getValue().toString().trim());
                    touristEditBinding.locationEdtTouristEdit.setText(snapshot.child("location").getValue().toString().trim());
                    touristEditBinding.descriptionEdtTouristEdit.setText(snapshot.child("message").getValue().toString().trim());
                    touristEditBinding.webUrlEdtTouristEdit.setText(snapshot.child("webUrl").getValue().toString().trim());
                    picUrl = snapshot.child("picUrl").getValue().toString().trim();
                    Glide.with(getContext()).load(picUrl).placeholder(R.drawable.photo_camera_24)
                            .into(touristEditBinding.touristPhotoImv);
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),
                        "failed due to" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        touristEditBinding.cancelBtntouristEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AdminDashboardActivity.class));
            }
        });
        touristEditBinding.submitBtntouristEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                dbReferenceEdit_tourist.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        progressDialog.dismiss();
                        name = touristEditBinding.nameEdtTouristEdit.getText().toString().trim();
                        location = touristEditBinding.locationEdtTouristEdit.getText().toString().trim();
                        description = touristEditBinding.descriptionEdtTouristEdit.getText().toString().trim();
                        websiteUrl = touristEditBinding.webUrlEdtTouristEdit.getText().toString().trim();
                        if (name.isEmpty()) {
                            touristEditBinding.nameEdtTouristEdit.setError("name required");
                        } else if (websiteUrl.isEmpty()) {
                            touristEditBinding.webUrlEdtTouristEdit.setError("Web link is required");
                        } else if (location.isEmpty()) {
                            touristEditBinding.locationEdtTouristEdit.setError("Location is required");
                        } else if (description.isEmpty()) {
                            touristEditBinding.descriptionEdtTouristEdit.setError("Description is required");
                        } else if (picUrl == null) {
                            MyApplication.dialogShowPic(getContext());
                        } else {
                            HashMap<String, Object> updateMAP = new HashMap<>();
                            updateMAP.put("name", name);
                            updateMAP.put("location", location);
                            updateMAP.put("webUrl", websiteUrl);
                            updateMAP.put("message", description);
                            updateMAP.put("picUrl", picUrl);
                            dbReferenceEdit_tourist.updateChildren(updateMAP).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        return touristEditBinding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOURIST_IMG_PIC && data != null && data.getData() != null) {
            imageUri = data.getData();
            touristEditBinding.touristPhotoImv.setImageURI(imageUri);
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss", Locale.US);
        Date now = new Date();
        String fileName = formatter.format(now);
        StorageReference imagename = storageReference_tourist.child("tourist_pic" + fileName);
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