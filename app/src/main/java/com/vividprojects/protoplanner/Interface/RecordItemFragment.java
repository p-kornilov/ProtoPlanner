package com.vividprojects.protoplanner.Interface;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.vividprojects.protoplanner.Adapters.HorizontalImagesListAdapter;
import com.vividprojects.protoplanner.Adapters.ShopsAdapter;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Images.BitmapUtils;
import com.vividprojects.protoplanner.Network.NetworkLoader;
import com.vividprojects.protoplanner.Presenters.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.PriceFormatter;
import com.vividprojects.protoplanner.Widgets.Chip;
import com.vividprojects.protoplanner.Widgets.ChipsLayout;
import com.vividprojects.protoplanner.Widgets.HorizontalImages;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Smile on 31.10.2017.
 */

public class RecordItemFragment extends Fragment implements Injectable {

    public static final String RECORD_ID = "RECORD_ID";
    private static final String FILE_PROVIDER_AUTHORITY = "com.vividprojects.protoplanner.file_provider";
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    ChipsLayout chl;
//    private Realm realm;
    private boolean inCommentEdit = false;
    private ViewSwitcher commentSwitcher;
    private EditText commentEdit;
    private TextView commentView;
    private RecyclerView shopsRecycler;
  //  private HorizontalImages images;
    private RecyclerView alternativesRecycler;
    private TextView mvTitle;
    private TextView mvPrice;
    private TextView mvValue;
    private TextView mvCount;
    private RecyclerView imagesRecycler;
    private ImageButton add_image;
    private HorizontalImagesListAdapter imagesListAdapter;
   // private TextView mvCurrency1;
    private TextView mvCurrency2;
    private RecordItemViewModel model;
    private PopupMenu loadImagePopup;

    private String mTempPhotoPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Test", "onCreate - Record Fragment");
//        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Test", "onCreateView - Record Fragment");
        View v = (View) inflater.inflate(R.layout.record_fragment, container, false);

        shopsRecycler = v.findViewById(R.id.rf_shops_recycler);
        mvTitle = v.findViewById(R.id.alt_title);
        mvPrice = v.findViewById(R.id.alt_price);
        mvValue = v.findViewById(R.id.alt_value);
        mvCount = v.findViewById(R.id.alt_count);
       // mvCurrency1 = v.findViewById(R.id.alt_currency1);
       // mvCurrency2 = v.findViewById(R.id.alt_currency2);

    //    RealmResults<VariantInShop> ls = realm.where(VariantInShop.class).equalTo("variant.title","Фильтр для воды").findAll();
    //!!!    lv.setAdapter(new ShopsAdapter(ls));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);
        shopsRecycler.setLayoutManager(layoutManager);
        shopsRecycler.setNestedScrollingEnabled(false);
        shopsRecycler.setFocusable(false);

        chl = v.findViewById(R.id.chipLayout);

/*        images = (HorizontalImages) v.findViewById(R.id.rf_images);
        images.setNoneImage(true);*/
        imagesRecycler = (RecyclerView) v.findViewById(R.id.ai_images);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        imagesRecycler.setLayoutManager(layoutManager3);
        imagesRecycler.setNestedScrollingEnabled(false);
        imagesRecycler.setFocusable(false);
        imagesListAdapter = new HorizontalImagesListAdapter(null,null);
        imagesRecycler.setAdapter(imagesListAdapter);
        ((SimpleItemAnimator) imagesRecycler.getItemAnimator()).setSupportsChangeAnimations(false);


        add_image = (ImageButton) v.findViewById(R.id.rf_add_image);
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(getContext(), view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mli_url:
                                RecordAddImageURLDialog addImageURLDialog = new RecordAddImageURLDialog();
                                addImageURLDialog.setOnOK((url)->{
                                    imagesRecycler.scrollToPosition(imagesListAdapter.loadingInProgress(0));
                                    model.loadImage(url).observe(getActivity(),progressObserver);
                                });
                                addImageURLDialog.show(getFragmentManager(),"Add_image_url");
                                return true;
                            case R.id.mli_gallery:

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
            }
        });

        alternativesRecycler = (RecyclerView) v.findViewById(R.id.rf_alternatives_recycler  );
    //    RealmResults<Record> ar = realm.where(Record.class).findAllAsync();

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setAutoMeasureEnabled(true);
        alternativesRecycler.setLayoutManager(layoutManager2);
        alternativesRecycler.setNestedScrollingEnabled(false);
        alternativesRecycler.setFocusable(false);

        commentSwitcher = (ViewSwitcher) v.findViewById(R.id.rf_comment_switcher);
        commentEdit = (EditText) v.findViewById(R.id.rf_comment_edit);
        commentView = (TextView) v.findViewById(R.id.rf_comment_text);

       // registerForContextMenu(add_image);

        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
