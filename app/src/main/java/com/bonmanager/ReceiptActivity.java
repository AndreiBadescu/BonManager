package com.bonmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.bonmanager.databinding.ReceiptViewBinding;


public class ReceiptActivity extends AppCompatActivity {

    ReceiptViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ReceiptViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        if (intent != null) {
            String numeComerciant = intent.getStringExtra("Nume comerciant");
            String total = intent.getStringExtra("Total");
            String cif = intent.getStringExtra("CIF");
            String data = intent.getStringExtra("Data");
            String ora = intent.getStringExtra("Ora");
            String tva = intent.getStringExtra("TVA");
            String produse = intent.getStringExtra("Produse");
            String preturi = intent.getStringExtra("Preturi");

            binding.numeComerciantView.setText(numeComerciant);
            binding.totalView.setText(total);
            binding.cifView.setText(cif);
            binding.dataView.setText(data);
            binding.oraView.setText(ora);
            binding.tvaView.setText(tva);
            binding.produseView.setText(produse);
            binding.preturiView.setText(preturi);
        }
    }
}