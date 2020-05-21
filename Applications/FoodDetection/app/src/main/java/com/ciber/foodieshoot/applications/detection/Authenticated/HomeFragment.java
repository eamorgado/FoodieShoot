package com.ciber.foodieshoot.applications.detection.Authenticated;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ciber.foodieshoot.applications.detection.R;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home,container,false);
        ScrollView sc = (ScrollView) v.findViewById(R.id.instructions);
        sc.setNestedScrollingEnabled(true);
        return v;
    }
}
