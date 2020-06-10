package com.padangsmartcity.model;

public class ModelNop {
    private String id_tagihan;
    private String nop;
    private String luas_bumi;
    private String luas_bng;
    private String njop_bumi;
    private String njop_bng;
    private String nama_wb;
    private String alamat;
    private String tahun;
    private String tanggal;
    private int jumlah;
    private String status;

    public ModelNop(String id_tagihan, String nop, String luas_bumi, String luas_bng, String njop_bumi, String njop_bng, String nama_wb, String alamat, String tahun, String tanggal, int jumlah, String status) {
        this.id_tagihan = id_tagihan;
        this.nop = nop;
        this.luas_bumi = luas_bumi;
        this.luas_bng = luas_bng;
        this.njop_bumi = njop_bumi;
        this.njop_bng = njop_bng;
        this.nama_wb = nama_wb;
        this.alamat = alamat;
        this.tahun = tahun;
        this.tanggal = tanggal;
        this.jumlah = jumlah;
        this.status = status;
    }

    public String getId_tagihan() {
        return id_tagihan;
    }

    public void setId_tagihan(String id_tagihan) {
        this.id_tagihan = id_tagihan;
    }

    public String getNop() {
        return nop;
    }

    public void setNop(String nop) {
        this.nop = nop;
    }

    public String getLuas_bumi() {
        return luas_bumi;
    }

    public void setLuas_bumi(String luas_bumi) {
        this.luas_bumi = luas_bumi;
    }

    public String getLuas_bng() {
        return luas_bng;
    }

    public void setLuas_bng(String luas_bng) {
        this.luas_bng = luas_bng;
    }

    public String getNjop_bumi() {
        return njop_bumi;
    }

    public void setNjop_bumi(String njop_bumi) {
        this.njop_bumi = njop_bumi;
    }

    public String getNjop_bng() {
        return njop_bng;
    }

    public void setNjop_bng(String njop_bng) {
        this.njop_bng = njop_bng;
    }

    public String getNama_wb() {
        return nama_wb;
    }

    public void setNama_wb(String nama_wb) {
        this.nama_wb = nama_wb;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
