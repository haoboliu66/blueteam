package com.blueteam.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blueteam.R;

public class WikiFragment extends Fragment {

    private ImageButton button1;

    private ImageButton button2;

    private ImageButton button3;

    private WebView web;

    private final String SPINE_HEALTH = "https://www.spine-health.com/";
    private final String WORKOUT = "https://www.spine-health.com/blog/7-tips-protect-your-lower-back";
    private final String RECOVERY = "https://www.youtube.com/watch?v=DWmGArQBtFI&t=329s";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("WikiFragment", "inflate WikiFragment");
        View view = inflater.inflate(R.layout.fragment_wiki, null);

        initView(view);

        initEvent();

        return view;
    }


    public void initView(View view){
        button1 = view.findViewById(R.id.spine_health);
        button2 = view.findViewById(R.id.workout);
        button3 = view.findViewById(R.id.recovery);
        web = view.findViewById(R.id.web);
    }

    public void initEvent(){
        // link to https://www.spine-health.com/
        button1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Toast.makeText(getContext(), "Directing...", Toast.LENGTH_SHORT);
                    Uri uri = Uri.parse(SPINE_HEALTH);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                return false;
            }
        });


        // link to https://www.spine-health.com/blog/7-tips-protect-your-lower-back
        button2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Toast.makeText(getContext(), "Directing...", Toast.LENGTH_SHORT);
                    Uri uri = Uri.parse(WORKOUT);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                return false;
            }
        });

        // link to https://www.youtube.com/watch?v=DWmGArQBtFI&t=329s
        button3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Toast.makeText(getContext(), "Directing...", Toast.LENGTH_SHORT);
                    Uri uri = Uri.parse(RECOVERY);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                return false;
            }
        });



    }


}
