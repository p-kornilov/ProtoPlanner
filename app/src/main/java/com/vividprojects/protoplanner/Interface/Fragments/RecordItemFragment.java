package com.vividprojects.protoplanner.Interface.Fragments;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.vividprojects.protoplanner.Adapters.HorizontalImagesListAdapter;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Images.BitmapUtils;
import com.vividprojects.protoplanner.Interface.Dialogs.EditTextDialog;
import com.vividprojects.protoplanner.Interface.Dialogs.EditVariantDialog;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.Interface.RecordAddImageURLDialog;
import com.vividprojects.protoplanner.MainActivity;
import com.vividprojects.protoplanner.ViewModels.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.PriceFormatter;
import com.vividprojects.protoplanner.Utils.RunnableParam;
import com.vividprojects.protoplanner.Widgets.ChipsLayout;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Smile on 31.10.2017.
 */

public class RecordItemFragment extends Fragment implements Injectable {

    public static final String RECORD_ID = "RECORD_ID";
    private static final String FILE_PROVIDER_AUTHORITY = "com.vividprojects.protoplanner.file_provider";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static final int REQUEST_IMAGE_URL_LOAD = 3;
    private static final int REQUEST_LABELS_SET = 4;
    private static final int REQUEST_EDIT_NAME = 5;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 11;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    private ChipsLayout labelsLayout;
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
    private ImageButton set_labels;
    private ImageButton edit_main_variant;
    private ImageButton commentEditButton;
    private HorizontalImagesListAdapter imagesListAdapter;
   // private TextView mvCurrency1;
    private TextView mvCurrency2;
    private RecordItemViewModel model;
    private PopupMenu loadImagePopup;
    private boolean empty = false;

    private String mTempPhotoPath;

    private String recordName = "";

