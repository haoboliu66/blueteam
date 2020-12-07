package com.blueteam.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.blueteam.R;

public class AmountView extends LinearLayout implements View.OnClickListener, TextWatcher {
    private static final String TAG = "AmountView";
    private int level = 1; //current level
    private int limit = 5; //level limit

    private OnLevelChangeListener mListener;

    private EditText etAmount;
    private Button btnDecrease;
    private Button btnIncrease;


    public AmountView(Context context) {
        super(context);
    }

    public AmountView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.number_add_sub_view, this);
        etAmount = findViewById(R.id.etAmount);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);

        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
        etAmount.addTextChangedListener(this);

        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.AmountView);
        int btnWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_btnWidth, LayoutParams.WRAP_CONTENT);
        int tvWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvWidth, 80);
        int tvTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_tvTextSize, 0);
        int btnTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView_btnTextSize, 0);
        obtainStyledAttributes.recycle();

        LayoutParams btnParams = new LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
        btnDecrease.setLayoutParams(btnParams);
        btnIncrease.setLayoutParams(btnParams);
        if (btnTextSize != 0) {
            btnDecrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
            btnIncrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
        }

        LayoutParams textParams = new LayoutParams(tvWidth, LayoutParams.MATCH_PARENT);
        etAmount.setLayoutParams(textParams);
        if (tvTextSize != 0) {
            etAmount.setTextSize(tvTextSize);
        }
    }

    public void setOnLevelChangeListener(OnLevelChangeListener onLevelChangeListener) {
        this.mListener = onLevelChangeListener;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLevel() {
        return level;
    }

    // button on click
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnDecrease) {
            Log.d(TAG, "decrement clicked");
            System.out.println("level: ---> " + level);
            System.out.println("limit: ---> " + limit);
            if (level > 1) {
                level--;

                Log.d(TAG, "onClick:===== " + level);
                etAmount.setText(String.valueOf(level));
            }
        } else if (i == R.id.btnIncrease) {
            Log.d(TAG, "increment clicked");
            System.out.println("level: +++> " + level);
            System.out.println("limit: +++> " + limit);
            if (level < limit) {
                level++;

                etAmount.setText(String.valueOf(level));
            }
        }
        //clear focus
        etAmount.clearFocus();

        if (mListener != null) {
            if(level == 1 || level == 5){
                mListener.LevelChange(this, level);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty())
            return;
        level = Integer.valueOf(s.toString());
        if (level > limit) {
            etAmount.setText(String.valueOf(limit));
            return;
        }

        if (mListener != null) {
            if(level == 1 || level == 5){
                mListener.LevelChange(this, level);
            }
        }
    }

    public interface OnLevelChangeListener {
        void LevelChange(View view, int level);
    }

}


