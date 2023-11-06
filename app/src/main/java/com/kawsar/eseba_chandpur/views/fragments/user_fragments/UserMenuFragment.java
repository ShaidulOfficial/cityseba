package com.kawsar.eseba_chandpur.views.fragments.user_fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kawsar.eseba_chandpur.Constant;
import com.kawsar.eseba_chandpur.R;
import com.kawsar.eseba_chandpur.databinding.FragmentUserMenuBinding;
import com.kawsar.eseba_chandpur.views.activities.Web_browser;
import com.kawsar.eseba_chandpur.views.fragments.user_fragments.newspaper_user.JobsNewsUserFragment;

import java.util.ArrayList;


public class UserMenuFragment extends Fragment {

    FragmentUserMenuBinding userMenuBinding;
    DatabaseReference dbRefMenu;
    private static final int TIME_INTERVAL = 2000;
    AdView adView;
    AdRequest adRequest;
    private InterstitialAd mInterstitialAd;

    public UserMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        userMenuBinding = FragmentUserMenuBinding.inflate(inflater, container, false);
        userMenuBinding.noticeTv.setSelected(true);
        userMenuBinding.emergencyTv.setSelected(true);
        MobileAds.initialize(getContext(), initializationStatus -> {
        });
        adRequest = new AdRequest.Builder().build();
        dbRefMenu = FirebaseDatabase.getInstance().getReference("Admin").child("Notice");
        dbRefMenu.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userMenuBinding.noticeTv.setText(snapshot.child("message").getValue().toString().trim());
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //===========Image Slider=========================//
        ArrayList<SlideModel> imageList = new ArrayList<>();
        imageList.add(new SlideModel(R.drawable.slider7, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slider8, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slider9, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slider10, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slider11, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slider12, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slider13, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slider14, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slider1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slider3, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slider4, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slider5, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slider6, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slider15, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slider16, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.slider2, ScaleTypes.FIT));
        userMenuBinding.imageSlider.setImageList(imageList);
        //==================end==================================//
        userMenuBinding.emergencyLinLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNo = "999";
                String call = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                getContext().startActivity(intent);
            }
        });

        // *************************** blood fragment ********************************
        userMenuBinding.bloodDonorUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment bloodDonorUserFragment = new BloodDonorUserFragment();
                FragmentTransaction bloodDonorUser_ft = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                bloodDonorUser_ft.replace(R.id.framlayout_user_dashboard, bloodDonorUserFragment)
                        .addToBackStack(null).commit();
            }
        });

        // *************************** Eseba fragment ********************************
        userMenuBinding.esebaUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullScreen();
                Fragment esebaFragment = new EsebaFragment();
                FragmentTransaction eseba_ft = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                eseba_ft.replace(R.id.framlayout_user_dashboard, esebaFragment)
                        .addToBackStack(null).commit();
            }
        });
        // *************************** education board fragment ********************************
        userMenuBinding.educationBoardUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                educationBoardDialog();
            }
        });    // *************************** feedback fragment ********************************
        userMenuBinding.feedbackUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullScreen();
                Fragment feedBackFragment = new FeedBackFragment();
                FragmentTransaction feedBack_ft = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                feedBack_ft.replace(R.id.framlayout_user_dashboard, feedBackFragment)
                        .addToBackStack(null).commit();
            }
        });
        // *************************** hotel fragment ********************************
        userMenuBinding.hotelUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullScreen();
                HotelFragmentUser hotelFragmentUser = new HotelFragmentUser();
                FragmentManager fragmentManagerHotel = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction_hotel = fragmentManagerHotel
                        .beginTransaction();
                fragmentTransaction_hotel.replace(R.id.framlayout_user_dashboard, hotelFragmentUser)
                        .addToBackStack(null).commit();
            }
        });
        // *************************** job fragment ********************************
        userMenuBinding.jobUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullScreen();
                Fragment jobUserFragment = new JobsNewsUserFragment();
                FragmentManager fragmentManagerjob = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction_job = fragmentManagerjob
                        .beginTransaction();
                fragmentTransaction_job.replace(R.id.framlayout_user_dashboard, jobUserFragment)
                        .addToBackStack(null).commit();
            }
        });
        // *************************** news paper fragment ********************************
        userMenuBinding.newsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullScreen();
                Fragment newsUserFragment = new NewsUserFragment();
                FragmentTransaction newsUserFt = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                newsUserFt.replace(R.id.framlayout_user_dashboard, newsUserFragment)
                        .addToBackStack(null).commit();
            }
        });

        // *************************** resturent fragment ********************************
        userMenuBinding.resturentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullScreen();
                Fragment resturentUserFragment = new ResturentUserFragment();
                FragmentTransaction resturentUser_ft = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                resturentUser_ft.replace(R.id.framlayout_user_dashboard, resturentUserFragment)
                        .addToBackStack(null).commit();
            }
        });
        // *************************** qibla fragment ********************************
        userMenuBinding.qiblaUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment_qibla = new QiblaFragment();
                FragmentTransaction fragmentTransaction_qibla = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction_qibla.replace(R.id.framlayout_user_dashboard, fragment_qibla)
                        .addToBackStack(null).commit();
            }
        });
        // *************************** service fragment ********************************
        userMenuBinding.servicesUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment servicesUserFragment = new ServicesUserFragment();
                FragmentTransaction fragmentTransaction_services = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                fragmentTransaction_services.replace(R.id.framlayout_user_dashboard, servicesUserFragment)
                        .addToBackStack(null).commit();
            }
        });
        // *************************** shopping fragment ********************************
        userMenuBinding.shoppingBtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullScreen();
                Fragment shoppingUserFragment = new ShoppingUserFragment();
                FragmentTransaction ftshopping = getActivity().getSupportFragmentManager()
                        .beginTransaction();
                ftshopping.replace(R.id.framlayout_user_dashboard, shoppingUserFragment)
                        .addToBackStack(null).commit();
            }
        });
        // *************************** ticket fragment ********************************
        userMenuBinding.ticketUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullScreen();
                Fragment paribahanUserFragment = new ParibahanUserFragment();
                FragmentTransaction paribahanUserFt = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                paribahanUserFt.replace(R.id.framlayout_user_dashboard, paribahanUserFragment)
                        .addToBackStack(null).commit();
            }
        });
        // *************************** tourist place fragment ********************************
        userMenuBinding.touristPlaceUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFullScreen();
                Fragment touristUserFragment = new TouristUserFragment();
                FragmentTransaction touristFt = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                touristFt.replace(R.id.framlayout_user_dashboard, touristUserFragment)
                        .addToBackStack(null).commit();
            }
        });
        return userMenuBinding.getRoot();
    }

    private void educationBoardDialog() {
        Dialog eduBoardDialog = new Dialog(getActivity());
        eduBoardDialog.setContentView(R.layout.custom_education_board_dialog);
        eduBoardDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        eduBoardDialog.setCancelable(false);
        CardView sscBtn = eduBoardDialog.findViewById(R.id.sscResultBtn);
        CardView hscBtn = eduBoardDialog.findViewById(R.id.hscResultBtn);
        CardView tcBtn = eduBoardDialog.findViewById(R.id.school_tcBtn);
        CardView certificateBoard = eduBoardDialog.findViewById(R.id.certificate_correctionBtn);
        ImageView backBtnEdu = eduBoardDialog.findViewById(R.id.back_btn_eduBoard);
        backBtnEdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eduBoardDialog.dismiss();
            }
        });
        sscBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_LINK = Constant.sscResult;
                Web_browser.WEBSITE_TITLE = "S.S.C Result";
                startActivity(new Intent(getContext(), Web_browser.class));
                eduBoardDialog.dismiss();
            }
        });
        hscBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_LINK = Constant.hscResult;
                Web_browser.WEBSITE_TITLE = "H.S.C Result";
                startActivity(new Intent(getContext(), Web_browser.class));
                eduBoardDialog.dismiss();
            }
        });
        tcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_LINK = Constant.schoolTransfer;
                Web_browser.WEBSITE_TITLE = "School T.C";
                startActivity(new Intent(getContext(), Web_browser.class));
                eduBoardDialog.dismiss();
            }
        });
        certificateBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web_browser.WEBSITE_LINK = Constant.certificateCorrection;
                Web_browser.WEBSITE_TITLE = "School Certificate";
                startActivity(new Intent(getContext(), Web_browser.class));
                eduBoardDialog.dismiss();
            }
        });
        eduBoardDialog.show();
    }

    @Override
    public void onResume() {
        loadInterstitial();
        super.onResume();
    }

    private void showFullScreen() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(getActivity());
        } else {
            Toast.makeText(getContext(), "no load fullScreen ads", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadInterstitial() {
        InterstitialAd.load(getContext(), Constant.admob_INTERSTITIAL_UNIT_ID, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }
}