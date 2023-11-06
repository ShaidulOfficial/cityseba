package com.kawsar.eseba_chandpur.views.fragments.user_fragments.paribahan;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kawsar.eseba_chandpur.Constant;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.databinding.FragmentRailUserBinding;
import com.kawsar.eseba_chandpur.views.activities.Web_browser;


public class RailUserFragment extends Fragment {

   FragmentRailUserBinding railUserBinding;
    public RailUserFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
     railUserBinding=FragmentRailUserBinding.inflate(inflater,container,false);

     railUserBinding.railBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Web_browser.WEBSITE_LINK = Constant.rail;
             Web_browser.WEBSITE_TITLE = "রেল টিকেট";
             Intent myIntent = new Intent(getContext(), Web_browser.class);
             startActivity(myIntent);
         }
     });
     return  railUserBinding.getRoot();
    }
}