package com.bonmanager.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bonmanager.MainActivity;
import com.bonmanager.Receipt;
import com.bonmanager.ReceiptActivity;
import com.bonmanager.databinding.FragmentHomeBinding;

import java.util.Arrays;
import java.util.List;

/**
 * HomeFragment class
 * Used for receipts history view
 */

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private static FragmentHomeBinding binding;
    private static final List<Receipt> receipts = MainActivity.getArrayList();
    public static Receipt newReceipt;
    private static Context context;
    private static Activity activity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = requireContext();
        activity = getActivity();

        if (!receipts.isEmpty()) {
            updateListOfReceipts();
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static void updateListOfReceipts() {
        ListAdapter listAdapter = new ListAdapter(context, receipts);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener((parent, view, position, id) -> {
            Receipt bon = receipts.get(position);
            Intent i = new Intent(context, ReceiptActivity.class);
            i.putExtra("Nume comerciant", bon.getNumeComerciant());
            i.putExtra("Total", bon.getTotal());
            i.putExtra("CIF", bon.getCif());
            i.putExtra("Data", bon.getData());
            i.putExtra("Ora", bon.getOra());
            i.putExtra("TVA", bon.getTva());
            i.putExtra("Produse", Arrays.toString(bon.getProduse()));
            i.putExtra("Preturi", Arrays.toString(bon.getPreturi()));
            i.putExtra("Index", position);
            activity.startActivity(i);
        });
    }

    public static void addReceipt(Receipt bon) {
        receipts.add(bon);
        MainActivity.SaveArrayList(receipts);
    }

    public static void changeReceipt(Receipt newReceipt, final int index) {
        if (index < 0 || index >= receipts.size())
            return;
        receipts.set(index, newReceipt);
        MainActivity.SaveArrayList(receipts);
    }

    public static void deleteReceipt(final int index) {
        if (index < 0 || index >= receipts.size())
            return;
        receipts.remove(index);
        MainActivity.SaveArrayList(receipts);
    }

    public static List<Receipt> getReceipts() {
        return receipts;
    }
}