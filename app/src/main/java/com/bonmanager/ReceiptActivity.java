package com.bonmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bonmanager.databinding.ReceiptViewBinding;
import com.bonmanager.ui.home.HomeFragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * ReceiptActivity class
 */
public class ReceiptActivity extends AppCompatActivity {

    ReceiptViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ReceiptViewBinding.inflate(getLayoutInflater());
        View root_view = binding.getRoot();
        setContentView(root_view);

        Intent intent = this.getIntent();
        if (intent != null) {
            final String numeComerciant = intent.getStringExtra("Nume comerciant");
            final String total = intent.getStringExtra("Total");
            final String cif = intent.getStringExtra("CIF");
            final String data = intent.getStringExtra("Data");
            final String ora = intent.getStringExtra("Ora");
            final String tva = intent.getStringExtra("TVA");
            final String produse = intent.getStringExtra("Produse");
            final String preturi = intent.getStringExtra("Preturi");
            final int index = intent.getIntExtra("Index", -1);

            EditText numeComerciantText = root_view.findViewById(R.id.nume_comerciant_view);
            EditText totalText = root_view.findViewById(R.id.total_view);
            EditText cifText = root_view.findViewById(R.id.cif_view);
            EditText dataText = root_view.findViewById(R.id.data_view);
            EditText oraText = root_view.findViewById(R.id.ora_view);
            EditText tvaText = root_view.findViewById(R.id.tva_view);
            EditText produseText = root_view.findViewById(R.id.produse_view);
            EditText preturiText = root_view.findViewById(R.id.preturi_view);
            EditText[] editTextArr = {numeComerciantText, totalText, cifText, dataText, oraText, tvaText, produseText, preturiText};

            ImageButton delete_btn = binding.receiptDeleteBtn;
            ImageButton edit_btn = binding.receiptEditBtn;
            ImageButton discard_btn = binding.receiptDiscardBtn;
            ImageButton save_btn = binding.receiptSaveBtn;

            System.out.println(numeComerciant);
            System.out.println(total);
            System.out.println(cif);
            System.out.println(data);
            System.out.println(ora);
            System.out.println(tva);
            System.out.println(produse);
            System.out.println(preturi);

            numeComerciantText.setText(numeComerciant);
            totalText.setText(total);
            cifText.setText(cif);
            dataText.setText(data);
            oraText.setText(ora);
            tvaText.setText(tva);
            produseText.setText(produse);
            preturiText.setText(preturi);

            edit_btn.setOnClickListener((view) -> {
                delete_btn.setVisibility(View.GONE);
                edit_btn.setVisibility(View.GONE);
                discard_btn.setVisibility(View.VISIBLE);
                save_btn.setVisibility(View.VISIBLE);

                makeAllFieldsEditable(editTextArr);
                totalText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                dataText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
                oraText.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);
                tvaText.setInputType(InputType.TYPE_CLASS_NUMBER);

//                totalText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable text) {
//                        text.append(" RON");
//                        totalText.setText(text);
//                    }
//                });
//
//                tvaText.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable text) {
//                        text.append("%");
//                        tvaText.setText(text);
//                    }
//                });

                discard_btn.setOnClickListener((view_btn) -> {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    delete_btn.setVisibility(View.VISIBLE);
                                    edit_btn.setVisibility(View.VISIBLE);
                                    discard_btn.setVisibility(View.GONE);
                                    save_btn.setVisibility(View.GONE);
                                    // discard changes, assign old values
                                    numeComerciantText.setText(numeComerciant);
                                    totalText.setText(total);
                                    cifText.setText(cif);
                                    dataText.setText(data);
                                    oraText.setText(ora);
                                    tvaText.setText(tva);
                                    produseText.setText(produse);
                                    preturiText.setText(preturi);
                                    makeAllFieldsNotEditable(editTextArr);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Are you sure you want to discard current changes ?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                });

                save_btn.setOnClickListener((view_btn) -> {
                    boolean ok = false;
                    for (int i = 0; i < totalText.getText().toString().length(); ++i) {
                        if (totalText.getText().toString().charAt(i) == '.') {
                            ok = true;
                            break;
                        }
                    }
                    if (ok) {
                        if (totalText.getText().length() > 0 &&
                                totalText.getText().toString().charAt(totalText.getText().toString().length() - 1) != 'N') {
                            totalText.setText(totalText.getText().toString() + " RON");
                        }
                    } else {
                        totalText.setText(totalText.getText().toString() + ".00 RON");
                    }

                    if (tvaText.getText().length() > 0 &&
                            tvaText.getText().toString().charAt(tvaText.getText().toString().length() - 1) != '%') {
                        tvaText.setText(tvaText.getText().toString() + "%");
                    }
                    dataText.setText(Receipt.standardizeDate(dataText.getText().toString()));

                    delete_btn.setVisibility(View.VISIBLE);
                    edit_btn.setVisibility(View.VISIBLE);
                    discard_btn.setVisibility(View.GONE);
                    save_btn.setVisibility(View.GONE);

                    makeAllFieldsNotEditable(editTextArr);
                    Receipt newReceipt = new Receipt(
                        numeComerciantText.getText().toString(),
                        cifText.getText().toString(),
                        dataText.getText().toString(),
                        oraText.getText().toString(),
                        tvaText.getText().toString(),
                        totalText.getText().toString(),
                        produseText.getText().toString(),
                        preturiText.getText().toString()
                    );
                    HomeFragment.changeReceipt(newReceipt, index);
                    HomeFragment.updateListOfReceipts();
                });
            });

            delete_btn.setOnClickListener((view) -> {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            HomeFragment.deleteReceipt(index);
                            HomeFragment.updateListOfReceipts();
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Are you sure you want to delete this receipt ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            });
        }
    }

    private void makeTextEditable(EditText text) {
        text.setEnabled(true);
        text.setClickable(true);
        text.setFocusable(true);
        text.setFocusableInTouchMode(true);
        text.setCursorVisible(true);
        text.setSelectAllOnFocus(true);
        text.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    private void makeTextNotEditable(EditText text) {
        text.setEnabled(false);
        text.setClickable(false);
        text.setFocusable(false);
        text.setFocusableInTouchMode(false);
        text.setCursorVisible(false);
        text.setSelectAllOnFocus(false);
        text.setInputType(InputType.TYPE_NULL);
    }

    private void makeAllFieldsEditable(EditText[] arr) {
        for (EditText obj: arr) {
            makeTextEditable(obj);
        }
    }

    private void makeAllFieldsNotEditable(EditText[] arr) {
        for (EditText obj: arr) {
            makeTextNotEditable(obj);
        }
    }
}