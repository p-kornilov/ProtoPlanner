package com.vividprojects.protoplanner.BindingModels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.vividprojects.protoplanner.BR;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Measure;
import com.vividprojects.protoplanner.CoreData.Variant;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.Utils.RunnableParam;

import java.lang.ref.WeakReference;
import java.util.List;

public class ShopEditBindingModel extends BaseObservable {
    private String price;
    private VariantInShop.Plain shop;
    private List<Currency.Plain> currencyList;
    private int currencyCursor = 0;
    private boolean priceError = false;

    private WeakReference<Context> context;
    private WeakReference<RunnableParam<Integer>> enableCheck;

    public void setEnableCheck(RunnableParam<Integer> f) {
        this.enableCheck = new WeakReference<>(f);
    }

    public void enableCheck() {
        if (enableCheck == null || enableCheck.get() == null)
            return;
        if (priceError)
            enableCheck.get().run(1);
        else
            enableCheck.get().run(0);

    }

    public double getPriceNum() {
        return shop.price;
    }

    public ShopEditBindingModel(Context context) {
        this.context = new WeakReference<>(context);
    }

    @Bindable
    public boolean getPriceError() {
        return priceError;
    }

    @Bindable
    public String getShopEditUrl(){
        return shop.url;
    }

    @Bindable
    public void setShopEditUrl(String url) {
        this.shop.url = url;
    }

    @Bindable
    public String getShopEditName(){
        return shop.title;
    }

    @Bindable
    public void setShopEditName(String name) {
        this.shop.title = name;
    }

    @Bindable
    public String getShopEditAddress(){
        return shop.address;
    }

    @Bindable
    public void setShopEditAddress(String address) {
        this.shop.address = address;
    }
    @Bindable
    public String getShopEditComment(){
        return shop.comment;
    }

    @Bindable
    public void setShopEditComment(String comment)  {
        this.shop.comment = comment;
    }

    @Bindable
    public String getShopEditPrice() {
        return price;
    }

    @Bindable
    public void setShopEditPrice(String price) {
        try {
            shop.price = Double.parseDouble(price);
            priceError = false;
        } catch (NumberFormatException e) {
            priceError = true;
        }
        this.price = price;
        enableCheck();
        notifyPropertyChanged(BR.priceError);
    }

    @Bindable
    public Currency.Plain getShopEditCurrency() {
        return shop.currency;
    }

    @Bindable
    public List<Currency.Plain> getShopEditCurrencyList() {
        return currencyList;
    }

    @Bindable
    public void setCurrencyCursor(int cursor) {
        shop.currency = currencyList.get(cursor);
        currencyCursor = cursor;
    }

    @Bindable
    public int getCurrencyCursor() {
        return currencyCursor;
    }

    public void setShopEditCurrencyList(List<Currency.Plain> currencyList) {
        this.currencyList = currencyList;
        notifyPropertyChanged(BR.shopEditCurrencyList);
    }

    public String getId() {
        return shop.id;
    }

    public void setShop(VariantInShop.Plain shop) {
        this.shop = shop;
        this.price = String.valueOf(shop.price);

        notifyPropertyChanged(BR.shopEditUrl);
        notifyPropertyChanged(BR.shopEditName);
        notifyPropertyChanged(BR.shopEditAddress);
        notifyPropertyChanged(BR.shopEditComment);

        //...
    }

    public void parseUrl() {

    }

    public VariantInShop.Plain getShop() {
        return this.shop;
    }

    public void setShopId(String id) {
        this.shop.id = id;
    }

}
