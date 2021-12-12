package com.otsuka.cards;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

// Google Ads
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.otsuka.cards.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdSize;


import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final String TAG = "MainActivity";
    public com.google.android.gms.ads.AdView googleBannerAd;
    private com.facebook.ads.AdView facebookBannerAd;
    public boolean test = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LinearLayout adContainer = binding.bannerAdContainer;

        // Banner Ad
        AdRequest bannerAdRequest = new AdRequest.Builder().build();

        googleBannerAd = new com.google.android.gms.ads.AdView(this);
        googleBannerAd.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
        if (!test) {
            googleBannerAd.setAdUnitId("ca-app-pub-4963099746269601/5585805612");
        } else {
            googleBannerAd.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
            RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("3F4E17538612DA42BA159696F049E455")).build();
            MobileAds.setRequestConfiguration(configuration);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,1.0f);
        adContainer.addView(googleBannerAd, params);

        googleBannerAd.loadAd(bannerAdRequest);
        googleBannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                System.out.println("ADs LOADED SUCCESSFULLY");
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                System.out.println("ERROR: " + adError);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }
}

