package com.example.campus_accessability_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UpdatesListAdapter extends ArrayAdapter<Report> {

    ArrayList<Report> reportList;
    Context context;

    public UpdatesListAdapter(Context context, ArrayList<Report> items){
        super(context,R.layout.updates_list_row, items);
        reportList = items;
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.updates_list_row, null);

        TextView number = convertView.findViewById(R.id.number);
        String user = reportList.get(position).username;
        number.setText(user);

        String reportMessage = "A user reported a path between: " + reportList.get(position).getStartNodeName() + " and " + reportList.get(position).getEndNodeName();
        TextView reportName = convertView.findViewById(R.id.reportPath);
        reportName.setText(reportMessage);

        String reportDate = "Reported on " + reportList.get(position).getDate().toString() + " at " + reportList.get(position).getTimeStamp().toString();
        TextView reportDateStamp = convertView.findViewById(R.id.reportDateTime);
        reportDateStamp.setText(reportDate);
        return convertView;
    }
}
