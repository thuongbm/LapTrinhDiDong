package com.example.donhang2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DonHangAdapter extends BaseAdapter {
    private Context context;
    private List<DonHang> list;

    public DonHangAdapter(Context context, List<DonHang> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_don_hang, viewGroup, false);
        }

        DonHang dh = list.get(i);

        TextView txtTen = view.findViewById(R.id.txtTenHang);
        TextView txtLoai = view.findViewById(R.id.txtLoaiGiao);
        TextView txtNgay = view.findViewById(R.id.txtNgayDat);
        TextView txtGiaGoc = view.findViewById(R.id.txtGiaGoc);
        TextView txtThanhTien = view.findViewById(R.id.txtThanhTien);

        // Đổ dữ liệu vào các TextView
        txtTen.setText(dh.getTenHang());
        txtLoai.setText(dh.getFormatLoaiGiaoHang());

        // Định dạng ngày: dd/MM/yyyy
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        txtNgay.setText(sdf.format(dh.getNgayDat()));

        // Định dạng tiền tệ: 1,000,000
        txtGiaGoc.setText("Giá: " + String.format("%,.0f", dh.getGiaHang()));
        txtThanhTien.setText("Thành tiền: " + String.format("%,.0f", dh.tinhThanhTien()));

        // --- CÂU 3.1: ĐỔI MÀU NỀN KHÁC NHAU CHO MỖI LOẠI ĐƠN HÀNG ---
        if (dh.isLoaiGiaoHang()) {
            // Nếu là Giao nhanh (true): Đổi sang màu xanh đen tối (hoặc tùy bạn chọn)
            view.setBackgroundColor(Color.parseColor("#1B2A4A"));
        } else {
            // Nếu là Giao thường (false): Đổi sang màu xám đen như thiết kế mẫu ban đầu
            view.setBackgroundColor(Color.parseColor("#2C2C2C"));
        }

        return view;
    }
}