package com.kawsar.eseba_chandpur.adapters.frag_tab_adapter_admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kawsar.eseba_chandpur.views.fragments.admin_fragments.newspaper.BanglaNewsFragment;
import com.kawsar.eseba_chandpur.views.fragments.admin_fragments.newspaper.BlogsNewsFragment;
import com.kawsar.eseba_chandpur.views.fragments.admin_fragments.newspaper.ChandpurNewsFragment;
import com.kawsar.eseba_chandpur.views.fragments.admin_fragments.newspaper.EducationNewsFragment;
import com.kawsar.eseba_chandpur.views.fragments.admin_fragments.newspaper.EnglishNewsFragment;
import com.kawsar.eseba_chandpur.views.fragments.admin_fragments.newspaper.InternationalNewsFragment;
import com.kawsar.eseba_chandpur.views.fragments.admin_fragments.newspaper.JobsNewsFragment;
import com.kawsar.eseba_chandpur.views.fragments.admin_fragments.newspaper.SportsNewsFragment;

public class NewspaperFragAdapterAdmin extends FragmentStateAdapter {
    String[] news_category_list = {"Chandpur News", "Bangla", "English", "International", "Education",
            "Sports", "Blogs"};

    public NewspaperFragAdapterAdmin(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ChandpurNewsFragment();
            case 1:
                return new BanglaNewsFragment();
            case 2:
                return new EnglishNewsFragment();
            case 3:
                return new InternationalNewsFragment();
            case 4:
                return new EducationNewsFragment();
            case 5:
                return new SportsNewsFragment();
            case 6:
                return new BlogsNewsFragment();
        }
        return new ChandpurNewsFragment();
    }

    @Override
    public int getItemCount() {
        return news_category_list.length;
    }

}
