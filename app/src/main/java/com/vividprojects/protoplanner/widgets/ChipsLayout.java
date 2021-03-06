package com.vividprojects.protoplanner.widgets;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

import com.vividprojects.protoplanner.coredata.Label;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.utils.ActivityResolver;
import com.vividprojects.protoplanner.utils.Display;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Smile on 06.11.2017.
 */
@RemoteViews.RemoteView
public class ChipsLayout extends ViewGroup {
    public static final int MODE_FULL = 0;
    public static final int MODE_NON_TOUCH = 1;
    public static final int MODE_SMALL = 2;
    public static final int MODE_NONE = 3;
    public static final int MODE_NON_SELECTABLE = 4;

    private static final int arrayLength = 50;
    int[] widthList = new int[arrayLength];
    int[] heightList = new int[arrayLength];
    boolean[] checkedList = new boolean[arrayLength];
    int[] visibilityList = new int[arrayLength];

    private Chip noneChip;

    private boolean selectedSort = false;
    private boolean showEmptyChip = false;
    private boolean nameSort = false;

    private final int item_padding = Display.calc_pixels(4);
    private final int item_shift = 0;//Display.calc_pixels(4);

    private int mode = MODE_FULL;
    private List<Label.Plain> labels;
    private Set<String> selected;

    private InverseBindingListener attrChange;

    public ChipsLayout(Context context) {
        super(context);
        labels = new ArrayList<>();
    }

