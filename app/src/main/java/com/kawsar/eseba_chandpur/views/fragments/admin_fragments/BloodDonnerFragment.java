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
import android.widget.ArrayAdapter;
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
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterBlood;
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterCommon;
import com.kawsar.eseba_chandpur.databinding.FragmentBloodDonnerBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BloodDonnerFragment extends Fragment {

    FragmentBloodDonnerBinding bloodDonnerBinding;
    AdapterBlood adapterCommonBlood;
    DatabaseReference databaseReference_bloodDonor;
    List<CommonModel> modelList_blood = new ArrayList<>();
    long timeStamp;

    public BloodDonnerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bloodDonnerBinding = FragmentBloodDonnerBinding.inflate(inflater,
                container, false);
        adapterCommonBlood = new AdapterBlood(getContext(), modelList_blood);
        adapterCommonBlood.notifyDataSetChanged();
        databaseReference_bloodDonor = FirebaseDatabase.getInstance().getReference("Admin").child("BloodDonors");
        databaseReference_bloodDonor.keepSynced(true);
        getDataFromFirebase();
        bloodDonnerBinding.bloodDonorAdminSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<CommonModel> filterlist = new ArrayList<>();
                for (CommonModel model : modelList_blood) {
                    if (model.getName().toLowerCase().contains(newText.toLowerCase())) {
                        filterlist.add(model);
                    }
                }
                if (filterlist.isEmpty()) {
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {
                    adapterCommonBlood.setFilterList_adminblood(filterlist);
                }
                return true;
            }
        });


// ******************** back button action  ***************************
        bloodDonnerBinding.backBtnBloodDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        bloodDonnerBinding.floatBtnBloodDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        return bloodDonnerBinding.getRoot();
    }

    private void getDataFromFirebase() {
        bloodDonnerBinding.loadingViewBloodDonor.setVisibility(View.VISIBLE);
        databaseReference_bloodDonor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList_blood.clear();
                bloodDonnerBinding.loadingViewBloodDonor.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {

                    CommonModel commonModel_blood = ds.getValue(CommonModel.class);
                    modelList_blood.add(commonModel_blood);

                }
                if (modelList_blood.isEmpty()) {
                    bloodDonnerBinding.loadingViewBloodDonor.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapterCommonBlood.notifyDataSetChanged();
                bloodDonnerBinding.bloodDonorAdminRcv.setHasFixedSize(true);
                bloodDonnerBinding.bloodDonorAdminRcv.setAdapter(adapterCommonBlood);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDialog() {
        Dialog custom_dialog = new Dialog(getActivity());
        custom_dialog.setContentView(R.layout.custom_insert_dialog_box_blood_donner);
        custom_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        AppCompatButton submit_btn = custom_dialog.findViewById(R.id.submit_btn_blood);
        AppCompatButton cancel_btn = custom_dialog.findViewById(R.id.cancel_btn_blood);
        TextView head_title_tv = custom_dialog.findViewById(R.id.head_title_blood_tv);
        head_title_tv.setText("নতুন তথ্য দিন");

        custom_dialog.setCancelable(false);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name_edt = custom_dialog.findViewById(R.id.name_edt_blood);
                EditText fbPage_edt = custom_dialog.findViewById(R.id.fbPage_edt_blood);
                EditText webUrl_edt = custom_dialog.findViewById(R.id.webUrl_edt_blood);
                timeStamp = System.currentTimeMillis();
                String dataType="BloodDonors";
                String userKey = databaseReference_bloodDonor.push().getKey();
                String name = name_edt.getText().toString().trim();
                String fb_page = fbPage_edt.getText().toString().trim();
                String website = webUrl_edt.getText().toString().trim();

                if (name.isEmpty()) {
                    name_edt.setError("Name required!");
                } else if (fb_page.isEmpty()) {
                    fbPage_edt.setError("Facebook Link required!");
                } else if (website.isEmpty()) {
                    webUrl_edt.setError("Web Link required!");
                } else {
                    // ***=====================================================================================***
// ********************************** Data Insert here........ **********************************

                    HashMap<String, Object> dataSetMapAmbulance = new HashMap<>();
                    dataSetMapAmbulance.put("name", name);
                    dataSetMapAmbulance.put("fbPageUrl", fb_page);
                    dataSetMapAmbulance.put("webUrl", website);
                    dataSetMapAmbulance.put("userId", userKey);
                    dataSetMapAmbulance.put("dataType", dataType);
                    dataSetMapAmbulance.put("timestamp", timeStamp);
                    databaseReference_bloodDonor.child(userKey).setValue(dataSetMapAmbulance);
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
        custom_dialog.create();
        custom_dialog.show();


    }

}