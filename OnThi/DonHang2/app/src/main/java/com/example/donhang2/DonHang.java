package com.example.donhang2;

import java.util.Date;

public class DonHang {
    private String maDonHang;
    private String tenHang;
    private Date ngayDat;
    private float giaHang;
    private boolean loaiGiaoHang; // true: Nhanh, false: Thường

    public DonHang(int index, String tenHang, Date ngayDat, float giaHang, boolean loaiGiaoHang) {
        int b = 17;
        int c = 5;
        int id = b + (index * c);

        this.maDonHang = "DH" + id;
        this.tenHang = tenHang;
        this.ngayDat = ngayDat;
        this.giaHang = giaHang;
        this.loaiGiaoHang = loaiGiaoHang;
    }

    // Hàm tính Thành tiền tự động dựa trên logic đề bài
    // Câu 1.2: Tạo method tinhThanhTien() tính thành tiền bao gồm Giá hàng + Phí vận chuyển
    public float tinhThanhTien() {
        float phiVanChuyen = 0;
        int a = 35; // Giá trị A dựa trên mã sinh viên của bạn

        // 1. Xét phí vận chuyển theo mốc Giá hàng
        if (this.giaHang < 1000000f) {
            phiVanChuyen = (a + 1) * 2000f; // Tương đương 72,000đ
        } else {
            phiVanChuyen = (a + 1) * 3000f; // Tương đương 108,000đ
        }

        // 2. Nếu là giao nhanh (true) thì cộng thêm 50.000đ
        if (this.loaiGiaoHang) {
            phiVanChuyen += 50000f;
        }

        // Thành tiền = Giá hàng + Phí vận chuyển
        return this.giaHang + phiVanChuyen;
    }

    // Hàm giảm giá 10% khi bấm OK trên Dialog
    public void giamGiaMuoiPhanTram() {
        this.giaHang = this.giaHang * 0.9f;
    }

    // --- Các hàm Getter và Setter ---
    public String getMaDonHang() { return maDonHang; }
    public String getTenHang() { return tenHang; }
    public Date getNgayDat() { return ngayDat; }
    public float getGiaHang() { return giaHang; }
    public void setGiaHang(float giaHang) { this.giaHang = giaHang; }
    public boolean isLoaiGiaoHang() { return loaiGiaoHang; }
    public String getFormatLoaiGiaoHang() { return this.loaiGiaoHang ? "Nhanh" : "Thường"; }
}