package com.bonmanager.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bonmanager.R;
import com.bonmanager.Receipt;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<Receipt> {


    public ListAdapter(Context context, List<Receipt> receipts){
        super(context, R.layout.list_item, receipts);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Receipt bon = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

/*        ImageView imageView = convertView.findViewById(R.id.profile_pic);*/
        TextView total = convertView.findViewById(R.id.total_list);
        TextView numeComerciant = convertView.findViewById(R.id.nume_comerciant_list);
        TextView date = convertView.findViewById(R.id.receipt_date_list);

/*        imageView.setImageResource(bon.getTotal());*/
        total.setText(bon.getTotal());
        numeComerciant.setText(bon.getNumeComerciant());
        date.setText(bon.getData());

        return convertView;
    }
}
