package com.vividprojects.protoplanner.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import com.vividprojects.protoplanner.R;

import java.util.ArrayList;

import static android.view.View.GONE;

/**
 * Created by Smile on 06.11.2017.
 */
@RemoteViews.RemoteView
public class ChipsLayout extends ViewGroup {
    /** The amount of space used by children in the left gutter. */
    private int mLeftWidth;

    private Chip noneChip;   // TODO Сделать автоматом

    /** The amount of space used by children in the right gutter. */
    private int mRightWidth;

    /** These are used for computing child frames based on their gravity. */
    private final Rect mTmpContainerRect = new Rect();
    private final Rect mTmpChildRect = new Rect();

    public ChipsLayout(Context context) {
        super(context);
    }

    public ChipsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChipsLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Any layout manager that doesn't scroll will want this.
     */
    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /**
     * Ask all children to measure themselves and compute the measurement of this
     * layout based on the children.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        // These keep track of the space we are using on the left and right for
        // views positioned there; we need member variables so we can also use
        // these for layout later.
        mLeftWidth = 0;
        mRightWidth = 0;

        // Measurement will ultimately be computing these values.
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        noneChip.setVisibility(GONE);

        // Iterate through all children, measuring them and computing our dimensions
        // from their size.

        int[] widthList = new int[count];
        int[] heightList = new int[count];
        int[] visibilityList = new int[count];
        boolean emptyChips = true;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            widthList[i] = 0;
            heightList[i] = 0;
            visibilityList[i] = GONE;
            if (child.getVisibility() != GONE) {
                emptyChips = false;
                // Measure the child.
                child.measure(0,0);
                //measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

                // Update our size information based on the layout params.  Children
                // that asked to be positioned on the left or right go in those gutters.
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
//                if (lp.position == LayoutParams.POSITION_LEFT) {
//                    mLeftWidth += Math.max(maxWidth,
//                            child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
//                } else if (lp.position == LayoutParams.POSITION_RIGHT) {
//                    mRightWidth += Math.max(maxWidth,
//                            child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
//                } else {
//                    maxWidth = Math.max(maxWidth,
//                            child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
//                }
                widthList[i] = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                heightList[i] = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
                visibilityList[i] = VISIBLE;
//                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        // Total width is the maximum width of all inner children plus the gutters.
//        maxWidth += mLeftWidth + mRightWidth;

        // Check against our minimum height and width

//        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        maxWidth = widthSize;

        if (!emptyChips) {
            int curWidth = 0;
            boolean firstReal = true;

            for (int i = 0; i < count; i++) {
                if (visibilityList[i] != GONE) {
                    if (firstReal) {
                        maxHeight = heightList[i];
                        firstReal = false;
                    }

                    Log.d("Test", "Child width - " + widthList[i]);

                    if (curWidth + widthList[i] > maxWidth) {
                        maxHeight += heightList[i];
                        curWidth = widthList[i];
                    } else {
                        curWidth += widthList[i];
                    }
                }
            }
        } else {
            noneChip.setVisibility(VISIBLE);
            noneChip.measure(0,0);
            LayoutParams lp = (LayoutParams) noneChip.getLayoutParams();
            maxHeight = noneChip.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
        }

        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());

        Log.d("Test", "Height - " + maxHeight);
        Log.d("Test", "Width - " + maxWidth);

        setMeasuredDimension(maxWidth,maxHeight);

        // Report our final dimensions.
//        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
//                resolveSizeAndState(maxHeight, heightMeasureSpec,
//                        childState << MEASURED_HEIGHT_STATE_SHIFT));

      //  setMeasuredDimension(getMeasuredWidth(),200);
    }

    /**
     * Position all children within this layout.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        final int count = getChildCount();
        int curWidth, curHeight, curLeft, curTop, maxHeight;

        //get the available size of child view
        int childLeft = this.getPaddingLeft();
        int childTop = this.getPaddingTop();
        int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        int childWidth = childRight - childLeft;
        int childHeight = childBottom - childTop;

        maxHeight = 0;
        curLeft = childLeft;
        curTop = childTop;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                //Get the maximum size of the child
                child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST),
                        MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
                curWidth = child.getMeasuredWidth();
                curHeight = child.getMeasuredHeight();
                //wrap is reach to the end
                if (curLeft + curWidth >= childRight) {
                    curLeft = childLeft;
                    curTop += maxHeight;
                    maxHeight = 0;
                }
                //do the layout
                child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);
                //store the max height
                if (maxHeight < curHeight)
                    maxHeight = curHeight;
                curLeft += curWidth;
            }
        }


    }

    // ----------------------------------------------------------------------
    // The rest of the implementation is for custom per-child layout parameters.
    // If you do not need these (for example you are writing a layout manager
    // that does fixed positioning of its children), you can drop all of this.

    public void noneChip(Context ctx) {
        noneChip = new Chip(ctx,"None",Color.GRAY,false);
        super.addView(noneChip);
        noneChip.setVisibility(GONE);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ChipsLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /**
     * Custom per-child layout information.
     */
    public static class LayoutParams extends MarginLayoutParams {
        /**
         * The gravity to apply with the View to which these layout parameters
         * are associated.
         */
        public int gravity = Gravity.TOP | Gravity.START;

        public static int POSITION_MIDDLE = 0;
        public static int POSITION_LEFT = 1;
        public static int POSITION_RIGHT = 2;

        public int position = POSITION_MIDDLE;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            // Pull the layout param values from the layout XML during
            // inflation.  This is not needed if you don't care about
            // changing the layout behavior in XML.
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.CustomLayoutLP);
            gravity = a.getInt(R.styleable.CustomLayoutLP_android_layout_gravity, gravity);
            position = a.getInt(R.styleable.CustomLayoutLP_layout_position, position);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}