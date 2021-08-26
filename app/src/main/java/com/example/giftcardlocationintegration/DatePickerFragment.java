package com.example.giftcardlocationintegration;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {

    private String TAG = "DatePickerFragment";
    private DatePickerCallback datePickerCallback;

    public interface DatePickerCallback {
        void finishedPickingDate(Date userSelectedDate);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        Date userSelectedDate = c.getTime();
        datePickerCallback.finishedPickingDate(userSelectedDate);
        Log.i(TAG, "Selected date: " + userSelectedDate);
    }

    public static DatePickerFragment newInstance() {
        return new DatePickerFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        datePickerCallback = (DatePickerCallback)getTargetFragment();
    }
}
