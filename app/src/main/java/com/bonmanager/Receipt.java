package com.bonmanager;

import java.util.Arrays;

public class Receipt {
    private String numeComerciant;
    private String cif;
    private String data;
    private String ora;
    private String tva;
    private String total;
    private String[] produse;
    private String[] preturi;
    private int id;
    static int last_id = 0;

    @Override
    public String toString() {
        return "Receipt{" +
                "numeComerciant='" + numeComerciant + '\'' +
                ", cif='" + cif + '\'' +
                ", data='" + data + '\'' +
                ", ora='" + ora + '\'' +
                ", tva='" + tva + '\'' +
                ", total='" + total + '\'' +
                ", produse=" + Arrays.toString(produse) +
                ", preturi=" + Arrays.toString(preturi) +
                '}';
    }

    public String toString2() {
        return  "    Nume comerciant: " + numeComerciant +
                "\n    CIF:                            " + cif +
                "\n    Data:                          " + data +
                "\n    Ora:                            " + ora +
                "\n    TVA:                           " + tva +
                "\n    Total:                          " + total +
                "\n    Produse:                    " + Arrays.toString(produse) +
                "\n    Preturi:                       " + Arrays.toString(preturi);
    }

    public String getNumeComerciant() {
        return numeComerciant;
    }

    public void setNumeComerciant(String numeComerciant) {
        this.numeComerciant = numeComerciant;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getTva() {
        return tva;
    }

    public void setTva(String tva) {
        this.tva = tva;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String[] getProduse() {
        return produse;
    }

    public void setProduse(String[] produse) {
        this.produse = produse;
    }

    public String[] getPreturi() {
        return preturi;
    }

    public void setPreturi(String[] preturi) {
        this.preturi = preturi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Receipt(String numeComerciant, String cif, String data, String ora, String tva, String total, String[] produse, String[] preturi) {
        this.numeComerciant = numeComerciant;
        this.cif = cif;
        this.data = data;
        this.ora = ora;
        this.tva = tva;
        this.total = total;
        this.produse = produse;
        this.preturi = preturi;
        this.id = last_id++;
        System.out.println(last_id);
    }
}
