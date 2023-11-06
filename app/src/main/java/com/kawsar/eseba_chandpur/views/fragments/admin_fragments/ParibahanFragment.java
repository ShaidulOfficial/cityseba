package com.kawsar.eseba_chandpur.views.fragments.admin_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.adapters.frag_tab_adapter_admin.ParibahanFragAdapterAdmin;
import com.kawsar.eseba_chandpur.adapters.frag_tab_adapter_user.ParibahanFragAdapterUser;
import com.kawsar.eseba_chandpur.databinding.FragmentParibahanBinding;

public class ParibahanFragment extends Fragment {
FragmentParibahanBinding paribahanBinding;
    ParibahanFragAdapterAdmin paribahanFragAdapterAdmin;
    String[] paribahan_category_AdminList = {"বাস", "লঞ্চ", "রেলওয়ে"};
    public ParibahanFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        paribahanBinding=FragmentParibahanBinding.inflate(inflater,container,false);
        paribahanBinding.backBtnParibahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        paribahanFragAdapterAdmin = new ParibahanFragAdapterAdmin(getActivity());
        paribahanBinding.viewPagerParibahanAdmin.setAdapter(paribahanFragAdapterAdmin);
        new TabLayoutMediator(paribahanBinding.tabLayAdmin,
                paribahanBinding.viewPagerParibahanAdmin,
                (tab, position) -> tab.setText(paribahan_category_AdminList[position])).attach();

        return paribahanBinding.getRoot();
    }
}