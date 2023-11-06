package com.kawsar.eseba_chandpur.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.denzcoskun.imageslider.BuildConfig;
import com.google.android.material.navigation.NavigationView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kawsar.eseba_chandpur.Constant;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.databinding.ActivityMainBinding;
import com.kawsar.eseba_chandpur.views.fragments.user_fragments.UserMenuFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private static final int TIME_INTERVAL = 2000;
    private long backPressed;
    ActionBarDrawerToggle toggle;
    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        mainBinding.textTitleTv.setSelected(true);
        // main fragment call here ................
        UserMenuFragment userMenu_dashboard_fragment = new UserMenuFragment();
        FragmentManager fragmentManagerUserMenu = getSupportFragmentManager();
        FragmentTransaction frag_transactionUserMenu = fragmentManagerUserMenu.beginTransaction();
        frag_transactionUserMenu.add(R.id.framlayout_user_dashboard, userMenu_dashboard_fragment);
        frag_transactionUserMenu.commit();
        //Set Date For Cover Section
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd,yyyy", Locale.US);
        Date date1 = new Date();
        String s_date = formatter.format(date1);
        mainBinding.tvDate.setText(s_date);

        permissionRequest();

//=====================================navigation drawer=======================================//
        // Navagation Drawar------------------------------

        toggle = new ActionBarDrawerToggle(MainActivity.this, mainBinding.drawerLayout, R.string.open, R.string.close);
        mainBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mainBinding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Admin:
                        dialog_show();
                        mainBinding.drawerLayout.closeDrawers();
                        break;
                    case R.id.Developer:
                        Intent nextActivity = new Intent(MainActivity.this, Developer.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("VAL", 100);
                        nextActivity.putExtras(bundle);
                        startActivity(nextActivity);
                        mainBinding.drawerLayout.closeDrawers();
                        break;
                    case R.id.mAbout:
                        About_app.heatTitle = Constant.aboutApp;
                        About_app.description = Constant.descriptionOfaboutApp;
                        startActivity(new Intent(MainActivity.this, About_app.class));
                        mainBinding.drawerLayout.closeDrawers();
                        break;
                    case R.id.home:
                        Fragment userMenuFragment = new UserMenuFragment();
                        FragmentTransaction userMenuFragmentFt = getSupportFragmentManager().beginTransaction();
                        userMenuFragmentFt.replace(R.id.framlayout_user_dashboard, userMenuFragment).commit();
                        mainBinding.drawerLayout.closeDrawers();
                        break;
                    case R.id.privacyMenu:
                        About_app.heatTitle = Constant.privacyPolicyTitle;
                        About_app.description = Constant.privacyPolicy;
                        startActivity(new Intent(MainActivity.this, About_app.class));
                        mainBinding.drawerLayout.closeDrawers();
                        break;
                    case R.id.rateUs:
                        String appname = getPackageName();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appname)));
                        mainBinding.drawerLayout.closeDrawers();
                        break;
                    case R.id.mShare:
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                            String shareMessage = "\nCumilla Zilla Seba\n\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "Choose One"));
                        } catch (Exception e) {
                            //e.toString();
                        }
                        mainBinding.drawerLayout.closeDrawers();
                        break;
                    case R.id.mExit:
                        new AlertDialog.Builder(MainActivity.this).
                                setIcon(R.drawable.danger).setTitle("বাহির!")
                                .setMessage("আপনি কি বের হতে চান?")
                                .setPositiveButton("হ্যাঁ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_MAIN);
                                        intent.addCategory(Intent.CATEGORY_HOME);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }).setNegativeButton("না", null).show();
                        mainBinding.drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });
        mainBinding.imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code Here
                mainBinding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });
//====================end=======================================================//
    }

    private void permissionRequest() {
        Dexter.withActivity(this)
                .withPermissions(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                )

                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        // check if all permissions are granted

                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                        }


                        // check for permanent denial of any permission

                        if (report.isAnyPermissionPermanentlyDenied()) {

                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }


    private void dialog_show() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setIcon(R.drawable.light_on);
        alertDialog.setTitle("Security Alert !!!");
        alertDialog.setMessage("Are you Admin?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainActivity.this, AdminLoginActivity.class));
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertPermissionDialog = alertDialog.create();
        alertPermissionDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //................................back presses exit======================================//
    // When user click bakpress button this method is called
//    @Override
//    public void onBackPressed() {
//        if (mainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            if (backPressed + TIME_INTERVAL > System.currentTimeMillis()) {
//                super.onBackPressed();
//                return;
//            } else {
//                Toast.makeText(getBaseContext(), "Press again to exit app", Toast.LENGTH_SHORT).show();
//            }
//            backPressed = System.currentTimeMillis();
//        }
//    }
}
   
