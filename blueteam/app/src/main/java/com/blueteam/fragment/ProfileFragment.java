package com.blueteam.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blueteam.R;
import com.blueteam.view.CircularImage;

public class ProfileFragment extends Fragment {

    CircularImage photo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("ProfileFragment", "inflate ProfileFragment");
        View view = inflater.inflate(R.layout.fragment_profile, null);

        // set profile photo
        photo = view.findViewById(R.id.cover_user_photo);
        photo.setImageResource(R.drawable.profile_img);

        return view;
    }


}
