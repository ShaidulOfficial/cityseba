package com.kawsar.eseba_chandpur.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kawsar.eseba_chandpur.Constant;
import com.kawsar.eseba_chandpur.MyApplication;
import com.kawsar.eseba_chandpur.databinding.ActivityDeveloperBinding;

public class Developer extends AppCompatActivity {


    ActivityDeveloperBinding activityDeveloperBinding;
    String whatsappNumber = "+8801719652083";
    public String num = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDeveloperBinding = ActivityDeveloperBinding.inflate(getLayoutInflater());
        setContentView(activityDeveloperBinding.getRoot());

        activityDeveloperBinding.backDeveloper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Developer.super.onBackPressed();
            }
        });
        activityDeveloperBinding.linkedInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appLink = Constant.linkedIn;
                String packageName = "com.linkedin.android";
                MyApplication.openLinkedin(Developer.this, appLink, packageName);
            }
        });
        activityDeveloperBinding.emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "ariyankawsar1998@gmail.com";
                String[] address = email.split(",%&");
                Intent mailIntent = new Intent(Intent.ACTION_SEND);
                mailIntent.putExtra(Intent.EXTRA_EMAIL, address);
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                mailIntent.putExtra(Intent.EXTRA_TEXT, "");
                mailIntent.setType("message/rfc822");
                if (mailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(mailIntent, "Choose an Option"));
                } else {
                    Toast.makeText(Developer.this, "No App is Installed", Toast.LENGTH_SHORT).show();
                }

            }
        });
        activityDeveloperBinding.whatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone=" + whatsappNumber;
                try {
                    PackageManager pm = getApplicationContext().getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }
            }
        });
    }

    //......................call method.....................//

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneNumber();
            }
        }
    }

    public void callPhoneNumber() {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Developer.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + num));
                startActivity(callIntent);

            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + num));
                startActivity(callIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //==================================================================================//
}