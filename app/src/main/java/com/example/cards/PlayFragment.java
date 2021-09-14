package com.example.cards;

import static android.content.ContentValues.TAG;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.health.SystemHealthManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import com.example.cards.databinding.FragmentPlayBinding;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Arrays;
import java.util.List;

public class PlayFragment extends Fragment {

    private FragmentPlayBinding binding;
    TextView tally;
    TextView cardCount;
    int count = 0;
    final int GAME_MAX = 10;
    private RewardedAd mRewardedAd;
    CountDownTimer timer;
    private boolean secondWindUsed = false;
    Button secondWindButton;
    Button playAgainButton;
    Button mainMenuButton;
    //SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
    //private SharedPreferences.Editor editor = prefs.edit();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentPlayBinding.inflate(inflater, container, false);
        // Gets elements of view
        tally = binding.tallyText;
        cardCount = binding.cardCount;
        secondWindButton = binding.secondWindButton;
        secondWindButton.setVisibility(View.VISIBLE);
        mainMenuButton = binding.mainMenuButton;
        playAgainButton = binding.playAgainButton;

        System.out.println("MainMenu: " + mainMenuButton + "PlayAgain: " + playAgainButton);

        // Timer
        timer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

            }
        }.start();

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Banner ad
        AdView bannerAd = binding.gameplayBanner;
        AdRequest bannerAdRequest = new AdRequest.Builder().build();

        /*
        LinearLayout container = binding.gameplayLayout;
        AdView bannerAd = new AdView((MainActivity)getActivity());
        bannerAd.setAdSize(AdSize.BANNER);
        bannerAd.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        container.addView(bannerAd, params);
*/
        bannerAd.loadAd(bannerAdRequest);


        // Video Ads
        AdRequest videoAdRequest = new AdRequest.Builder().build();
        //createNewAd(videoAdRequest);

        // Test id for Video Ads
        List<String> testDeviceIds = Arrays.asList("3F4E17538612DA42BA159696F049E455");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        binding.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTally(view, Integer.parseInt(binding.leftButton.getText().toString()));
                // TODO: Update for division and multiplication
                int leftValue = Integer.parseInt(newCard());

                // Gets the value of the tally and sets up color string
                int tallyValue = Integer.parseInt(tally.getText().toString());
                updateColor(tallyValue);

                binding.leftButton.setText(Integer.toString(leftValue));

                if (Math.abs(tallyValue) >= GAME_MAX) {
                    gameOver();
                }
            }
        });
        binding.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTally(view, Integer.parseInt(binding.rightButton.getText().toString()));
                // TODO: Update for division and multiplication
                int rightValue = Integer.parseInt(newCard());

                // Gets the value of the tally and sets up color string
                int tallyValue = Integer.parseInt(tally.getText().toString());
                updateColor(tallyValue);

                if (Math.abs(tallyValue) >= GAME_MAX) {
                    gameOver();
                }
                binding.rightButton.setText(Integer.toString(rightValue));
            }
        });
        binding.mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(PlayFragment.this)
                        .navigate(R.id.action_PlayFragment_to_FirstFragment);
            }
        });
        binding.playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
        binding.secondWindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAd();
            }
        });
    }
    private void updateTally(View view, int delta) {
        String countString = tally.getText().toString();
        Integer currTally = Integer.parseInt(countString);
        currTally += delta;
        tally.setText(currTally.toString());

        count++;
        cardCount.setText("Cards: " + count);
    }

    // Returns a new card value
    // TODO: implement division and multiplication cards
    private String newCard() {
        // Puts numbers in range -4 to 4
        int value = (int)(Math.random()*11-5.5);
        // lowers odds of seeing a zero
        if (value == 0) {
            value = (int)(Math.random()*11-5.5);
        }
        if (value == 0) {
            value = (int)(Math.random()*11-5.5);
        }
        return String.valueOf(value);
    }

    // Sets the color for the tally
    private void updateColor (int tallyValue) {
        String color = "#000000";
        if (Math.abs(tallyValue) <= GAME_MAX) {
            double blueHex = Math.abs(tallyValue)*(25.5);
            String blue = Integer.toHexString(255-(int)blueHex);
            // Leading zero checker
            if (blue.length() == 1) {
                blue = "0" + blue;
            }
            color = "#ff" + blue + blue;
        } else {  // Game over condition
            color = "#ff0000";
        }
        tally.setTextColor(Color.parseColor(color));
    }

    // Makes Game Over box apear
    private void gameOver() {
        // Disables left and right buttons
        binding.rightButton.setEnabled(false);
        binding.leftButton.setEnabled(false);
        // Fades in Game Over
        LinearLayout gameOverFrame = binding.gameOverFrame;
        gameOverFrame.setElevation(2);
        gameOverFrame.setVisibility(View.GONE);
        gameOverFrame.setAlpha(0f);
        gameOverFrame.setVisibility(View.VISIBLE);
        gameOverFrame.animate().alpha(1f).setDuration(500).setListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animator){
                disableButtons();
            }
            public void onAnimationEnd(Animator animator){
                enableButtons();
            }
            public void onAnimationCancel(Animator animator){
                enableButtons();
            }
            public void onAnimationRepeat(Animator animator){
            }
        });
    }

    private void disableButtons() {
        playAgainButton.setEnabled(false);
        mainMenuButton.setEnabled(false);
        secondWindButton.setEnabled(false);
    }

    private void enableButtons() {
        secondWindButton.setEnabled(true);
        mainMenuButton.setEnabled(true);
        playAgainButton.setEnabled(true);
    }

    // Resets only Tally value
    public void secondWind() {
        secondWindUsed = true;
        // Reset Tally
        tally.setTextColor(Color.parseColor("#ffffff"));
        tally.setText("0");
        // Reset buttons
        binding.rightButton.setEnabled(true);
        binding.leftButton.setEnabled(true);
        // Resets Game Over layout
        LinearLayout gameOverFrame = binding.gameOverFrame;
        gameOverFrame.setElevation(0);
        gameOverFrame.setVisibility(View.GONE);
        gameOverFrame.setAlpha(0f);
        Button secondWindButton = binding.secondWindButton;
        secondWindButton.setVisibility(View.GONE);
    }

    // Resets all values
    private void reset() {
        // Resets Tally
        tally.setTextColor(Color.parseColor("#ffffff"));
        tally.setText("0");
        // Resets Buttons
        binding.rightButton.setText(Integer.toString(1));
        binding.leftButton.setText(Integer.toString(-1));
        binding.rightButton.setEnabled(true);
        binding.leftButton.setEnabled(true);
        // Resets Game Over layout
        LinearLayout gameOverFrame = binding.gameOverFrame;
        gameOverFrame.setElevation(0);
        gameOverFrame.setVisibility(View.GONE);
        gameOverFrame.setAlpha(0f);
        // Reset Card Count
        cardCount.setText("Cards: 0");
        count = 0;
        // Resets secondWind
        secondWindButton.setVisibility(View.VISIBLE);

    }

    private RewardedAd createNewAd(AdRequest adRequest) {
        RewardedAd.load((MainActivity)getActivity(), "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        setFullscreenCallback();
                        Log.d(TAG, "Ad was loaded.");
                    }
                });

        return mRewardedAd;
    }

    private void setFullscreenCallback() {
        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad was shown.");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.d(TAG, "Ad failed to show.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad was dismissed.");
                mRewardedAd = null;
            }
        });
    }

    public void showAd() {
        if (mRewardedAd != null) {
            Activity activityContext = (MainActivity)getActivity();
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.

                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                    mRewardedAd = createNewAd(new AdRequest.Builder().build());
                    secondWind();
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
