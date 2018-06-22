package com.vividprojects.protoplanner.viewmodels;

import android.Manifest;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.vividprojects.protoplanner.bindingmodels.ShopEditBindingModel;
import com.vividprojects.protoplanner.bindingmodels.VariantEditBindingModel;
import com.vividprojects.protoplanner.bindingmodels.VariantItemBindingModel;
import com.vividprojects.protoplanner.coredata.Currency;
import com.vividprojects.protoplanner.coredata.Measure;
import com.vividprojects.protoplanner.coredata.Resource;
import com.vividprojects.protoplanner.coredata.Variant;
import com.vividprojects.protoplanner.coredata.VariantInShop;
import com.vividprojects.protoplanner.datamanager.DataRepository;
import com.vividprojects.protoplanner.ui.helpers.DialogFullScreenHelper;
import com.vividprojects.protoplanner.ui.NavigationController;
import com.vividprojects.protoplanner.ui.AddImageURLDialog;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.utils.Camera;
import com.vividprojects.protoplanner.utils.RunnableParam;
import com.vividprojects.protoplanner.utils.SingleLiveEvent;

import java.io.File;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

/**
 * Created by Smile on 06.12.2017.
 */

public class VariantItemViewModel extends ViewModel {
    private final LiveData<Resource<Variant.Plain>> mvItem;
    private final MediatorLiveData<Variant.Plain> variantItem;
    private final LiveData<Resource<VariantInShop.Plain>> refreshedShop;
    private final MutableLiveData<String> refreshShopId = new MutableLiveData<>();
    private final MutableLiveData<String> recordId = new MutableLiveData<>();
    private final MutableLiveData<String> variantId = new MutableLiveData<>();
    private final MutableLiveData<String> onSaveId = new MutableLiveData<>();
    private final MutableLiveData<String> defaultImage = new MutableLiveData<>();

    private DataRepository dataRepository;
    private VariantItemBindingModel bindingModelVariant;
    private VariantEditBindingModel bindingModelVariantEdit;
    private ShopEditBindingModel bindingModelShopEdit;

    private final SingleLiveEvent<Integer> loadProgress;

    private boolean inImageLoading = false;

    private Fragment master;
    private String mTempPhotoPath;
    private int requestImageUrlLoad;
    private int requestImageGallery;
    private int requestImageCapture;
    private int requestImageShow;
    private int permissionRequestReadStorage;
    private int requestEditShop;
    private int requestEditVariant;
    private boolean isTablet;

    public void init(Fragment master
            ,int requestImageUrlLoad
            ,int requestImageGallery
            ,int requestImageCapture
            ,int requestImageShow
            ,int requestEditShop
            ,int requestEditVariant
            ,int permissionRequestReadStorage
            ,boolean isTablet
            ,boolean isMaster) {
        this.master = master;
        this.requestImageUrlLoad = requestImageUrlLoad;
        this.requestImageGallery = requestImageGallery;
        this.requestImageCapture = requestImageCapture;
        this.requestImageShow = requestImageShow;
        this.requestEditShop = requestEditShop;
        this.requestEditVariant = requestEditVariant;
        this.permissionRequestReadStorage = permissionRequestReadStorage;
        this.isTablet = isTablet;

        bindingModelVariant.setOnEditClick(onVariantEditClick);
        bindingModelVariant.setImagesAdapter(onImageSelect);
        bindingModelVariant.setOnAddImageClick(onAddImageClick);
        bindingModelVariant.setOnAddShopClick(onAddShopClick);
        bindingModelVariant.setContext(master, isMaster);
    }

    public Runnable onAddShopClick = () -> {
        Bundle b = new Bundle();
        b.putString("ID", "");
        b.putString("VARIANTID", bindingModelVariant.getVariantId());
        DialogFullScreenHelper.showDialog(DialogFullScreenHelper.DIALOG_SHOP, master, !isTablet, requestEditShop, b);
    };

