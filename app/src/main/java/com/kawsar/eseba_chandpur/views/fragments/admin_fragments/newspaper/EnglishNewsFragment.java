package com.kawsar.eseba_chandpur.views.fragments.admin_fragments.newspaper;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterNews;
import com.kawsar.eseba_chandpur.databinding.FragmentBanglaNewsBinding;
import com.kawsar.eseba_chandpur.databinding.FragmentEnglishNewsBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class EnglishNewsFragment extends Fragment {

    FragmentEnglishNewsBinding englishNewsBinding;
    AdapterNews adapter_englishNews;
    DatabaseReference dbRef_englishNews;
    List<CommonModel> modelList_englishNews = new ArrayList<>();
    long timeStamp;
    String newsPaperName, webLink;
    final int NEWS_PIC = 100;
    String news_pic_Url;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReferenceNews;
    ImageView Newsphoto_btn;

    public EnglishNewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        englishNewsBinding = FragmentEnglishNewsBinding.inflate(inflater
                , container, false);
        dbRef_englishNews = FirebaseDatabase.getInstance().getReference("NewsPapers").child("English");
        dbRef_englishNews.keepSynced(true);
        storage = FirebaseStorage.getInstance();
        storageReferenceNews = storage.getReference("NewsPaper_Pic_Uploads").child("English");
        getDataFromFirebase();
        adapter_englishNews = new AdapterNews(getContext(), modelList_englishNews);
        adapter_englishNews.notifyDataSetChanged();
        englishNewsBinding.floatBtnEnglishNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });
        englishNewsBinding.englishNewsAdminSearchV.clearFocus();
        englishNewsBinding.englishNewsAdminSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        englishNewsBinding.layNonetEnglishNewsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                englishNewsBinding.layNonetEnglishNewsAdmin.setVisibility(View.GONE);
            }
        });
        englishNewsBinding.englishNewsSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    englishNewsBinding.layNonetEnglishNewsAdmin.setVisibility(View.VISIBLE);
                } else {
                    englishNewsBinding.layNonetEnglishNewsAdmin.setVisibility(View.GONE);
                }
                getDataFromFirebase();
                adapter_englishNews.notifyDataSetChanged();
                englishNewsBinding.englishNewsSwip.setRefreshing(false);
            }
        });
        return englishNewsBinding.getRoot();
    }

    private void filterList(String newText) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : modelList_englishNews) {
            if (model.getName().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(model);
            } else {
            }
        }
        if (filterlist.isEmpty()) {
            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapter_englishNews.setFilterList_news(filterlist);
        }
    }

    private void getDataFromFirebase() {
        englishNewsBinding.loadingEnglishNews.setVisibility(View.VISIBLE);
        dbRef_englishNews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList_englishNews.clear();
                englishNewsBinding.loadingEnglishNews.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel = ds.getValue(CommonModel.class);
                    modelList_englishNews.add(commonModel);
                }
                if (modelList_englishNews.isEmpty()) {
                    englishNewsBinding.loadingEnglishNews.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                } else {
                }
                adapter_englishNews.notifyDataSetChanged();
                englishNewsBinding.englishNewsAdminRcv.setHasFixedSize(true);
                englishNewsBinding.englishNewsAdminRcv.setAdapter(adapter_englishNews);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void show_dialog() {
        Dialog news_dialog = new Dialog(getContext());
        news_dialog.setContentView(R.layout.custom_insert_dialog_news);
        news_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        news_dialog.create();
        news_dialog.setCancelable(false);
        AppCompatButton submit_btn = news_dialog.findViewById(R.id.submit_btnNews);
        AppCompatButton cancel_btn = news_dialog.findViewById(R.id.cancel_btnNews);
        Newsphoto_btn = news_dialog.findViewById(R.id.newsPhoto);
        TextView head_title_tv = news_dialog.findViewById(R.id.head_title_tvnews);
        head_title_tv.setText("পত্রিকার তথ্য দিন");
        Newsphoto_btn.setOnClickListener(new View.OnClickListener() {
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
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText web_edt = news_dialog.findViewById(R.id.webUrlNews);
                EditText name_edt = news_dialog.findViewById(R.id.name_edtNews);
                webLink = web_edt.getText().toString().trim();
                newsPaperName = name_edt.getText().toString().trim();
                timeStamp = System.currentTimeMillis();
                String userKey = dbRef_englishNews.push().getKey();
                String dataType = "English";
                if (TextUtils.isEmpty(newsPaperName)) {
                    name_edt.setError("Name is required");
                } else if (TextUtils.isEmpty(webLink)) {
                    web_edt.setError("Website required");
                } else if (news_pic_Url == null) {
                    MyApplication.dialogShowPic(getContext());
                } else {
                    // ***===============================( ||||||||||||||||||||||| )============================***
// ********************************** Data Insert here........ **********************************
                    HashMap<String, Object> dataSetMapNews = new HashMap<>();
                    dataSetMapNews.put("name", newsPaperName);
                    dataSetMapNews.put("webUrl", webLink);
                    dataSetMapNews.put("userId", userKey);
                    dataSetMapNews.put("dataType", dataType);
                    dataSetMapNews.put("timestamp", timeStamp);
                    dataSetMapNews.put("picUrl", news_pic_Url);
                    dbRef_englishNews.child(userKey).setValue(dataSetMapNews);
                    news_dialog.dismiss();
                }

            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                news_dialog.dismiss();
            }
        });
        news_dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEWS_PIC && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Newsphoto_btn.setImageURI(imageUri);
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
        StorageReference imagename = storageReferenceNews.child("news_logo-" + fileName);
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