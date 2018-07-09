package com.vividprojects.protoplanner.bindingmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.coredata.VariantInShop;
import com.vividprojects.protoplanner.utils.ItemActionsRecord;
import com.vividprojects.protoplanner.utils.ItemActionsShop;
import com.vividprojects.protoplanner.utils.PriceFormatter;

import java.lang.ref.WeakReference;

public class RecordItemListBindingModel extends BaseObservable {
    private Record.Plain record;

    private ItemActionsRecord listAdapter;
    private boolean isEmpty = false;
    private String defaultImage;

    private WeakReference<Context> context;

    public RecordItemListBindingModel(Context context, ItemActionsRecord listAdapter, Record.Plain record, String defaultImage) {
        this.record = record;
        this.context = new WeakReference<>(context);
        this.listAdapter = listAdapter;
        this.defaultImage = defaultImage;
/*        if ((shop.address == null || shop.address.length() == 0)
                && (shop.comment == null || shop.comment.length() == 0)
                && (shop.title == null || shop.title.length() == 0)
                && (shop.url == null || shop.url.length() == 0))
            isEmpty = true;*/

        notifyPropertyChanged(BR.recordItemListImage);
        notifyPropertyChanged(BR.recordItemListBasicVariantName);
        notifyPropertyChanged(BR.recordItemListValueDecor);
        notifyPropertyChanged(BR.recordItemListPriceDecor);
        notifyPropertyChanged(BR.recordItemListCountDecor);
        //notifyPropertyChanged(BR.shopIsEmpty);
    }

    @Bindable
    public String getRecordItemListName() {
        return record.name;
    }

    @Bindable
    public String getRecordItemListBasicVariantName() {
        return record.mainVariant != null ? record.mainVariant.title : null;
    }

    @Bindable
    public String getRecordItemListValueDecor() {
        return record.mainVariant != null ? PriceFormatter.createValue(record.mainVariant.primaryShop.currency, record.mainVariant.primaryShop.price * record.mainVariant.count) : null;
    }

    @Bindable
    public String getRecordItemListPriceDecor() {
        return record.mainVariant != null ? PriceFormatter.createPrice(context.get(), record.mainVariant.primaryShop.currency, record.mainVariant.primaryShop.price, record.mainVariant.measure) : null;
    }

    @Bindable
    public String getRecordItemListCountDecor() {
        return record.mainVariant != null ? PriceFormatter.createCount(context.get(), record.mainVariant.count, record.mainVariant.measure) : null;
    }

    @Bindable
    public String getRecordItemListImage() {
        if (record.mainVariant.small_images.size() != 0 && record.mainVariant.small_images.size() > record.mainVariant.defaultImage)
            return record.mainVariant.small_images.get(record.mainVariant.defaultImage);
        else
            return defaultImage;
    }

    @Bindable
    public Label.Plain[] getRecordItemListLabels() {
        return record.labels.toArray(new Label.Plain[record.labels.size()]);
    }

    public void onItemClick() {
        listAdapter.itemRecordEdit(record.id);
    }

    public void onMenuClicked(View view) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mre_edit:
                        listAdapter.itemRecordEdit(record.id);
                        return true;
                    case R.id.mre_block:
                        /*if (!shop.basicVariant)
                            listAdapter.itemShopPrimary(shop.id);*/
                        return true;
                    case R.id.mre_delete:
                        listAdapter.itemRecordDelete(record.id);
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_record_edit, popup.getMenu());
        popup.show();
    }
}
