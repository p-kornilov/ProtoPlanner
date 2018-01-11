package com.vividprojects.protoplanner.Interface;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vividprojects.protoplanner.CoreData.Block;
import com.vividprojects.protoplanner.DB.SDFileManager;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Images.GlideApp;
import com.vividprojects.protoplanner.Images.PPGlideModule;
import com.vividprojects.protoplanner.PPApplication;
import com.vividprojects.protoplanner.R;

import javax.inject.Inject;

/**
 * Created by Smile on 19.10.2017.
 */

public class BlockFragment extends Fragment implements Injectable {
    private ImageView iv;
    private Button button;
    private Fragment fragment;
    @Inject
    SDFileManager fileManager;   // TODO Заинжектить dataRepository и из нее раскрутить/проверить всю цепочку, потом перенести логику в model

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

        button = rootView.findViewById(R.id.button_load);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Log.d("Test", "External Storage - " + getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                fileManager.test();
                GlideApp.with(fragment)
                     //  .load("http://anub.ru/uploads/07.2015/976_podborka_34.jpg")
                        .load(R.raw.testpicture)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(iv);
            }
        });

        return rootView;
    }
}
