package com.project.mytelevet.doctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.project.mytelevet.doctor.model.ViewVetAppointmentItem;

import java.util.ArrayList;

public class ViewVetAppointmentAdapter extends ArrayAdapter<ViewVetAppointmentItem> {
    public ViewVetAppointmentAdapter(Context context, ArrayList<ViewVetAppointmentItem> items) {
        super(context, 0, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewVetAppointmentItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(android.R.id.text1);
        tvName.setText("Appointment with " + item.getName());

        return convertView;
    }

    public String getItemName(int position)
    {
        ViewVetAppointmentItem item = getItem(position);
        String name = item.getName();

        return name;
    }
}