    public ChipsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChipsLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        labels = new ArrayList<>();
        selected = new HashSet<>();
    }

    public void deleteChip(String id) {
        for (int i=0; i < labels.size(); i++) {
            if (labels.get(i).id.equals(id)) {
                labels.remove(i);
                removeViewAt(i);
                break;
            }
        }
        invalidate();
        requestLayout();
    }

    public void insertChip(Label.Plain label, Activity activity) {
        this.labels.add(label);
        Chip chip = new Chip(getContext());
        chip.setData(label,mode);
        addView(chip);
        if (activity != null)
            activity.registerForContextMenu(chip);
        if (nameSort) {
            nameSort();
            if (selectedSort)
                selectedSort();
        }
        invalidate();
        requestLayout();
    }

    public void editChip(Label.Plain label) {
        for (int i=0; i< labels.size(); i++) {
            if (labels.get(i).id.equals(label.id)) {
                labels.get(i).name = label.name;
                labels.get(i).color = label.color;
                ((Chip)getChildAt(i)).setColor(label.color);
                ((Chip)getChildAt(i)).setName(label.name);
                break;
            }
        }
        if (nameSort) {
            nameSort();
            if (selectedSort)
                selectedSort();
        }
        invalidate();
        requestLayout();
    }

    public void setMode(int mode){
        this.mode = mode;
    }

    public void setData(List<Label.Plain> labels, String[] selected) {
        setData(labels,selected,null);
    }

    public void setShowEmptyChip(boolean showEmptyChip) {
        this.showEmptyChip = showEmptyChip;
    }

    public void setData(List<Label.Plain> labels, String[] selected, Activity activity) {
        this.labels.clear();
        this.selected.clear();

        removeAllViews();

        if (showEmptyChip) {
            noneChip = new Chip(getContext());
            noneChip.setData(null, MODE_NONE);
            noneChip.setVisibility(GONE);
            addView(noneChip);
            this.labels.add(Label.getPlain(0,"None","","0"));
        }

        this.labels.addAll(labels);
        if (selected!=null) {
            for (int i=0; i < selected.length; i++)
                this.selected.add(selected[i]);
        }

        for (Label.Plain label : labels) {
            Chip chip = new Chip(getContext());
            if (selected!=null)
                chip.setData(label,mode,this.selected.contains(label.id));
            else
                chip.setData(label,mode);

            addView(chip);
            activity = activity != null ? activity : ActivityResolver.getActivity(this);
            if (activity != null)
                activity.registerForContextMenu(chip);
//            this.labels.add(LabelHolder.getHolder(label.name,isSelected));
        }
    }

    public void moveChild(int from, int to){
        View child = this.getChildAt(from);
        detachViewFromParent(from);
        attachViewToParent(child,to,child.getLayoutParams());
        labels.add(to,labels.remove(from));
        //requestLayout();
    }

    public void setFilter(String filter){
        int count = getChildCount();
        for (int i = count-1; i >= 0; i--) {
            Chip child = (Chip) getChildAt(i);
            if (child.matchFilter(filter))
                child.setVisibility(VISIBLE);
            else
                child.setVisibility(GONE);
        }
        invalidate();
    }

    public void setSelectedSort(boolean sort){
        selectedSort = sort;
        if (selectedSort) {
            if (nameSort)
                nameSort();
            selectedSort();
        }
        invalidate();
        requestLayout();
    }

    private void selectedSort() {
        int countSelected = 0;
        for (int i=0; i < labels.size(); i++) {
            if (selected.contains(labels.get(i).id)) {
                moveChild(i,countSelected);
                countSelected++;
            }
        }
    }

    private void nameSort(){
        int childCount = labels.size();
        LabelHolder[] oldholder = new LabelHolder[childCount];
        String[] test = new String[childCount];
        for (int i=0; i < childCount; i++) {
            String t = labels.get(i).name;
            oldholder[i] = LabelHolder.getHolder(t,i);
            test[i] = t;
        }

        Arrays.sort(oldholder,(x, y)->{
            return x.name.toLowerCase().compareTo(y.name.toLowerCase());
        });

        for (int i=0; i < childCount; i++) {
            int curPos = oldholder[i].position;
            moveChild(curPos,i);
            for (int k=0;k<childCount;k++)
                if (oldholder[k].position>=i && oldholder[k].position<curPos) oldholder[k].position++;

        }
    }

    public void setNameSort(boolean sort){
        nameSort = sort;
        if (nameSort) {
            nameSort();
            if (selectedSort)
                selectedSort();
            invalidate();
            requestLayout();
        }
    }

    public void chipSelected(String id) {
        selected.add(id);
        if (selectedSort)
            setSelectedSort(true);
        if (attrChange != null)
            attrChange.onChange();
    }

    public void chipUnSelected(String id) {
        selected.remove(id);
        if (selectedSort)
            setSelectedSort(true);
        if (attrChange != null)
            attrChange.onChange();
    }

    public String[] getSelected(){
        String[] rlist = new String[selected.size()];
        selected.toArray(rlist);
        return rlist;
    }

    public String[] getAllLabels(){
        int size = labels.size();
        String[] rlist = new String[size];
        for (int i = 0; i < size; i++)
            rlist[i] = labels.get(i).id;
        return rlist;
    }

    public void setSelectedChangeListener(final InverseBindingListener attrChange) {
        this.attrChange = attrChange;
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        if (noneChip!=null)
            noneChip.setVisibility(GONE);

        if (count>arrayLength) {
            widthList = new int[count];
            heightList = new int[count];
            checkedList = new boolean[count];
            visibilityList = new int[count];
        }

        boolean emptyChips = true;
        for (int i = 0; i < count; i++) {
            final Chip child = (Chip) getChildAt(i);
            widthList[i] = 0;
            heightList[i] = 0;
            visibilityList[i] = GONE;
            checkedList[i] = child.isLabelSelected();
            if (child.getVisibility() != GONE) {
                emptyChips = false;
                child.measure(0,0);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                widthList[i] = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin + item_padding*2;
                heightList[i] = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin  + item_padding*2;
                visibilityList[i] = VISIBLE;
            }
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        maxWidth = widthSize - getPaddingStart() - getPaddingEnd();

        boolean lastChecked = false;

        if (!emptyChips) {
            int curWidth = 0;
            boolean firstReal = true;

            for (int i = 0; i < count; i++) {
                if (visibilityList[i] != GONE) {
                    if (firstReal) {
                        maxHeight = heightList[i];
                        firstReal = false;
                    }

                    if (selectedSort && lastChecked && !checkedList[i]){
                        maxHeight += heightList[i] + Display.calc_pixels(10);
                        curWidth = widthList[i];
                    } else {
                        if (curWidth + widthList[i] > maxWidth) {
                            maxHeight += heightList[i];
                            curWidth = widthList[i];
                        } else {
                            curWidth += widthList[i];
                        }
                    }
                    lastChecked = checkedList[i];
                }
            }
        } else if (noneChip!=null) {
            noneChip.setVisibility(VISIBLE);
            noneChip.measure(0,0);
            LayoutParams lp = (LayoutParams) noneChip.getLayoutParams();
            maxHeight = noneChip.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
        }

        maxHeight += getPaddingBottom() + getPaddingTop();
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth += getPaddingStart() + getPaddingEnd();

        setMeasuredDimension(maxWidth,maxHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        final int count = getChildCount();
        int curWidth, curHeight, curLeft, curTop, maxHeight;
        int childLeft = this.getPaddingLeft() ;
        int childTop = this.getPaddingTop();
        int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        int childWidth = childRight - childLeft;
        int childHeight = childBottom - childTop;

        maxHeight = 0;
        curLeft = childLeft;
        curTop = childTop;

        boolean lastChecked = false;

        for (int i = 0; i < count; i++) {
            Chip child = (Chip) getChildAt(i);
            if (child.getVisibility() != GONE) {
                child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST),
                        MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
                curWidth = child.getMeasuredWidth();
                curHeight = child.getMeasuredHeight();
                if (selectedSort && lastChecked && !child.isLabelSelected()) {
                    curLeft = childLeft;
                    curTop += maxHeight + Display.calc_pixels(10);
                    maxHeight = 0;
                } else {
                    if (curLeft + curWidth + item_padding * 2 >= childRight) {
                        curLeft = childLeft;
                        curTop += maxHeight;
                        maxHeight = 0;
                    }
                }
                boolean elevated = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    if (child.getElevation()>0) elevated = true;
                if (elevated) {
                    child.layout(curLeft + item_padding, curTop - item_shift + item_padding, curLeft + item_padding + curWidth, curTop + item_padding - item_shift + curHeight);
                }
                else
                    child.layout(curLeft + item_padding, curTop + item_padding, curLeft + item_padding + curWidth, curTop + item_padding + curHeight);
                if (maxHeight < curHeight + item_padding*2)
                    maxHeight = curHeight + item_padding*2;
                curLeft += curWidth + item_padding*2;
                lastChecked = child.isLabelSelected(); //TODO Заменить на Array поддерживающий состояния chip
            }
        }
    }

    public void noneChip(Context ctx) {
        noneChip = new Chip(ctx,"None",Color.GRAY,false);
        noneChip.isNone(true);
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

    public static class LayoutParams extends MarginLayoutParams {
        public int gravity = Gravity.TOP | Gravity.START;

        public static int POSITION_MIDDLE = 0;
        public static int POSITION_LEFT = 1;
        public static int POSITION_RIGHT = 2;

        public int position = POSITION_MIDDLE;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
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

    @BindingAdapter("bind:selectedSort")
    public static void setSelectedSort(ChipsLayout chl, boolean selectedSort) {
        chl.setSelectedSort(selectedSort);
    }

    @BindingAdapter("bind:nameSort")
    public static void setNameSort(ChipsLayout chl, boolean nameSort) {
        chl.setNameSort(nameSort);
    }

    @BindingAdapter("bind:chipsFilter")
    public static void setChipsFilter(ChipsLayout chl, String filter) {
        chl.setFilter(filter);
    }

    @BindingAdapter("bind:chipsInsertChip")
    public static void bindingInsertChip(ChipsLayout chl, Label.Plain chip) {
        if (chip != null)
            chl.insertChip(chip, ActivityResolver.getActivity(chl));
    }

    @BindingAdapter("bind:chipsEditChip")
    public static void bindingEditChip(ChipsLayout chl, Label.Plain chip) {
        if (chip != null)
            chl.editChip(chip);
    }

    @BindingAdapter("bind:chipsRemoveChip")
    public static void bindingRemoveChip(ChipsLayout chl, String id) {
        if (id != null)
            chl.deleteChip(id);
    }

    @InverseBindingAdapter(attribute = "bind:labelsLayoutDataSelected", event = "labelsLayoutDataSelectedChanged")
    public static String[] getLabelsSelected(ChipsLayout chl) {
        return chl.getSelected();
    }

    @BindingAdapter("app:labelsLayoutDataSelectedChanged")
    public static void setListeners(ChipsLayout chl, final InverseBindingListener attrChange) {
        chl.setSelectedChangeListener(attrChange);
    }

}

class LabelHolder {
    public String name = "";
    public boolean selected = false;
    public int position = 0;

    LabelHolder(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    LabelHolder(String name, int position) {
        this.name = name;
        this.position = position;
    }

    public static LabelHolder getHolder(String name, boolean selected) {
        return new LabelHolder(name,selected);
    }

    public static LabelHolder getHolder(String name, int position) {
        return new LabelHolder(name,position);
    }
}
