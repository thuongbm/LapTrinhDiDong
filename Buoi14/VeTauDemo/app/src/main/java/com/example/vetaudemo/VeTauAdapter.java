package com.example.vetaudemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VeTauAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<VeTau> listOriginal; // Danh sách gốc
    private List<VeTau> listFiltered; // Danh sách sau khi lọc

    public VeTauAdapter(Context context, List<VeTau> list) {
        this.context = context;
        this.listOriginal = list;
        this.listFiltered = list;
    }

    @Override
    public int getCount() {
        return listFiltered.size();
    }

    @Override
    public Object getItem(int i) {
        return listFiltered.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_vetau, viewGroup, false);
        }

        VeTau vt = listFiltered.get(i);
        TextView txtLoTrinh = view.findViewById(R.id.txtLoTrinh);
        TextView txtGia = view.findViewById(R.id.txtGia);
        TextView txtLoaiVe = view.findViewById(R.id.txtLoaiVe);

        txtLoTrinh.setText(vt.getGaDi() + " -> " + vt.getGaDen());
        txtGia.setText(String.format("%.3f", vt.getDonGia()));
        txtLoaiVe.setText(vt.isLoaiVe() ? "Khứ Hồi" : "Một Chiều");

        return view;
    }

    // Xử lý logic tìm kiếm ở đây
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString().toLowerCase();
                if (strSearch.isEmpty()) {
                    listFiltered = listOriginal;
                } else {
                    List<VeTau> list = new ArrayList<>();
                    for (VeTau vt : listOriginal) {
                        // Tìm theo tên ga đi hoặc ga đến
                        if (vt.getGaDi().toLowerCase().contains(strSearch) ||
                                vt.getGaDen().toLowerCase().contains(strSearch)) {
                            list.add(vt);
                        }
                    }
                    listFiltered = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listFiltered = (List<VeTau>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
