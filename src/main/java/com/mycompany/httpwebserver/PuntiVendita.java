package com.mycompany.httpwebserver;

import java.util.ArrayList;

public class PuntiVendita {
    private int size;
    private ArrayList<PuntoVendita> lista;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<PuntoVendita> getLista() {
        return lista;
    }

    public void setLista(ArrayList<PuntoVendita> lista) {
        this.lista = lista;
    }
}
