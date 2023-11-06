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
import com.kawsar.eseba_chandpur.databinding.FragmentHotelBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HotelFragment extends Fragment {

    FragmentHotelBinding hotelBinding;
    AdapterCommon adapterCommon_hotel;
    DatabaseReference databaseReference_hotel;
    List<CommonModel> modelList_hotel = new ArrayList<>();
    long timeStamp;

    public HotelFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hotelBinding = FragmentHotelBinding.inflate(inflater, container,
                false);
        databaseReference_hotel = FirebaseDatabase.getInstance()
                .getReference("Admin").child("Hotels");
        databaseReference_hotel.keepSynced(true);
        getDataFromFirebase();
        adapterCommon_hotel = new AdapterCommon(getContext(), modelList_hotel);
        adapterCommon_hotel.notifyDataSetChanged();
        hotelBinding.floatBtnHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });
        hotelBinding.backBtnAdminHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        hotelBinding.hotelAdminSearchV.clearFocus();
        hotelBinding.hotelAdminSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        return hotelBinding.getRoot();
    }

    private void filterList(String newText) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : modelList_hotel) {
            if (model.getSearch_data().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(model);

            }
        }
        if (filterlist.isEmpty()) {

            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterCommon_hotel.setFilterList_admin(filterlist);
        }
    }


    private void getDataFromFirebase() {
        hotelBinding.loadingProgressLottieViewHotel.setVisibility(View.VISIBLE);
        databaseReference_hotel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList_hotel.clear();
                hotelBinding.loadingProgressLottieViewHotel.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {
                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelList_hotel.add(commonModel_hospital);
                }
                if (modelList_hotel.isEmpty()) {
                    hotelBinding.loadingProgressLottieViewHotel.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {
                }
                adapterCommon_hotel.notifyDataSetChanged();
                hotelBinding.hotelAdminRcv.setHasFixedSize(true);
                hotelBinding.hotelAdminRcv.setAdapter(adapterCommon_hotel);
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
        AppCompatButton submit_btn = custom_dialog.findViewById(R.id.submit_btn);
        AppCompatButton cancel_btn = custom_dialog.findViewById(R.id.cancel_btn);
        TextView head_title_tv = custom_dialog.findViewById(R.id.head_title_tv);
        head_title_tv.setText("হোটেলের তথ্য দিন");
        custom_dialog.setCancelable(false);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name_edt = custom_dialog.findViewById(R.id.name_edt);
                EditText location_edt = custom_dialog.findViewById(R.id.location_edt);
                EditText mobile_edt = custom_dialog.findViewById(R.id.mobile_edt);

                timeStamp = System.currentTimeMillis();
                String dataType = "Hotels";
                String userKey = databaseReference_hotel.push().getKey();
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

                                HashMap<String, Object> dataSetMapHotel = new HashMap<>();
                                dataSetMapHotel.put("name", name);
                                dataSetMapHotel.put("location", location);
                                dataSetMapHotel.put("mobile", mobile);
                                dataSetMapHotel.put("search_data", search_data);
                                dataSetMapHotel.put("userId", userKey);
                                dataSetMapHotel.put("timestamp", timeStamp);
                                dataSetMapHotel.put("dataType", dataType);
                                databaseReference_hotel.child(userKey).setValue(dataSetMapHotel);
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