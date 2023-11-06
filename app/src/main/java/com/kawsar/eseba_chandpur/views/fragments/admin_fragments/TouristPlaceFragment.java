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
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterResturent;
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterTourist;
import com.kawsar.eseba_chandpur.databinding.FragmentResturentBinding;
import com.kawsar.eseba_chandpur.databinding.FragmentTouristPlaceBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class TouristPlaceFragment extends Fragment {

    FragmentTouristPlaceBinding touristPlaceBinding;
    AdapterTourist adapterTourist;
    DatabaseReference databaseReferenceTourist;
    FirebaseStorage storage;
    StorageReference storageReferenceTourist;
    List<CommonModel> modelListTourist = new ArrayList<>();
    ImageView photo_btn;
    long timeStamp;
    final int TOURIST_SPOT_PIC = 100;
    String touristPic_Url;
    Uri imageUri;

    public TouristPlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        touristPlaceBinding = FragmentTouristPlaceBinding.inflate(inflater, container,
                false);
        databaseReferenceTourist = FirebaseDatabase.getInstance()
                .getReference("Admin").child("TouristPlace");
        databaseReferenceTourist.keepSynced(true);
        storage = FirebaseStorage.getInstance();
        storageReferenceTourist = storage.getReference("Tourist_Photo");
        getDataFromFirebase();
        adapterTourist = new AdapterTourist(getContext(), modelListTourist);
        adapterTourist.notifyDataSetChanged();
        touristPlaceBinding.floatBtnTourist.setVisibility(View.VISIBLE);
        touristPlaceBinding.floatBtnTourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });
        touristPlaceBinding.backBtnAdminTourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        touristPlaceBinding.touristAdminSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    touristPlaceBinding.layNonetTourist.setVisibility(View.VISIBLE);
                    touristPlaceBinding.floatBtnTourist.setVisibility(View.GONE);
                } else {
                    touristPlaceBinding.layNonetTourist.setVisibility(View.GONE);
                    touristPlaceBinding.floatBtnTourist.setVisibility(View.VISIBLE);
                }
                getDataFromFirebase();
                adapterTourist.notifyDataSetChanged();
                touristPlaceBinding.touristAdminSwip.setRefreshing(false);
            }
        });
        touristPlaceBinding.layNonetTourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touristPlaceBinding.layNonetTourist.setVisibility(View.GONE);
                touristPlaceBinding.floatBtnTourist.setVisibility(View.VISIBLE);
            }
        });
        touristPlaceBinding.touristAdminSearchV.clearFocus();
        touristPlaceBinding.touristAdminSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        return touristPlaceBinding.getRoot();
    }

    private void filterList(String newText) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : modelListTourist) {
            if (model.getSearch_data().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(model);
            } else {
            }
        }
        if (filterlist.isEmpty()) {

            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterTourist.setFilterList_touristAdmin(filterlist);
        }
    }

    private void getDataFromFirebase() {
        touristPlaceBinding.loadingTourist.setVisibility(View.VISIBLE);
        databaseReferenceTourist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelListTourist.clear();
                touristPlaceBinding.loadingTourist.setVisibility(View.GONE);
                modelListTourist.clear();
                touristPlaceBinding.loadingTourist.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelListTourist.add(commonModel_hospital);
                }
                if (modelListTourist.isEmpty()) {
                    touristPlaceBinding.loadingTourist.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                } else {
                }
                touristPlaceBinding.touristAdminRcv.setAdapter(adapterTourist);
                touristPlaceBinding.touristAdminRcv.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void show_dialog() {
        Dialog custom_dialog = new Dialog(getContext());
        custom_dialog.setContentView(R.layout.custom_insert_dialog_tourist);
        custom_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        custom_dialog.create();
        custom_dialog.setCancelable(false);
        AppCompatButton submit_btn = custom_dialog.findViewById(R.id.submit_btnTourist);
        AppCompatButton cancel_btn = custom_dialog.findViewById(R.id.cancel_btnTourist);
        photo_btn = custom_dialog.findViewById(R.id.touristPhoto);
        TextView head_title_tv = custom_dialog.findViewById(R.id.head_title_tvTourist);
        head_title_tv.setText("নতুন পর্যটন স্থান যোগ করুন");

        photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyApplication.isNetworkAvailable(getContext())) {
                    MyApplication.noNetdialogShow(getContext());
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, TOURIST_SPOT_PIC);
                }
            }
        });
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name_edt = custom_dialog.findViewById(R.id.name_edtTourist);
                EditText location_edt = custom_dialog.findViewById(R.id.location_edtTourist);
                EditText webUrl_edt = custom_dialog.findViewById(R.id.webUrl_edtTourist);
                EditText description_edt = custom_dialog.findViewById(R.id.description_edtTourist);
                timeStamp = System.currentTimeMillis();
                String dataType = "TouristPlace";
                String userkeyTourist = databaseReferenceTourist.push().getKey();
                String name = name_edt.getText().toString().trim();
                String location = location_edt.getText().toString().trim();
                String description = description_edt.getText().toString().trim();
                String webUrl = webUrl_edt.getText().toString().trim();
                String search_data = name.toLowerCase() + "," + location.toLowerCase();
                if (name.isEmpty()) {
                    name_edt.setError("name required");
                } else if (webUrl.isEmpty()) {
                    webUrl_edt.setText("WebSite not available");
                } else if (location.isEmpty()) {
                    location_edt.setError("Location required");
                } else if (description.isEmpty()) {
                    description_edt.setError("Description required");
                } else if (touristPic_Url == null) {
                    MyApplication.dialogShowPic(getContext());
                } else {
                    HashMap<String, Object> touristMap = new HashMap<>();
                    touristMap.put("name", name);
                    touristMap.put("location", location);
                    touristMap.put("message", description);
                    touristMap.put("webUrl", webUrl);
                    touristMap.put("search_data", search_data);
                    touristMap.put("userId", userkeyTourist);
                    touristMap.put("picUrl", touristPic_Url);
                    touristMap.put("timestamp", timeStamp);
                    touristMap.put("dataType", dataType);
                    databaseReferenceTourist.child(userkeyTourist).setValue(touristMap);
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
        if (requestCode == TOURIST_SPOT_PIC && resultCode == RESULT_OK && data != null && data.getData() != null) {
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
        StorageReference imagename = storageReferenceTourist.child("tourist_pic-" + fileName);
        imagename.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                pd.dismiss();
                Toast.makeText(getContext(), "image uploaded", Toast.LENGTH_SHORT).show();
                imagename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        touristPic_Url = String.valueOf(uri);
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