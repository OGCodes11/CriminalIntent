package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Lifecycle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String REQUEST_DATE = "DialogDate";
    private static final String REQUEST_TIME = "DialogTime";

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private CheckBox mCallPoliceCheckBox;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(requireActivity()).getCrime(crimeId);
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        setupMenu();
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                mCrime.setChanged(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate(), REQUEST_DATE);
                dialog.show(manager, REQUEST_DATE);
            }
        });

        getParentFragmentManager().setFragmentResultListener(REQUEST_DATE, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey == null) {
                    return;
                }
                if (REQUEST_DATE == requestKey) {
                    Calendar c = DatePickerFragment.getSelectedDate(result);
                    mCrime.setDate(c);
                    mCrime.setChanged(true);
                    updateDate();
                }
            }
        });

        mTimeButton = v.findViewById(R.id.crime_time);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate(), REQUEST_TIME);
                dialog.show(manager, REQUEST_TIME);
            }
        });

        getParentFragmentManager().setFragmentResultListener(REQUEST_TIME, this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                if (requestKey == null) {
                    return;
                }
                if (REQUEST_TIME == requestKey) {
                    Calendar time = TimePickerFragment.getSelectedTime(result);
                    mCrime.setTime(time);
                    mCrime.setChanged(true);
                    updateTime();
                }
            }
        });

        mSolvedCheckBox = v.findViewById(R.id.ic_solver);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            mCrime.setSolved(isChecked);
            mCrime.setChanged(true);
        });

        return v;
    }

    private void updateDate() {mDateButton.setText(mCrime.getDateString());}

    private void updateTime() {mTimeButton.setText(mCrime.getTimeString());}

    public void setupMenu()
    {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.fragment_crime, menu);
            }

            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.delete_crime) {

                    CrimeLab.get(getActivity()).removeCrime(mCrime);
                    CrimeLab.get(getActivity()).setListChanged(true);CrimeLab.get(getActivity()).setListChanged(true);

                    getActivity().finish();
                    return true;
                }
                else if (item.getItemId() == R.id.new_crime) {
                    Crime crime = new Crime();
                    CrimeLab.get(getActivity()).addCrime(crime);
                    CrimeLab.get(getActivity()).setListChanged(true);
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
}



