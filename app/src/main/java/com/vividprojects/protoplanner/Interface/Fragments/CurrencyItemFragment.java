package com.vividprojects.protoplanner.Interface.Fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.Presenters.CurrencyItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.PriceFormatter;
import com.vividprojects.protoplanner.Widgets.PrefixedEditText;

import javax.inject.Inject;

/**
 * Created by Smile on 19.10.2017.
 */

public class CurrencyItemFragment extends Fragment implements Injectable {
    private boolean fabVisible = true;
    private CurrencyItemViewModel model;

    private EditText currency_name;
    private EditText currency_code;
    private EditText currency_symbol;
    private Spinner currency_pattern;
    private CheckBox currency_rate_check;
    private PrefixedEditText currency_rate;
    private PrefixedEditText base_rate;
    private TextView currency_update_date;


    @Inject
    ViewModelProvider.Factory viewModelFactory;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("Test", "onCreate - Records Fragment");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Test", "onCreateView - RootListFragment");
        View v = (View) inflater.inflate(R.layout.currency_edit_fragment, container, false);

/*
        PrefixedEditText baseEdit = v.findViewById(R.id.cef_base_rate);
        baseEdit.setPrefix("$");
        PrefixedEditText currentEdit = v.findViewById(R.id.cef_currency_rate);
        baseEdit.setPrefix("F");
*/
        currency_name = v.findViewById(R.id.cef_name);
        currency_code = v.findViewById(R.id.cef_code);
        currency_symbol = v.findViewById(R.id.cef_symbol);
        currency_pattern = v.findViewById(R.id.cef_pattern);
        currency_rate_check = v.findViewById(R.id.cef_manual_rate_check);
        currency_rate = v.findViewById(R.id.cef_currency_rate);
        base_rate = v.findViewById(R.id.cef_base_rate);
        currency_update_date = v.findViewById(R.id.cef_currency_rate_date);

        currency_symbol.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    symbolEditFinish(currency_symbol.getText().toString());
                    return true;
                }
                return false;
            }
        });

        currency_symbol.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    symbolEditFinish(currency_symbol.getText().toString());
                }
            }
        });

        currency_pattern.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                model.setPattern(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return v;
    }

    private void symbolEditFinish(String text) {
        model.setSymbol(PriceFormatter.collapseUnicodes(text));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();

        if (args != null && args.containsKey(NavigationController.CURRENCY_ID)){
            int iso_code = args.getInt(NavigationController.CURRENCY_ID);
            if (iso_code > 0) {
                model = ViewModelProviders.of(getActivity(),viewModelFactory).get(CurrencyItemViewModel.class);
                model.setIsoCode(iso_code);

                model.getCurrency().observe(this,currency->{
                    if (currency != null) {
                        if (currency.custom_name != null)
                            currency_name.setText(currency.custom_name);
                        else
                            currency_name.setText(getContext().getResources().getString(currency.iso_name_id));
                        currency_code.setText(currency.iso_code_str);

                        currency_symbol.setText(PriceFormatter.extendUnicodes(currency.symbol));

//                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, PriceFormatter.createListValue(currency.symbol,100.00));
                    }

                });

                model.getSymbol().observe(this,bundle->{
                    if (bundle != null) {
                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item, PriceFormatter.createListValue(bundle.first, 100.00));
                        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
                        currency_pattern.setAdapter(spinnerAdapter);
                        currency_pattern.setSelection(bundle.second);
                    }
                });
            }
        }




/*        if (args != null && args.containsKey("FILTER")){  // TODO Сделать восстановление состояния фильтра и ожет быть чего другого
        //    model.setFilter();
            model.setFilter(args.getStringArrayList("FILTER"));
        } else {
            model.setFilter(null);
        }*/

//        model.setFilter("");



/*
        model.getList().observe(this,list -> {
            if (list != null)
//                recycler.setAdapter(new CurrencyListAdapter(list,getActivity()));
                currencyListAdapter.setData(list);
        });
*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.item_check:
                break;
        }
        return true;
    }

    public static CurrencyItemFragment create(int iso_code) {
        CurrencyItemFragment currencyItemFragment = new CurrencyItemFragment();
        Bundle args = new Bundle();
        args.putInt(NavigationController.CURRENCY_ID,iso_code);
        currencyItemFragment.setArguments(args);
        return currencyItemFragment;
    }

}