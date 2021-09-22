package com.otsuka.cards;

import static android.content.ContentValues.TAG;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.otsuka.cards.databinding.FragmentPlayBinding;
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
    AdView bannerAd;
    AdRequest bannerAdRequest;
    long tickRate = 10;
    long maxTick = 2000;
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
        //bannerAd = binding.gameplayBanner;

        binding.rightButton.setEnabled(false);
        binding.leftButton.setEnabled(false);

        // Timer
        timer = new CountDownTimer(maxTick, tickRate) {
            public void onTick(long millisUntilFinished) {
                View root = binding.getRoot();
                if (root != null) {
                    root.setBackgroundColor(Color.parseColor("#" + (9 - 10 * millisUntilFinished / maxTick) + "00000"));
                }
            }

            public void onFinish() {
                gameOver();
            }
        };

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        countdown(3);


        // Video Ads
        AdRequest videoAdRequest = new AdRequest.Builder().build();
        createNewAd(videoAdRequest);


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

    private void countdown(int count) {
        TextView secondCount = binding.countdown;
        if (count > 0) {
            secondCount.setVisibility(View.GONE);
            secondCount.setAlpha(1f);
            secondCount.setVisibility(View.VISIBLE);
            secondCount.animate().alpha(0f).setDuration(100).setStartDelay(800).setListener(new Animator.AnimatorListener() {
                public void onAnimationStart(Animator animator){

                }
                public void onAnimationEnd(Animator animator){
                    secondCount.animate().alpha(1f).setDuration(100).setListener(null);
                    secondCount.setText(String.valueOf(count-1));
                    countdown(count-1);
                }
                public void onAnimationCancel(Animator animator){
                }
                public void onAnimationRepeat(Animator animator){
                }
            });
        } else {
            secondCount.setVisibility(View.GONE);
            binding.rightButton.setEnabled(true);
            binding.leftButton.setEnabled(true);
            timer.start();
        }
    }

    //-------------------------------GAMEPLAY-------------------------------//
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
        binding.getRoot().setBackgroundColor(Color.parseColor("#000000"));
        timer.cancel();
        if (count % 10 == 0) {
            maxTick*=0.8;
            timer = new CountDownTimer(maxTick, tickRate) {
                @Override
                public void onTick(long millisUntilFinished) {
                    binding.getRoot().setBackgroundColor(Color.parseColor("#" +(9 - 10 * millisUntilFinished/maxTick) + "00000"));
                }

                @Override
                public void onFinish() {
                    gameOver();
                }
            }.start();
        } else {
            timer.start();
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

    //-------------------------------GAME OVER-------------------------------//
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
                timer.cancel();
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
        binding.countdown.setText(String.valueOf(3));
    }

    // Resets all values
    private void reset() {
        // Resets Tally
        tally.setTextColor(Color.parseColor("#ffffff"));
        tally.setText("0");
        // Resets Buttons
        binding.rightButton.setText(Integer.toString(1));
        binding.leftButton.setText(Integer.toString(-1));
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

        // Reset background and timer
        binding.getRoot().setBackgroundColor(Color.parseColor("#000000"));
        binding.countdown.setText(String.valueOf(3));
        countdown(3);
        maxTick = 2000;
        timer = new CountDownTimer(maxTick, tickRate) {
            public void onTick(long millisUntilFinished) {
                binding.getRoot().setBackgroundColor(Color.parseColor("#" +(9 - 10 * millisUntilFinished/maxTick) + "00000"));
            }

            public void onFinish() {
                gameOver();
            }
        };
    }


    //-------------------------------ADVERTISMENTS-------------------------------//
    private RewardedAd createNewAd(AdRequest adRequest) {
        RewardedAd.load((MainActivity)getActivity(), "ca-app-pub-4963099746269601/3102386152",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        System.out.println("ERROR LOADING AD");
                        System.out.println(loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        setFullscreenCallback();
                        Log.d(TAG, "Ad was loaded.");
                    }
                });
        System.out.println(mRewardedAd);
        return mRewardedAd;
    }

    private void setFullscreenCallback() {
        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "Ad was shown.");
                AdRequest videoAdRequest = new AdRequest.Builder().build();
                createNewAd(videoAdRequest);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.d(TAG, "Ad failed to show.");
                AdRequest videoAdRequest = new AdRequest.Builder().build();
                createNewAd(videoAdRequest);
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, "Ad was dismissed.");
                mRewardedAd = null;
                AdRequest videoAdRequest = new AdRequest.Builder().build();
                createNewAd(videoAdRequest);
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

    public void loadAds() {
        bannerAd.loadAd(bannerAdRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}