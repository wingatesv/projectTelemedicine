package com.project.mytelevet.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.mytelevet.customer.model.ViewAppointmentItem;

import java.util.ArrayList;

public class ViewAppointmentAdapter  extends ArrayAdapter<ViewAppointmentItem> {
    public ViewAppointmentAdapter(Context context, ArrayList<ViewAppointmentItem> items) {
        super(context, 0, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewAppointmentItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(android.R.id.text1);
        tvName.setText("Appointment with Dr. " + item.getName());

        return convertView;
    }

    public String getItemName(int position)
    {
        ViewAppointmentItem item = getItem(position);
        String name = item.getName();

        return name;
    }
}
