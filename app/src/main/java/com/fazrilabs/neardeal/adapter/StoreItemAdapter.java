package com.fazrilabs.neardeal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fazrilabs.neardeal.R;
import com.fazrilabs.neardeal.model.Store;

import java.util.List;

/**
 * Created by blastocode on 4/5/16.
 */
public class StoreItemAdapter extends ArrayAdapter<Store>{

    public StoreItemAdapter(Context context, List<Store> storeList) {
        super(context, R.layout.item_store, storeList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = layoutInflater.inflate(R.layout.item_store, null);
        }

        Store store = getItem(position);

        TextView nameTextView = (TextView) v.findViewById(R.id.name);
        TextView addressTextView = (TextView) v.findViewById(R.id.address);

        nameTextView.setText(store.name);
        addressTextView.setText(store.address);

        return v;
    }
}
