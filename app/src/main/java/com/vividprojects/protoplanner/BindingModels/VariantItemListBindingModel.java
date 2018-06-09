package com.vividprojects.protoplanner.BindingModels;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.vividprojects.protoplanner.Adapters.HorizontalImagesListAdapter;
import com.vividprojects.protoplanner.Adapters.ShopListAdapter;
import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.DataManager.DataRepository;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.ItemActionsShop;
import com.vividprojects.protoplanner.Utils.ItemActionsVariant;
import com.vividprojects.protoplanner.Utils.PriceFormatter;
import com.vividprojects.protoplanner.Utils.RunnableParam;

import java.lang.ref.WeakReference;

public class VariantItemListBindingModel extends BaseObservable {
    private Variant.Plain variant;
    private String price;
    private String count;
    private String name;

    private WeakReference<Context> context;
    private ItemActionsVariant listAdapter;

    public VariantItemListBindingModel() {
    }

    public VariantItemListBindingModel(Context context, ItemActionsVariant listAdapter, Variant.Plain variant) {
        this.variant = variant;
        this.context = new WeakReference<>(context);
        this.listAdapter = listAdapter;
        notifyPropertyChanged(BR.variantName);
        notifyPropertyChanged(BR.variantCountDecor);
        notifyPropertyChanged(BR.variantPriceDecor);
        notifyPropertyChanged(BR.variantCount);
        notifyPropertyChanged(BR.variantPrice);
        notifyPropertyChanged(BR.variantValueDecor);
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
                    //    listAdapter.itemEdit(measure.hash);
                        return true;
                    case R.id.mve_default:
/*
                        if (!measure.def)
                            listAdapter.itemDefault(measure.hash);
*/
                        return true;
                    case R.id.mve_delete:
//                        listAdapter.itemDelete(measure.hash);
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
