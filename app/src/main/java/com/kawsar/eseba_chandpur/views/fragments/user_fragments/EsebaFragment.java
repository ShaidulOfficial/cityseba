package com.kawsar.eseba_chandpur.views.fragments.user_fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kawsar.eseba_chandpur.Constant;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.databinding.FragmentEsebaBinding;
import com.kawsar.eseba_chandpur.views.activities.Web_browser;


public class EsebaFragment extends Fragment {
    FragmentEsebaBinding esebaBinding;
    public EsebaFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        esebaBinding = FragmentEsebaBinding.inflate(inflater, container,
                false);

        esebaBinding.backBtnAdminEseba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        esebaBinding.passportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passportDialogShow();
            }
        });
        esebaBinding.nidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nidDialogShow();
            }
        });
        esebaBinding.birthRegiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthRegistrationDialogShow();
            }
        });
        esebaBinding.sokolSonodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_LINK = Constant.sokolShonod;
                Web_browser.WEBSITE_TITLE = "সকল সনদ";
                startActivity(new Intent(getContext(), Web_browser.class));
            }
        });
        esebaBinding.covidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_LINK = Constant.covid19;
                Web_browser.WEBSITE_TITLE = "কোভিড-১৯";
                startActivity(new Intent(getContext(), Web_browser.class));
            }
        });
        esebaBinding.abasonBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_LINK = Constant.abasonOdidoptor;
                Web_browser.WEBSITE_TITLE = "আবাসন অধিদপ্তর";
                startActivity(new Intent(getContext(), Web_browser.class));
            }
        });
        esebaBinding.voktaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_LINK = Constant.vokta;
                Web_browser.WEBSITE_TITLE = "ভোক্তা অধিকারন";
                startActivity(new Intent(getContext(), Web_browser.class));
            }
        });

        esebaBinding.khotiyanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_LINK = Constant.khotiyanReserch;
                Web_browser.WEBSITE_TITLE = "খতিয়ান অনুসন্ধান";
                startActivity(new Intent(getContext(), Web_browser.class));
            }
        });


        return esebaBinding.getRoot();
    }

    private void nidDialogShow() {
        Dialog nidDialog = new Dialog(getActivity());
        nidDialog.setContentView(R.layout.custom_nid_dialog);
        nidDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        nidDialog.setCancelable(false);
        CardView nidAppply=nidDialog.findViewById(R.id.nidApplyBtn);
        CardView nidCorrection=nidDialog.findViewById(R.id.nid_correctionBtn);
        ImageView backBtnnid=nidDialog.findViewById(R.id.back_btn_nid);
        backBtnnid.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                nidDialog.dismiss();
            }
        });
        nidAppply.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                 Web_browser.WEBSITE_TITLE = "এন আই ডি";
                Web_browser.WEBSITE_LINK = Constant.nidApply;
                startActivity(new Intent(getContext(), Web_browser.class));
                nidDialog.dismiss();
            }
        });

        nidCorrection.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Web_browser.WEBSITE_LINK = Constant.nidCorrection;
                 Web_browser.WEBSITE_TITLE = "এন আই ডি";
                startActivity(new Intent(getContext(), Web_browser.class));
                nidDialog.dismiss();
            }
        });
        nidDialog.show();
    }

    private void birthRegistrationDialogShow() {
        Dialog birthRegisterDialog = new Dialog(getActivity());
        birthRegisterDialog.setContentView(R.layout.custom_birth_registration_dialog);
        birthRegisterDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        birthRegisterDialog.setCancelable(false);
        CardView birthRegisterAppply = birthRegisterDialog.findViewById(R.id.birthRegisterApplyBtn);
        CardView birthRegisterCorrection = birthRegisterDialog.findViewById(R.id.birthRegister_correctionBtn);
        CardView birthRegisterVerifyRenew = birthRegisterDialog.findViewById(R.id.birthRegisterVerifyBtn);
        ImageView backBtn_birthRegister = birthRegisterDialog.findViewById(R.id.back_btn_birthRegister);
        backBtn_birthRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthRegisterDialog.dismiss();
            }
        });
        birthRegisterAppply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_LINK = Constant.birthRegistrationApply;
                Web_browser.WEBSITE_TITLE = "জন্ম নিবন্ধন";
                startActivity(new Intent(getContext(), Web_browser.class));
                birthRegisterDialog.dismiss();
            }
        });
        birthRegisterCorrection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_LINK =Constant.birthRegistrationCorrection;
                Web_browser.WEBSITE_TITLE = "জন্ম নিবন্ধন";
                startActivity(new Intent(getContext(), Web_browser.class));
                birthRegisterDialog.dismiss();
            }
        });
        birthRegisterVerifyRenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_LINK = Constant.birthRegistrationVerify;
                Web_browser.WEBSITE_TITLE = "জন্ম নিবন্ধন";
                startActivity(new Intent(getContext(), Web_browser.class));
                birthRegisterDialog.dismiss();
            }
        });
        birthRegisterDialog.show();
    }

    private void passportDialogShow() {
        Dialog passportDialog = new Dialog(getActivity());
        passportDialog.setContentView(R.layout.custom_passport_dialog);
        passportDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        passportDialog.setCancelable(false);
        CardView passportAppply = passportDialog.findViewById(R.id.passportApplyBtn);
        CardView passportCorrection = passportDialog.findViewById(R.id.passport_correctionBtn);
        CardView passportRenew = passportDialog.findViewById(R.id.passportRenewBtn);
        ImageView backBtnPassport = passportDialog.findViewById(R.id.back_btn_passport);
        backBtnPassport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passportDialog.dismiss();
            }
        });
        passportAppply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_LINK = Constant.pasportApply;
                Web_browser.WEBSITE_TITLE = "ই-পাসপোর্ট";
                startActivity(new Intent(getContext(), Web_browser.class));
                passportDialog.dismiss();
            }
        });
        passportCorrection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_LINK =Constant.pasportCorrection;
                Web_browser.WEBSITE_TITLE = "ই-পাসপোর্ট";
                startActivity(new Intent(getContext(), Web_browser.class));
                passportDialog.dismiss();
            }
        });
        passportRenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_LINK = Constant.pasportRenew;
                Web_browser.WEBSITE_TITLE = "ই-পাসপোর্ট";
                startActivity(new Intent(getContext(), Web_browser.class));
                passportDialog.dismiss();
            }
        });
        passportDialog.show();
    }

}
