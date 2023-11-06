package com.kawsar.eseba_chandpur.views.fragments.admin_fragments;

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
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterShopping;
import com.kawsar.eseba_chandpur.databinding.FragmentShoppingBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ShoppingFragment extends Fragment {


    FragmentShoppingBinding shoppingBinding;
    AdapterShopping adapter_shopping;
    DatabaseReference dbRef_shopping;
    List<CommonModel> modelList_shopping = new ArrayList<>();
    long timeStamp;
    String shoppingName, webLink;
    final int SHOPPING_PIC = 100;
    String shopping_pic_Url;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference_shopping;
    ImageView shopping_btn;

    public ShoppingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shoppingBinding = FragmentShoppingBinding.inflate(inflater
                , container, false);

        dbRef_shopping = FirebaseDatabase.getInstance().getReference("Admin").child("Shopping");
        dbRef_shopping.keepSynced(true);
        storage = FirebaseStorage.getInstance();
        storageReference_shopping = storage.getReference("Shopping_Photo");
        getDataFromFirebase();
        adapter_shopping = new AdapterShopping(getContext(), modelList_shopping);
        adapter_shopping.notifyDataSetChanged();
        shoppingBinding.floatBtnShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });
        shoppingBinding.backBtnAdminShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        shoppingBinding.shoppingAdminSearchV.clearFocus();
        shoppingBinding.shoppingAdminSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        shoppingBinding.layNonetShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingBinding.layNonetShopping.setVisibility(View.GONE);
            }
        });
        shoppingBinding.shoppingAdminSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    shoppingBinding.layNonetShopping.setVisibility(View.VISIBLE);
                } else {
                    shoppingBinding.layNonetShopping.setVisibility(View.GONE);
                }
                getDataFromFirebase();
                adapter_shopping.notifyDataSetChanged();
                shoppingBinding.shoppingAdminSwip.setRefreshing(false);
            }
        });

        return shoppingBinding.getRoot();
    }

    private void filterList(String newText) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : modelList_shopping) {
            if (model.getName().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(model);
            } else {
            }
        }
        if (filterlist.isEmpty()) {

            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapter_shopping.setFilterList_shopping(filterlist);
        }
    }

    private void getDataFromFirebase() {
        shoppingBinding.loadingShopping.setVisibility(View.VISIBLE);
        dbRef_shopping.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList_shopping.clear();
                shoppingBinding.loadingShopping.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel = ds.getValue(CommonModel.class);
                    modelList_shopping.add(commonModel);
                }
                if (modelList_shopping.isEmpty()) {
                    shoppingBinding.loadingShopping.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                } else {
                }
                adapter_shopping.notifyDataSetChanged();
                shoppingBinding.shoppingAdminRcv.setHasFixedSize(true);
                shoppingBinding.shoppingAdminRcv.setAdapter(adapter_shopping);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void show_dialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_insert_dialog_news);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.create();
        dialog.setCancelable(false);
        AppCompatButton submit_btn = dialog.findViewById(R.id.submit_btnNews);
        AppCompatButton cancel_btn = dialog.findViewById(R.id.cancel_btnNews);
        shopping_btn = dialog.findViewById(R.id.newsPhoto);
        TextView head_title_tv = dialog.findViewById(R.id.head_title_tvnews);
        head_title_tv.setText("শপিং এর তথ্য দিন");
        shopping_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    MyApplication.noNetdialogShow(getContext());
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, SHOPPING_PIC);
                }
            }
        });
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText web_edt = dialog.findViewById(R.id.webUrlNews);
                EditText name_edt = dialog.findViewById(R.id.name_edtNews);
                webLink = web_edt.getText().toString().trim();
                shoppingName = name_edt.getText().toString().trim();
                timeStamp = System.currentTimeMillis();
                String userKey = dbRef_shopping.push().getKey();
                String dataType = "Shopping";
                if (TextUtils.isEmpty(shoppingName)) {
                    name_edt.setError("Name is required");
                } else if (TextUtils.isEmpty(webLink)) {
                    web_edt.setError("Website required");
                } else if (shopping_pic_Url == null) {
                    MyApplication.dialogShowPic(getContext());
                } else {
                    // ***===============================( ||||||||||||||||||||||| )============================***
// ********************************** Data Insert here........ **********************************
                    HashMap<String, Object> dataSetMapShop = new HashMap<>();
                    dataSetMapShop.put("name", shoppingName);
                    dataSetMapShop.put("webUrl", webLink);
                    dataSetMapShop.put("userId", userKey);
                    dataSetMapShop.put("dataType", dataType);
                    dataSetMapShop.put("timestamp", timeStamp);
                    dataSetMapShop.put("picUrl", shopping_pic_Url);
                    dbRef_shopping.child(userKey).setValue(dataSetMapShop);
                    dialog.dismiss();
                }

            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SHOPPING_PIC && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            shopping_btn.setImageURI(imageUri);
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
        StorageReference imagename = storageReference_shopping.child("shopping_logo-" + fileName);
        imagename.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                pd.dismiss();
                Toast.makeText(getContext(), "image uploaded", Toast.LENGTH_SHORT).show();
                imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        shopping_pic_Url = String.valueOf(uri);
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