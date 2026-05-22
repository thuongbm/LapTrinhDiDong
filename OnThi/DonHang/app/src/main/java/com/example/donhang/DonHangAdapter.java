package com.example.donhang;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class DonHangAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<DonHang> listDonHang;

    public DonHangAdapter(Context context, int layout, ArrayList<DonHang> listDonHang) {
        this.context = context;
        this.layout = layout;
        this.listDonHang = listDonHang;
    }

    @Override
    public int getCount() { return listDonHang.size(); }

    @Override
    public Object getItem(int position) { return listDonHang.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
        }

        LinearLayout lnItemBounds = convertView.findViewById(R.id.lnItemBounds);
        TextView tvTenHang = convertView.findViewById(R.id.tvTenHang);
        TextView tvLoaiGiao = convertView.findViewById(R.id.tvLoaiGiao);
        TextView tvNgayDat = convertView.findViewById(R.id.tvNgayDat);
        TextView tvGiaHang = convertView.findViewById(R.id.tvGiaHang);
        TextView tvThanhTien = convertView.findViewById(R.id.tvThanhTien);

        DonHang donHang = listDonHang.get(position);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat formatter = new DecimalFormat("#,###", symbols);

        tvTenHang.setText(donHang.getTenHang());
        tvLoaiGiao.setText(donHang.isLoaiGiaoHang() ? "Nhanh" : "Thường");
        tvNgayDat.setText(donHang.getNgayDat());

        tvGiaHang.setText("Giá: " + formatter.format(donHang.getGiaHang()));
        tvThanhTien.setText("Thành tiền: " + formatter.format(donHang.tinhThanhTien()));

        // THAY ĐỔI THEO CÂU 3.1: Đổi màu nền dựa theo Loại Đơn Hàng (Giao nhanh / Giao thường)
        if (donHang.isLoaiGiaoHang()) {
            // Loại Giao nhanh: Đặt nền màu tối, chữ trắng giống mẫu Quạt và Áo thun
            lnItemBounds.setBackgroundColor(Color.parseColor("#262626"));
            tvTenHang.setTextColor(Color.WHITE);
            tvLoaiGiao.setTextColor(Color.WHITE);
            tvNgayDat.setTextColor(Color.WHITE);
            tvGiaHang.setTextColor(Color.parseColor("#B3B3B3"));
            tvThanhTien.setTextColor(Color.WHITE);
        } else {
            // Loại Giao thường: Đặt nền màu sáng, chữ đen giống mẫu Nước giặt và Bình hoa
            lnItemBounds.setBackgroundColor(Color.parseColor("#E6E6E6"));
            tvTenHang.setTextColor(Color.BLACK);
            tvLoaiGiao.setTextColor(Color.BLACK);
            tvNgayDat.setTextColor(Color.BLACK);
            tvGiaHang.setTextColor(Color.parseColor("#555555"));
            tvThanhTien.setTextColor(Color.BLACK);
        }

        return convertView;
    }
}