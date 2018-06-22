package com.vividprojects.protoplanner.ui.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vividprojects.protoplanner.di.Injectable;
import com.vividprojects.protoplanner.images.GlideApp;
import com.vividprojects.protoplanner.viewmodels.ImageViewViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.widgets.ZoomImageView;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by Smile on 19.10.2017.
 */

public class ImageViewFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private ImageViewViewModel model;

    private int position;
    private ZoomImageView imageView;
    //private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Test", "onCreate - Records Fragment");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Test", "onCreateView - RootListFragment");
        View v = (View) inflater.inflate(R.layout.image_view_fragment, container, false);
        imageView = (ZoomImageView) v.findViewById(R.id.iv_iv);
    //    textView = v.findViewById(R.id.iv_name);
//        recycler.setAdapter(new TestRecyclerAdapter(getActivity()));
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        model = ViewModelProviders.of(getActivity(),viewModelFactory).get(ImageViewViewModel.class);

        Bundle args = getArguments();

        if (args != null && args.containsKey("POSITION")){
        //    model.setFilter();
            position = args.getInt("POSITION");
        } else {
            position = 0;
        }

        //textView.setText("Position " + position);

        model.getImages().observe(this,resource -> {
            if (resource != null)
                GlideApp.with(imageView)
                        .load(new File(resource.first.get(position)))
                        .error(R.drawable.ic_error_outline_white_24dp)
                        .into(imageView);
        });
    }
}