package com.kawsar.eseba_chandpur.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.kawsar.eseba_chandpur.Constant;
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.databinding.ActivityAboutAppBinding;

public class About_app extends AppCompatActivity {
    ActivityAboutAppBinding aboutAppBinding;
    public static String heatTitle = "";
    public static String description = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aboutAppBinding = ActivityAboutAppBinding.inflate(getLayoutInflater());
        setContentView(aboutAppBinding.getRoot());

        aboutAppBinding.backAbout.setOnClickListener(view -> About_app.super.onBackPressed());
        aboutAppBinding.sssBtn.setOnClickListener(v -> {
            try {
                Uri uri = Uri.parse("market://search?q=pub:Shaidul Soft. Studio");
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } catch (ActivityNotFoundException e) {
                Uri uri1 = Uri.parse("https://play.google.com/store/apps/details?id=Shaidul Soft. Studio");
                startActivity(new Intent(Intent.ACTION_VIEW, uri1));
            }
        });
        aboutAppBinding.linkedinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appLink=Constant.linkedIn;
                String packageName="com.linkedin.android";
                MyApplication.openLinkedin(About_app.this,appLink,packageName);
            }
        });
        aboutAppBinding.zahidurLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_TITLE="Research Gate";
                Web_browser.WEBSITE_LINK= Constant.zahidur_researchGate;
                startActivity(new Intent(About_app.this, Web_browser.class));
            }
        });
        aboutAppBinding.britanniaLinkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_TITLE="Britannia University";
                Web_browser.WEBSITE_LINK= Constant.britanniLink;
                startActivity(new Intent(About_app.this, Web_browser.class));
            }
        });
        aboutAppBinding.headTitleTv.setText(heatTitle);
        aboutAppBinding.descriptionAboutTv.setText(description);

    }

}