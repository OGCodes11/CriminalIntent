package database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.criminalintent.Crime;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        GregorianCalendar c = (GregorianCalendar) Calendar.getInstance();
        c.setTimeInMillis(date);
        crime.setDate(c);
        crime.setTime(c);
        crime.setSolved(isSolved != 0);

        return crime;
    }
}
