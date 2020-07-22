package com.ione.iseller;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.DatePicker;

public class BirthDatePickerPopUp extends DialogFragment implements
        DatePickerDialog.OnDateSetListener{
    Communicator communicator;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Getting activity and make it referred with communicator reference.
        communicator = (Communicator) getActivity();

        // Getting current date as default date.
        final Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int dayOfMonth = calender.get(Calendar.DAY_OF_MONTH);

        // Creating new instance of DatePicker and returning it
        return new DatePickerDialog(getActivity(), this, year, month, dayOfMonth);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        communicator.communication(year, month, dayOfMonth);
    }

    interface Communicator {
        void communication(int year, int month, int dayOfMonth);
    }
}
