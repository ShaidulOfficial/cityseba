package com.kawsar.eseba_chandpur.views.fragments.admin_fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

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
import com.kawsar.eseba_chandpur.databinding.FragmentHospitalBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HospitalFragment extends Fragment {

    FragmentHospitalBinding hospitalBinding;
    AdapterCommon adapterCommon_hospital;
    DatabaseReference databaseReference_hospital;
    List<CommonModel> modelList_hospital = new ArrayList<>();
    long timeStamp;

    public HospitalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        hospitalBinding = FragmentHospitalBinding.inflate(inflater, container, false);

        databaseReference_hospital = FirebaseDatabase.getInstance().getReference("Admin").child("Hospitals");

        getDataFromFirebase();
        adapterCommon_hospital = new AdapterCommon(getContext(), modelList_hospital);
        adapterCommon_hospital.notifyDataSetChanged();
        hospitalBinding.floatBtnHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });
        hospitalBinding.backBtnAdminHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });

        hospitalBinding.hospitalAdminSearchV.clearFocus();
        hospitalBinding.hospitalAdminSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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


        return hospitalBinding.getRoot();
    }

    private void filterList(String newText) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : modelList_hospital) {
            if (model.getSearch_data().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(model);

            } else {

            }
        }
        if (filterlist.isEmpty()) {

            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterCommon_hospital.setFilterList_admin(filterlist);
        }
    }

    private void getDataFromFirebase() {
        hospitalBinding.loadingProgressLottieViewHospital.setVisibility(View.VISIBLE);
        databaseReference_hospital.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList_hospital.clear();
                hospitalBinding.loadingProgressLottieViewHospital.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelList_hospital.add(commonModel_hospital);
                }
                if (modelList_hospital.isEmpty()) {
                    hospitalBinding.loadingProgressLottieViewHospital.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                } else {
                }
                adapterCommon_hospital.notifyDataSetChanged();
                hospitalBinding.hospitalAdminRcv.setAdapter(adapterCommon_hospital);
                hospitalBinding.hospitalAdminRcv.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void show_dialog() {
        Dialog custom_dialog = new Dialog(getContext());
        custom_dialog.setContentView(R.layout.custom_insert_dialog_box);
        custom_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        custom_dialog.create();
        custom_dialog.setCancelable(false);
        AppCompatButton submit_btn = custom_dialog.findViewById(R.id.submit_btn);
        AppCompatButton cancel_btn = custom_dialog.findViewById(R.id.cancel_btn);
        TextView head_title_tv = custom_dialog.findViewById(R.id.head_title_tv);
        head_title_tv.setText("হাস্পাতালের তথ্য দিন");
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name_edt = custom_dialog.findViewById(R.id.name_edt);
                EditText location_edt = custom_dialog.findViewById(R.id.location_edt);
                EditText mobile_edt = custom_dialog.findViewById(R.id.mobile_edt);
                timeStamp = System.currentTimeMillis();
                String userKey_hospital = databaseReference_hospital.push().getKey();
                String dataType = "Hospitals";
                String name = name_edt.getText().toString().trim();
                String location = location_edt.getText().toString().trim();
                String mobile = mobile_edt.getText().toString().trim();
                String search_data = name.toLowerCase() + "," + location.toLowerCase() + "," + mobile.toLowerCase();
                if (!name.isEmpty()) {
                    name_edt.setError(null);
                    if (!location.isEmpty()) {
                        location_edt.setError(null);
                        if (!mobile.isEmpty()) {
                            mobile_edt.setError(null);
                            if (mobile.length() != 11) {
                                mobile_edt.setError("Invalid Number");
                            } else {
// ***===============================( ||||||||||||||||||||||| )============================***
// ********************************** Data Insert here........ **********************************
                                HashMap<String, Object> dataSetMapHospital = new HashMap<>();
                                dataSetMapHospital.put("name", name);
                                dataSetMapHospital.put("location", location);
                                dataSetMapHospital.put("mobile", mobile);
                                dataSetMapHospital.put("search_data", search_data);
                                dataSetMapHospital.put("userId", userKey_hospital);
                                dataSetMapHospital.put("dataType", dataType);
                                dataSetMapHospital.put("timestamp", timeStamp);
                                databaseReference_hospital.child(userKey_hospital).setValue(dataSetMapHospital);
                                custom_dialog.dismiss();

                            }
                        } else {
                            mobile_edt.setError("Mobile number required!");
                        }
                    } else {
                        location_edt.setError("Location required!");
                    }
                } else {
                    name_edt.setError("Name required!");
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
