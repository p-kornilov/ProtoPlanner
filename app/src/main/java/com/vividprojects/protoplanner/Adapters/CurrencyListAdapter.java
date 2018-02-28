package com.vividprojects.protoplanner.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Outline;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.Images.GlideApp;
import com.vividprojects.protoplanner.Interface.Activity.ContainerActivity;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.PPApplication;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.TMP.TestRecyclerAdapter;
import com.vividprojects.protoplanner.Utils.PriceFormatter;
import com.vividprojects.protoplanner.Widgets.Chip;
import com.vividprojects.protoplanner.Widgets.ChipsLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Smile on 27.10.2017.
 */


public class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyListAdapter.ViewHolder> {

    private List<Currency.Plain> data;
    private List<Currency.Plain> filtered_data;
    private Currency.Plain base;
    private Map<Integer,String> names;

    public CurrencyListAdapter() {
        filtered_data = new ArrayList<>();
        names = new HashMap<>();
    }

    public void setData(List<Currency.Plain> data, Context context) {
        this.data = data;
        names.clear();
        int i = 0;
        int base_currency = -1;
        for (Currency.Plain c : this.data) {
            names.put(c.iso_name_id,context.getResources().getString(c.iso_name_id));
            if (c.iso_code_int == c.exchange_base)
                base_currency = i;
            i++;
        }
        if (base_currency > -1) {
            base = this.data.remove(base_currency);
            this.data.add(0, base);
        }
        this.filtered_data = this.data;
    }

    public void setFilter(String filter) {
        filter = filter.toLowerCase();
        filtered_data = new ArrayList<>();
        for (Currency.Plain c : data) {
            if (c.iso_code_str.toLowerCase().contains(filter) || names.get(c.iso_name_id).toLowerCase().contains(filter))
                filtered_data.add(c);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item, parent, false); // Сначала вернуть view инфлаттером
        ViewCompat.setElevation(v,v.getResources().getDimension(R.dimen.cardElevation));
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Currency.Plain obj = filtered_data.get(position);
        if (obj == null)
            return;
        holder.data = obj;


        int drawableResource;
        if (filtered_data.size() == 1)
            drawableResource = R.drawable.list_item_background_single;
        else if (position == 0)
            drawableResource = R.drawable.list_item_background_top;
        else if (position == filtered_data.size() - 1)
            drawableResource = R.drawable.list_item_background_bottom;
        else
            drawableResource = R.drawable.list_item_background;

        holder.root.setBackground(ContextCompat.getDrawable(holder.root.getContext(),drawableResource));

        if (filtered_data.size() == 1 || position==0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                holder.root.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                holder.root.setOutlineProvider(new CurrencyListAdapter.CustomOutline());
        }

        holder.currency_name.setText(names.get(obj.iso_name_id));
        holder.currency_code.setText(obj.iso_code_str);
        if (obj.iso_code_int == obj.exchange_base) {
            holder.currency_example.setText(PriceFormatter.getValue(obj, 1*obj.exchange_rate));
            holder.currency_base.setText("");
            holder.currency_default.setVisibility(View.VISIBLE);
        } else {
            holder.currency_example.setText(PriceFormatter.getValue(obj, 1*obj.exchange_rate));
            holder.currency_base.setText(" = " + PriceFormatter.getValue(base, 1));
            holder.currency_default.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return filtered_data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView currency_name;
        TextView currency_example;
        TextView currency_default;
        TextView currency_code;
        TextView currency_base;
        ImageButton edit_button;
        public Currency.Plain data;
        public View root;

        ViewHolder(View view) {
            super(view);
            root = view;
            currency_name = view.findViewById(R.id.currency_name);
            currency_example = view.findViewById(R.id.currency_symbol);
            currency_code = view.findViewById(R.id.currency_code);
            currency_default = view.findViewById(R.id.currency_default);
            currency_base = view.findViewById(R.id.currency_base);
            edit_button = view.findViewById(R.id.currency_edit_button);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //mCallback.onRecordEditClick(data.getId());
                   // appController.openRecord(data.getId());

                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class CustomOutline extends ViewOutlineProvider {

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRect(0, (int)view.getResources().getDimension(R.dimen.cardElevation), view.getWidth(),view.getHeight());
        }
    }
}

