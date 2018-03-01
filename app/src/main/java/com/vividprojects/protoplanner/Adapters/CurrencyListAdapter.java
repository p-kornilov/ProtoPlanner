package com.vividprojects.protoplanner.Adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Outline;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.vividprojects.protoplanner.Interface.Fragments.CurrencyListFragment;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.PPApplication;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.TMP.TestRecyclerAdapter;
import com.vividprojects.protoplanner.Utils.PriceFormatter;
import com.vividprojects.protoplanner.Widgets.Chip;
import com.vividprojects.protoplanner.Widgets.ChipsLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
    private CurrencyListFragment context;
    private String filter;
    private LinearLayoutManager layoutManager;

    public CurrencyListAdapter(CurrencyListFragment context, LinearLayoutManager layoutManager) {
        filtered_data = new ArrayList<>();
        names = new HashMap<>();
        this.context = context;
        this.layoutManager = layoutManager;
    }

    public void setData(List<Currency.Plain> data) {
        this.data = data;
        names.clear();
        for (Currency.Plain c : this.data) {
            if (c.custom_name != null)
                names.put(c.iso_code_int,c.custom_name);
            else
                names.put(c.iso_code_int,context.getResources().getString(c.iso_name_id));
            if (c.iso_code_int == c.exchange_base)
                base = c;
        }

        sortList();

        this.filtered_data = this.data;
    }

    private void sortList() {
        Currency.Plain[] holder_list = new Currency.Plain[this.data.size()];
        this.data.toArray(holder_list);

        Arrays.sort(holder_list,(x, y)->{
            if (x.sorting_weight == y.sorting_weight)
                return names.get(x.iso_code_int).toLowerCase().compareTo(names.get(y.iso_code_int).toLowerCase());
            else
                return (y.sorting_weight - x.sorting_weight)*100;
        });

        this.data.clear();
        this.data.addAll(Arrays.asList(holder_list));

        if (base != null) {
            this.data.remove(base);
            this.data.add(0, base);
        }
    }

    public void setFilter(String filter) {
        this.filter = filter;
        if (filter != null && filter.length()>0) {
            filter = filter.toLowerCase();
            filtered_data = new ArrayList<>();
            for (Currency.Plain c : data) {
                if (c.iso_code_str.toLowerCase().contains(filter) || names.get(c.iso_code_int).toLowerCase().contains(filter))
                    filtered_data.add(c);
            }
            notifyDataSetChanged();
        } else
            filtered_data = data;
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
        holder.currency_item = obj;
        holder.position = position;


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

        holder.currency_name.setText(names.get(obj.iso_code_int));
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
        public Currency.Plain currency_item;
        public View root;
        public int position;

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

            edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(context.getContext(), view);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.mce_edit:
/*
                                RecordAddImageURLDialog addImageURLDialog = new RecordAddImageURLDialog();
                                addImageURLDialog.setTargetFragment(RecordItemFragment.this, REQUEST_IMAGE_URL_LOAD);
                                addImageURLDialog.show(getFragmentManager(), "Add_image_url");
*/
                                    return true;
                                case R.id.mce_default:
                                    setDefaultItem(currency_item,position);
/*
                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                                } else {
                                    Intent i = new Intent(
                                            Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                                    startActivityForResult(i, REQUEST_IMAGE_GALLERY);
                                }
*/
                                    return true;
                                case R.id.mce_delete:
                                    deleteItem(currency_item,position);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_currency_edit, popup.getMenu());
                    if (currency_item == base)
                        popup.getMenu().findItem(R.id.mce_default).setVisible(false);
                    else
                        popup.getMenu().findItem(R.id.mce_default).setVisible(true);
                    popup.show();
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

    private void setDefaultItem(Currency.Plain currency_item, int position) {
        int old_base_filtered = filtered_data.indexOf(currency_item);
        base = currency_item;
        for (Currency.Plain c : data) {
            c.exchange_base = base.iso_code_int;
            if (c != base)
                c.exchange_rate = c.exchange_rate/base.exchange_rate;
        }
        base.exchange_rate = 1;
        sortList();
        setFilter(filter);
//        notifyItemMoved(position,0);
        notifyItemRemoved(position);
//        notifyItemInserted(0);
        context.scrollToTop();
      //  notifyDataSetChanged();
        int first = layoutManager.findFirstVisibleItemPosition();
        int last = layoutManager.findLastVisibleItemPosition();
      //  context.scrollToTop();
        for (int i = first; i< last+1; i++)
            notifyItemChanged(i);
        //notifyDataSetChanged();

    }

    private void deleteItem(Currency.Plain currency_item, int position) {
        if (currency_item.iso_code_int != base.iso_code_int) {
            AlertDialog alert = new AlertDialog.Builder(context.getContext()).create();
            alert.setTitle("Delete");
            alert.setMessage("Delete " + names.get(currency_item.iso_code_int) + "?");
            alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            data.remove(currency_item);
                            filtered_data.remove(currency_item);
                            context.deleteCurrency(currency_item.iso_code_int);
                            notifyItemRemoved(position);
                            dialog.dismiss();
                        }
                    });
            alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
            alert.show();
        } else {
            AlertDialog alert = new AlertDialog.Builder(context.getContext()).create();
            alert.setTitle("Warning");
            alert.setMessage("Unable to delete default currency!\nPlease change default before deletion.");
            alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
    }
}

