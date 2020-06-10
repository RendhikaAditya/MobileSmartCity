package com.padangsmartcity.model;

public class ModelPemberitahuan {
    String id_pemberitahuan, id_user, isi_pemberitahuan, tanggal;

    public String getId_pemberitahuan() {
        return id_pemberitahuan;
    }

    public void setId_pemberitahuan(String id_pemberitahuan) {
        this.id_pemberitahuan = id_pemberitahuan;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getIsi_pemberitahuan() {
        return isi_pemberitahuan;
    }

    public void setIsi_pemberitahuan(String isi_pemberitahuan) {
        this.isi_pemberitahuan = isi_pemberitahuan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
