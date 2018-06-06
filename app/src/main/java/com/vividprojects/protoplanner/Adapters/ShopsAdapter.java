package com.vividprojects.protoplanner.Adapters;

import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.R;

import java.util.HashSet;
import java.util.Set;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Smile on 27.10.2017.
 */


public class ShopsAdapter extends RealmRecyclerViewAdapter<VariantInShop, ShopsAdapter.MyViewHolder> {

    private boolean inDeletionMode = false;
    private Set<Integer> countersToDelete = new HashSet<Integer>();
   // private int mExpandedPosition=-1;
    private boolean[] mExpandedArray;
//    private RecyclerView masterRV;

    public ShopsAdapter(OrderedRealmCollection<VariantInShop> data /*, RecyclerView rv*/) {
        super(data, true);
        setHasStableIds(true);

        mExpandedArray = new boolean[data.size()];
//        masterRV = rv;
    }

    void enableDeletionMode(boolean enabled) {
        inDeletionMode = enabled;
        if (!enabled) {
            countersToDelete.clear();
        }
        notifyDataSetChanged();
    }

    Set<Integer> getCountersToDelete() {
        return countersToDelete;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final VariantInShop obj = getItem(position);
        holder.data = obj;
        //noinspection ConstantConditions
//            holder.record_title.setText(obj.getId());
        holder.si_title.setText(obj.getTitle());
        holder.si_url.setText(obj.getURL());
        holder.si_price.setText(""+obj.getPrice());
        holder.si_comment.setText(obj.getComment());
        holder.si_address.setText(obj.getAddress());

        final boolean isExpanded = mExpandedArray[position];
        final ViewGroup rv = holder.root;
        final int p = position;
        if (isExpanded) {
            holder.si_comment.setVisibility(View.VISIBLE);
            holder.si_address.setVisibility(View.VISIBLE);
            holder.si_image_comment.setVisibility(View.VISIBLE);
            holder.si_image_address.setVisibility(View.VISIBLE);
            holder.si_expand.setImageResource(R.drawable.ic_expand_less_black_24dp);
        } else {
            holder.si_comment.setVisibility(View.GONE);
            holder.si_address.setVisibility(View.GONE);
            holder.si_image_comment.setVisibility(View.GONE);
            holder.si_image_address.setVisibility(View.GONE);
            holder.si_expand.setImageResource(R.drawable.ic_expand_more_black_24dp);
        }
        holder.itemView.setActivated(isExpanded);
        holder.si_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExpandedArray[p])
                    //int i = 0;
                    // TransitionManager.beginDelayedTransition(masterRV);
                    TransitionManager.beginDelayedTransition(rv);
                mExpandedArray[p] = !mExpandedArray[p];
                notifyDataSetChanged();
            }
        });

/*
            holder.deletedCheckBox.setChecked(countersToDelete.contains(obj.createCount()));
            if (inDeletionMode) {
                holder.deletedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            countersToDelete.add(obj.createCount());
                        } else {
                            countersToDelete.remove(obj.createCount());
                        }
                    }
                });
            } else {
                holder.deletedCheckBox.setOnCheckedChangeListener(null);
            }
            holder.deletedCheckBox.setVisibility(inDeletionMode ? View.VISIBLE : View.GONE);
*/
    }

/*
        @Override
        public long getItemId(int index) {
            //noinspection ConstantConditions
            return getItem(index).createCount();
        }
*/

    class MyViewHolder extends RecyclerView.ViewHolder {
        ViewGroup root;
        TextView si_price;
        TextView si_url;
        TextView si_title;
        TextView si_address;
        TextView si_comment;
        ImageView si_image_url;
        ImageView si_image_title;
        ImageView si_image_address;
        ImageView si_image_comment;
        ImageButton si_expand;
        public VariantInShop data;

        MyViewHolder(View view) {
            super(view);
            si_price = (TextView) view.findViewById(R.id.si_price);
            si_url = (TextView) view.findViewById(R.id.si_url);
            si_title = (TextView) view.findViewById(R.id.si_title);
            si_address = (TextView) view.findViewById(R.id.si_address);
            si_comment = (TextView) view.findViewById(R.id.si_comment);
            si_image_url = (ImageView) view.findViewById(R.id.si_image_url);
            si_image_title = (ImageView) view.findViewById(R.id.si_image_title);
            si_image_address = (ImageView) view.findViewById(R.id.si_image_address);
            si_image_comment = (ImageView) view.findViewById(R.id.si_image_comment);
       //     si_expand = (ImageButton) view.findViewById(R.id.si_expand);
            root = (ViewGroup) view;

/*            Button edit_button = (Button) view.findViewById(R.id.si_expand);
            edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Test", "------------------------------ Edit click" + data.getId());
                    mCallback.onRecordEditClick(data.getId());
                }
            });*/

        }
    }
}

