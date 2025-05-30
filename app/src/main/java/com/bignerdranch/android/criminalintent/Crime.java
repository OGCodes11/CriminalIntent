package com.bignerdranch.android.criminalintent;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.GregorianCalendar;
import java.util.Calendar;

public class Crime {
    private UUID mId;
    private String mTitle;
    private Calendar mDate;
    private Calendar mTime;
    private boolean mSolved;
    private boolean mChanged;
    public Crime() {
        this(UUID.randomUUID());
    }

    public Crime(UUID id) {
        mId = id;
        mDate = (GregorianCalendar) Calendar.getInstance();
        mTime = (GregorianCalendar) Calendar.getInstance();
    }

    public UUID getId() {
        return mId;
    }

    public String getDateString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(mDate.getTime());
    }

    public String getTimeString() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        String formattedTime = timeFormat.format(mDate.getTime());
        return formattedTime;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Calendar getDate() {
        return mDate;
    }

    public void setDate(Calendar date) {
        mDate.set(Calendar.DATE, date.get(Calendar.DATE));
        mDate.set(Calendar.MONTH, date.get(Calendar.MONTH));
        mDate.set(Calendar.YEAR, date.get(Calendar.YEAR));
    }

    public Calendar getTime() {
        return mTime;
    }

    public void setTime(Calendar date) {
        mDate.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY));
        mDate.set(Calendar.MINUTE, date.get(Calendar.MINUTE));
        mDate.set(Calendar.SECOND, date.get(Calendar.SECOND));
        mDate.set(Calendar.MILLISECOND, date.get(Calendar.MILLISECOND));
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public boolean isChanged() {return mChanged;}

    public void setChanged(boolean changed) {mChanged = changed;}
}
