package com.vividprojects.protoplanner.Widgets;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.Display;

import static com.vividprojects.protoplanner.Widgets.ChipsLayout.MODE_FULL;
import static com.vividprojects.protoplanner.Widgets.ChipsLayout.MODE_NONE;
import static com.vividprojects.protoplanner.Widgets.ChipsLayout.MODE_NON_TOUCH;
import static com.vividprojects.protoplanner.Widgets.ChipsLayout.MODE_SMALL;
import static com.vividprojects.protoplanner.Widgets.Pallet.AMBER;
import static com.vividprojects.protoplanner.Widgets.Pallet.BLACK;
import static com.vividprojects.protoplanner.Widgets.Pallet.BLUE;
import static com.vividprojects.protoplanner.Widgets.Pallet.BLUE_GRAY;
import static com.vividprojects.protoplanner.Widgets.Pallet.BROWN;
import static com.vividprojects.protoplanner.Widgets.Pallet.CYAN;
import static com.vividprojects.protoplanner.Widgets.Pallet.DEEP_ORANGE;
import static com.vividprojects.protoplanner.Widgets.Pallet.DEEP_PURPLE;
import static com.vividprojects.protoplanner.Widgets.Pallet.GREEN;
import static com.vividprojects.protoplanner.Widgets.Pallet.GREY;
import static com.vividprojects.protoplanner.Widgets.Pallet.INDIGO;
import static com.vividprojects.protoplanner.Widgets.Pallet.LIGHT_BLUE;
import static com.vividprojects.protoplanner.Widgets.Pallet.LIGHT_GREEN;
import static com.vividprojects.protoplanner.Widgets.Pallet.LIME;
import static com.vividprojects.protoplanner.Widgets.Pallet.ORANGE;
import static com.vividprojects.protoplanner.Widgets.Pallet.PINK;
import static com.vividprojects.protoplanner.Widgets.Pallet.PURPLE;
import static com.vividprojects.protoplanner.Widgets.Pallet.RED;
import static com.vividprojects.protoplanner.Widgets.Pallet.TEAL;
import static com.vividprojects.protoplanner.Widgets.Pallet.YELLOW;
import static com.vividprojects.protoplanner.Widgets.Pallet.tAMBER;
import static com.vividprojects.protoplanner.Widgets.Pallet.tBLACK;
import static com.vividprojects.protoplanner.Widgets.Pallet.tBLUE;
import static com.vividprojects.protoplanner.Widgets.Pallet.tBLUE_GRAY;
import static com.vividprojects.protoplanner.Widgets.Pallet.tBROWN;
import static com.vividprojects.protoplanner.Widgets.Pallet.tCYAN;
import static com.vividprojects.protoplanner.Widgets.Pallet.tDEEP_ORANGE;
import static com.vividprojects.protoplanner.Widgets.Pallet.tDEEP_PURPLE;
import static com.vividprojects.protoplanner.Widgets.Pallet.tGREEN;
import static com.vividprojects.protoplanner.Widgets.Pallet.tGREY;
import static com.vividprojects.protoplanner.Widgets.Pallet.tINDIGO;
import static com.vividprojects.protoplanner.Widgets.Pallet.tLIGHT_BLUE;
import static com.vividprojects.protoplanner.Widgets.Pallet.tLIGHT_GREEN;
import static com.vividprojects.protoplanner.Widgets.Pallet.tLIME;
import static com.vividprojects.protoplanner.Widgets.Pallet.tORANGE;
import static com.vividprojects.protoplanner.Widgets.Pallet.tPINK;
import static com.vividprojects.protoplanner.Widgets.Pallet.tPURPLE;
import static com.vividprojects.protoplanner.Widgets.Pallet.tRED;
import static com.vividprojects.protoplanner.Widgets.Pallet.tTEAL;
import static com.vividprojects.protoplanner.Widgets.Pallet.tYELLOW;

/**
 * Created by Smile on 06.11.2017.
 */

public class Chip extends ConstraintLayout {
    View rootView;
    TextView textView;
    ImageView deleteButton;
    Context mContext;
    ConstraintLayout mContent;

    private int mode = MODE_FULL;
    private Label.Plain label;
    private boolean isNone = false;

    public Chip(Context context) {
        super(context);
        init(context);
    }

    public Chip(Context context, String title, int color, boolean b) {
        super(context);
        init(context);
        setTitle(title);
        setColor(color);
        deleteButton.setVisibility(GONE);
    }