    public RunnableParam<View> onAddImageClick = (view) -> {
        if (master == null)
            return;
        PopupMenu popup = new PopupMenu(master.getContext(), view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mli_url:
                        AddImageURLDialog addImageURLDialog = new AddImageURLDialog();
                        addImageURLDialog.setTargetFragment(master, requestImageUrlLoad);
                        addImageURLDialog.show(master.getFragmentManager(), "Add_image_url");
                        return true;
                    case R.id.mli_gallery:
                        if (ContextCompat.checkSelfPermission(master.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(master.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, permissionRequestReadStorage);
                        } else {
                            Intent i = new Intent(
                                    Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            master.startActivityForResult(i, requestImageGallery);
                        }
                        return true;
                    case R.id.mli_foto:
                        File photoFile = Camera.prepareTemp(master);
                        mTempPhotoPath = photoFile.getAbsolutePath();
                        Camera.launchCamera(master, photoFile, requestImageCapture);
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

    public Runnable onVariantEditClick = () -> {
        Bundle b = new Bundle();
        b.putString("ID", bindingModelVariant.getVariantId());
        DialogFullScreenHelper.showDialog(DialogFullScreenHelper.DIALOG_VARIANT, master, !isTablet, requestEditVariant, b);
    };

    public RunnableParam<Integer> onImageSelect = (position)->{
        NavigationController.openImageViewForResult(position,bindingModelVariant.getVariantId(), master, requestImageShow);
    };

    public String getTempPhotoPath() {
        return mTempPhotoPath;
    }

    @Inject
    public VariantItemViewModel(DataRepository dataRepository) {

        this.dataRepository = dataRepository;

        bindingModelVariant     = new VariantItemBindingModel();
        bindingModelVariantEdit = new VariantEditBindingModel(dataRepository.getContext());
        bindingModelShopEdit    = new ShopEditBindingModel(dataRepository.getContext());

        refreshedShop = Transformations.switchMap(refreshShopId, input -> VariantItemViewModel.this.dataRepository.loadShop(input));

        mvItem = Transformations.switchMap(variantId, input -> {
            if (!input.equals("Empty"))
                return VariantItemViewModel.this.dataRepository.loadVariant(input);
            else return null;
        });

        variantItem = new MediatorLiveData<>();
        variantItem.addSource(mvItem, mv -> {
            variantItem.setValue(mv.data);});

        loadProgress = new SingleLiveEvent<>();

    }

    public void setRecordId(String id) {
        if (Objects.equals(recordId.getValue(), id)) {
            return;
        }
        recordId.setValue(id);
    }

    public void setVariantId(String id) {
        if (Objects.equals(variantId.getValue(), id)) {
            return;
        }
        variantId.setValue(id);
    }

    public LiveData<Variant.Plain> getVariantItem() {
        return variantItem;
    }
    public SingleLiveEvent<Integer> getLoadProgress() {return loadProgress;}

    private void addImage(String thumbName) {
        String fullName = DataRepository.toFullImage(thumbName);
        variantItem.getValue().full_images.add(fullName);
        variantItem.getValue().small_images.add(thumbName);
        bindingModelVariant.setLoadedImage(thumbName);
    }

    public SingleLiveEvent<Integer> loadImage(String url) {
        if (!inImageLoading) {
            inImageLoading = true;
            addImage(dataRepository.saveImageFromURLtoVariant(url, variantItem.getValue().id, loadProgress,()->{inImageLoading=false;}));
        }
        return loadProgress;
    }

    public SingleLiveEvent<Integer> loadCameraImage() {
        if (!inImageLoading) {
            inImageLoading = true;
            addImage(dataRepository.saveImageFromCameratoVariant(mTempPhotoPath, variantItem.getValue().id, loadProgress,()->{inImageLoading=false;}));
        }
        return loadProgress;
    }

    public SingleLiveEvent<Integer> loadGalleryImage(Uri fileName) {
        if (!inImageLoading) {
            inImageLoading = true;
            addImage(dataRepository.saveImageFromGallerytoVariant(fileName, variantItem.getValue().id, loadProgress,()->{inImageLoading=false;}));
        }
        return loadProgress;
    }

    public boolean isInImageLoading() {
        return inImageLoading;
    }

    public LiveData<List<Measure.Plain>> getMeasures() {
        return dataRepository.getMeasures();
    }

    public LiveData<Measure.Plain> getMeasure(int hash) {
        return dataRepository.getMeasure(hash);
    }

    public LiveData<List<Currency.Plain>> getCurrencies() {
        return dataRepository.getCurrencies();
    }

    public VariantItemBindingModel getBindingModelVariant() {
        return bindingModelVariant;
    }

    public VariantEditBindingModel getBindingModelVariantEdit() {
        return bindingModelVariantEdit;
    }

    public ShopEditBindingModel getBindingModelShopEdit() {
        return bindingModelShopEdit;
    }

    public String getVariantId() {
        return variantId.getValue();
    }

    public void saveVariant(String variantId) {
        if (variantId != null && (variantItem.getValue() == null || !variantId.equals(variantItem.getValue().id)))
            dataRepository.saveMainVariantToRecord(variantId, recordId.getValue());
        LiveData<Resource<Variant.Plain>> currentVariant = dataRepository.loadVariant(variantId);
        variantItem.addSource(currentVariant, new Observer<Resource<Variant.Plain>>() {
            @Override
            public void onChanged(@Nullable Resource<Variant.Plain> variant) {
                variantItem.setValue(variant.data);
                variantItem.removeSource(currentVariant);
            }
        });
    }

    public void saveVariant(String id, String name, double price, double count, int currency, int measure) {
        String saveId = dataRepository.saveVariant(id, name, price, count, currency, measure);
        setVariantId(saveId);
    }

    public void deleteShop(String id) {
        dataRepository.deleteShop(id);
    }

    public void refreshShop(String shopId) {
        refreshShopId.setValue(shopId);
    }

    public LiveData<Resource<VariantInShop.Plain>> getRefreshedShop() {
        return refreshedShop;
    }

    public void setShopPrimary(String shopId) {
        dataRepository.setShopPrimary(shopId, variantItem.getValue().id);
        LiveData<Resource<Variant.Plain>> currentVariant = dataRepository.loadVariant(variantItem.getValue().id);
        variantItem.addSource(currentVariant, new Observer<Resource<Variant.Plain>>() {
            @Override
            public void onChanged(@Nullable Resource<Variant.Plain> variant) {
                variantItem.setValue(variant.data);
                variantItem.removeSource(currentVariant);
            }
        });
    }

    public LiveData<String> getOnSaveId() {
        return onSaveId;
    }

    public void save(){
        onSaveId.setValue(getVariantId());
        return;
    }

    public void setDefaultImage(int position) {
        dataRepository.setDefaultImage(variantItem.getValue().id ,position);
        String newImage = variantItem.getValue().full_images.get(position);
        if (Objects.equals(defaultImage.getValue(), newImage)) {
            return;
        }
        defaultImage.setValue(newImage);
    }

    public MutableLiveData<String> getDefaultImage() {
        return defaultImage;
    }
}
