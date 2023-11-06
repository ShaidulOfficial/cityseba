package com.kawsar.eseba_chandpur.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.views.fragments.admin_fragments.AdminMenuFragment;

public class AdminDashboardActivity extends AppCompatActivity {

    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        fragmentManager = getSupportFragmentManager();
        Fragment admin_dashboard_fragment = new AdminMenuFragment();
        FragmentTransaction frag_transaction = fragmentManager.beginTransaction();
        frag_transaction.replace(R.id.framlayout_admin_dashboard, admin_dashboard_fragment).commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}