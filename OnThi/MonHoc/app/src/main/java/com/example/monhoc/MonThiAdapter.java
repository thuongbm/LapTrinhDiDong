package com.example.monhoc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.monhoc.MonThi;
import com.example.monhoc.R;

import java.util.List;

public class MonThiAdapter extends BaseAdapter {
    private Context context;
    private List<MonThi> list;

    public MonThiAdapter(Context context, List<MonThi> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() { return list.size(); }
    @Override
    public Object getItem(int i) { return list.get(i); }
    @Override
    public long getItemId(int i) { return list.get(i).getMa(); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mon_thi, parent, false);
        }

        MonThi monThi = list.get(position);

        TextView tvTenMon = convertView.findViewById(R.id.tvTenMon);
        TextView tvSoTinChi = convertView.findViewById(R.id.tvSoTinChi);
        TextView tvDiemQT = convertView.findViewById(R.id.tvDiemQT);
        TextView tvDiemThi = convertView.findViewById(R.id.tvDiemThi);
        TextView tvDiemTK = convertView.findViewById(R.id.tvDiemTK);

        tvTenMon.setText(monThi.getTenMon());
        tvSoTinChi.setText(monThi.getSoTinChi() + "TC");
        tvDiemQT.setText("QT: " + monThi.getDiemQuaTrinh());
        tvDiemThi.setText("Thi: " + monThi.getDiemThi());
        tvDiemTK.setText("TK: " + monThi.getDiemTongKet());

        // Tìm đến cuối hàm getView trong MonThiAdapter.java và sửa lại như sau:
        if (monThi.getDiemTongKet() >= 4.0f) {
            // Điểm tổng kết >= 4: đặt nền màu sáng/màu bình thường
            convertView.setBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"));
        } else {
            // Điểm tổng kết < 4: hiển thị bằng 1 màu khác để cảnh báo (Ví dụ: Đỏ nhạt)
            convertView.setBackgroundColor(android.graphics.Color.parseColor("#FFCDD2"));
        }

        return convertView;
    }
}