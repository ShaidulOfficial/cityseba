package com.kawsar.eseba_chandpur.views.fragments.admin_fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterCommon;
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterEmergency;
import com.kawsar.eseba_chandpur.databinding.FragmentAmbulanceBinding;
import com.kawsar.eseba_chandpur.databinding.FragmentEmergencyBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EmergencyFragment extends Fragment {


    FragmentEmergencyBinding emergencyBinding;
    AdapterEmergency adapterEmergency;
    DatabaseReference databaseReferenceEmergency;
    List<CommonModel> modelListEmergency = new ArrayList<>();
    long timeStamp;

    public EmergencyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        emergencyBinding = FragmentEmergencyBinding.inflate(inflater, container
                , false);

        adapterEmergency = new AdapterEmergency(getContext(), modelListEmergency);
        adapterEmergency.notifyDataSetChanged();
        databaseReferenceEmergency = FirebaseDatabase.getInstance().getReference("Admin").child("Emergency");
        databaseReferenceEmergency.keepSynced(true);
        getDataFromFirebase();

        emergencyBinding.backBtnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        emergencyBinding.floatBtnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });

        return emergencyBinding.getRoot();
    }

    private void getDataFromFirebase() {
        emergencyBinding.loadingEmergency.setVisibility(View.VISIBLE);
        databaseReferenceEmergency.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelListEmergency.clear();
                emergencyBinding.loadingEmergency.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelListEmergency.add(commonModel_hospital);
                }
                if (modelListEmergency.isEmpty()) {
                    emergencyBinding.loadingEmergency.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapterEmergency.notifyDataSetChanged();
                emergencyBinding.emergencyAdminRcv.setHasFixedSize(true);
                emergencyBinding.emergencyAdminRcv.setAdapter(adapterEmergency);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void show_dialog() {
        Dialog custom_dialog = new Dialog(getContext());
        custom_dialog.setContentView(R.layout.custom_insert_dialog_emergency);
        custom_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        AppCompatButton submit_btn = custom_dialog.findViewById(R.id.submit_btnEmergency);
        AppCompatButton cancel_btn = custom_dialog.findViewById(R.id.cancel_btnEmergency);
        TextView head_title_tv = custom_dialog.findViewById(R.id.head_title_tvEmergency);
        head_title_tv.setText("ইমারজেন্সি কন্টাক্ট তথ্য দিন");

        custom_dialog.setCancelable(false);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name_edt = custom_dialog.findViewById(R.id.name_edtEmergency);
                EditText mobile_edt = custom_dialog.findViewById(R.id.mobile_edtEmergency);
                timeStamp = System.currentTimeMillis();
                String userKey = databaseReferenceEmergency.push().getKey();
                String name = name_edt.getText().toString().trim();
                String mobile = mobile_edt.getText().toString().trim();
                String dataType = "Emergency";
                String search_data = name.toLowerCase() + "," + mobile.toLowerCase();
                if (TextUtils.isEmpty(name)) {
                    name_edt.setError("Name is required");
                } else if (TextUtils.isEmpty(mobile)) {
                    mobile_edt.setError("Mobile is required");
                } else {
// ***=====================================================================================***
// ********************************** Data Insert here........ **********************************

                    HashMap<String, Object> dataSetMapEmergency = new HashMap<>();
                    dataSetMapEmergency.put("name", name);
                    dataSetMapEmergency.put("mobile", mobile);
                    dataSetMapEmergency.put("search_data", search_data);
                    dataSetMapEmergency.put("userId", userKey);
                    dataSetMapEmergency.put("dataType", dataType);
                    dataSetMapEmergency.put("timestamp", timeStamp);
                    databaseReferenceEmergency.child(userKey).setValue(dataSetMapEmergency);
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
}