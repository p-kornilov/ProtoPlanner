package com.vividprojects.protoplanner.Interface.Fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.vividprojects.protoplanner.DI.Injectable;
import com.vividprojects.protoplanner.Interface.NavigationController;
import com.vividprojects.protoplanner.Presenters.CurrencyItemViewModel;
import com.vividprojects.protoplanner.R;
import com.vividprojects.protoplanner.Utils.PriceFormatter;
import com.vividprojects.protoplanner.Utils.TextInputError;
import com.vividprojects.protoplanner.Widgets.PrefixedEditText;

import java.util.regex.Pattern;

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
    private TextInputLayout currency_symbol_layout;
    private TextView currency_symbol_helper;
    private Spinner currency_pattern;
    private CheckBox currency_rate_check;
    private PrefixedEditText currency_rate;
    private TextInputLayout currency_rate_layout;
    private PrefixedEditText base_rate;
    private TextInputLayout base_rate_layout;
    private TextView currency_update_date;
    private ImageButton pattern_button;


    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private final TextWatcher textWatcher = new TextWatcher() {
        private String text;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            int ff = 2;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String charSequenceS = charSequence.toString();
            if (text == null)
                text = charSequenceS;
            if (!charSequenceS.equals(text)) {
                text = charSequenceS;
                symbolCheck(charSequenceS);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            int i = 1;
        }
    };


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

    private void symbolCheck(String text) {
        String symbol;
        try {
            symbol = PriceFormatter.collapseUnicodes(text);
        } catch (TextInputError error) {

            currency_symbol.removeTextChangedListener(textWatcher);

            String replacedWith = "<font color='#ff1744'>" + "\\" + error.getErrorPart() + "</font>";
            String originalString = text;
            String regexPattern = Pattern.quote(error.getErrorPart());

            String modifiedString = originalString.replaceAll(regexPattern,replacedWith);
            int cursor = currency_symbol.getSelectionStart();
            currency_symbol.setText(Html.fromHtml(modifiedString));
            currency_symbol.addTextChangedListener(textWatcher);
            currency_symbol.setSelection(cursor);
            currency_symbol_layout.setHintTextAppearance(R.style.HintError);

            currency_symbol_helper.setTextColor(ContextCompat.getColor(getContext(), R.color.textInputError));
            currency_symbol_helper.setText(R.string.currency_symbol_helper_error);
            currency_symbol.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, R.drawable.ic_error_outline_red_24dp,0);

            return;
        }

        currency_symbol_layout.setHintTextAppearance(R.style.HintNormal);
        int cursor = currency_symbol.getSelectionStart();
        currency_symbol.removeTextChangedListener(textWatcher);
        currency_symbol.setText(currency_symbol.getText().toString());
        currency_symbol.addTextChangedListener(textWatcher);
        currency_symbol.setSelection(cursor);

        currency_symbol_helper.setTextColor(ContextCompat.getColor(getContext(), R.color.helperText));
        currency_symbol_helper.setText(R.string.currency_symbol_helper);
        currency_symbol.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, 0,0);

        forceRippleAnimation(currency_pattern);

        model.setSymbol(symbol);
    }

    protected void forceRippleAnimation(View view)
    {
        Drawable background = view.getBackground();
        if(Build.VERSION.SDK_INT >= 21 && background instanceof RippleDrawable)
        {
            final RippleDrawable rippleDrawable = (RippleDrawable) background;
            rippleDrawable.setState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled});
            Handler handler = new Handler();
            handler.postDelayed(new Runnable()
            {
                @Override public void run()
                {
                    rippleDrawable.setState(new int[]{});
                }
            }, 100);
        }
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
                        if (currency.custom_name != null) {
                            currency_name.setText(currency.custom_name);
                            currency_rate_layout.setHint(currency.custom_name + " (" + currency.iso_code_str + ")");
                        }
                        else {
                            String name = getContext().getResources().getString(currency.iso_name_id);
                            currency_name.setText(name);
                            currency_rate_layout.setHint(name + " (" + currency.iso_code_str + ")");
                        }

                        currency_code.setText(currency.iso_code_str);
                        currency_symbol.setText(currency.symbol);

                        currency_symbol.removeTextChangedListener(textWatcher);
                        currency_symbol.addTextChangedListener(textWatcher);

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