package com.vividprojects.protoplanner.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
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

import com.vividprojects.protoplanner.di.Injectable;
import com.vividprojects.protoplanner.images.GlideApp;
import com.vividprojects.protoplanner.viewmodels.BlockListViewModel;
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
    private ProgressBar pBar;

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

        pBar = rootView.findViewById(R.id.progressBar);

        Fragment fragment = this;
        button = rootView.findViewById(R.id.button_load);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlideApp.with(iv)
                        .load(new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),"img_f_c3c59002-5a86-3c7e-b7ed-93f2c79255de.jpg"))
                        .into(iv);
                model.load("http://anub.ru/uploads/07.2015/976_podborka_34.jpg");
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

        model.getLoadProgress().observe(this,progress->{
            if (progress != null) {
                pBar.setVisibility(ProgressBar.VISIBLE);
                pBar.setProgress(progress);
                if (progress==100) {
                    pBar.setVisibility(ProgressBar.INVISIBLE);
                }
            }
        });


    }
}
