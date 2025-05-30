package com.bignerdranch.android.criminalintent;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";
    private static final String ARG_REQUEST_CODE = "request_code";
    private static final String RESULT_DATE_KEY = "result_date";

    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Calendar date, String requestCode) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        args.putSerializable(ARG_REQUEST_CODE, requestCode);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = (Calendar) getArguments().getSerializable(ARG_DATE);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();

                        Calendar resultDate = new GregorianCalendar(year, month, day);
                        Bundle result = new Bundle();

                        result.putSerializable(RESULT_DATE_KEY, resultDate);
                        String resultRequestCode = requireArguments().getString(ARG_REQUEST_CODE, "");
                        requireActivity().getSupportFragmentManager().setFragmentResult(resultRequestCode, result);

                    }
                }).create();
    }

    public static Calendar getSelectedDate(Bundle result){
        Calendar resultDate = (Calendar) result.getSerializable(RESULT_DATE_KEY);
        return resultDate;
    }
}
