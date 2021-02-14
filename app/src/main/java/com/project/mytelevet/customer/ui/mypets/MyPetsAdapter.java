package com.project.mytelevet.customer.ui.mypets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.project.mytelevet.customer.model.MyPetsItem;
import java.util.ArrayList;


// Adapter is used to convert an ArrayList of objects into View items loaded into the ListView container. (linked together with ListView)
// Reference : https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView

public class MyPetsAdapter extends ArrayAdapter<MyPetsItem> {
    public  MyPetsAdapter(Context context, ArrayList<MyPetsItem> items)
    {
        super(context, 0, items);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        MyPetsItem item = getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(this.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(android.R.id.text1);
        tvName.setText(item.getName());

        return convertView;
    }

    public String getItemName(int position)
    {
        MyPetsItem item = getItem(position);
        String name = item.getName();

        return name;
    }
}
