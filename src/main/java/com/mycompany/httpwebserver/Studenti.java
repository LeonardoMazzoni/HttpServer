package com.mycompany.httpwebserver;

import java.util.ArrayList;
import java.util.List;

public class Studenti {
    private ArrayList<Studente> listaStudenti;

    public Studenti() {
        listaStudenti = new ArrayList<Studente>();
    }

    public List<Studente> getListaStudenti() {
        return listaStudenti;
    }

    public void setListaStudenti(ArrayList<Studente> listaStudenti) {
        this.listaStudenti = listaStudenti;
    }

    @Override
    public String toString() {
        return "Studenti{" + "listaStudenti=" + listaStudenti + '}';
    }
}
