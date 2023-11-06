package com.kawsar.eseba_chandpur.views.fragments.user_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.adapters.frag_tab_adapter_user.NewspaperFragAdapterUser;
import com.kawsar.eseba_chandpur.adapters.frag_tab_adapter_user.ParibahanFragAdapterUser;
import com.kawsar.eseba_chandpur.databinding.FragmentParibahanUserBinding;

public class ParibahanUserFragment extends Fragment {
    FragmentParibahanUserBinding paribahanBinding;
    ParibahanFragAdapterUser paribahanFragAdapterUser;
    String[] paribahan_category_listUser = {"বাস", "লঞ্চ", "রেলওয়ে"};

    public ParibahanUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        paribahanBinding = FragmentParibahanUserBinding.inflate(inflater
                , container, false);
        paribahanFragAdapterUser = new ParibahanFragAdapterUser(getActivity());
        paribahanBinding.viewPagerParibahanUser.setAdapter(paribahanFragAdapterUser);
        new TabLayoutMediator(paribahanBinding.tabLayoutParibahanUser,
                paribahanBinding.viewPagerParibahanUser,
                (tab, position) -> tab.setText(paribahan_category_listUser[position])).attach();

        return paribahanBinding.getRoot();
    }
}