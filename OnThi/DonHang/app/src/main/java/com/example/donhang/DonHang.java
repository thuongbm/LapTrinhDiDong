package com.example.donhang;

public class DonHang {
    private String maDonHang;
    private String tenDonHang;
    private String ngayDat;
    private float giaHang;
    private boolean kieuGiaoHang; // true: nhanh, false: thường

    // Constructor đầy đủ tham số
    public DonHang(String maDonHang, String tenDonHang, String ngayDat, float giaHang, boolean kieuGiaoHang) {
        this.maDonHang = maDonHang;
        this.tenDonHang = tenDonHang;
        this.ngayDat = ngayDat;
        this.giaHang = giaHang;
        this.kieuGiaoHang = kieuGiaoHang;
    }

    // Constructor không tham số
    public DonHang() {
    }

    // --- Getter và Setter ---

    public String getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(String maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getTenHang() {
        return tenDonHang;
    }

    public void setTenHang(String tenDonHang) {
        this.tenDonHang = tenDonHang;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    public float getGiaHang() {
        return giaHang;
    }

    public void setGiaHang(float giaHang) {
        this.giaHang = giaHang;
    }

    // ĐÃ SỬA: Tránh gọi đệ quy vô hạn gây sập ứng dụng
    public boolean isLoaiGiaoHang() {
        return kieuGiaoHang;
    }

    public void setLoaiGiaoHang(boolean kieuGiaoHang) {
        this.kieuGiaoHang = kieuGiaoHang;
    }

    // ĐÃ SỬA: Đổi tên hàm thành tinhThanhTien() để đồng bộ với DonHangAdapter đã viết ở bài trước
    public float tinhThanhTien() {
        int A = 35; // Hai số cuối mã sinh viên 201201235 của bạn
        float phiVanChuyen = 0;

        // Tính phí cơ bản dựa trên giá hàng
        if (giaHang < 1000000) {
            phiVanChuyen = (A + 1) * 2000; // (35 + 1) * 2000 = 72.000
        } else {
            phiVanChuyen = (A + 1) * 3000; // ĐÃ SỬA: (35 + 1) * 3000 = 108.000
        }

        // Nếu là giao hàng nhanh, cộng thêm phụ phí 50.000
        if (this.kieuGiaoHang) {
            phiVanChuyen += 50000;
        }

        return this.giaHang + phiVanChuyen;
    }
}