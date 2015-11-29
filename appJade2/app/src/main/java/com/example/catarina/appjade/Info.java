package com.example.catarina.appjade;

/**
 * Created by Catarina on 29/11/2015.
 */
public class Info {
    public String bateria;
    public String localizacao;
    public String wifi;


    public Info() {
        super();
    }

    public String getBateria() {
        return bateria;
    }



    public String getLocalizacao() {
        return localizacao;
    }

    public String getWifi() {
        return wifi;
    }

    public void setBateria(String bateria) {
        this.bateria = bateria;
    }


    public void setWifi(String wifi) {
        this.wifi = wifi;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
}
