package com.vividprojects.protoplanner.TMP;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.Display;

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
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.testRecordAdapterLabel);
            cardView = v.findViewById(R.id.card_view);
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
        ViewHolder vh = new ViewHolder(v);
        // После найти каждый view findView
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset[position]);
        if (position==0) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.cardView.getLayoutParams();
                p.setMargins(Display.calc_pixels(8), Display.calc_pixels(8), Display.calc_pixels(8), 0);
                holder.cardView.requestLayout();
        } else if (position == mDataset.length-1) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.cardView.getLayoutParams();
            p.setMargins(Display.calc_pixels(8), 0, Display.calc_pixels(8), Display.calc_pixels(8));
            holder.cardView.requestLayout();
        }
        else {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.cardView.getLayoutParams();
            p.setMargins(Display.calc_pixels(8), 0, Display.calc_pixels(8), 0);
            holder.cardView.requestLayout();
        }
        Log.d("Test", "------------------------------ on Bind View holder - " + mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
