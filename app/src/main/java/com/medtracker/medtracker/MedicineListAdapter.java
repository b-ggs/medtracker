package com.medtracker.medtracker;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by boggs on 11/22/15.
 */
public class MedicineListAdapter extends ArrayAdapter<MedicineObject> {

    public MedicineListAdapter(Context context, ArrayList<MedicineObject> medicineObjects) {
        super(context, 0, medicineObjects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MedicineObject medicineObject = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_medicine_item, parent, false);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView dosageTextView = (TextView) convertView.findViewById(R.id.dosageTextView);
        TextView layoutTimesTextView = (TextView) convertView.findViewById(R.id.layoutTimesTextView);
        TextView layoutDatesTextView = (TextView) convertView.findViewById(R.id.layoutDaysTextView);

        nameTextView.setText(medicineObject.getName());
        dosageTextView.setText(String.format("%s %s", medicineObject.getDosageAmount(), medicineObject.getDosageUnit()));
        layoutDatesTextView.setText(MedicineHelper.formatDaysToTakeForDatabase(medicineObject));
        layoutTimesTextView.setText(MedicineHelper.formatTimesForAdapter(medicineObject));

        return convertView;
    }
}
