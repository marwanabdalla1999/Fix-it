package com.example.eslah;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

public class spinner_adpter extends ArrayAdapter<Object> {

    private Context context;
    int resId;
    private String[] mAnimListItems;

    public spinner_adpter(Context context, int textViewResourceId,
                          String[] strText) {
        super(context, textViewResourceId, strText);
        this.resId = textViewResourceId;
        this.context = context;
        this.mAnimListItems = strText;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(resId, null);
        }

        TextView tv =convertView.findViewById(R.id.tvItem);




        tv.setText(mAnimListItems[position]);

        return convertView;
    }
}
