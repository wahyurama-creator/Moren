package com.wahyurama.moren;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.wahyurama.moren.Adapter.OnboardingAdapter;
import com.wahyurama.moren.SignIn.SignInActivity;

import java.util.ArrayList;
import java.util.List;

public class OnboardingScreen extends AppCompatActivity {

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicator;
    private MaterialButton buttonOnboardingAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_item);

        layoutOnboardingIndicator =
                findViewById(R.id.layoutOnboardingIndicator);
        buttonOnboardingAction = findViewById(R.id.buttonOnboardingAction);

        setUpOnboardingItems();

        ViewPager2 onBoardingViewPager = findViewById(R.id.onBoardingViewPager);
        onBoardingViewPager.setAdapter(onboardingAdapter);

        setUpOnboardingIndicators();
        setCurrentOnboardingIndicators(0);

        onBoardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicators(position);
            }
        });

        buttonOnboardingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBoardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()) {
                    onBoardingViewPager.setCurrentItem(onBoardingViewPager.getCurrentItem() + 1);
                } else {
                    startActivity(new Intent(getApplicationContext(),
                            SignInActivity.class));
                    finish();
                }
            }
        });

    }

    private void setUpOnboardingItems() {
        List<OnboardingItem> onboardingItems = new ArrayList<>();

        OnboardingItem easyUse = new OnboardingItem();
        easyUse.setTitle("Easy to Use");
        easyUse.setDescription("We make booking your car easier, enter the type of car and let's" +
                " drive");
        easyUse.setImages(R.drawable.slpash_screen_one);

        OnboardingItem chooseCar = new OnboardingItem();
        chooseCar.setTitle("Choose Your Car");
        chooseCar.setDescription("Choose your car type according to your " +
                "needs, where each car has" +
                " a different seat");
        chooseCar.setImages(R.drawable.slpash_screen_two);

        OnboardingItem takeTrip = new OnboardingItem();
        takeTrip.setTitle("Take Your Trip Now");
        takeTrip.setDescription("Prepare your belongings and start the " +
                "journey with us, Happy driving");
        takeTrip.setImages(R.drawable.slpash_screen_three);

        onboardingItems.add(easyUse);
        onboardingItems.add(chooseCar);
        onboardingItems.add(takeTrip);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);
    }

    private void setUpOnboardingIndicators() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
        layoutParams.setMargins(4, 0, 4, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(), R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicator.addView(indicators[i]);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setCurrentOnboardingIndicators(int index) {
        int childCount = layoutOnboardingIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutOnboardingIndicator.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.onboarding_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.onboarding_indicator_inactive)
                );
            }
        }

        if (index == onboardingAdapter.getItemCount() - 1) {
            buttonOnboardingAction.setText("Get Started");
        } else {
            buttonOnboardingAction.setText("Next");
        }
    }
}