    public Chip(Context context,int color, boolean type_normal) {
        super(context);
        init(context);
        setColor(color);

        if (!type_normal) {
            deleteButton.setVisibility(GONE);
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
        textView = rootView.findViewById(R.id.textChip);
        mContent = rootView.findViewById(R.id.content);
        deleteButton = rootView.findViewById(R.id.deleteChip);
    }

    public boolean matchFilter(String filter){
        if (label != null)
            return label.name.contains(filter);
        else
            return false;
    }

    public void isNone(boolean isNone){
        this.isNone = isNone;
    }

    public boolean isLabelSelected() {
        return deleteButton.getVisibility() == VISIBLE;
    }

    public void setData(Label.Plain label, int mode,boolean selected){
        this.mode = mode;
        this.label = label;
        switch (mode) {
            case MODE_FULL:
                setColor(label.color);
                textView.setText(label.name);
                textView.setPadding(0,0,Display.calc_pixels(12),0);
                if (selected)
                    deleteButton.setVisibility(VISIBLE);
                else
                    deleteButton.setVisibility(GONE);
                mContent.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (deleteButton.getVisibility()==VISIBLE)
                            hideButton();
                        else
                            showButton();
                    }
                });
                deleteButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hideButton();
                    }
                });
                break;
            case MODE_NON_TOUCH:
                setColor(label.color);
                textView.setText(label.name);
                textView.setPadding(0,0,Display.calc_pixels(12),0);
                deleteButton.setVisibility(GONE);
                mContent.setOnClickListener(null);
                deleteButton.setOnClickListener(null);
                break;
            case MODE_SMALL:
                deleteButton.setVisibility(GONE);
                textView.setText(" ");
                LayoutParams params = (LayoutParams) mContent.getLayoutParams();
                params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
                mContent.setLayoutParams(params);
                break;
            case MODE_NONE:
                deleteButton.setVisibility(GONE);
                setColor(Color.GRAY);
                textView.setText("None");
                textView.setPadding(0,0,Display.calc_pixels(12),0);
                deleteButton.setVisibility(GONE);
                mContent.setOnClickListener(null);
                deleteButton.setOnClickListener(null);
        }
    }

    public void setData(Label.Plain label, int mode){
        setData(label,mode,false);
    }

    private void hideButton(){
        TransitionManager.beginDelayedTransition(mContent, new ChangeBounds());
        textView.setPadding(0,0,Display.calc_pixels(12),0);
        deleteButton.setVisibility(GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(0);
        }

        ((ChipsLayout)getParent()).chipSelected();
    }

    private void showButton(){
        TransitionManager.beginDelayedTransition(mContent);
        deleteButton.setVisibility(VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setElevation(Display.calc_pixels(8));
        }
        textView.setPadding(0,0,0,0);
        ((ChipsLayout)getParent()).chipSelected();
    }

    public String getTitle(){
        return textView.getText().toString();
    }

    public void setTitle(String title) {
        textView.setText(title);
    }

    public void setColor(int color){
        mContent.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        switch (color) {
            case RED:
                textView.setTextColor(Color.WHITE);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            case PINK:
                textView.setTextColor(tPINK);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            case PURPLE:
                textView.setTextColor(tPURPLE);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            case DEEP_PURPLE:
                textView.setTextColor(tDEEP_PURPLE);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            case INDIGO:
                textView.setTextColor(tINDIGO);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            case BLUE:
                textView.setTextColor(tBLUE);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            case LIGHT_BLUE:
                textView.setTextColor(tLIGHT_BLUE);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            case CYAN:
                textView.setTextColor(tCYAN);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            case TEAL:
                textView.setTextColor(tTEAL);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            case GREEN:
                textView.setTextColor(tGREEN);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            case LIGHT_GREEN:
                textView.setTextColor(tLIGHT_GREEN);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            case LIME:
                textView.setTextColor(tLIME);
                deleteButton.setImageResource(R.drawable.ic_check_circle_black_24dp);
                break;
            case YELLOW:
                textView.setTextColor(tYELLOW);
                deleteButton.setImageResource(R.drawable.ic_check_circle_black_24dp);
                break;
            case AMBER:
                textView.setTextColor(tAMBER);
                deleteButton.setImageResource(R.drawable.ic_check_circle_black_24dp);
                break;
            case ORANGE:
                textView.setTextColor(tORANGE);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            case DEEP_ORANGE:
                textView.setTextColor(tDEEP_ORANGE);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            case BROWN:
                textView.setTextColor(tBROWN);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            case GREY:
                textView.setTextColor(tGREY);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            case BLUE_GRAY:
                textView.setTextColor(tBLUE_GRAY);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            case BLACK:
                textView.setTextColor(tBLACK);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;
            default:
                textView.setTextColor(tRED);
                deleteButton.setImageResource(R.drawable.ic_check_circle_grey_24dp);
                break;

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w,h,oldw,oldh);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new CustomOutline(w, h));
        }
        invalidate();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class CustomOutline extends ViewOutlineProvider {
        int width;
        int height;

        CustomOutline(int width, int height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(new Rect(0, 0, width, height),Display.calc_pixels(16));
        }
    }
}
