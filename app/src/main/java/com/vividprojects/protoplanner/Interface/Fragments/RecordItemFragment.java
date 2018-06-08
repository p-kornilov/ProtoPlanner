package com.vividprojects.protoplanner.Interface.Fragments;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vividprojects.protoplanner.BindingModels.RecordItemBindingModel;
import com.vividprojects.protoplanner.BindingModels.VariantItemBindingModel;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Images.BitmapUtils;
import com.vividprojects.protoplanner.Interface.Dialogs.EditTextDialog;
import com.vividprojects.protoplanner.Interface.Helpers.DialogFullScreenHelper;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.Interface.RecordAddImageURLDialog;
import com.vividprojects.protoplanner.MainActivity;
import com.vividprojects.protoplanner.Utils.ItemActionsShop;
import com.vividprojects.protoplanner.ViewModels.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.RunnableParam;
import com.vividprojects.protoplanner.databinding.RecordFragmentBinding;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Smile on 31.10.2017.
 */

public class RecordItemFragment extends Fragment implements Injectable, ItemActionsShop {

    public static final String RECORD_ID = "RECORD_ID";
    private static final String FILE_PROVIDER_AUTHORITY = "com.vividprojects.protoplanner.file_provider";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static final int REQUEST_IMAGE_URL_LOAD = 3;
    private static final int REQUEST_LABELS_SET = 4;
    private static final int REQUEST_EDIT_NAME = 5;
    private static final int REQUEST_EDIT_COMMENT = 6;
    private static final int REQUEST_EDIT_VARIANT = 7;
    private static final int REQUEST_EDIT_SHOP = 8;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 11;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    private RecyclerView shopsRecycler;
    private RecyclerView alternativesRecycler;
  //  private ImageButton add_image;
    private RecordItemViewModel model;
    private boolean empty = false;

    private String mTempPhotoPath;

    private RecordFragmentBinding binding;
    private RecordItemBindingModel bindingModelRecord;
    private VariantItemBindingModel bindingModelVariant;

    private RunnableParam<Integer> onImageSelect = (position)->{
        navigationController.openImageView(position,bindingModelVariant.getVariantId());
    };

    private Runnable onCommentEditClick = () -> {
        EditTextDialog editNameDialog = new EditTextDialog();
        editNameDialog.setTargetFragment(this, REQUEST_EDIT_COMMENT);
        Bundle b = new Bundle();
        b.putString("TITLE","Edit");
        b.putString("HINT","Comment");
        b.putString("POSITIVE","Save");
        b.putString("NEGATIVE","Cancel");
        b.putString("EDITTEXT", bindingModelRecord.getRecordComment());
        editNameDialog.setArguments(b);
        editNameDialog.show(getFragmentManager(), "Edit comment");
    };

    private Runnable onLabelsEditClick = () -> {
        Label.Plain[] labels = bindingModelRecord.getRecordLabels();
        int size = labels.length;
        String[] ids = new String[size];
        for (int i = 0; i < size; i++)
            ids[i] = labels[i].id;

        NavigationController.openLabelsForResult(ids,this, REQUEST_LABELS_SET);
    };

    private Runnable onVariantEditClick = () -> {
        Bundle b = new Bundle();
        b.putString("ID", bindingModelVariant.getVariantId());
        DialogFullScreenHelper.showDialog(DialogFullScreenHelper.DIALOG_VARIANT, this, !navigationController.isTablet(), REQUEST_EDIT_VARIANT, b);
    };

