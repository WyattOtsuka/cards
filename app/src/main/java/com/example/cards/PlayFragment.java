package com.example.cards;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
//import androidx.navigation.fragment.NavHostFragment;

import com.example.cards.databinding.FragmentPlayBinding;

public class PlayFragment extends Fragment {

    private FragmentPlayBinding binding;
    TextView tally;
    final int GAME_MAX = 10;
    //SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
    //private SharedPreferences.Editor editor = prefs.edit();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentPlayBinding.inflate(inflater, container, false);
        tally = binding.tallyText;

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

                binding.rightButton.setText(Integer.toString(rightValue));
            }
        });
    }
    private void updateTally(View view, int delta) {
        String countString = tally.getText().toString();
        Integer count = Integer.parseInt(countString);
        count += delta;
        tally.setText(count.toString());
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
        if (Math.abs(tallyValue) < GAME_MAX) {
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
        System.out.println(Integer.parseInt(binding.rightButton.getText().toString()) + ": " + color);
        tally.setTextColor(Color.parseColor(color));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
