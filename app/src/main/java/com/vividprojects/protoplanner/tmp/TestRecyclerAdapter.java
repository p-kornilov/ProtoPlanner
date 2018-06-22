package com.vividprojects.protoplanner.tmp;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.TextView;

import com.vividprojects.protoplanner.R;

/**
 * Created by Smile on 01.11.2017.
 */

public class TestRecyclerAdapter extends RecyclerView.Adapter<TestRecyclerAdapter.ViewHolder> {
    private String[] mDataset = {
            "Test1",
            "Test2",
            "Test3",
            "Test4",
            "Test5",
            "Test6",
            "Test7",
            "Test8",
            "Test9",
            "Test10",
            "Test11",
            "Test12",
            "Test1",
            "Test2",
            "Test3",
            "Test4",
            "Test5",
            "Test6",
            "Test7",
            "Test8",
            "Test9",
            "Test10",
            "Test11",
            "Test12"
    };

    private LayoutInflater inflater;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public CardView cardView;
        public View root;
        public ViewHolder(View v) {

            super(v);

            root = v;
            mTextView = (TextView) v.findViewById(R.id.testRecordAdapterLabel);
          //  cardView = v.findViewById(R.id.card_view);
            Log.d("Test", "------------------------------ View holder");
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Test", "------------------------------ Edit click" + mTextView.getText());
                }
            });
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TestRecyclerAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TestRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view

       // View v = inflater.inflate(R.layout.test_record_adapter_element, parent, false); // Сначала вернуть view инфлаттером
 //       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_record_adapter_element, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewCompat.setElevation(v,v.getResources().getDimension(R.dimen.cardElevation));

        ViewHolder vh = new ViewHolder(v);
        // После найти каждый view findView
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        int drawableResource;
        if (mDataset.length == 1)
            drawableResource = R.drawable.list_item_background_single;
        else if (position == 0)
            drawableResource = R.drawable.list_item_background_top;
        else if (position == mDataset.length - 1)
            drawableResource = R.drawable.list_item_background_bottom;
        else
            drawableResource = R.drawable.list_item_background;

        holder.root.setBackground(ContextCompat.getDrawable(holder.root.getContext(),drawableResource));
//        holder.root.setPadding(Display.calc_pixels(16),Display.calc_pixels(16),Display.calc_pixels(16),Display.calc_pixels(16));


        if (mDataset.length == 1 || position==0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                holder.root.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
/*
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.cardView.getLayoutParams();
                p.setMargins(0, Display.calc_pixels(8), 0, 0);
                holder.cardView.requestLayout();
*/
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                holder.root.setOutlineProvider(new CustomOutline());
        }
        holder.mTextView.setText(mDataset[position]);
        Log.d("Test", "------------------------------ on Bind View holder - " + mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class CustomOutline extends ViewOutlineProvider {

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRect(0, (int)view.getResources().getDimension(R.dimen.cardElevation), view.getWidth(),view.getHeight());
        }
    }
}