    private RunnableParam<Integer> onImageSelect = (position)->{
        navigationController.openImageView(position,model.getMainVariantItem().getValue().data.title);
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
            v = (View) inflater.inflate(R.layout.empty_item_fragment, container, false);
        } else {
            v = (View) inflater.inflate(R.layout.record_fragment, container, false);

            shopsRecycler = v.findViewById(R.id.rf_shops_recycler);
            mvTitle = v.findViewById(R.id.alt_title);
            mvPrice = v.findViewById(R.id.alt_price);
            mvValue = v.findViewById(R.id.alt_value);
            mvCount = v.findViewById(R.id.alt_count);

            edit_main_variant = v.findViewById(R.id.rf_variant_edit_button);
            edit_main_variant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditVariantDialog editVariantDialog = EditVariantDialog.create();
                    editVariantDialog.setTargetFragment(RecordItemFragment.this, REQUEST_IMAGE_URL_LOAD);
                    editVariantDialog.show(getFragmentManager(), "Edit main variant");
                }
            });

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setAutoMeasureEnabled(true);
            shopsRecycler.setLayoutManager(layoutManager);
            shopsRecycler.setNestedScrollingEnabled(false);
            shopsRecycler.setFocusable(false);

            labelsLayout = v.findViewById(R.id.chipLayout);

            imagesRecycler = (RecyclerView) v.findViewById(R.id.ai_images);
            RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            imagesRecycler.setLayoutManager(layoutManager3);
            imagesRecycler.setNestedScrollingEnabled(false);
            imagesRecycler.setFocusable(false);
            imagesListAdapter = new HorizontalImagesListAdapter(null, null, onImageSelect);
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
                }
            });

            set_labels = v.findViewById(R.id.rf_set_tags);
            set_labels.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = 1;
                    navigationController.openLabelsForResult(labelsLayout.getAllLabels(),RecordItemFragment.this,REQUEST_LABELS_SET);
                }
            });

            alternativesRecycler = (RecyclerView) v.findViewById(R.id.rf_alternatives_recycler);

            RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
            layoutManager2.setAutoMeasureEnabled(true);
            alternativesRecycler.setLayoutManager(layoutManager2);
            alternativesRecycler.setNestedScrollingEnabled(false);
            alternativesRecycler.setFocusable(false);

            commentSwitcher = (ViewSwitcher) v.findViewById(R.id.rf_comment_switcher);
            commentEdit = (EditText) v.findViewById(R.id.rf_comment_edit);
            commentView = (TextView) v.findViewById(R.id.rf_comment_text);
            commentEditButton = v.findViewById(R.id.rf_comment_edit_button);

            commentEditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRecordEdit();
                }
            });
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
        } else
            inflater.inflate(R.menu.menu_record, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.ra_edit_name:
                EditTextDialog editNameDialog = new EditTextDialog();
                editNameDialog.setTargetFragment(this, REQUEST_EDIT_NAME);
                Bundle b = new Bundle();
                b.putString("TITLE","Edit note");
                b.putString("HINT","Name");
                b.putString("POSITIVE","Save");
                b.putString("NEGATIVE","Cancel");
                b.putString("EDITTEXT",recordName);
                editNameDialog.setArguments(b);
                editNameDialog.show(getFragmentManager(), "Edit name");
                break;
        }
        return true;
        //return super.onOptionsItemSelected(item);
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
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    imagesRecycler.scrollToPosition(imagesListAdapter.loadingInProgress(0));
                    model.loadCameraImage(mTempPhotoPath);
                } else {
                    BitmapUtils.deleteImageFile(getContext().getApplicationContext(), mTempPhotoPath);
                }
                return;
            case REQUEST_IMAGE_GALLERY:
                if (resultCode == RESULT_OK && data != null) {
                    imagesRecycler.scrollToPosition(imagesListAdapter.loadingInProgress(0));
                    model.loadGalleryImage(data.getData());
                }
                return;
            case REQUEST_IMAGE_URL_LOAD:
                if (resultCode == RESULT_OK && data != null) {
                    String url = data.getExtras().get("URL").toString();
                    imagesRecycler.scrollToPosition(imagesListAdapter.loadingInProgress(0));
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
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

       // final RecordItemViewModel model = ViewModelProviders.of(getActivity(),viewModelFactory).get(RecordItemViewModel.class);

        if (!empty) {
            model = ViewModelProviders.of(getActivity(), viewModelFactory).get(RecordItemViewModel.class);

            Bundle args = getArguments();

            if (args != null && args.containsKey(RECORD_ID)) {
                //    model.setFilter();
                model.setId(args.getString(RECORD_ID));
            } else {
                model.setId(null);
            }

            model.getRecordItem().observe(this, resource -> {
                if (resource != null && resource.data != null) {
                    commentView.setText(resource.data.comment);
                    recordName = resource.data.name;

                    labelsLayout.setMode(ChipsLayout.MODE_NON_TOUCH);
                    labelsLayout.setData(resource.data.labels,null);
                }
            });

            if (navigationController.isTablet())
                model.getRecordName().observe(this, name -> {
                    if (name != null) {
                        ((MainActivity) getActivity()).getSecondToolBar().setTitle(name);
                    }
                });

            model.getMainVariantItem().observe(this, resource -> {
                if (resource != null && resource.data != null) {
                    mvTitle.setText(resource.data.title);
                    mvCount.setText(PriceFormatter.createCount(this.getContext(), resource.data.count, resource.data.measure));
                    mvValue.setText(PriceFormatter.createValue(resource.data.currency, resource.data.price * resource.data.count));
                    mvPrice.setText(PriceFormatter.createPrice(this.getContext(), resource.data.currency, resource.data.price, resource.data.measure));
                    imagesListAdapter.setData(resource.data.small_images);
                }
            });

            model.getLoadProgress().observe(this, (progress) -> {
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
            });
        }
    }

    public boolean onRecordEdit(){
        commentSwitcher.showNext();
        //ImageButton im = (ImageButton) view;
        inCommentEdit = !inCommentEdit;
        if (inCommentEdit) {
            commentEditButton.setImageResource(R.drawable.ic_check_black_24dp);
            commentEdit.setText(commentView.getText());
            commentEdit.setSelection(commentEdit.getText().length());
            commentEdit.requestFocus();
        } else {
            commentEditButton.setImageResource(R.drawable.ic_edit_gray_24dp);
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
