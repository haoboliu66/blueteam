package com.blueteam.utils;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.blueteam.R;

import java.util.Calendar;

public class CustomDatePickerDialogFragment extends DialogFragment implements DatePicker.OnDateChangedListener, View.OnClickListener {

    public static final String CURRENT_DATE = "datepicker_current_date";
    public static final String START_DATE = "datepicker_start_date";
    public static final String END_DATE = "datepicker_end_date";
    Calendar currentDate;
    Calendar startDate;
    Calendar endDate;

    DatePicker datePicker;
    TextView backButton;
    TextView ensureButton;
    View splitLineV;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        Bundle bundle = getArguments();
        currentDate = (Calendar) bundle.getSerializable(CURRENT_DATE);
        startDate = (Calendar) bundle.getSerializable(START_DATE);
        endDate = (Calendar) bundle.getSerializable(END_DATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (inflater == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setDimAmount(0.8f);
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int style;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            style = R.style.ZZBDatePickerDialogLStyle;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            style = R.style.ZZBDatePickerDialogLStyle;
        } else {
            style = getTheme();
        }
        return new Dialog(getActivity(), style);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            datePicker = view.findViewById(R.id.datePickerView);
            backButton = view.findViewById(R.id.back);
            backButton.setOnClickListener(this);
            ensureButton = view.findViewById(R.id.ensure);
            ensureButton.setOnClickListener(this);
            splitLineV = view.findViewById(R.id.splitLineV);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                datePicker.setCalendarViewShown(false);
                datePicker.setSpinnersShown(true);
                ensureButton.setVisibility(View.VISIBLE);
                splitLineV.setVisibility(View.VISIBLE);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                    && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                ensureButton.setVisibility(View.VISIBLE);
                splitLineV.setVisibility(View.VISIBLE);
                ViewGroup mContainer = (ViewGroup) datePicker.getChildAt(0);
                View header = mContainer.getChildAt(0);
                header.setVisibility(View.GONE);
            } else {
                ViewGroup mContainer = (ViewGroup) datePicker.getChildAt(0);
                View header = mContainer.getChildAt(0);
                header.setVisibility(View.GONE);

                //layout change
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) datePicker.getLayoutParams();
                layoutParams.bottomMargin = 10;
                datePicker.setLayoutParams(layoutParams);
            }
            initDatePicker();
        }
    }

    private void initDatePicker() {
        if (datePicker == null) {
            return;
        }
        if (currentDate == null) {
            currentDate = Calendar.getInstance();
            currentDate.setTimeInMillis(System.currentTimeMillis());
        }
        datePicker.init(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH), this);
        if (startDate != null) {
            datePicker.setMinDate(startDate.getTimeInMillis());
        }
        if (endDate != null) {


            endDate.set(Calendar.HOUR_OF_DAY, 23);
            endDate.set(Calendar.MINUTE, 59);
            endDate.set(Calendar.SECOND, 59);
            datePicker.setMaxDate(endDate.getTimeInMillis());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                dismiss();
                break;
            case R.id.ensure:
                returnSelectedDateUnderLOLLIPOP();
                break;
            default:
                break;
        }
    }

    private void returnSelectedDateUnderLOLLIPOP() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), 0, 0, 0);
            selectedDate.set(Calendar.MILLISECOND, 0);
            if (selectedDate.before(startDate) || selectedDate.after(endDate)) {
                Toast.makeText(getActivity(), "Invalid Date", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (onSelectedDateListener != null) {
            onSelectedDateListener.onSelectedDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        }
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onSelectedDateListener = null;
    }

    public interface OnSelectedDateListener {
        void onSelectedDate(int year, int monthOfYear, int dayOfMonth);
    }

    OnSelectedDateListener onSelectedDateListener;

    public void setOnSelectedDateListener(OnSelectedDateListener onSelectedDateListener) {
        this.onSelectedDateListener = onSelectedDateListener;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        if (onSelectedDateListener != null) {
            onSelectedDateListener.onSelectedDate(year, monthOfYear, dayOfMonth);
        }
        dismiss();
    }


}
