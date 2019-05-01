package com.example.iexpens.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iexpens.R;
import com.example.iexpens.activity.Banks;
import com.example.iexpens.activity.Product;

import java.util.List;

import androidx.annotation.NonNull;

public class BankListViewAdapter extends ArrayAdapter<Banks> {

    public BankListViewAdapter(Context context, int resource, List<Banks> objects) {
        super(context, resource, objects);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (null == v) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.banklist_item, null);
        }

        Banks banks = getItem(position);
        ImageView img = v.findViewById(R.id.imageViewbanks);
        TextView txtTilte = v.findViewById(R.id.txtbankTitle);

        img.setImageResource(banks.getImageId());
        txtTilte.setText(banks.getTitle());

        return v;
    }
}
