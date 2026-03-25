package com.example.songapplication;

public class Song {
    // Các thuộc tính của bài hát
    private int maBaiHat;
    private String ten;
    private float diemDanhGia;
    private String tenCaSi;

    // Constructor (Hàm khởi tạo) đầy đủ các tham số
    public Song(int maBaiHat, String ten, float diemDanhGia, String tenCaSi) {
        this.maBaiHat = maBaiHat;
        this.ten = ten;
        this.diemDanhGia = diemDanhGia;
        this.tenCaSi = tenCaSi;
    }

    // Các phương thức Getter và Setter (nên có để truy xuất dữ liệu sau này)
    public int getMaBaiHat() {
        return maBaiHat;
    }

    public void setMaBaiHat(int maBaiHat) {
        this.maBaiHat = maBaiHat;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public float getDiemDanhGia() {
        return diemDanhGia;
    }

    public void setDiemDanhGia(float diemDanhGia) {
        this.diemDanhGia = diemDanhGia;
    }

    public String getTenCaSi() {
        return tenCaSi;
    }

    public void setTenCaSi(String tenCaSi) {
        this.tenCaSi = tenCaSi;
    }
}
