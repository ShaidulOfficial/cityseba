package com.kawsar.eseba_chandpur.views.fragments.admin_fragments.paribahan;

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
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterParibahan;
import com.kawsar.eseba_chandpur.databinding.FragmentShipBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShipFragment extends Fragment {
    FragmentShipBinding shipBinding;
    AdapterParibahan adapterParibahan;
    DatabaseReference dbRef_paribahan;
    List<CommonModel> modelList_paribahan = new ArrayList<>();
    long timeStamp;

    public ShipFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        shipBinding = FragmentShipBinding.inflate(inflater, container, false);
        dbRef_paribahan = FirebaseDatabase.getInstance()
                .getReference("Admin").child("Ship");
        dbRef_paribahan.keepSynced(true);
        getDataFromFirebase();
        adapterParibahan = new AdapterParibahan(getContext(), modelList_paribahan);
        adapterParibahan.notifyDataSetChanged();
        shipBinding.floatBtnShip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });
        shipBinding.shipSearchV.clearFocus();
        shipBinding.shipSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<CommonModel> filterlist = new ArrayList<>();
                for (CommonModel model : modelList_paribahan) {
                    if (model.getSearch_data().toLowerCase().contains(newText.toLowerCase())) {
                        filterlist.add(model);
                    } else {
                    }
                }
                if (filterlist.isEmpty()) {

                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {
                    adapterParibahan.setFilterList_Paribahan_admin(filterlist);
                }
                return true;
            }
        });

        return shipBinding.getRoot();
    }

    private void getDataFromFirebase() {
        dbRef_paribahan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList_paribahan.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelList_paribahan.add(commonModel_hospital);
                }
                if (modelList_paribahan.isEmpty()) {
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {
                }
                adapterParibahan.notifyDataSetChanged();
                shipBinding.rcvShipAdmin.setHasFixedSize(true);
                shipBinding.rcvShipAdmin.setAdapter(adapterParibahan);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void show_dialog() {
        Dialog custom_dialog = new Dialog(getContext());
        custom_dialog.setContentView(R.layout.custom_insert_dialog_paribahan);
        custom_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        AppCompatButton submit_btn = custom_dialog.findViewById(R.id.submit_btnParibahan);
        AppCompatButton cancel_btn = custom_dialog.findViewById(R.id.cancel_btnParibahan);
        TextView head_title_tv = custom_dialog.findViewById(R.id.head_title_tvParibahan);
        head_title_tv.setText("পরিবহন সেবা তথ্য দিন");
        custom_dialog.setCancelable(false);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name_edt = custom_dialog.findViewById(R.id.name_edtParibahan);
                EditText location_edt = custom_dialog.findViewById(R.id.location_edtParibahan);
                EditText mobile_edt = custom_dialog.findViewById(R.id.mobile_edtParibahan);
                EditText journey_edt = custom_dialog.findViewById(R.id.journeyStart_edtParibahan);

                timeStamp = System.currentTimeMillis();
                String dataType = "Ship";
                String userKey = dbRef_paribahan.push().getKey();
                String name = name_edt.getText().toString().trim();
                String location = location_edt.getText().toString().trim();
                String mobile = mobile_edt.getText().toString().trim();
                String journey = journey_edt.getText().toString().trim();
                String search_data = name.toLowerCase() + "," + location.toLowerCase() + "," + mobile.toLowerCase();
                if (!name.isEmpty()) {
                    name_edt.setError(null);
                    if (!location.isEmpty()) {
                        location_edt.setError(null);
                        if (!journey.isEmpty()) {
                            journey_edt.setError(null);
                            if (!mobile.isEmpty()) {
                                mobile_edt.setError(null);
                                if (mobile.length() != 11) {
                                    mobile_edt.setError("Invalid Number");
                                } else {
// ***=====================================================================================***
// ********************************** Data Insert here........ **********************************

                                    HashMap<String, Object> dataSet = new HashMap<>();
                                    dataSet.put("name", name);
                                    dataSet.put("location", location);
                                    dataSet.put("mobile", mobile);
                                    dataSet.put("message", journey);
                                    dataSet.put("search_data", search_data);
                                    dataSet.put("userId", userKey);
                                    dataSet.put("timestamp", timeStamp);
                                    dataSet.put("dataType", dataType);
                                    dbRef_paribahan.child(userKey).setValue(dataSet);
                                    custom_dialog.dismiss();


                                }
                            } else {
                                mobile_edt.setError("Mobile number required!");
                            }
                        } else {
                            journey_edt.setError("Journey start required!");

                        }
                    } else {
                        location_edt.setError("Location required!");
                    }
                } else {
                    name_edt.setError("Paribahan Name required!");
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