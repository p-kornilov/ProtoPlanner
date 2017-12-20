package com.vividprojects.protoplanner.Interface;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.vividprojects.protoplanner.Adapters.RecordListAdapter;
import com.vividprojects.protoplanner.Adapters.ShopsAdapter;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.MainCommunication;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Widgets.Chip;
import com.vividprojects.protoplanner.Widgets.ChipsLayout;
import com.vividprojects.protoplanner.Widgets.HorizontalImages;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Smile on 31.10.2017.
 */

public class RecordFragment extends Fragment {

    ChipsLayout chl;
    private Realm realm;
    private MainCommunication mCallback;
    private boolean inCommentEdit = false;
    private ViewSwitcher commentSwitcher;
    private EditText commentEdit;
    private TextView commentView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

/*        Activity activity = getActivity();
        try {
            mCallback = (MainCommunication) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MainCommunication");
        }*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Test", "onCreate - Record Fragment");
        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Test", "onCreateView - Record Fragment");
        View v = (View) inflater.inflate(R.layout.record_fragment, container, false);

        RecyclerView lv = v.findViewById(R.id.recyclerView1);

        String[] values = new String[] { "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };

//        lv.setAdapter(new TestRecyclerAdapter(values,getContext()));
        RealmResults<VariantInShop> ls = realm.where(VariantInShop.class).equalTo("variant.title","Булка").findAll();
        lv.setAdapter(new ShopsAdapter(ls,mCallback));
//        lv.setAdapter(new ShopsAdapter(ls,mCallback));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);
        lv.setLayoutManager(layoutManager);
        lv.setNestedScrollingEnabled(false);
        lv.setFocusable(false);

/*        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(lv.getContext(), DividerItemDecoration.VERTICAL);
        lv.addItemDecoration(dividerItemDecoration);*/

        chl = v.findViewById(R.id.chipLayout);

        Chip chip = new Chip(getContext());
        chip.setTitle("Hello");
        chip.setColor(Color.BLUE);
        chl.addView(chip);
        Chip chip1 = new Chip(getContext());
        chip1.setTitle("Hello sdf sd fsd f");
        chip1.setColor(Color.RED);
        chl.addView(chip1);
        Chip chip2 = new Chip(getContext());
        chip2.setTitle("SDdfdfg df bgdf gd  sd");
        chip2.setColor(Color.GREEN);
        chl.addView(chip2);
        Chip chip3 = new Chip(getContext());
        chip3.setTitle("EDFF");
        chip3.setColor(Color.YELLOW);
        chl.addView(chip3);
        Chip chip4 = new Chip(getContext());
        chip4.setTitle("Aaaaaaaaaaaaaaa");
        chip4.setColor(Color.MAGENTA);
        chl.addView(chip4);
        Chip noneChip = new Chip(getContext(),"None",Color.GRAY,false);
        chl.noneChip(noneChip);

        HorizontalImages hi = (HorizontalImages) v.findViewById(R.id.horizontalImages);
        hi.setNoneImage(true);
//        hi.setNoneImage(false);
//        hi.setNoneImage(true);

/*
        ImageView iv = new ImageView(getContext());
        iv.setImageResource(R.drawable.record_test);
        int width_in_dp = 100;
        final float scale = getResources().getDisplayMetrics().density;
        int width_in_px = (int) (width_in_dp * scale + 0.5f);

        int paddind = (int) (4*scale+0.5f);
        iv.setLayoutParams(new LinearLayout.LayoutParams(width_in_px,LinearLayout.LayoutParams.MATCH_PARENT));
        iv.setPadding(paddind,0,paddind,0);
        hi.addView(iv);
*/
        RecyclerView alternatives = (RecyclerView) v.findViewById(R.id.recyclerView2);
//        recycler.setAdapter(new TestRecyclerAdapter(ar,getContext()));
//        recycler.setAdapter(new MyAdapter(testarray,getContext()));
        RealmResults<Record> ar = realm.where(Record.class).findAllAsync();

        Log.d("Test", "--------------- Records count - " + ar.size());

     //   alternatives.setAdapter(new RecordListAdapter(ar,mCallback));

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setAutoMeasureEnabled(true);
        alternatives.setLayoutManager(layoutManager2);
        alternatives.setNestedScrollingEnabled(false);
        alternatives.setFocusable(false);

        commentSwitcher = (ViewSwitcher) v.findViewById(R.id.rf_comment_switcher);
//        ImageButton editCommentButton = (ImageButton) v.findViewById(R.id.rf_comment_edit_button);
        //commentEdit
        commentEdit = (EditText) v.findViewById(R.id.rf_comment_edit);
        commentView = (TextView) v.findViewById(R.id.rf_comment_text);

/*        editCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewSwitcher.showNext();
                ImageButton im = (ImageButton) view;
                inCommentEdit = !inCommentEdit;
                if (inCommentEdit) {
                    im.setImageResource(R.drawable.ic_check_black_24dp);
                    editComment.setSelection(editComment.getText().length());
                    editComment.requestFocus();
                } else {
                    im.setImageResource(R.drawable.ic_edit_black_24dp);
                    textComment.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });*/

        return v;
    }

    void addChip() {
        Chip chip4 = new Chip(getContext());
        chip4.setTitle("Test chip");
        chip4.setColor(Color.MAGENTA);
        chl.addView(chip4);
    }

    public boolean onRecordEdit(){
        commentSwitcher.showNext();
        //ImageButton im = (ImageButton) view;
        inCommentEdit = !inCommentEdit;
        if (inCommentEdit) {
        //    im.setImageResource(R.drawable.ic_check_black_24dp);
            commentEdit.setSelection(commentEdit.getText().length());
            commentEdit.requestFocus();
        } else {
            //im.setImageResource(R.drawable.ic_edit_black_24dp);
            commentView.requestFocus();
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
        return inCommentEdit;
    }
}
