package com.kawsar.eseba_chandpur.views.fragments.admin_fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
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
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterPolice;
import com.kawsar.eseba_chandpur.databinding.FragmentPoliceBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PoliceFragment extends Fragment {
    FragmentPoliceBinding policeBinding;
    AdapterPolice adapterCommon_police;
    DatabaseReference databaseReference_police;
    List<CommonModel> modelList_police = new ArrayList<>();
    long timeStamp;

    public PoliceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        policeBinding = FragmentPoliceBinding.inflate(inflater, container,
                false);
        databaseReference_police = FirebaseDatabase.getInstance().getReference("Admin").child("Polices");
        getDataFromFirebase();
        adapterCommon_police = new AdapterPolice(getContext(), modelList_police);
        adapterCommon_police.notifyDataSetChanged();
        policeBinding.floatBtnPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });
        policeBinding.backBtnAdminPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        policeBinding.policeAdminSearchV.clearFocus();
        policeBinding.policeAdminSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        return policeBinding.getRoot();
    }

    private void filterList(String newText) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : modelList_police) {
            if (model.getSearch_data().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(model);

            }
        }
        if (filterlist.isEmpty()) {

            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterCommon_police.setFilterList_police(filterlist);
        }
    }

    private void show_dialog() {
        Dialog custom_dialog = new Dialog(getContext());
        custom_dialog.setContentView(R.layout.custom_insert_dialog_bank);
        custom_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        custom_dialog.create();
        custom_dialog.setCancelable(false);
        AppCompatButton submit_btn = custom_dialog.findViewById(R.id.submit_btn_bank);
        AppCompatButton cancel_btn = custom_dialog.findViewById(R.id.cancel_btn_bank);
        TextView head_title_tv = custom_dialog.findViewById(R.id.head_titleTv_bank);
        head_title_tv.setText("পুলিশ এর নতুন তথ্য দিন");

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name_edt = custom_dialog.findViewById(R.id.name_edt_bank);
                EditText location_edt = custom_dialog.findViewById(R.id.location_edt_bank);
                EditText mobile_edt = custom_dialog.findViewById(R.id.mobile_edt_bank);
                EditText postType_edt = custom_dialog.findViewById(R.id.postType_edt_bank);

                timeStamp = System.currentTimeMillis();
                String dataType = "Polices";
                String userKey = databaseReference_police.push().getKey();
                String name = name_edt.getText().toString().trim();
                String location = location_edt.getText().toString().trim();
                String mobile = mobile_edt.getText().toString().trim();
                String postType = postType_edt.getText().toString().trim();
                String search_data = name.toLowerCase() + "," + location.toLowerCase() + postType.toLowerCase() + "," + mobile.toLowerCase();

                if (TextUtils.isEmpty(name)) {
                    name_edt.setError("Name is required");
                } else if (TextUtils.isEmpty(postType)) {
                    postType_edt.setError("Post is required");
                } else if (TextUtils.isEmpty(location)) {
                    location_edt.setError("Location is required");
                } else if (TextUtils.isEmpty(mobile)) {
                    mobile_edt.setError("Mobile is required");
                } else if (mobile.length() != 11) {
                    mobile_edt.setError("Must be 11 digits ");
                } else {
                    HashMap<String, Object> dataSetPolice = new HashMap<>();
                    dataSetPolice.put("name", name);
                    dataSetPolice.put("location", location);
                    dataSetPolice.put("post_type", postType);
                    dataSetPolice.put("mobile", mobile);
                    dataSetPolice.put("search_data", search_data);
                    dataSetPolice.put("userId", userKey);
                    dataSetPolice.put("dataType", dataType);
                    dataSetPolice.put("timestamp", timeStamp);
                    databaseReference_police.child(userKey).setValue(dataSetPolice);
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

    private void getDataFromFirebase() {
        policeBinding.loadingProgressLottieViewPolice.setVisibility(View.VISIBLE);
        databaseReference_police.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList_police.clear();
                policeBinding.loadingProgressLottieViewPolice.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelList_police.add(commonModel_hospital);
                }
                if (modelList_police.isEmpty()) {
                    policeBinding.loadingProgressLottieViewPolice.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapterCommon_police.notifyDataSetChanged();
                policeBinding.policeAdminRcv.setHasFixedSize(true);
                policeBinding.policeAdminRcv.setAdapter(adapterCommon_police);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}