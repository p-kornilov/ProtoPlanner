package com.vividprojects.protoplanner.Interface.Fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.vividprojects.protoplanner.BindingModels.CurrencyItemBindingModel;
import com.vividprojects.protoplanner.CoreData.Currency;
import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.ViewModels.CurrencyItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.PriceFormatter;
import com.vividprojects.protoplanner.databinding.CurrencyEditFragmentBaseBinding;
import com.vividprojects.protoplanner.databinding.CurrencyEditFragmentBinding;

import javax.inject.Inject;

/**
 * Created by Smile on 19.10.2017.
 */

public class CurrencyItemFragment extends Fragment implements Injectable {
    private boolean fabVisible = true;
    private CurrencyItemViewModel model;

  //  private EditText currency_name;
  //  private EditText currency_code;
  //  private EditText currency_symbol;
    private TextInputLayout currency_symbol_layout;
    private TextView currency_symbol_helper;
    private Spinner currency_pattern;
    private CheckBox currency_rate_check;
    private EditText currency_rate;
    private TextInputLayout currency_rate_layout;
    private EditText base_rate;
    private TextInputLayout base_rate_layout;
    private TextView currency_update_date;
    private ImageButton pattern_button;

    private CurrencyEditFragmentBinding binding;


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
        //View v = (View) inflater.inflate(R.layout.currency_edit_fragment, container, false);

        binding = CurrencyEditFragmentBinding.inflate(inflater);
        View v = binding.getRoot();

/*
        PrefixedEditText baseEdit = v.findViewById(R.id.cef_base_rate);
        baseEdit.setPrefix("$");
        PrefixedEditText currentEdit = v.findViewById(R.id.cef_currency_rate);
        baseEdit.setPrefix("F");
*/
    //    currency_name = v.findViewById(R.id.cef_name);
    //    currency_code = v.findViewById(R.id.cef_code);
    //    currency_symbol = v.findViewById(R.id.cef_symbol);
        currency_symbol_layout = v.findViewById(R.id.cef_symbol_layout);
        currency_symbol_helper = v.findViewById(R.id.cef_symbol_helper);
        currency_pattern = v.findViewById(R.id.cef_pattern);
        currency_rate_check = v.findViewById(R.id.cef_manual_rate_check);
        currency_rate = v.findViewById(R.id.cef_currency_rate);
        base_rate = v.findViewById(R.id.cef_base_rate);
        currency_rate_layout = v.findViewById(R.id.cef_currency_rate_layout);
        base_rate_layout = v.findViewById(R.id.cef_base_rate_layout);
        currency_update_date = v.findViewById(R.id.cef_currency_rate_date);
        pattern_button = v.findViewById(R.id.cef_pattern_button);


        currency_pattern.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                model.setPattern(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pattern_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currency_pattern.performClick();
            }
        });

        currency_rate_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               // model.set
            }
        });

        return v;
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

                CurrencyItemBindingModel bindingModel = model.getBindingModel();

                binding.setSymbolModel(bindingModel);

                model.getCurrency().observe(this,currency->{
                    if (currency != null) {
                        if (currency.custom_name != null) {
                        //    currency_name.setText(currency.custom_name);
                            binding.setCurrencyName(currency.custom_name);
                            currency_rate_layout.setHint(currency.custom_name + " (" + currency.iso_code_str + ")");
                        }
                        else {
                            String name = getContext().getResources().getString(currency.iso_name_id);
                            //currency_name.setText(name);
                            binding.setCurrencyName(name);
                            currency_rate_layout.setHint(name + " (" + currency.iso_code_str + ")");
                        }

                        binding.setCurrencyCode(currency.iso_code_str);

                        bindingModel.setSymbol(currency.symbol);

                        currency_rate_check.setChecked(currency.auto_update);
                    }

                });

                model.getBase().observe(this,base->{
                    if (base != null) {
                        if (base.custom_name != null)
                            base_rate_layout.setHint(base.custom_name + " (" + base.iso_code_str + ")");
                        else {
                            base_rate_layout.setHint(getContext().getResources().getString(base.iso_name_id) + " (" + base.iso_code_str + ")");
                        }
                    }
                });

                model.getSymbol().observe(this,bundle->{
                    if (bundle != null) {
/*                        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, R.id.spinner_item, PriceFormatter.createListValue(bundle.first, 100.00));
                        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item_dropdown);
                        currency_pattern.setAdapter(spinnerAdapter);*/

                        bindingModel.setPattern(bundle.second);
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
        args.putInt(NavigationController.CURRENCY_ID, iso_code);
        currencyItemFragment.setArguments(args);
        return currencyItemFragment;
    }

}