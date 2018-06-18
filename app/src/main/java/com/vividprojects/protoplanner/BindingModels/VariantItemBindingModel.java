package com.vividprojects.protoplanner.BindingModels;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vividprojects.protoplanner.Adapters.HorizontalImagesListAdapter;
import com.vividprojects.protoplanner.Adapters.ShopListAdapter;
import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.Utils.ItemActionsShop;
import com.vividprojects.protoplanner.Utils.PriceFormatter;
import com.vividprojects.protoplanner.Utils.RunnableParam;

import java.lang.ref.WeakReference;

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
    public boolean getIsVariantImagesEmpty() {
        return imagesListAdapter.isEmptyAdapter();
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
        return variant != null ? PriceFormatter.createValue(variant.primaryShop.currency, variant.primaryShop.price * variant.count) : null;
    }

    @Bindable
    public String getVariantPrice() {
        return variant != null ? String.valueOf(variant.primaryShop.price) : null;
    }

    @Bindable
    public String getVariantCount() {
        return variant != null ? String.valueOf(variant.count) : null;
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
        return variant != null ? PriceFormatter.createPrice(context.get(), variant.primaryShop.currency, variant.primaryShop.price, variant.measure) : null;
    }

    @Bindable
    public String getVariantCountDecor() {
        return variant != null ? PriceFormatter.createCount(context.get(), variant.count, variant.measure) : null;
    }

    public String getVariantId() {
        return variant.id;
    }

    public void setVariant(Variant.Plain variant) {
        this.variant = variant;
        this.name = variant.title;
        this.price = String.valueOf(variant.primaryShop.price);
        this.count = String.valueOf(variant.count);
        imagesListAdapter.setData(variant.small_images, variant.defaultImage);
        shopListAdapter.setData(variant.shops, variant.primaryShop);
       // shopListAdapter.
        notifyPropertyChanged(BR.variantName);
        notifyPropertyChanged(BR.variantCountDecor);
        notifyPropertyChanged(BR.variantPriceDecor);
        notifyPropertyChanged(BR.variantCount);
        notifyPropertyChanged(BR.variantPrice);
        notifyPropertyChanged(BR.variantValueDecor);
        notifyPropertyChanged(BR.isVariantImagesEmpty);

        //...
    }

    public void refreshShop(VariantInShop.Plain shop) {
        shopListAdapter.refresh(shop);
    }

    public void initImageLoad() {
        imagesScroll = imagesListAdapter.loadingInProgress(0);
        notifyPropertyChanged(BR.variantImagesScrollTo);
        notifyPropertyChanged(BR.isVariantImagesEmpty);
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
                                notifyPropertyChanged(BR.isVariantImagesEmpty);
                            }
                        });
                alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                        imagesListAdapter.loadingDone(false, "");
                        notifyPropertyChanged(BR.isVariantImagesEmpty);
                    }
                });
                alert.show();
            }
            if (progress == DataRepository.LOAD_DONE) {
                imagesListAdapter.imageReady();
                notifyPropertyChanged(BR.isVariantImagesEmpty);
                //imagesListAdapter.setData(resource.data.small_images);
                // imagesRecycler.
            }
            if (progress == DataRepository.SAVE_TO_DB_DONE) {
                imagesListAdapter.loadingDone(true, loaded_image);
                notifyPropertyChanged(BR.isVariantImagesEmpty);
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
