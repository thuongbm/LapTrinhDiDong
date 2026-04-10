package com.example.vetaudemo;

public class VeTau {
    private int maVe;
    private String gaDen;
    private String gaDi;
    private float donGia;
    private boolean loaiVe; // true: Khứ hồi, false: Một chiều

    // Constructor không đối số
    public VeTau() {
    }

    // Constructor có đầy đủ đối số
    public VeTau(int maVe, String gaDi, String gaDen, float donGia, boolean loaiVe) {
        this.maVe = maVe;
        this.gaDi = gaDi;
        this.gaDen = gaDen;
        this.donGia = donGia;
        this.loaiVe = loaiVe;
    }

    // Getter và Setter
    public int getMaVe() { return maVe; }
    public void setMaVe(int maVe) { this.maVe = maVe; }

    public String getGaDen() { return gaDen; }
    public void setGaDen(String gaDen) { this.gaDen = gaDen; }

    public String getGaDi() { return gaDi; }
    public void setGaDi(String gaDi) { this.gaDi = gaDi; }

    public float getDonGia() { return donGia; }
    public void setDonGia(float donGia) { this.donGia = donGia; }

    public boolean isLoaiVe() { return loaiVe; }
    public void setLoaiVe(boolean loaiVe) { this.loaiVe = loaiVe; }
}
