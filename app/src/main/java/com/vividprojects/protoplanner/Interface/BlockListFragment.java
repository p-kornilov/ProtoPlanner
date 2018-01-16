package com.vividprojects.protoplanner.Interface;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BaseTarget;
import com.vividprojects.protoplanner.CoreData.Block;
import com.vividprojects.protoplanner.DB.SDFileManager;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Images.FullTarget;
import com.vividprojects.protoplanner.Images.GlideApp;
import com.vividprojects.protoplanner.Images.PPGlideModule;
import com.vividprojects.protoplanner.PPApplication;
import com.vividprojects.protoplanner.Presenters.BlockListViewModel;
import com.vividprojects.protoplanner.R;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by Smile on 19.10.2017.
 */

public class BlockListFragment extends Fragment implements Injectable {
    private ImageView iv;
    private Button button;
    private Fragment fragment;

    private BlockListViewModel model;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Test", "onCreate - Blocks Fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Test", "onCreateView - RootListFragment");
        View rootView = inflater.inflate(R.layout.blocks_fragment, container, false);

        fragment = this;
        iv = rootView.findViewById(R.id.imageView2);

        ProgressBar pBar = rootView.findViewById(R.id.progressBar);

        Fragment fragment = this;
        button = rootView.findViewById(R.id.button_load);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LiveData<Integer> progress = model.load();
                pBar.setVisibility(ProgressBar.VISIBLE);
                progress.observe( fragment,resource -> {
                    if (resource != null) {
                        pBar.setProgress(resource);
                        if (resource==100) {
                            pBar.setVisibility(ProgressBar.INVISIBLE);
                        }
                    }
                    });
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        model = ViewModelProviders.of(getActivity(),viewModelFactory).get(BlockListViewModel.class);

        Bundle args = getArguments();

        if (args != null && args.containsKey("FILTER")){  // TODO Сделать восстановление состояния фильтра и ожет быть чего другого
            //    model.setFilter();
            model.setFilter(args.getStringArrayList("FILTER"));
        } else {
            model.setFilter(null);
        }


    }
}
