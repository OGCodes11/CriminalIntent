package com.bignerdranch.android.criminalintent;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_TIME = "com.bignerdranch.android.criminalintent.time";
    public static final String TAG = "TimePickerFragment";

    private static final String ARG_TIME = "time";
    private static final String ARG_REQUEST_CODE = "request_code_time";
    private static final String RESULT_TIME_KEY = "result_time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Calendar time, String requestCode) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, time);
        args.putSerializable(ARG_REQUEST_CODE, requestCode);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar t = (Calendar) getArguments().getSerializable(ARG_TIME);
        int hour = t.get(Calendar.HOUR_OF_DAY);
        int minute = t.get(Calendar.MINUTE);
        int second = t.get(Calendar.SECOND);
        int millisecond = t.get(Calendar.MILLISECOND);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_picker);
        mTimePicker.setHour(hour);
        mTimePicker.setMinute(minute);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hours = mTimePicker.getHour();
                        int minutes = mTimePicker.getMinute();

                        t.set(Calendar.HOUR_OF_DAY, hours);
                        t.set(Calendar.MINUTE, minutes);
                        t.set(Calendar.SECOND,second);
                        t.set(Calendar.MILLISECOND, millisecond);

                        Bundle result = new Bundle();
                        result.putSerializable(RESULT_TIME_KEY, t);
                        String resultRequestCode = requireArguments().getString(ARG_REQUEST_CODE, "");
                        requireActivity().getSupportFragmentManager().setFragmentResult(resultRequestCode, result);

                    }
                }).create();
    }

    public static Calendar getSelectedTime(Bundle result){
        Calendar resultTime = (Calendar) result.getSerializable(RESULT_TIME_KEY);
        return resultTime;
    }
}