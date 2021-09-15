package com.example.cards;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.cards.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Banner Ad
        AdRequest bannerAdRequest = new AdRequest.Builder().build();
        AdRequest AdRequest = new AdRequest.Builder().build();
        AdView bannerAd = binding.gameplayBanner;
        bannerAd.loadAd(bannerAdRequest);

        // Video Ad
        AdRequest videoAdRequest = new AdRequest.Builder().build();

    }

    public void play() {
    }

}