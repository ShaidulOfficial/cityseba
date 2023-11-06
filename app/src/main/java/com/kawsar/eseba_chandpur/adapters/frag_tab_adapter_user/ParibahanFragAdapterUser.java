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
import com.kawsar.eseba_chandpur.views.fragments.user_fragments.paribahan.BusUserFragment;
import com.kawsar.eseba_chandpur.views.fragments.user_fragments.paribahan.RailUserFragment;
import com.kawsar.eseba_chandpur.views.fragments.user_fragments.paribahan.ShipUserFragment;

public class ParibahanFragAdapterUser extends FragmentStateAdapter {
    String[] paribahan_category_listUser = {"বাস", "লঞ্চ", "রেলওয়ে"};

    public ParibahanFragAdapterUser(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BusUserFragment();
            case 1:
                return new ShipUserFragment();
            case 2:
                return new RailUserFragment();
                    }
        return new BusUserFragment();
    }

    @Override
    public int getItemCount() {
        return paribahan_category_listUser.length;
    }
}
