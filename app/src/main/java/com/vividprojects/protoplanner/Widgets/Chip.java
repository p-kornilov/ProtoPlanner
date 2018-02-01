package com.vividprojects.protoplanner.Widgets;

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

import com.vividprojects.protoplanner.R;

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

/*        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {setVisibility(GONE);
            }
        });*/

        mContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteButton.getVisibility()==VISIBLE) {
                    setDeleteButtonVisible(false);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        setElevation(0);
                    }
                }
                else {
                    setDeleteButtonVisible(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
                        float px = 16 * (metrics.densityDpi / 160f);
                        setElevation(Math.round(px));
                    }

                }
            }
        });
    }


    public void setTitle(String title) {
        textView.setText(title);
    }

    public void setColor(int color){
        mContent.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
/*        if (color== Color.YELLOW) {
            textView.setTextColor(Color.BLACK);
            deleteButton.setImageResource(R.drawable.ic_check_circle_black_24dp);
        }*/


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
/*        LayerDrawable rd = (LayerDrawable) mContent.getBackground();

        GradientDrawable sd = (GradientDrawable) rd.findDrawableByLayerId(R.id.level1);
        sd.setLevel(2);
        sd.setVisible(true,true);
        //sd.setColor();
        sd.setStroke(3,color);*/

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


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        /// ..
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
            this.height = height-3;
        }



        @Override
        public void getOutline(View view, Outline outline) {
           // outline.setRect(0, 0, width, height);
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            float px = 16 * (metrics.densityDpi / 160f);

            outline.setRoundRect(new Rect(0, 0, width, height),Math.round(px));
        }
    }
}
