package com.kawsar.eseba_chandpur.views.fragments.admin_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kawsar.eseba_chandpur.adapters.frag_tab_adapter_admin.NewspaperFragAdapterAdmin;
import com.kawsar.eseba_chandpur.databinding.FragmentNewspaperBinding;

public class NewspaperFragment extends Fragment {

    FragmentNewspaperBinding newspaperBinding;
    String[] news_category_listAdmin = new String[]{"Chandpur News", "Bangla", "English", "International", "Education",
            "Sports", "Blogs"};
    NewspaperFragAdapterAdmin newspaperFragAdapterAdmin;

    public NewspaperFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        newspaperBinding = FragmentNewspaperBinding.inflate(inflater,
                container, false);

        newspaperBinding.backBtnAdminNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        newspaperFragAdapterAdmin = new NewspaperFragAdapterAdmin(getActivity());
        newspaperBinding.viewPagerNewsAdmin.setAdapter(newspaperFragAdapterAdmin);
        new TabLayoutMediator(
                newspaperBinding.tabLayoutNewsAdmin, newspaperBinding
                .viewPagerNewsAdmin, ((tab, position) -> tab.setText(news_category_listAdmin[position]))
        ).attach();


        return newspaperBinding.getRoot();

    }
}