/*        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = this.
        inflater.inflate(R.menu.context_menu, menu);*/
    }

    private void launchCamera() {

        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(getContext().getApplicationContext());
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                mTempPhotoPath = photoFile.getAbsolutePath();

                // Get the content URI for the image file
                Uri photoURI = FileProvider.getUriForFile(getContext().getApplicationContext(),FILE_PROVIDER_AUTHORITY,photoFile);

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imagesRecycler.scrollToPosition(imagesListAdapter.loadingInProgress(0));
            model.loadCameraImage(mTempPhotoPath).observe(getActivity(),progressObserver);
        } else {
            BitmapUtils.deleteImageFile(getContext().getApplicationContext(), mTempPhotoPath);
        }
    }

    private void processAndSetImage() {

        // Resample the saved image to fit the ImageView
/*        mResultsBitmap = BitmapUtils.resamplePic(getContext().getApplicationContext(), mTempPhotoPath);

        mResultsBitmap = Emojifier.detectFacesAndOverlayEmoji(this, mResultsBitmap);

        // Set the new bitmap to the ImageView
        mImageView.setImageBitmap(mResultsBitmap);*/
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

       // final RecordItemViewModel model = ViewModelProviders.of(getActivity(),viewModelFactory).get(RecordItemViewModel.class);

        model = ViewModelProviders.of(getActivity(),viewModelFactory).get(RecordItemViewModel.class);

        Bundle args = getArguments();

        if (args != null && args.containsKey(RECORD_ID)){
            //    model.setFilter();
            model.setId(args.getString(RECORD_ID));
        } else {
            model.setId(null);
        }

        model.getRecordItem().observe(this,resource -> {
            if (resource != null && resource.data != null) {
                commentView.setText(resource.data.comment);
                chl.removeAllViews();
                chl.noneChip(getContext());
/*                for (Label label : resource.data.getLabels()) {
                    Chip chip = new Chip(getContext());
                    chip.setTitle(label.getName());
                    chip.setColor(label.getColor());
                    chl.addView(chip);
                }*/
            }
        });

        model.getMainVariantItem().observe(this,resource ->{
            if (resource != null && resource.data != null) {
                mvTitle.setText(resource.data.title);
                mvCount.setText(PriceFormatter.getCount(resource.data.count,resource.data.measure));
                mvValue.setText(PriceFormatter.getValue(resource.data.currency,resource.data.price*resource.data.count));
                mvPrice.setText(PriceFormatter.getPrice(resource.data.currency,resource.data.price,resource.data.measure));
                imagesListAdapter.setData(resource.data.small_images);
            }
        });

        if (model.isInImageLoading())
            model.getLoadProgress().observe(this,progressObserver);

      //  model.getLp().observe(this,p->{});
    }

    Observer<Integer> progressObserver = (progress) -> {
        if (progress != null) {
            if (progress>=0 && progress <=100) {
                imagesListAdapter.loadingInProgress(progress);
            }
            if (progress == DataRepository.LOAD_ERROR) {
                AlertDialog alert = new AlertDialog.Builder(getContext()).create();
                alert.setTitle("Error");
                alert.setMessage("Enable to load image");
                alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                imagesListAdapter.loadingDone(false, "");
                            }
                        });
                alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                        imagesListAdapter.loadingDone(false, "");
                    }
                });
                alert.show();
            }
            if (progress == DataRepository.LOAD_DONE) {
                imagesListAdapter.imageReady();
                //imagesListAdapter.setData(resource.data.small_images);
                // imagesRecycler.
            }
            if (progress == DataRepository.SAVE_TO_DB_DONE) {
                imagesListAdapter.loadingDone(true, model.getLoadedImage());
                //imagesListAdapter.setData(resource.data.small_images);
                // imagesRecycler.
            }
        }
    };

    void addChip() {
        Chip chip4 = new Chip(getContext());
        chip4.setTitle("Test chip");
        chip4.setColor(Color.MAGENTA);
        chl.addView(chip4);
    }

    public boolean onRecordEdit(){   // TODO !!! Переделать на общение через ViewModel !!!!
        commentSwitcher.showNext();
        //ImageButton im = (ImageButton) view;
        inCommentEdit = !inCommentEdit;
        if (inCommentEdit) {
        //    im.setImageResource(R.drawable.ic_check_black_24dp);
            commentEdit.setText(commentView.getText());
            commentEdit.setSelection(commentEdit.getText().length());
            commentEdit.requestFocus();
        } else {
            //im.setImageResource(R.drawable.ic_edit_black_24dp);
            commentView.setText(commentEdit.getText());
            commentView.requestFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
        return inCommentEdit;
    }

    public static RecordItemFragment create(String id) {
        RecordItemFragment recordItemFragment = new RecordItemFragment();
        Bundle args = new Bundle();
        args.putString(RECORD_ID,id);
        recordItemFragment.setArguments(args);
        return recordItemFragment;
    }
}
