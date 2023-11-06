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
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterCommon;
import com.kawsar.eseba_chandpur.databinding.FragmentPolliBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PolliFragment extends Fragment {
    FragmentPolliBinding polliBinding;
    AdapterCommon adapterCommon_polli;
    DatabaseReference databaseReference_polli;
    List<CommonModel> modelList_polli = new ArrayList<>();
    long timeStamp;

    public PolliFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        polliBinding = FragmentPolliBinding.inflate(inflater, container,
                false);
        databaseReference_polli = FirebaseDatabase.getInstance().getReference("Admin").child("PolliBiddut");
        getDataFromFirebase();
        adapterCommon_polli = new AdapterCommon(getContext(), modelList_polli);
        adapterCommon_polli.notifyDataSetChanged();
        polliBinding.floatBtnPolli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });
        polliBinding.backBtnAdminPolli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        polliBinding.polliAdminSearchV.clearFocus();
        polliBinding.polliAdminSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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


        return polliBinding.getRoot();
    }

    private void filterList(String newText) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : modelList_polli) {
            if (model.getSearch_data().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(model);

            }
        }
        if (filterlist.isEmpty()) {

            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterCommon_polli.setFilterList_admin(filterlist);
        }
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
        head_title_tv.setText("পল্লী বিদ্যুৎ নতুন তথ্য দিন");

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name_edt = custom_dialog.findViewById(R.id.name_edt);
                EditText location_edt = custom_dialog.findViewById(R.id.location_edt);
                EditText mobile_edt = custom_dialog.findViewById(R.id.mobile_edt);
                timeStamp = System.currentTimeMillis();
                String dataType = "PolliBiddut";
                String userKey = databaseReference_polli.push().getKey();
                String name = name_edt.getText().toString().trim();
                String location = location_edt.getText().toString().trim();
                String mobile = mobile_edt.getText().toString().trim();
                String search_data = name.toLowerCase() + "," + location.toLowerCase() + "," + mobile.toLowerCase();

                if (TextUtils.isEmpty(name)) {
                    name_edt.setError("Name is required");
                } else if (TextUtils.isEmpty(location)) {
                    location_edt.setError("Location is required");
                } else if (TextUtils.isEmpty(mobile)) {
                    mobile_edt.setError("Mobile is required");
                } else if (mobile.length() != 11) {
                    mobile_edt.setError("Must be 11 digits ");
                } else {
                    HashMap<String, Object> dataSetPolli = new HashMap<>();
                    dataSetPolli.put("name", name);
                    dataSetPolli.put("location", location);
                    dataSetPolli.put("mobile", mobile);
                    dataSetPolli.put("search_data", search_data);
                    dataSetPolli.put("userId", userKey);
                    dataSetPolli.put("timestamp", timeStamp);
                    dataSetPolli.put("dataType", dataType);
                    databaseReference_polli.child(userKey).setValue(dataSetPolli);
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
        polliBinding.loadingProgressLottieViewPolli.setVisibility(View.VISIBLE);
        databaseReference_polli.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList_polli.clear();
                polliBinding.loadingProgressLottieViewPolli.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {

                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelList_polli.add(commonModel_hospital);

                }
                if (modelList_polli.isEmpty()) {
                    polliBinding.loadingProgressLottieViewPolli.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapterCommon_polli.notifyDataSetChanged();
                polliBinding.polliAdminRcv.setHasFixedSize(true);
                polliBinding.polliAdminRcv.setAdapter(adapterCommon_polli);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}