package com.example.nhahangdemo;

public class NhaHang {
    private int id;
    private String name;
    private String address;
    private double rate;

    public NhaHang(int id, String name, String address, double rate) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rate = rate;
    }

    public NhaHang(String name, String address, double rate) {
        this.name = name;
        this.address = address;
        this.rate = rate;
    }

    public int getId() { return id; }
    public String getTen() { return name; }
    public String getDiaChi() { return address; }
    public double getDanhGia() { return rate; }
}
