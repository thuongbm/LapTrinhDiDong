package com.example.nhahangdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NhaHangAdapter extends BaseAdapter {
    private Context context;
    private List<NhaHang> list;

    public NhaHangAdapter(Context context, List<NhaHang> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() { return list.size(); }

    @Override
    public Object getItem(int position) { return list.get(position); }

    @Override
    public long getItemId(int position) { return list.get(position).getId(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_nhahang, parent, false);
        }

        NhaHang nh = list.get(position);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvAddress = convertView.findViewById(R.id.tvAddress);
        TextView tvRating = convertView.findViewById(R.id.tvRating);

        tvName.setText(nh.getTen());
        tvAddress.setText(nh.getDiaChi());
        tvRating.setText(String.valueOf(nh.getDanhGia()));

        return convertView;
    }
}