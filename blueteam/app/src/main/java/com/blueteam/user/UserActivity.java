package com.blueteam.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blueteam.R;
import com.blueteam.observable.CurvedLevel;
import com.blueteam.view.AmountView;

import java.util.Observable;
import java.util.Observer;

/**
 * main UI deprecated /v2
 */
public class UserActivity extends AppCompatActivity implements Observer {

    // bluetooth data imitation
    CurvedLevel curvedLevel = CurvedLevel.getInstance();

    ImageView imageView;

    // vibration level
    public static int userSetting;

    //self-defined sub-add view
    private AmountView mAmountView;

    private Button btn_confirm;

    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        initView();

        initEvent();

    }


    private void initView(){
        imageView = findViewById(R.id.main_pic);

        // set level change && confirm button
        mAmountView = findViewById(R.id.amount_view);

//        mAmountView.setLimit(userSetting);
        btn_confirm = findViewById(R.id.btn_confirm);

    }


    private void initEvent(){
        mAmountView.setOnLevelChangeListener(new AmountView.OnLevelChangeListener() {
            @Override
            public void LevelChange(View view, int level) {
                String level1 = "Reach Minimum";
                String level5 = "Reach Maximum";
                Toast.makeText(getApplicationContext(), level == 1 ? level1 : level5, Toast.LENGTH_SHORT).show();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSetting = mAmountView.getLevel();
                Toast.makeText(UserActivity.this, "Current Level Set: " + mAmountView.getLevel(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void update(Observable o, Object arg) {
        // load resources according to curvedLevel
        switch (((CurvedLevel) o).getLevel()) {
            case 2:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img2);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img3);
                break;
            case 4:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img4);
                break;
            case 5:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img5);
                break;
            default:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img1);
        }
        imageView.setImageBitmap(bitmap);
    }




}
