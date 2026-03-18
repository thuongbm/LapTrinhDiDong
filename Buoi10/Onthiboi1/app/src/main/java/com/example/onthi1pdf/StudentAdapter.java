package com.example.onthi1pdf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class StudentAdapter extends android.widget.BaseAdapter {
    private android.content.Context context;
    private java.util.ArrayList<Student> list;

    public StudentAdapter(android.content.Context context, java.util.ArrayList<Student> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() { return list.size(); }
    @Override
    public Object getItem(int i) { return list.get(i); }
    @Override
    public long getItemId(int i) { return i; }

    @Override
    public android.view.View getView(int i, android.view.View view, android.view.ViewGroup parent) {
        if (view == null) {
            view = android.view.LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }
        Student s = list.get(i);
        android.widget.TextView t1 = view.findViewById(android.R.id.text1);
        android.widget.TextView t2 = view.findViewById(android.R.id.text2);

        t1.setText(s.getName());
        t2.setText("Total: " + s.calculateSumScore());
        return view;
    }
}