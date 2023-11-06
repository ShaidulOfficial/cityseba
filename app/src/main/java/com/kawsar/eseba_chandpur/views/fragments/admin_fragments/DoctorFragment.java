package com.kawsar.eseba_chandpur.views.fragments.admin_fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.kawsar.eseba_chandpur.Constant;
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterCommon;
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterDoctor;
import com.kawsar.eseba_chandpur.databinding.FragmentDoctorBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DoctorFragment extends Fragment {

    FragmentDoctorBinding doctorBinding;
    AdapterDoctor adapter_doctor;
    DatabaseReference databaseReference_doctor;
    List<CommonModel> modelList_doctor = new ArrayList<>();
    long timeStamp;

    public DoctorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        doctorBinding = FragmentDoctorBinding.inflate(inflater, container, false);
        doctorBinding.backBtnAdminDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        databaseReference_doctor = FirebaseDatabase.getInstance().getReference(Constant.ServiceBucket).child("Doctors");

        getDataFromFirebase();
        adapter_doctor = new AdapterDoctor(getContext(), modelList_doctor);
        adapter_doctor.notifyDataSetChanged();
        doctorBinding.floatBtnDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });
        doctorBinding.doctorAdminSearchV.clearFocus();
        doctorBinding.doctorAdminSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        return doctorBinding.getRoot();

    }

    private void filterList(String newText) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : modelList_doctor) {
            if (model.getSearch_data().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(model);

            } else {

            }
        }
        if (filterlist.isEmpty()) {

            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapter_doctor.setFilterDoctor(filterlist);
        }
    }

    private void getDataFromFirebase() {
        doctorBinding.loadingDoctor.setVisibility(View.VISIBLE);
        databaseReference_doctor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList_doctor.clear();
                doctorBinding.loadingDoctor.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {

                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelList_doctor.add(commonModel_hospital);

                }
                if (modelList_doctor.isEmpty()) {
                    doctorBinding.loadingDoctor.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapter_doctor.notifyDataSetChanged();
                doctorBinding.doctorAdminRcv.setAdapter(adapter_doctor);
                doctorBinding.doctorAdminRcv.setHasFixedSize(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void show_dialog() {
        Dialog custom_dialog = new Dialog(getContext());
        custom_dialog.setContentView(R.layout.custom_insert_dialog_box_doctor);
        custom_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        custom_dialog.create();
        custom_dialog.setCancelable(false);
        AppCompatButton submit_btn = custom_dialog.findViewById(R.id.submit_btn_doctor);
        AppCompatButton cancel_btn = custom_dialog.findViewById(R.id.cancel_btn_doctor);
        TextView head_title_tv = custom_dialog.findViewById(R.id.head_title_doctor);
        head_title_tv.setText("ডাক্তারের তথ্য দিন");
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name_edt = custom_dialog.findViewById(R.id.name_edt_doctor);
                EditText hospital_name_edt = custom_dialog.findViewById(R.id.hospital_edt_doctor);
                EditText qualification_edt = custom_dialog.findViewById(R.id.qualification_edt_doctor);
                EditText mobile_edt = custom_dialog.findViewById(R.id.mobile_edt_doctor);
                EditText chamberTime_edt = custom_dialog.findViewById(R.id.chamberTime_edt_doctor);

                timeStamp = System.currentTimeMillis();
                String userKey_doctor = databaseReference_doctor.push().getKey();
                String dataType = "Doctors";
                String name = name_edt.getText().toString().trim();
                String chamberTime = chamberTime_edt.getText().toString().trim();
                String hospital_name = hospital_name_edt.getText().toString().trim();
                String qualification = qualification_edt.getText().toString().trim();
                String mobile = mobile_edt.getText().toString().trim();
                String search_data = name.toLowerCase() + "," + hospital_name.toLowerCase() + "," + mobile.toLowerCase();
                if (TextUtils.isEmpty(name)) {
                    name_edt.setError("Name is required");
                } else if (TextUtils.isEmpty(qualification)) {
                    qualification_edt.setError("Qualification required");
                } else if (TextUtils.isEmpty(hospital_name)) {
                    hospital_name_edt.setError("Hospital name required");
                } else if (TextUtils.isEmpty(chamberTime)) {
                    chamberTime_edt.setError("ChamberTime required");
                } else if (TextUtils.isEmpty(mobile)) {
                    mobile_edt.setError("Mobile required");
                } else if (mobile.length() != 11) {
                    mobile_edt.setError("11 Digit must be required");
                } else {
                    // ***===============================( ||||||||||||||||||||||| )============================***
// ********************************** Data Insert here........ **********************************
                    HashMap<String, Object> dataSetMapdoctor = new HashMap<>();
                    dataSetMapdoctor.put("name", name);
                    dataSetMapdoctor.put("mobile", mobile);
                    dataSetMapdoctor.put("search_data", search_data);
                    dataSetMapdoctor.put("hospit_name", hospital_name);
                    dataSetMapdoctor.put("chemberTime", chamberTime);
                    dataSetMapdoctor.put("qualification", qualification);
                    dataSetMapdoctor.put("userId", userKey_doctor);
                    dataSetMapdoctor.put("dataType", dataType);
                    dataSetMapdoctor.put("timestamp", timeStamp);
                    databaseReference_doctor.child(userKey_doctor).setValue(dataSetMapdoctor);
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