package com.example.monhoc;

public class MonThi {
    private int maMon;
    private String tenMon;
    private int soTinChi;
    private float diemQuaTrinh;
    private float diemThi;
    private int loai;

    // Biến static quản lý tăng mã tự động (Bắt đầu từ 4, bước nhảy 11)
    private static int currentId = 4;
    private static final int STEP = 11;

    // Constructor rỗng
    public MonThi() {
    }

    // Constructor 4 tham số dùng khi khởi tạo nhanh/thêm mới (Tự sinh mã và gán loại = 1)
    public MonThi(String tenMon, int soTinChi, float diemQuaTrinh, float diemThi) {
        this.maMon = currentId;
        currentId += STEP;
        this.tenMon = tenMon;
        this.soTinChi = soTinChi;
        this.diemQuaTrinh = Math.round(diemQuaTrinh * 10.0f) / 10.0f;
        this.diemThi = Math.round(diemThi * 10.0f) / 10.0f;
        this.loai = 1; // 4 mod 3 = 1
    }

    // Constructor đầy đủ 6 tham số
    public MonThi(int maMon, String tenMon, int soTinChi, float diemQuaTrinh, float diemThi, int loai) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.soTinChi = soTinChi;
        this.diemQuaTrinh = diemQuaTrinh;
        this.diemThi = diemThi;
        this.loai = loai;
    }

    // Getter và Setter
    public int getMa() { return maMon; }
    public void setMa(int maMon) { this.maMon = maMon; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public int getSoTinChi() { return soTinChi; }
    public void setSoTinChi(int soTinChi) { this.soTinChi = soTinChi; }

    public float getDiemQuaTrinh() { return diemQuaTrinh; }
    public void setDiemQuaTrinh(float diemQuaTrinh) { this.diemQuaTrinh = diemQuaTrinh; }

    public float getDiemThi() { return diemThi; }
    public void setDiemThi(float diemThi) { this.diemThi = diemThi; }

    public int getLoai() { return loai; }
    public void setLoai(int loai) { this.loai = loai; }

    // Câu 1.2: Method tính điểm tổng kết tùy theo Loại của môn thi
    public float getDiemTongKet() {
        float tk = 0.0f;
        switch (this.loai) {
            case 0:
                tk = (diemQuaTrinh * 0.3f) + (diemThi * 0.7f);
                break;
            case 1:
                tk = (diemQuaTrinh * 0.4f) + (diemThi * 0.6f);
                break;
            case 2:
                tk = (diemQuaTrinh * 0.5f) + (diemThi * 0.5f);
                break;
            default:
                tk = (diemQuaTrinh * 0.4f) + (diemThi * 0.6f);
                break;
        }
        return Math.round(tk * 10.0f) / 10.0f; // Làm tròn 1 chữ số sau dấu phẩy
    }
}