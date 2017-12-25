package com.vividprojects.protoplanner.Interface;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
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

import com.vividprojects.protoplanner.Adapters.ShopsAdapter;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Presenters.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Widgets.Chip;
import com.vividprojects.protoplanner.Widgets.ChipsLayout;
import com.vividprojects.protoplanner.Widgets.HorizontalImages;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Smile on 31.10.2017.
 */

public class RecordItemFragment extends Fragment implements Injectable {

    public static final String RECORD_ID = "record_id";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    ChipsLayout chl;
    private Realm realm;
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

//        lv.setAdapter(new TestRecyclerAdapter(getContext()));
        RealmResults<VariantInShop> ls = realm.where(VariantInShop.class).equalTo("variant.title","Фильтр для воды").findAll();
        lv.setAdapter(new ShopsAdapter(ls));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);
        lv.setLayoutManager(layoutManager);
        lv.setNestedScrollingEnabled(false);
        lv.setFocusable(false);

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

        RecyclerView alternatives = (RecyclerView) v.findViewById(R.id.recyclerView2);
        RealmResults<Record> ar = realm.where(Record.class).findAllAsync();

        Log.d("Test", "--------------- Records count - " + ar.size());

     //   alternatives.setAdapter(new RecordListAdapter(ar,mCallback));

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setAutoMeasureEnabled(true);
        alternatives.setLayoutManager(layoutManager2);
        alternatives.setNestedScrollingEnabled(false);
        alternatives.setFocusable(false);

        commentSwitcher = (ViewSwitcher) v.findViewById(R.id.rf_comment_switcher);
        commentEdit = (EditText) v.findViewById(R.id.rf_comment_edit);
        commentView = (TextView) v.findViewById(R.id.rf_comment_text);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

       // final RecordItemViewModel model = ViewModelProviders.of(getActivity(),viewModelFactory).get(RecordItemViewModel.class);
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

    public static RecordItemFragment create(String id) {
        RecordItemFragment recordItemFragment = new RecordItemFragment();
        Bundle args = new Bundle();
        args.putString(RECORD_ID,id);
        recordItemFragment.setArguments(args);
        return recordItemFragment;
    }
}
