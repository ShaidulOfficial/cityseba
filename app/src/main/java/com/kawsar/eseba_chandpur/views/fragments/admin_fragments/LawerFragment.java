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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterCommon;
import com.kawsar.eseba_chandpur.databinding.FragmentLawerBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LawerFragment extends Fragment {
    FragmentLawerBinding lawerBinding;
    AdapterCommon adapterCommon_lawer;
    DatabaseReference databaseReference_lawer, dbRef_lawer;
    List<CommonModel> modelList_lawer = new ArrayList<>();
    long timeStamp;

    public LawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lawerBinding = FragmentLawerBinding.inflate(inflater, container,
                false);
        databaseReference_lawer = FirebaseDatabase.getInstance().getReference("Admin").child("Lawyers");
        databaseReference_lawer.keepSynced(true);
        dbRef_lawer = FirebaseDatabase.getInstance().getReference("Admin").child("LawyerWebLink");
        dbRef_lawer.keepSynced(true);
        getDataFromFirebase();
        adapterCommon_lawer = new AdapterCommon(getContext(), modelList_lawer);
        adapterCommon_lawer.notifyDataSetChanged();
        lawerBinding.floatBtnLawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });
        lawerBinding.backBtnAdminLawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        lawerBinding.addWebLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog custom_dialogWeb = new Dialog(getContext());
                custom_dialogWeb.setContentView(R.layout.custom_insert_lawer_web);
                custom_dialogWeb.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                custom_dialogWeb.create();
                custom_dialogWeb.setCancelable(false);
                AppCompatButton submit_btn = custom_dialogWeb.findViewById(R.id.submit_btnWeb);
                AppCompatButton cancel_btn = custom_dialogWeb.findViewById(R.id.cancel_btnWeb);
                submit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText name_edt = custom_dialogWeb.findViewById(R.id.webname_edt);
                        String web_name = name_edt.getText().toString().trim();
                        if (web_name.isEmpty()) {
                            name_edt.setError("Please fill the WebSite");
                        } else {
                            // ********************************** Data Insert here........ **********************************
                            HashMap<String, Object> dataSetLawyerWeb = new HashMap<>();
                            dataSetLawyerWeb.put("webUrl", web_name);
                            dbRef_lawer.setValue(dataSetLawyerWeb).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getContext(), "Link submit done!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            custom_dialogWeb.dismiss();
                        }
                    }
                });
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        custom_dialogWeb.dismiss();
                    }
                });
                custom_dialogWeb.show();
            }
        });
        lawerBinding.webLinkLawyerAdmin.setSelected(true);
        dbRef_lawer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    lawerBinding.webLinkLawyerAdmin.setText(snapshot.child("webUrl")
                            .getValue().toString().trim());
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        lawerBinding.lawerAdminSearchV.clearFocus();
        lawerBinding.lawerAdminSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        return lawerBinding.getRoot();
    }


    private void filterList(String newText) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : modelList_lawer) {
            if (model.getSearch_data().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(model);

            }
        }
        if (filterlist.isEmpty()) {

            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterCommon_lawer.setFilterList_admin(filterlist);
        }
    }


    private void getDataFromFirebase() {
        lawerBinding.loadingProgressLottieViewLawer.setVisibility(View.VISIBLE);
        databaseReference_lawer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList_lawer.clear();
                lawerBinding.loadingProgressLottieViewLawer.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {

                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelList_lawer.add(commonModel_hospital);

                }
                if (modelList_lawer.isEmpty()) {
                    lawerBinding.loadingProgressLottieViewLawer.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapterCommon_lawer.notifyDataSetChanged();
                lawerBinding.lawerAdminRcv.setHasFixedSize(true);
                lawerBinding.lawerAdminRcv.setAdapter(adapterCommon_lawer);
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
        head_title_tv.setText("আইনজীবিদের সঠিক তথ্য দিন");
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name_edt = custom_dialog.findViewById(R.id.name_edt);
                EditText location_edt = custom_dialog.findViewById(R.id.location_edt);
                EditText mobile_edt = custom_dialog.findViewById(R.id.mobile_edt);

                timeStamp = System.currentTimeMillis();
                String dataType = "Lawyers";
                String userKey_lawyer = databaseReference_lawer.push().getKey();
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
// ***=====================================================================================***
// ********************************** Data Insert here........ **********************************

                                HashMap<String, Object> dataSetLawyer = new HashMap<>();
                                dataSetLawyer.put("name", name);
                                dataSetLawyer.put("location", location);
                                dataSetLawyer.put("mobile", mobile);
                                dataSetLawyer.put("search_data", search_data);
                                dataSetLawyer.put("userId", userKey_lawyer);
                                dataSetLawyer.put("timestamp", timeStamp);
                                dataSetLawyer.put("dataType", dataType);
                                databaseReference_lawer.child(userKey_lawyer).setValue(dataSetLawyer);
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