package com.example.thi;

import java.util.Date;

public class BanBe {
    private String id;
    private String hoVaTen;
    private String lop;
    private float yeuThich;
    private Date ngaySinh;
    private float danhGia;
    private int trongSo;
    private boolean loai;


    int a = 22;
    int b = 30;
    int c = 12;
    public BanBe(){

    }
    public BanBe(String id, String hoVaTen, float yeuThich, Date ngaySinh, float danhGia, boolean loai){
        this.id = id;
        this.hoVaTen = hoVaTen;
        this.yeuThich = yeuThich;
        this.ngaySinh = ngaySinh;
        this.danhGia = danhGia;
        this.trongSo = (a % 3);
        this.loai = loai;
    }

    private String getLop(){
        return lop;
    }
    private void setLop(String lop){
        this.lop = lop;
    }

    private String getMa(){
        return id;
    }
    private void setMa(String id){
        this.id = id;
    }
    private String getHovaTen(){
        return hoVaTen;
    }

    private void setHoVaTen(String hoVaTen){
        this.hoVaTen = hoVaTen;
    }

    private float getYeuThich(){
        return yeuThich;
    }

    private void setYeuThich(float yeuThich){
        this.yeuThich = yeuThich;
    }

    private Date getNgaySinh(){
        return ngaySinh;
    }

    private void setNgaySinh(Date ngaySinh){
        this.ngaySinh = ngaySinh;
    }

    private void TinhDanhGia(){
        if (this.yeuThich < 5){
            this.danhGia = (this.yeuThich + this.trongSo * 2) / b;
        }

        if (this.yeuThich > 5){
            this.danhGia = (this.yeuThich + this.trongSo * 3) / b;
        }
    }
}
