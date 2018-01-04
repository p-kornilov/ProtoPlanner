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
import com.vividprojects.protoplanner.CoreData.Label;
import com.vividprojects.protoplanner.CoreData.Record;
import com.vividprojects.protoplanner.CoreData.VariantInShop;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Presenters.RecordItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Widgets.Chip;
import com.vividprojects.protoplanner.Widgets.ChipsLayout;
import com.vividprojects.protoplanner.Widgets.HorizontalImages;

import org.w3c.dom.Text;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Smile on 31.10.2017.
 */

public class RecordItemFragment extends Fragment implements Injectable {

    public static final String RECORD_ID = "RECORD_ID";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    ChipsLayout chl;
//    private Realm realm;
    private boolean inCommentEdit = false;
    private ViewSwitcher commentSwitcher;
    private EditText commentEdit;
    private TextView commentView;
    private RecyclerView shopsRecycler;
    private HorizontalImages images;
    private RecyclerView alternativesRecycler;
    private TextView mvTitle;
    private TextView mvPrice;
    private TextView mvValue;
    private TextView mvCount;
   // private TextView mvCurrency1;
    private TextView mvCurrency2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Test", "onCreate - Record Fragment");
//        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Test", "onCreateView - Record Fragment");
        View v = (View) inflater.inflate(R.layout.record_fragment, container, false);

        shopsRecycler = v.findViewById(R.id.rf_shops_recycler);
        mvTitle = v.findViewById(R.id.alt_title);
        mvPrice = v.findViewById(R.id.alt_price);
        mvValue = v.findViewById(R.id.alt_value);
        mvCount = v.findViewById(R.id.alt_count);
       // mvCurrency1 = v.findViewById(R.id.alt_currency1);
       // mvCurrency2 = v.findViewById(R.id.alt_currency2);

    //    RealmResults<VariantInShop> ls = realm.where(VariantInShop.class).equalTo("variant.title","Фильтр для воды").findAll();
    //!!!    lv.setAdapter(new ShopsAdapter(ls));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);
        shopsRecycler.setLayoutManager(layoutManager);
        shopsRecycler.setNestedScrollingEnabled(false);
        shopsRecycler.setFocusable(false);

        chl = v.findViewById(R.id.chipLayout);

        images = (HorizontalImages) v.findViewById(R.id.rf_images);
        images.setNoneImage(true);

        alternativesRecycler = (RecyclerView) v.findViewById(R.id.rf_alternatives_recycler  );
    //    RealmResults<Record> ar = realm.where(Record.class).findAllAsync();

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        layoutManager2.setAutoMeasureEnabled(true);
        alternativesRecycler.setLayoutManager(layoutManager2);
        alternativesRecycler.setNestedScrollingEnabled(false);
        alternativesRecycler.setFocusable(false);

        commentSwitcher = (ViewSwitcher) v.findViewById(R.id.rf_comment_switcher);
        commentEdit = (EditText) v.findViewById(R.id.rf_comment_edit);
        commentView = (TextView) v.findViewById(R.id.rf_comment_text);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

       // final RecordItemViewModel model = ViewModelProviders.of(getActivity(),viewModelFactory).get(RecordItemViewModel.class);

        final RecordItemViewModel model = ViewModelProviders.of(getActivity(),viewModelFactory).get(RecordItemViewModel.class);

        Bundle args = getArguments();

        if (args != null && args.containsKey(RECORD_ID)){
            //    model.setFilter();
            model.setId(args.getString(RECORD_ID));
        } else {
            model.setId(null);
        }

        model.getRecordItem().observe(this,resource -> {
            if (resource.data != null) {
                commentView.setText(resource.data.getComment());
                chl.removeAllViews();
                chl.noneChip(getContext());
                for (Label label : resource.data.getLabels()) {
                    Chip chip = new Chip(getContext());
                    chip.setTitle(label.getName());
                    chip.setColor(label.getColor());
                    chl.addView(chip);
                }

                mvTitle.setText(resource.data.getMainVariant().getTitle());
                mvCount.setText(resource.data.getMainVariant().getFormattedCount());
                mvValue.setText(resource.data.getMainVariant().getFormattedValue());
                mvPrice.setText(resource.data.getMainVariant().getFormattedPriceFull());
                //mvCurrency1.setText(getResources().getString(R.string.RUB));
            }
        });
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
            commentEdit.setText(commentView.getText());
            commentEdit.setSelection(commentEdit.getText().length());
            commentEdit.requestFocus();
        } else {
            //im.setImageResource(R.drawable.ic_edit_black_24dp);
            commentView.setText(commentEdit.getText());
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
