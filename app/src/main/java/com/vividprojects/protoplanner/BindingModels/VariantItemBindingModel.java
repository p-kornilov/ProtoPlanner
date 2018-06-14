package com.vividprojects.protoplanner.BindingModels;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.vividprojects.protoplanner.Adapters.HorizontalImagesListAdapter;
import com.vividprojects.protoplanner.Adapters.ShopListAdapter;
import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Interface.Fragments.RecordItemFragment;
import com.vividprojects.protoplanner.Interface.RecordAddImageURLDialog;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.ItemActionsShop;
import com.vividprojects.protoplanner.Utils.PriceFormatter;
import com.vividprojects.protoplanner.Utils.RunnableParam;

import java.lang.ref.WeakReference;
import java.util.List;

public class VariantItemBindingModel extends BaseObservable {
    private Variant.Plain variant;
    private String price;
    private String count;
    private String name;

    private ShopListAdapter shopListAdapter;

    private HorizontalImagesListAdapter imagesListAdapter;
    private int imagesScroll = 0;
    private String loaded_image = "";

    private WeakReference<Runnable> onEditClick;
    private WeakReference<RunnableParam<View>> onAddImageClick;
    private WeakReference<Runnable> onAddShopClick;
    private WeakReference<Context> context;

    public VariantItemBindingModel() {
    }

    public void setContext(Fragment fragment) {
        this.context = new WeakReference<>(fragment.getContext());
        this.shopListAdapter = new ShopListAdapter(this.context.get());
        this.shopListAdapter.setMaster((ItemActionsShop) fragment);
    }

    public void setImagesAdapter(RunnableParam onImageSelect) {
        imagesListAdapter = new HorizontalImagesListAdapter(null, null, onImageSelect);
    }

    @Bindable
    public RecyclerView.Adapter getVariantImagesAdapter() {
        return imagesListAdapter;
    }

    @Bindable
    public RecyclerView.Adapter getVariantShopsAdapter() {
        return shopListAdapter;
    }

    @Bindable
    public String getVariantName(){
        return variant != null ? variant.title : null;
    }

    @Bindable
    public void setVariantName(String name) {
        variant.title = name;
    }

    @Bindable
    public String getVariantValueDecor() {
        if (variant != null)
            return PriceFormatter.createValue(variant.primaryShop.currency, variant.primaryShop.price * variant.count);
        else
            return null;
    }

    @Bindable
    public String getVariantPrice() {
        if (variant != null)
            return String.valueOf(variant.primaryShop.price);
        else
            return null;
    }

    @Bindable
    public String getVariantCount() {
        if (variant != null)
            return String.valueOf(variant.count);
        else
            return null;
    }

    @Bindable
    public void setVariantPrice(String price) {
        this.price = price;
    }

    @Bindable
    public void setVariantCount(String count) {
        this.count = count;
    }

    @Bindable
    public String getVariantPriceDecor() {
        if (variant != null)
            return PriceFormatter.createPrice(context.get(), variant.primaryShop.currency, variant.primaryShop.price, variant.measure);
        else
            return null;
    }

    @Bindable
    public String getVariantCountDecor() {
        if (variant != null)
            return PriceFormatter.createCount(context.get(), variant.count, variant.measure);
        else
            return null;
    }

    public String getVariantId() {
        return variant.id;
    }

    public void setVariant(Variant.Plain variant) {
        this.variant = variant;
        this.name = variant.title;
        this.price = String.valueOf(variant.primaryShop.price);
        this.count = String.valueOf(variant.count);
        imagesListAdapter.setData(variant.small_images);
        shopListAdapter.setData(variant.shops, variant.primaryShop);
       // shopListAdapter.
        notifyPropertyChanged(BR.variantName);
        notifyPropertyChanged(BR.variantCountDecor);
        notifyPropertyChanged(BR.variantPriceDecor);
        notifyPropertyChanged(BR.variantCount);
        notifyPropertyChanged(BR.variantPrice);
        notifyPropertyChanged(BR.variantValueDecor);

        //...
    }

    public void refreshShop(VariantInShop.Plain shop) {
        shopListAdapter.refresh(shop);
    }

    public void initImageLoad() {
        imagesScroll = imagesListAdapter.loadingInProgress(0);
        notifyPropertyChanged(BR.variantImagesScrollTo);
    }

    @Bindable
    public int getVariantImagesScrollTo() {
        return imagesScroll;
    }

    public void setLoadProgress(int progress) {
            if (progress>=0 && progress <=100) {
                imagesListAdapter.loadingInProgress(progress);
            }
            if (progress == DataRepository.LOAD_ERROR) {
                AlertDialog alert = new AlertDialog.Builder(context.get()).create();
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
                imagesListAdapter.loadingDone(true, loaded_image);
                //imagesListAdapter.setData(resource.data.small_images);
                // imagesRecycler.
            }
    }

    public void setLoadedImage(String image) {
        loaded_image = image;
    }

    public void setOnEditClick(Runnable func) {
        this.onEditClick = new WeakReference<>(func);
    }

    public void onEditClick() {
        if (onEditClick != null && onEditClick.get() != null)
            onEditClick.get().run();
    }

    public void setOnAddImageClick(RunnableParam<View> func) {
        this.onAddImageClick = new WeakReference<>(func);
    }

    public void onAddImageClick(View view) {
        if (onAddImageClick != null && onAddImageClick.get() != null)
            onAddImageClick.get().run(view);
    }

    public void setOnAddShopClick(Runnable func) {
        this.onAddShopClick = new WeakReference<>(func);
    }

    public void onAddShopClick() {
        if (onAddShopClick != null && onAddShopClick.get() != null)
            onAddShopClick.get().run();

    }

}
