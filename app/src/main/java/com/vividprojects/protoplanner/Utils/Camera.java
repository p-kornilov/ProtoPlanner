package com.vividprojects.protoplanner.Utils;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;

import com.vividprojects.protoplanner.Images.BitmapUtils;

import java.io.File;
import java.io.IOException;

public class Camera {
    private static final String FILE_PROVIDER_AUTHORITY = "com.vividprojects.protoplanner.file_provider";

    public static void launchCamera(Fragment master, File photoFile, int requestImageCapture ) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(master.getActivity().getPackageManager()) != null) {
            if (photoFile != null) {
                //mTempPhotoPath = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(master.getContext().getApplicationContext(),FILE_PROVIDER_AUTHORITY,photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                master.startActivityForResult(takePictureIntent, requestImageCapture);
            }
        }
    }

    public static File prepareTemp(Fragment master) {
        File photoFile = null;
        try {
            photoFile = BitmapUtils.createTempImageFile(master.getContext().getApplicationContext());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return photoFile;
    }
}
