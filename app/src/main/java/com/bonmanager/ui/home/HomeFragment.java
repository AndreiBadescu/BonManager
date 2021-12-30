package com.bonmanager.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    private FragmentHomeBinding binding;
    private TextView test;
    private static final List<Receipt> receipts = MainActivity.getArrayList();
    private static Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = requireContext();

/*        test = (TextView) root.findViewById(R.id.text_home);*/
        if (!receipts.isEmpty()) {
            //test.setText(receipts.toString());
            ListAdapter listAdapter = new ListAdapter(requireContext(), receipts);
            binding.listview.setAdapter(listAdapter);
            binding.listview.setClickable(true);
            binding.listview.setOnItemClickListener((parent, view, position, id) -> {
                Receipt bon = receipts.get(position);
                Intent i = new Intent(requireContext(), ReceiptActivity.class);
                i.putExtra("Nume comerciant", bon.getNumeComerciant());
                i.putExtra("Total", bon.getTotal() + " RON");
                i.putExtra("CIF", bon.getCif());
                i.putExtra("Data", bon.getData());
                i.putExtra("Ora", bon.getOra());
                i.putExtra("TVA", bon.getTva());
                i.putExtra("Produse", Arrays.toString(bon.getProduse()));
                i.putExtra("Preturi", Arrays.toString(bon.getPreturi()));
                startActivity(i);
            });
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static void AddReceipt(Receipt bon) {
        receipts.add(bon);
        MainActivity.SaveArrayList(receipts);
    }

    public static List<Receipt> getReceipts() {
        return receipts;
    }
}