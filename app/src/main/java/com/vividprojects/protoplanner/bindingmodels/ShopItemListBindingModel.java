package com.vividprojects.protoplanner.bindingmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.coredata.VariantInShop;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.utils.ItemActionsShop;
import com.vividprojects.protoplanner.utils.PriceFormatter;

public class ShopItemListBindingModel extends BaseObservable {
    private VariantInShop.Plain shop;

    private Context context;
    private ItemActionsShop listAdapter;
    private boolean isEmpty = false;

    public ShopItemListBindingModel(Context context, ItemActionsShop listAdapter, VariantInShop.Plain shop) {
        this.shop = shop;
        this.context = context;
        this.listAdapter = listAdapter;
        if ((shop.address == null || shop.address.length() == 0)
                && (shop.comment == null || shop.comment.length() == 0)
                && (shop.title == null || shop.title.length() == 0)
                && (shop.url == null || shop.url.length() == 0))
            isEmpty = true;

        notifyPropertyChanged(BR.shopTitle);
        notifyPropertyChanged(BR.shopURL);
        notifyPropertyChanged(BR.shopAddress);
        notifyPropertyChanged(BR.shopComment);
        notifyPropertyChanged(BR.shopPrice);
        notifyPropertyChanged(BR.shopIsEmpty);
    }

    @Bindable
    public boolean getShopIsEmpty() {
        return isEmpty;
    }

    @Bindable
    public String getShopTitle() {
        return shop.title;
    }

    @Bindable
    public String getShopURL() {
        return shop.url;
    }

    @Bindable
    public String getShopAddress() {
        return shop.address;
    }

    @Bindable
    public String getShopComment() {
        return shop.comment;
    }

    @Bindable
    public int getIsBasic() {
        if (shop.basicVariant)
            return View.VISIBLE;
        else
            return View.INVISIBLE;
    }

    @Bindable
    public String getShopPrice() {
        return PriceFormatter.createPrice(context, shop.currency, shop.price, shop.measure);
    }

    public void onItemClick() {
        listAdapter.itemShopEdit(shop.id);
    }

    public void onMenuClicked(View view) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mse_edit:
                        listAdapter.itemShopEdit(shop.id);
                        return true;
                    case R.id.mse_set_basic:
                        if (!shop.basicVariant)
                            listAdapter.itemShopPrimary(shop.id);
                        return true;
                    case R.id.mse_delete:
                        listAdapter.itemShopDelete(shop.id);
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_shop_edit, popup.getMenu());
        if (shop.basicVariant) {
            popup.getMenu().findItem(R.id.mse_set_basic).setEnabled(false);
            popup.getMenu().findItem(R.id.mse_delete).setEnabled(false);
        }
        popup.show();
    }
}
