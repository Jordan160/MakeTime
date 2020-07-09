package com.jvetter2.maketime;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.gc.materialdesign.views.ButtonFlat;

public class OnboardingActivity extends FragmentActivity {
    private ViewPager2 pager;
    //private SmartTabLayout indicator;
    private ButtonFlat skip;
    private ButtonFlat next;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        pager = (ViewPager2)findViewById(R.id.pager);
        //indicator = (SmartTabLayout)findViewById(R.id.indicator);
        skip = (ButtonFlat)findViewById(R.id.skip);
        next = (ButtonFlat)findViewById(R.id.next);

//        FragmentStatePagerAdapter adapter = new FragmentStateAdapter() {
//            @Override
//            public int getItemCount() {
//                return 3;
//            }
//
//            @NonNull
//            @Override
//            public Fragment createFragment(int position) {
//                switch (position) {
//                    case 0:
//                        return new OnboardingFragment1();
////                    case 1 : return new OnboardingFragment2();
////                    case 2 : return new OnboardingFragment3();
//                    default:
//                        return null;
//                }
//            }
//        };

//            @Override
//            public Fragment getItem(int position) {
//                switch (position) {
//                    case 0 : return new OnboardingFragment1();
////                    case 1 : return new OnboardingFragment2();
////                    case 2 : return new OnboardingFragment3();
//                    default: return null;
//                }
//            }
//
//            @Override
//            public int getCount() {
//                return 3;
//            }

        pager.setAdapter(new ScreenSlidePagerAdapter(this));

//        indicator.setViewPager(pager);
//
//        indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int position) {
//                if(position == 2){
//                    skip.setVisibility(View.GONE);
//                    next.setText("Done");
//                } else {
//                    skip.setVisibility(View.VISIBLE);
//                    next.setText("Next");
//                }
//            }
//
//        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishOnboarding();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pager.getCurrentItem() == 2){
                    finishOnboarding();
                } else {
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                }
            }
        });
    }

    private void finishOnboarding() {
        SharedPreferences preferences =
                getSharedPreferences("my_preferences", MODE_PRIVATE);

        preferences.edit()
                .putBoolean("onboarding_completed",true).apply();

        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);

        finish();
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

            @Override
            public int getItemCount() {
                return 3;
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return new OnboardingFragment1();
                    case 1 :
                        return new OnboardingFragment2();
                    case 2 :
                        return new OnboardingFragment3();
                    default:
                        return null;
                }
            }
    }
}
