package com.tangbing.admin.myfirsttestproject.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tangbing.admin.myfirsttestproject.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by admin on 2019/2/26.
 */

public class CustomListViewAdapter extends ArrayAdapter<String> {


    public CustomListViewAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public CustomListViewAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
         if(convertView==null){
             convertView= LayoutInflater.from(getContext()).inflate(R.layout.item_custom_list,null);
         }
         view=convertView;
        TextView textView=view.findViewById(R.id.text);
         textView.setText(getItem(position));
        return view;
    }
}
