package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private static final String EXTRA_CRIME_ID =
            "com.bignerdranch.android.criminalintent.crime_id";

    private ViewPager2 mViewPager;
    private List<Crime> mCrimes;
    private FragmentStateAdapter mPagerAdapter;

    private Button mFirstCrimeButton;
    private Button mLastCrimeButton;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager2) findViewById(R.id.crime_view_pager);
        mFirstCrimeButton = (Button) findViewById(R.id.to_first_crime);
        mLastCrimeButton = (Button) findViewById(R.id.to_last_crime);
        mCrimes = CrimeLab.get(this).getCrimes();
        mPagerAdapter = new CrimePagerAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);

        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        mFirstCrimeButton.setOnClickListener(v -> mViewPager.setCurrentItem(0, true));
        mLastCrimeButton.setOnClickListener(v -> mViewPager.setCurrentItem(mCrimes.size() - 1, true));

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateButton(position);
            }
        });

        OnBackPressedCallback callBack = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
                setEnabled(true);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callBack);
    }

    private void updateButton(int position) {
        mFirstCrimeButton.setEnabled(position != 0);
        mLastCrimeButton.setEnabled(position != mCrimes.size() - 1);
    }

    private class CrimePagerAdapter extends FragmentStateAdapter{

        public CrimePagerAdapter(FragmentActivity fa){
            super(fa);
        }

        @Override
        public Fragment createFragment(int position){

            Crime mCrime = mCrimes.get(position);
            return CrimeFragment.newInstance(mCrime.getId());
        }

        @Override
        public int getItemCount(){
            return mCrimes.size();
        }
    }

}
