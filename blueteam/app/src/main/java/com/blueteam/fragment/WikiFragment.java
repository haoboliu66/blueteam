package com.blueteam.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blueteam.R;

public class WikiFragment extends Fragment {

    ImageButton button1;

    ImageButton button2;

    ImageButton button3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("WikiFragment", "inflate WikiFragment");
        View view = inflater.inflate(R.layout.fragment_wiki, null);

//        button1 = view.findViewById(R.id.spine_health);
//        button2 = view.findViewById(R.id.workout);
//        button3 = view.findViewById(R.id.recovery);

        return view;
    }


}
