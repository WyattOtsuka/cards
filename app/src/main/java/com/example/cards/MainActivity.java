package com.example.cards;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.cards.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AdRequest bannerAdRequest = new AdRequest.Builder().build();
        AdRequest AdRequest = new AdRequest.Builder().build();
    }

}