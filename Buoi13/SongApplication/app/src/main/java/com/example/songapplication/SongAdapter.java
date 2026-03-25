package com.example.songapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class SongAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Song> songList;

    public SongAdapter(Context context, int layout, List<Song> songList) {
        this.context = context;
        this.layout = layout;
        this.songList = songList;
    }

    @Override
    public int getCount() { return songList.size(); }

    @Override
    public Object getItem(int i) { return null; }

    @Override
    public long getItemId(int i) { return 0; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout, null);

        // Ánh xạ
        TextView txtTen = view.findViewById(R.id.txtTenBaiHat);
        TextView txtCaSi = view.findViewById(R.id.txtTenCaSi);
        TextView txtDiem = view.findViewById(R.id.txtDiem);

        // Gán giá trị
        Song s = songList.get(i);
        txtTen.setText(s.getTen());
        txtCaSi.setText(s.getTenCaSi());
        txtDiem.setText(String.valueOf(s.getDiemDanhGia()));

        return view;
    }
}
