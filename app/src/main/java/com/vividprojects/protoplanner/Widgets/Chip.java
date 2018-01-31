package com.vividprojects.protoplanner.Widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vividprojects.protoplanner.R;

/**
 * Created by Smile on 06.11.2017.
 */

public class Chip extends RelativeLayout {
    View rootView;
    TextView textView;
    ImageView deleteButton;
    Context mContext;
    LinearLayout mContent;
    boolean type_normal = true;  // TODO Сделать везде проверку

    public Chip(Context context) {
        super(context);
        init(context);
    }

    public Chip(Context context, String title, int color, boolean b) {
        super(context);
        init(context);
        setTitle(title);
        setColor(color);
        setDeleteButtonVisible(b);
    }

    public Chip(Context context,int color, boolean type_normal) {
        super(context);
        init(context);
        setColor(color);
        this.type_normal = type_normal;
        if (!type_normal) {
            setDeleteButtonVisible(false);
            textView.setText(" ");

            LayoutParams params = (LayoutParams) mContent.getLayoutParams();
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
            mContent.setLayoutParams(params);
        }
    }

    public Chip(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.chip_layout,this);
        textView = (TextView) rootView.findViewById(R.id.textChip);
        mContent = (LinearLayout) rootView.findViewById(R.id.content);

        deleteButton = rootView.findViewById(R.id.deleteChip);

        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(GONE);
            }
        });
    }

    public void setTitle(String title) {
        textView.setText(title);
    }

    public void setColor(int color){
        //mContent.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        LayerDrawable rd = (LayerDrawable) mContent.getBackground();

        GradientDrawable sd = (GradientDrawable) rd.findDrawableByLayerId(R.id.level1);
        sd.setLevel(2);
        sd.setVisible(true,true);
        //sd.setColor();
        sd.setStroke(3,color);

        Log.d("Test", "< SET Color >");
    }

    public void setDeleteButtonVisible(boolean b) {
        if (b) {
            deleteButton.setVisibility(VISIBLE);
            textView.setPadding(textView.getPaddingLeft(),textView.getPaddingTop(),0,textView.getPaddingBottom());
        } else {
            deleteButton.setVisibility(GONE);
            int padding_in_dp = 8;
            final float scale = getResources().getDisplayMetrics().density;
            int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
            textView.setPadding(textView.getPaddingLeft(),textView.getPaddingTop(),padding_in_px,textView.getPaddingBottom());
        }
    }

    public void setDeleteButtonStyle(int id){
        deleteButton.setImageResource(id);
    }

}
