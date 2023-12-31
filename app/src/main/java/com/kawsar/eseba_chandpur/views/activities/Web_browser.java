package com.kawsar.eseba_chandpur.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.kawsar.eseba_chandpur.R;

public class Web_browser extends AppCompatActivity {


    // init variable here
    WebView webView;
    LinearLayout layNonet;
    LottieAnimationView progressBar;
    LinearLayout layRoot;
    ImageView imgBack;
    TextView webTitile;
    Animation animation;
    String USER_AGENT_ = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19";
    String USER_AGENT_XX = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19";

    public static String WEBSITE_LINK = "";
    public static String WEBSITE_TITLE = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);

        progressBar = findViewById(R.id.progressBar);
        layRoot = findViewById(R.id.layRoot);
        layNonet = findViewById(R.id.layNonet);
        imgBack = findViewById(R.id.imgBack);
        webTitile = findViewById(R.id.webTitile);
        animation = android.view.animation.AnimationUtils.loadAnimation(Web_browser.this, R.anim.middle_to_top);


        //Creating webView programitcally which supports media player and js automatically like a browser
        webView = new WebView(Web_browser.this);
        webView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        webView.setFitsSystemWindows(false); // your preferences
        webView.setVerticalScrollBarEnabled(false); // your preferences
        webView.setPadding(10,10,10,10); // your preferences
        layRoot.addView(webView);

        // Enabling some setting so that browser can work properly
        webView.getSettings().setUserAgentString(USER_AGENT_);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
          webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        //webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebViewClient(new HelloWebViewClient());
        webView.setWebChromeClient(new ChromeClient());


        //-------------------new setting
        webView.getSettings().setBlockNetworkLoads (false);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.getSettings().setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webView.getSettings().setUserAgentString(getString(R.string.app_name));

        webView.loadUrl(WEBSITE_LINK);

        if(!isNetworkAvailable(Web_browser.this)){
            webView.setVisibility(View.GONE);
            layNonet.setVisibility(View.VISIBLE);
        }else{
            webView.setVisibility(View.VISIBLE);
            layNonet.setVisibility(View.GONE);
        }

        webTitile.setText(""+WEBSITE_TITLE);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    } // onCreate Ends here  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null;
    }

    // =================================================



    //================================================= defining custom class (HelloWebViewClient)
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    }
    //=================================================



    //================================================= defining custom class (ChromeClient)
    private class ChromeClient extends WebChromeClient {
        // Defining some variables


        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;
        ChromeClient() {}

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            if(newProgress >= 100){
                // Page loading finish
                progressBar.setVisibility(View.GONE);

            }else{
                progressBar.setVisibility(View.VISIBLE);
            }
        }


        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }




    }
    // Custom class ends here #############################

    @Override
    public void onBackPressed() {

        // When landing in home screen
        if (!webView.canGoBack()) {
            super.onBackPressed();
        } else{
            webView.goBack();
        }


    } // end of onBackpressed method


}