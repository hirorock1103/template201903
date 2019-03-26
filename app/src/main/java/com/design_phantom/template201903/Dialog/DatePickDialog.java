package com.design_phantom.template201903.Dialog;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.design_phantom.template201903.Common.Common;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DatePickDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private String target;

    private DatePickResultListener listener;

    public interface DatePickResultListener {
        public void datePickResultNotice(String yyyyMmdd, String targetPlace);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DatePickResultListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        try {
            target = getArguments().getString("target");
        } catch (Exception e) {
            target = null;
        }

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String str = String.format(Locale.JAPAN, "%d/%d/%d", year, month + 1, dayOfMonth);
        Date date = Common.getDateByStr(str, Common.DATE_FORMAT_SAMPLE_1);
        String dateNew = Common.formatDate(date, Common.DATE_FORMAT_SAMPLE_1);
        listener.datePickResultNotice(dateNew, target);
    }
}