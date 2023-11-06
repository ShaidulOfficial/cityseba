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
import com.kawsar.eseba_chandpur.adapters.admin_adapters.AdapterJournal;
import com.kawsar.eseba_chandpur.databinding.FragmentJournalBinding;
import com.kawsar.eseba_chandpur.models.CommonModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class JournalFragment extends Fragment {

    FragmentJournalBinding journalBinding;
    AdapterJournal adapterCommon_journal;
    DatabaseReference databaseReference_journal;
    List<CommonModel> modelList_journal = new ArrayList<>();
    long timeStamp;

    public JournalFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        journalBinding = FragmentJournalBinding.inflate(inflater, container,
                false);
        databaseReference_journal = FirebaseDatabase.getInstance().getReference("Admin").child("Journals");
        getDataFromFirebase();
        adapterCommon_journal = new AdapterJournal(getContext(), modelList_journal);
        adapterCommon_journal.notifyDataSetChanged();
        journalBinding.floatBtnJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_dialog();
            }
        });
        journalBinding.backBtnAdminJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        journalBinding.journalAdminSearchV.clearFocus();
        journalBinding.journalAdminSearchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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


        return journalBinding.getRoot();
    }

    private void show_dialog() {
        Dialog custom_dialog = new Dialog(getContext());
        custom_dialog.setContentView(R.layout.custom_insert_dialog_journal);
        custom_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        custom_dialog.create();
        custom_dialog.setCancelable(false);
        AppCompatButton submit_btn = custom_dialog.findViewById(R.id.submit_btn_journal);
        AppCompatButton cancel_btn = custom_dialog.findViewById(R.id.cancel_btn_journal);
        TextView head_title_tv = custom_dialog.findViewById(R.id.head_titleTv_journal);
        head_title_tv.setText("Add New Journal");

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name_edt = custom_dialog.findViewById(R.id.name_edt_journal);
                EditText channelName_edt = custom_dialog.findViewById(R.id.channelName_edt_journal);
                EditText channelLink_edt = custom_dialog.findViewById(R.id.channelLink_edt_journal);
                EditText mobile_edt = custom_dialog.findViewById(R.id.mobile_edt_journal);

                timeStamp = System.currentTimeMillis();
                String dataType = "Journals";
                String userKey_journal = databaseReference_journal.push().getKey();
                String name = name_edt.getText().toString().trim();
                String channelName = channelName_edt.getText().toString().trim();
                String channelLink = channelLink_edt.getText().toString().trim();
                String mobile = mobile_edt.getText().toString().trim();
                String search_data = name.toLowerCase() + "," + channelName.toLowerCase() + "," + mobile.toLowerCase();

                if (TextUtils.isEmpty(name)) {
                    name_edt.setError("Name is required");
                } else if (TextUtils.isEmpty(channelName)) {
                    channelName_edt.setError("Channel name required");
                } else if (TextUtils.isEmpty(channelLink)) {
                    channelLink_edt.setError("Channel link required");
                } else if (TextUtils.isEmpty(mobile)) {
                    mobile_edt.setError("Mobile is required");
                } else if (mobile.length() != 11) {
                    mobile_edt.setError("Must be 11 digits ");
                } else {
                    HashMap<String, Object> dataSetJournal = new HashMap<>();
                    dataSetJournal.put("name", name);
                    dataSetJournal.put("channelName", channelName);
                    dataSetJournal.put("webUrl", channelLink);
                    dataSetJournal.put("mobile", mobile);
                    dataSetJournal.put("search_data", search_data);
                    dataSetJournal.put("userId", userKey_journal);
                    dataSetJournal.put("timestamp", timeStamp);
                    dataSetJournal.put("dataType", dataType);
                    databaseReference_journal.child(userKey_journal).setValue(dataSetJournal);
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
        journalBinding.loadingProgressLottieViewJournal.setVisibility(View.VISIBLE);
        databaseReference_journal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList_journal.clear();
                journalBinding.loadingProgressLottieViewJournal.setVisibility(View.GONE);
                for (DataSnapshot ds : snapshot.getChildren()) {

                    CommonModel commonModel_hospital = ds.getValue(CommonModel.class);
                    modelList_journal.add(commonModel_hospital);

                }
                if (modelList_journal.isEmpty()) {
                    journalBinding.loadingProgressLottieViewJournal.setVisibility(View.VISIBLE);
                    MyApplication.dialogShow(getActivity());
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                } else {

                }
                adapterCommon_journal.notifyDataSetChanged();
                journalBinding.journalAdminRcv.setHasFixedSize(true);
                journalBinding.journalAdminRcv.setAdapter(adapterCommon_journal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterList(String newText) {
        List<CommonModel> filterlist = new ArrayList<>();
        for (CommonModel model : modelList_journal) {
            if (model.getSearch_data().toLowerCase().contains(newText.toLowerCase())) {
                filterlist.add(model);

            }
        }
        if (filterlist.isEmpty()) {

            Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        } else {
            adapterCommon_journal.setFilterList_journal(filterlist);
        }
    }


}