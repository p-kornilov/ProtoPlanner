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
import com.vividprojects.protoplanner.coredata.Block;
import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.coredata.Record;
import com.vividprojects.protoplanner.utils.ItemActionsBlock;
import com.vividprojects.protoplanner.utils.ItemActionsRecord;
import com.vividprojects.protoplanner.utils.PriceFormatter;
import com.vividprojects.protoplanner.utils.Selectable;

import java.lang.ref.WeakReference;

public class BlockItemListBindingModel extends BaseObservable implements Selectable {
    private Block.Plain block;

    private ItemActionsBlock listAdapter;
    private boolean isEmpty = false;
    private String defaultImage;
    private boolean selected = false;
    private float value = 0;
    private boolean valueError = false;

    private WeakReference<Context> context;

    public BlockItemListBindingModel(Context context, ItemActionsBlock listAdapter, Block.Plain block, String defaultImage) {
        this.block = block;
        this.context = new WeakReference<>(context);
        this.listAdapter = listAdapter;
        this.defaultImage = defaultImage;
        if (block.value < 0 || block.value == -0) {
            this.value = -1*block.value;
            this.valueError = true;
        } else {
            this.value = block.value;
            this.valueError = false;
        }

        notifyPropertyChanged(BR.blockItemListName);
        notifyPropertyChanged(BR.blockItemListElementsCount);
        notifyPropertyChanged(BR.blockItemListSelected);
        notifyPropertyChanged(BR.blockItemListValueDecor);
        notifyPropertyChanged(BR.blockItemListValueError);
    }

    @Bindable
    public boolean getBlockItemListSelected() {
        return selected;
    }

    @Bindable
    public String getBlockItemListName() {
        return block.name;
    }

    @Bindable
    public String getBlockItemListValueDecor() {
        return PriceFormatter.createValue(block.currency, value);
    }

    @Bindable
    public boolean getBlockItemListValueError() {
        return valueError;
    }

    @Bindable
    public String getBlockItemListElementsCount() {
        return "(" + block.elementsCount + ")";
    }

    public void onItemClick() {
        listAdapter.itemBlockEdit(block.id);
        setSelected(true);
    }

    @Override
    public void setSelected(boolean s) {
        if (selected != s) {
            selected = s;
            notifyPropertyChanged(BR.blockItemListSelected);
        }
    }

    public void onMenuClicked(View view) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mbe_edit:
                        listAdapter.itemBlockEdit(block.id);
                        setSelected(true);
                        return true;
                    case R.id.mbe_delete:
                        listAdapter.itemBlockEdit(block.id);
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_block_edit, popup.getMenu());
        popup.show();
    }
}