    private RunnableParam<View> onAddImageClick = (view) -> {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mli_url:
                        RecordAddImageURLDialog addImageURLDialog = new RecordAddImageURLDialog();
                        addImageURLDialog.setTargetFragment(RecordItemFragment.this, REQUEST_IMAGE_URL_LOAD);
                        addImageURLDialog.show(getFragmentManager(), "Add_image_url");
                        return true;
                    case R.id.mli_gallery:
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        } else {
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(i, REQUEST_IMAGE_GALLERY);
                        }
                        return true;
                    case R.id.mli_foto:
                        launchCamera();
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_load_image, popup.getMenu());
        popup.show();
    };

    private Runnable onAddShopClick = () -> {
        Bundle b = new Bundle();
        b.putString("ID", "");
        b.putString("VARIANTID", bindingModelVariant.getVariantId());
        DialogFullScreenHelper.showDialog(DialogFullScreenHelper.DIALOG_SHOP, this, !navigationController.isTablet(), REQUEST_EDIT_SHOP, b);
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("Test", "onCreate - Record Fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Test", "onCreateView - Record Fragment");
        Bundle args = getArguments();

        if (args != null && args.containsKey(RECORD_ID)){
            //    model.setFilter();
            if (args.getString(RECORD_ID).equals("Empty"))
                empty = true;
            else
                empty= false;
        }

        View v;

        if (empty) {
            //v = (View) inflater.inflate(R.layout.empty_item_fragment, container, false);
            return inflater.inflate(R.layout.empty_item_fragment, container, false);
        } else {
            //v = (View) inflater.inflate(R.layout.record_fragment, container, false);
            binding = RecordFragmentBinding.inflate(inflater);
            //return binding.getRoot();
            v = binding.getRoot();  // TODO Сделать правильно (удалить v)

/*            shopsRecycler = v.findViewById(R.id.rf_shops_recycler);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setAutoMeasureEnabled(true);
            shopsRecycler.setLayoutManager(layoutManager);
            shopsRecycler.setNestedScrollingEnabled(false);
            shopsRecycler.setFocusable(false);*/

            alternativesRecycler = (RecyclerView) v.findViewById(R.id.rf_alternatives_recycler);

            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
            layoutManager2.setAutoMeasureEnabled(true);
            alternativesRecycler.setLayoutManager(layoutManager2);
            alternativesRecycler.setNestedScrollingEnabled(false);
            alternativesRecycler.setFocusable(false);

        };

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (navigationController.isTablet()) {
            ((MainActivity) getActivity()).getSecondToolBar().getMenu().clear();
            ((MainActivity) getActivity()).getSecondToolBar().inflateMenu(R.menu.menu_record);
            ((MainActivity) getActivity()).getSecondToolBar().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onOptionsItemSelected(item);
                }
            });
        } else {
            if (menu.size() == 0)
                inflater.inflate(R.menu.menu_record, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.ra_edit_name:
                EditTextDialog editNameDialog = new EditTextDialog();
                editNameDialog.setTargetFragment(this, REQUEST_EDIT_NAME);
                Bundle b = new Bundle();
                b.putString("TITLE","Edit record name");
                b.putString("HINT","Name");
                b.putString("POSITIVE","Save");
                b.putString("NEGATIVE","Cancel");
                b.putString("EDITTEXT", bindingModelRecord.getRecordName());
                editNameDialog.setArguments(b);
                editNameDialog.show(getFragmentManager(), "Edit name");
                return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
/*        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.
        inflater.inflate(R.menu.context_menu, menu);*/
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(getContext().getApplicationContext());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                mTempPhotoPath = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(getContext().getApplicationContext(),FILE_PROVIDER_AUTHORITY,photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    bindingModelVariant.initImageLoad();
                    model.loadCameraImage(mTempPhotoPath);
                } else {
                    BitmapUtils.deleteImageFile(getContext().getApplicationContext(), mTempPhotoPath);
                }
                return;
            case REQUEST_IMAGE_GALLERY:
                if (resultCode == RESULT_OK && data != null) {
                    bindingModelVariant.initImageLoad();
                    model.loadGalleryImage(data.getData());
                }
                return;
            case REQUEST_IMAGE_URL_LOAD:
                if (resultCode == RESULT_OK && data != null) {
                    String url = data.getExtras().get("URL").toString();
                    bindingModelVariant.initImageLoad();
                    model.loadImage(url);
                }
                return;
            case REQUEST_LABELS_SET:
                if (resultCode == RESULT_OK && data != null) {
                    String[] t = data.getStringArrayExtra("SELECTED");
                    model.setLabels(data.getStringArrayExtra("SELECTED"));
                }
                return;
            case REQUEST_EDIT_NAME:
                if (resultCode == RESULT_OK && data != null) {
                    model.setRecordName(data.getStringExtra("EDITTEXT"));
                }
                return;
            case REQUEST_EDIT_COMMENT:
                if (resultCode == RESULT_OK && data != null) {
                    String comment = data.getStringExtra("EDITTEXT");
                    model.setComment(comment);
                    bindingModelRecord.setRecordComment(comment);
                    // Другой вариант
                    //model.setComment(comment).observe(this, c -> {
                    //    bindingModelRecord.setRecordComment(comment);
                    //});
                }
                return;
            case REQUEST_EDIT_VARIANT:
                if (resultCode == RESULT_OK && data != null) {
                    String id = data.getStringExtra("ID");
                    model.saveMainVariant(id);
                }
                return;
            case REQUEST_EDIT_SHOP:
                if (resultCode == RESULT_OK && data != null) {
                    String id = data.getStringExtra("ID");
                    model.refreshShop(id);
                }
                return;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if (!empty) {
            model = ViewModelProviders.of(getActivity(), viewModelFactory).get(RecordItemViewModel.class);

            bindingModelRecord = model.getBindingModelRecord();
            bindingModelRecord.setOnCommentEditClick(onCommentEditClick);
            bindingModelRecord.setOnLabelsEditClick(onLabelsEditClick);
            binding.setRecordModel(bindingModelRecord);

            bindingModelVariant = model.getBindingModelVariant();
            bindingModelVariant.setOnEditClick(onVariantEditClick);
            bindingModelVariant.setImagesAdapter(onImageSelect);
            bindingModelVariant.setOnAddImageClick(onAddImageClick);
            bindingModelVariant.setOnAddShopClick(onAddShopClick);
            bindingModelVariant.setContext(this);
            binding.setVariantModel(bindingModelVariant);

            Bundle args = getArguments();

            if (args != null && args.containsKey(RECORD_ID)) {
                //    model.setFilter();
                model.setId(args.getString(RECORD_ID));
            } else {
                model.setId(null);
            }

            model.getRecordItem().observe(this, resource -> {
                if (resource != null && resource.data != null)
                    bindingModelRecord.setRecord(resource.data);
            });

            if (navigationController.isTablet())
                model.getRecordName().observe(this, name -> {
                    if (name != null)
                        ((MainActivity) getActivity()).getSecondToolBar().setTitle(name);
                });

            model.getMainVariantItem().observe(this, resource -> {
                if (resource != null ) {
                    bindingModelVariant.setVariant(resource);
                }
            });

            model.getLoadProgress().observe(this, (progress) -> {
                if (progress != null)
                    bindingModelVariant.setLoadProgress(progress);
            });

            model.getRefreshedShop().observe(this, shop -> {
                if (shop != null && shop.data != null)
                    bindingModelVariant.refreshShop(shop.data);
            });
        }
    }

    public static RecordItemFragment create(String id) {
        RecordItemFragment recordItemFragment = new RecordItemFragment();
        Bundle args = new Bundle();
        args.putString(RECORD_ID,id);
        recordItemFragment.setArguments(args);
        return recordItemFragment;
    }

    @Override
    public void itemShopDelete(String id) {
        model.deleteShop(id);
    }

    @Override
    public void itemShopEdit(String item) {
        Bundle b = new Bundle();
        b.putString("ID", item);
        b.putString("VARIANTID", bindingModelVariant.getVariantId());
        DialogFullScreenHelper.showDialog(DialogFullScreenHelper.DIALOG_SHOP, this, !navigationController.isTablet(), REQUEST_EDIT_SHOP, b);
    }

    @Override
    public void itemShopPrimary(String id) {
        model.setShopPrimary(id);
    }
}
