package com.vividprojects.protoplanner.ui.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.vividprojects.protoplanner.bindingmodels.VariantItemBindingModel;
import com.vividprojects.protoplanner.images.BitmapUtils;
import com.vividprojects.protoplanner.ui.NavigationController;
import com.vividprojects.protoplanner.viewmodels.VariantItemViewModel;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class VariantFragmentHelper {

    public static final int REQUEST_IMAGE_CAPTURE = 101;
    public static final int REQUEST_IMAGE_GALLERY = 102;
    public static final int REQUEST_IMAGE_URL_LOAD = 103;
    public static final int REQUEST_IMAGE_SHOW = 104;
    public static final int REQUEST_EDIT_VARIANT = 107;
    public static final int REQUEST_EDIT_SHOP = 108;
    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 111;

    public static void itemShopDelete(String id, VariantItemViewModel model) {
        model.deleteShop(id);
    }

    public static void itemShopEdit(String item, VariantItemBindingModel bindingModel, boolean isTablet, Fragment fragment) {
        Bundle b = new Bundle();
        b.putString("ID", item);
        b.putString("VARIANTID", bindingModel.getVariantId());
        DialogFullScreenHelper.showDialog(DialogFullScreenHelper.DIALOG_SHOP, fragment, !isTablet, REQUEST_EDIT_SHOP, b);
    }

    public static void itemShopPrimary(String id, VariantItemViewModel model) {
        model.setShopPrimary(id);
    }

    public static void itemVariantDelete(String id) {


    }

    public static void itemVariantOpen(String id, Fragment fragment) {
        NavigationController.openVariantForResult(id, fragment);
    }

    public static void itemVariantBasic(String id) {

    }

    public static boolean onActivityResult(int requestCode, int resultCode, Intent data, VariantItemViewModel model, VariantItemBindingModel bindingModel, Context context) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    bindingModel.initImageLoad();
                    model.loadCameraImage();
                } else {
                    BitmapUtils.deleteImageFile(context.getApplicationContext(), model.getTempPhotoPath());
                }
                return true;
            case REQUEST_IMAGE_GALLERY:
                if (resultCode == RESULT_OK && data != null) {
                    bindingModel.initImageLoad();
                    model.loadGalleryImage(data.getData());
                }
                return true;
            case REQUEST_IMAGE_URL_LOAD:
                if (resultCode == RESULT_OK && data != null) {
                    String url = data.getExtras().get("URL").toString();
                    bindingModel.initImageLoad();
                    model.loadImage(url);
                }
                return true;
            case REQUEST_IMAGE_SHOW:
                if ((resultCode == RESULT_OK || resultCode == RESULT_CANCELED) && data != null) {
                    int position = data.getExtras().getInt("DEFAULT_IMAGE", 0);
                    bindingModel.defaultImageChanged(position);
                    model.setDefaultImage(position);
                }
                return true;
            case REQUEST_EDIT_VARIANT:
                if (resultCode == RESULT_OK && data != null) {
                    String id = data.getStringExtra("ID");
                    model.saveVariant(id);
                }
                return true;
            case REQUEST_EDIT_SHOP:
                if (resultCode == RESULT_OK && data != null) {
                    String id = data.getStringExtra("ID");
                    model.refreshShop(id);
                }
                return true;
        }
        return false;
    }

    public static void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
