package com.kawsar.eseba_chandpur.adapters.frag_tab_adapter_user;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kawsar.eseba_chandpur.views.fragments.user_fragments.newspaper_user.BanglaNewsUserFragment;
import com.kawsar.eseba_chandpur.views.fragments.user_fragments.newspaper_user.BlogsNewsUserFragment;
import com.kawsar.eseba_chandpur.views.fragments.user_fragments.newspaper_user.ChandpurNewsUserFragment;
import com.kawsar.eseba_chandpur.views.fragments.user_fragments.newspaper_user.EducationNewsUserFragment;
import com.kawsar.eseba_chandpur.views.fragments.user_fragments.newspaper_user.EnglishNewsUserFragment;
import com.kawsar.eseba_chandpur.views.fragments.user_fragments.newspaper_user.InternationalNewsUserFragment;
import com.kawsar.eseba_chandpur.views.fragments.user_fragments.newspaper_user.JobsNewsUserFragment;
import com.kawsar.eseba_chandpur.views.fragments.user_fragments.newspaper_user.SportsNewsUserFragment;

public class NewspaperFragAdapterUser extends FragmentStateAdapter {
    String[] news_category_listUser = {"Chandpur News", "Bangla", "English", "International", "Education",
            "Sports", "Blogs"};

    public NewspaperFragAdapterUser(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ChandpurNewsUserFragment();
            case 1:
                return new BanglaNewsUserFragment();
            case 2:
                return new EnglishNewsUserFragment();
            case 3:
                return new InternationalNewsUserFragment();
            case 4:
                return new EducationNewsUserFragment();
            case 5:
                return new SportsNewsUserFragment();
            case 6:
                return new BlogsNewsUserFragment();
        }
        return new ChandpurNewsUserFragment();
    }

    @Override
    public int getItemCount() {
        return news_category_listUser.length;
    }

}
