package com.vividprojects.protoplanner.bindingmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.coredata.Variant;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.utils.ItemActionsVariant;
import com.vividprojects.protoplanner.utils.PriceFormatter;

import java.lang.ref.WeakReference;

public class VariantItemListBindingModel extends BaseObservable {
    private Variant.Plain variant;
    private String price;
    private String count;
    private String name;
    private String defaultImage;

    private WeakReference<Context> context;
    private ItemActionsVariant listAdapter;

    public VariantItemListBindingModel() {
    }

    public VariantItemListBindingModel(Context context, ItemActionsVariant listAdapter, Variant.Plain variant, String defaultImage) {
        this.variant = variant;
        this.context = new WeakReference<>(context);
        this.listAdapter = listAdapter;
        this.defaultImage = defaultImage;
        notifyPropertyChanged(BR.variantName);
        notifyPropertyChanged(BR.variantCountDecor);
        notifyPropertyChanged(BR.variantPriceDecor);
        notifyPropertyChanged(BR.variantCount);
        notifyPropertyChanged(BR.variantPrice);
        notifyPropertyChanged(BR.variantValueDecor);
        notifyPropertyChanged(BR.variantItemListImage);
    }

    @Bindable
    public String getVariantItemListImage() {
        if (variant.small_images.size() != 0 && variant.small_images.size() > variant.defaultImage)
            return variant.small_images.get(variant.defaultImage);
        else
            return defaultImage;
    }

    @Bindable
    public String getVariantName(){
        return variant.title;
    }

    @Bindable
    public String getVariantValueDecor() {
        return PriceFormatter.createValue(variant.primaryShop.currency, variant.primaryShop.price * variant.count);
    }

    @Bindable
    public String getVariantPriceDecor() {
        return PriceFormatter.createPrice(context.get(), variant.primaryShop.currency, variant.primaryShop.price, variant.measure);
    }

    @Bindable
    public String getVariantCountDecor() {
        return PriceFormatter.createCount(context.get(), variant.count, variant.measure);
    }

    public void onItemClick() {
        listAdapter.variantOpen(variant.id);
    }

    public String getVariantId() {
        return variant.id;
    }

    public void onMenuClicked(View view) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mve_edit:
                        listAdapter.variantOpen(variant.id);
                        return true;
                    case R.id.mve_default:
                        listAdapter.variantSetBasic(variant.id);
                        return true;
                    case R.id.mve_delete:
                        listAdapter.variantDelete(variant.id);
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_variant_edit, popup.getMenu());
/*        if (measure.def) {
            popup.getMenu().findItem(R.id.mce_default).setEnabled(false);
            popup.getMenu().findItem(R.id.mce_delete).setEnabled(false);
        }*/
        popup.show();
    }
}
