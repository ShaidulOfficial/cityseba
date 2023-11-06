package com.kawsar.eseba_chandpur.adapters.frag_tab_adapter_admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kawsar.eseba_chandpur.views.fragments.admin_fragments.paribahan.BusFragment;
import com.kawsar.eseba_chandpur.views.fragments.admin_fragments.paribahan.RailFragment;
import com.kawsar.eseba_chandpur.views.fragments.admin_fragments.paribahan.ShipFragment;
import com.kawsar.eseba_chandpur.views.fragments.user_fragments.paribahan.BusUserFragment;
import com.kawsar.eseba_chandpur.views.fragments.user_fragments.paribahan.RailUserFragment;
import com.kawsar.eseba_chandpur.views.fragments.user_fragments.paribahan.ShipUserFragment;

public class ParibahanFragAdapterAdmin extends FragmentStateAdapter {
    String[] paribahan_category_Admin = {"বাস", "লঞ্চ", "রেলওয়ে"};

    public ParibahanFragAdapterAdmin(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BusFragment();
            case 1:
                return new ShipFragment();
            case 2:
                return new RailFragment();
        }
        return new BusFragment();
    }

    @Override
    public int getItemCount() {
        return paribahan_category_Admin.length;
    }
}
