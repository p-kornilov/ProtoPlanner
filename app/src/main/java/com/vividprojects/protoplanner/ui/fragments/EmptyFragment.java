package com.vividprojects.protoplanner.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vividprojects.protoplanner.R;

/**
 * Created by Smile on 19.10.2017.
 */

public class EmptyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.empty_fragment, container, false);
    }
}