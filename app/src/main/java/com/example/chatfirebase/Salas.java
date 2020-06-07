package com.example.chatfirebase;

import java.util.Collection;

public class Salas extends User {


    private Collection Cinema;
    private Collection Novidades;
    private Collection Tecnologia;

    public Collection getCinema() {
        return Cinema;
    }

    public void setCinema(Collection cinema) {
        Cinema = cinema;
    }

    public Collection getNovidades() {
        return Novidades;
    }

    public void setNovidades(Collection novidades) {
        Novidades = novidades;
    }

    public Collection getTecnologia() {
        return Tecnologia;
    }

    public void setTecnologia(Collection tecnologia) {
        Tecnologia = tecnologia;
    }

    public Collection getEconomia() {
        return Economia;
    }

    public void setEconomia(Collection economia) {
        Economia = economia;
    }

    private Collection Economia;


}

