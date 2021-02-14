package com.project.mytelevet.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.mytelevet.customer.model.FindVetItem;

import java.util.ArrayList;

public class FindVetAdapter  extends ArrayAdapter<FindVetItem> {
    public FindVetAdapter(Context context, ArrayList<FindVetItem> items) {
        super(context, 0, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        FindVetItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(android.R.id.text1);
        tvName.setText("Dr. " + item.getName());

        return convertView;
    }

    public String getItemName(int position)
    {
        FindVetItem item = getItem(position);
        String name = item.getName();

        return name;
    }
}
