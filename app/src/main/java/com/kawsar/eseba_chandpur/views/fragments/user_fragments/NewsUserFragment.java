package com.kawsar.eseba_chandpur.views.fragments.user_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kawsar.eseba_chandpur.adapters.frag_tab_adapter_user.NewspaperFragAdapterUser;
import com.kawsar.eseba_chandpur.databinding.FragmentNewsUserBinding;


public class NewsUserFragment extends Fragment {


    FragmentNewsUserBinding newsUserBinding;
    NewspaperFragAdapterUser newspaperFragAdapterUser;
    String[] news_category_list_user = new String[]{"Chandpur News", "Bangla", "English", "International", "Education",
            "Sports", "Blogs"};

    public NewsUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        newsUserBinding = FragmentNewsUserBinding.inflate(inflater, container, false);


        newspaperFragAdapterUser = new NewspaperFragAdapterUser(getActivity());
        newsUserBinding.viewPagerNewsUser.setAdapter(newspaperFragAdapterUser);
        new TabLayoutMediator(newsUserBinding.tabLayoutNewsUser,
                newsUserBinding.viewPagerNewsUser,
                (tab, position) -> tab.setText(news_category_list_user[position])).attach();

        return newsUserBinding.getRoot();
    }
}