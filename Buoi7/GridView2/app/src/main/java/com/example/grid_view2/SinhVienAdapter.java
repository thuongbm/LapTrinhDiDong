package com.example.grid_view2;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SinhVienAdapter extends ArrayAdapter<SinhVien> {

    public SinhVienAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
    public SinhVienAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public SinhVienAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<SinhVien> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public SinhVienAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull SinhVien[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public SinhVienAdapter(@NonNull Context context, int resource, @NonNull List<SinhVien> objects) {
        super(context, resource, objects);
    }

    public SinhVienAdapter(@NonNull Context context, int resource, @NonNull SinhVien[] objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item, null);
        }
        SinhVien sv = getItem(position);
        if (sv != null) {
            //anh xa
            TextView tvHoTen = (TextView) v.findViewById(R.id.textView);
            TextView tvNamSinh = (TextView) v.findViewById(R.id.textView2);
            //gan gia tri
            tvHoTen.setText(sv.Name);
            tvNamSinh.setText(String.valueOf(sv.Age));
        }
        return v;
    }
}
