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
import com.kawsar.eseba_chandpur.databinding.FragmentNewsEditBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class NewsEditFragment extends Fragment {
    FragmentNewsEditBinding newsEditBinding;
    String userId = "", dataType = "";
    DatabaseReference dbReferenceEdit_newsEdit;
    String name, webLink;
    final int NEWS_PIC = 101;
    String news_pic_Url;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReferenceNews;

    public NewsEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        newsEditBinding = FragmentNewsEditBinding.inflate(inflater, container, false);
        Bundle bundle = this.getArguments();
        userId = bundle.getString("userId");
        dataType = bundle.getString("data_Type");
        newsEditBinding.headTitleTvnewsEdit.setText(dataType);
        dbReferenceEdit_newsEdit = FirebaseDatabase.getInstance().getReference("NewsPapers").child(dataType)
                .child(userId);
        storage = FirebaseStorage.getInstance();
        storageReferenceNews = storage.getReference("NewsPaper_Pic_Uploads").child(dataType);
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Updating...");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);

        newsEditBinding.backBtnNewsEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        newsEditBinding.cancelBtnNewsEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        newsEditBinding.newsPhotoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    MyApplication.noNetdialogShow(getContext());
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, NEWS_PIC);
                }
            }
        });
        dbReferenceEdit_newsEdit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    newsEditBinding.nameEdtNewsEdit.setText(snapshot.child("name").getValue().toString().trim());
                    newsEditBinding.webUrlNewsEdit.setText(snapshot.child("webUrl").getValue().toString().trim());
                    news_pic_Url = snapshot.child("picUrl").getValue().toString().trim();
                    Glide.with(getContext()).load(news_pic_Url).placeholder(R.drawable.chandpur)
                            .into(newsEditBinding.newsPhotoEdit);
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),
                        "failed due to" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        newsEditBinding.submitBtnNewsEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                dbReferenceEdit_newsEdit.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        progressDialog.dismiss();
                        name = newsEditBinding.nameEdtNewsEdit.getText().toString().trim();
                        webLink = newsEditBinding.webUrlNewsEdit.getText().toString().trim();
                        if (name.isEmpty()) {
                            newsEditBinding.nameEdtNewsEdit.setError("name required");
                        } else if (webLink.isEmpty()) {
                            newsEditBinding.webUrlNewsEdit.setError("mobile required");
                        } else if (news_pic_Url == null) {
                            MyApplication.dialogShowPic(getContext());
                        } else {
                            HashMap<String, Object> updateMap = new HashMap<>();
                            updateMap.put("name", name);
                            updateMap.put("webUrl", webLink);
                            updateMap.put("picUrl", news_pic_Url);

                            dbReferenceEdit_newsEdit.updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        return newsEditBinding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEWS_PIC && data != null && data.getData() != null) {
            imageUri = data.getData();
            newsEditBinding.newsPhotoEdit.setImageURI(imageUri);
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
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH:mm:ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);
        final StorageReference store = storageReferenceNews.child("News_Banner_Images");
        StorageReference imagename = store.child("news_pic" + fileName);
        imagename.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                pd.dismiss();
                Toast.makeText(getContext(), "image uploaded", Toast.LENGTH_SHORT).show();


                imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        news_pic_Url = String.valueOf(uri);

                        Toast.makeText(getContext(), "Upload done!", Toast.LENGTH_SHORT).show();


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