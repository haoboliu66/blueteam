package com.blueteam.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blueteam.R;
import com.blueteam.utils.CustomDatePickerDialogFragment;

import java.util.Calendar;

public class ReportFragment extends Fragment implements View.OnClickListener, CustomDatePickerDialogFragment.OnSelectedDateListener {

    Button reportButton;

    Button dateButton;

    long day = 24 * 60 * 60 * 1000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("ReportFragment", "inflate ReportFragment");
        View view = inflater.inflate(R.layout.fragment_report, null);

        initView(view);

        initEvent();

        return view;
    }


    public void initView(View view) {
        dateButton = view.findViewById(R.id.datepicker);
        reportButton = view.findViewById(R.id.report_btn);
    }

    public void initEvent() {
        dateButton.setOnClickListener(this);

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Toast.makeText(context, "Finding Report For You!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.datepicker:
                showDatePickDialog();
                break;
            default:
                break;
        }
    }

    /**
     * window for data pick
     */
    private void showDatePickDialog() {
        CustomDatePickerDialogFragment fragment = new CustomDatePickerDialogFragment();
        fragment.setOnSelectedDateListener(this);

        Bundle bundle = new Bundle();
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTimeInMillis(System.currentTimeMillis());

        // java.util.Calendar
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        bundle.putSerializable(CustomDatePickerDialogFragment.CURRENT_DATE, currentDate);


        long start = currentDate.getTimeInMillis() - day * 2;
        long end = currentDate.getTimeInMillis() - day;

        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(start);
        Calendar endDate = Calendar.getInstance();
        endDate.setTimeInMillis(end);

        bundle.putSerializable(CustomDatePickerDialogFragment.START_DATE, startDate);
        bundle.putSerializable(CustomDatePickerDialogFragment.END_DATE, currentDate);

        fragment.setArguments(bundle);
        fragment.show(getActivity().getSupportFragmentManager(), CustomDatePickerDialogFragment.class.getSimpleName());
    }

    @Override
    public void onSelectedDate(int year, int monthOfYear, int dayOfMonth) {
        Toast.makeText(getContext(), year + "/" + (monthOfYear + 1) + "/" + dayOfMonth + "/", Toast.LENGTH_SHORT).show();
    }


}
