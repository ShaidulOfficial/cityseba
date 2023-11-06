package com.kawsar.eseba_chandpur.views.fragments.admin_fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterCommon;
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterResturent;
import com.kawsar.eseba_chandpur.databinding.FragmentResturentBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ResturentFragment extends Fragment {

    FragmentResturentBinding resturentBinding;
    AdapterResturent adapterResturent;
    DatabaseReference databaseReference_resturent;
    FirebaseStorage storage;
    StorageReference storageReferenceRestu;
    List<CommonModel> modelList_resturent = new ArrayList<>();
    ImageView photo_btn;
    long timeStamp;
    final int RESTURENT_PIC = 100;
    String resturentpic_Url;
    Uri imageUri;

    public ResturentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        resturentBinding = FragmentResturentBinding.inflate(inflater, container,
                false);
        databaseReference_resturent = FirebaseDatabase.getInstance()
                .getReference("Admin").child("Resturents");
        databaseReference_resturent.keepSynced(true);
        storage = FirebaseStorage.getInstance();
        storageReferenceRestu = storage.getReference("Resturent_Photo");
        getDataFromFirebase();
        adapterResturent = new AdapterResturent(getContext(), modelList_resturent);
        adapterResturent.notifyDataSetChanged();
        resturentBinding.floatBtnResturent.setVisibility(View.VISIBLE);
        resturentBinding.floatBtnResturent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });
        resturentBinding.backBtnAdminResturent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        resturentBinding.resturentAdminSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    resturentBinding.layNonetResturent.setVisibility(View.VISIBLE);
                    resturentBinding.floatBtnResturent.setVisibility(View.GONE);
                } else {
                    resturentBinding.layNonetResturent.setVisibility(View.GONE);
                    resturentBinding.floatBtnResturent.setVisibility(View.VISIBLE);
                }
                getDataFromFirebase();
                adapterResturent.notifyDataSetChanged();
                resturentBinding.resturentAdminSwip.setRefreshing(false);
            }
        });
        resturentBinding.layNonetResturent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resturentBinding.layNonetResturent.setVisibility(View.GONE);
                resturentBinding.floatBtnResturent.setVisibility(View.VISIBLE);
            }
        });
        resturentBinding.resturentAdminSearchV.clearFocus();
        resturentBinding.resturentAdminSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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


        return resturentBinding.getRoot();
    }

    private void filterList(String newText) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : modelList_resturent) {
            if (model.getSearch_data().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(model);
            } else {
            }
        }
        if (filterlist.isEmpty()) {

            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterResturent.setFilterList_resturentAdmin(filterlist);
        }
    }

    private void getDataFromFirebase() {
        resturentBinding.loadingResturent.setVisibility(View.VISIBLE);
        databaseReference_resturent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList_resturent.clear();
                resturentBinding.loadingResturent.setVisibility(View.GONE);
                modelList_resturent.clear();
                resturentBinding.loadingResturent.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelList_resturent.add(commonModel_hospital);
                }
                if (modelList_resturent.isEmpty()) {
                    resturentBinding.loadingResturent.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                } else {
                }
                resturentBinding.resturentAdminRcv.setAdapter(adapterResturent);
                resturentBinding.resturentAdminRcv.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void show_dialog() {
        Dialog custom_dialog = new Dialog(getContext());
        custom_dialog.setContentView(R.layout.custom_insert_dialog_resturent);
        custom_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        custom_dialog.create();
        custom_dialog.setCancelable(false);
        AppCompatButton submit_btn = custom_dialog.findViewById(R.id.submit_btnResturent);
        AppCompatButton cancel_btn = custom_dialog.findViewById(R.id.cancel_btnResturent);
        photo_btn = custom_dialog.findViewById(R.id.resturentPhoto);
        TextView head_title_tv = custom_dialog.findViewById(R.id.head_title_tvResturent);
        head_title_tv.setText("নতুন রেষ্টুরেন্ট যোগ করুন");
        photo_btn.setOnClickListener(new View.OnClickListener() {
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
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name_edt = custom_dialog.findViewById(R.id.name_edtResturent);
                EditText location_edt = custom_dialog.findViewById(R.id.location_edtResturent);
                EditText mobile_edt = custom_dialog.findViewById(R.id.mobile_edtResturent);
                EditText webUrl_edt = custom_dialog.findViewById(R.id.webUrl_edtResturent);
                EditText description_edt = custom_dialog.findViewById(R.id.description_edtResturent);

                timeStamp = System.currentTimeMillis();
                String dataType = "Resturents";
                String userkeyRestu = databaseReference_resturent.push().getKey();
                String name = name_edt.getText().toString().trim();
                String location = location_edt.getText().toString().trim();
                String mobile = mobile_edt.getText().toString().trim();
                String description = description_edt.getText().toString().trim();
                String webUrl = webUrl_edt.getText().toString().trim();
                String search_data = name.toLowerCase() + "," + location.toLowerCase() + "," + mobile.toLowerCase();
                if (name.isEmpty()) {
                    name_edt.setError("name required");
                } else if (location.isEmpty()) {
                    location_edt.setError("Location required");
                } else if (description.isEmpty()) {
                    description_edt.setError("Description required");
                } else if (mobile.isEmpty()) {
                    mobile_edt.setError("Mobile required");
                } else if (mobile.length() != 11) {
                    mobile_edt.setError("11 Digit Must be required!");
                } else if (resturentpic_Url == null) {
                    MyApplication.dialogShowPic(getContext());
                } else {
                    HashMap<String, Object> restuMap = new HashMap<>();
                    restuMap.put("name", name);
                    restuMap.put("location", location);
                    restuMap.put("mobile", mobile);
                    restuMap.put("message", description);
                    restuMap.put("webUrl", webUrl);
                    restuMap.put("search_data", search_data);
                    restuMap.put("userId", userkeyRestu);
                    restuMap.put("picUrl", resturentpic_Url);
                    restuMap.put("timestamp", timeStamp);
                    restuMap.put("dataType", dataType);
                    databaseReference_resturent.child(userkeyRestu).setValue(restuMap);
                    custom_dialog.dismiss();
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                custom_dialog.dismiss();
            }
        });
        custom_dialog.show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESTURENT_PIC && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            photo_btn.setImageURI(imageUri);
            sendImagetoStorage(imageUri);
        } else {
            Toast.makeText(getContext(), "not pic get", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendImagetoStorage(Uri imageUri) {
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Please wait");
        pd.setIcon(R.drawable.danger);
        pd.show();
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM-dd-yyyy_hh:mm-aa", Locale.US);
        Date now = new Date();
        String fileName = formatter.format(now);
        StorageReference imagename = storageReferenceRestu.child("restu_pic-" + fileName);
        imagename.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                pd.dismiss();
                Toast.makeText(getContext(), "image uploaded", Toast.LENGTH_SHORT).show();
                imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        resturentpic_Url = String.valueOf(uri);
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