package com.example.mapas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Var implements Serializable {
    public  List<List<HashMap<String, String>>> routes;
    public  Punto coordenadas;

    public Var(List<List<HashMap<String, String>>> routes, Punto coordenadas) {
        this.routes = routes;
        this.coordenadas = coordenadas;
    }

    public List<List<HashMap<String, String>>> getRoutes() {
        return routes;
    }

    public void setRoutes(List<List<HashMap<String, String>>> routes) {
        this.routes = routes;
    }

    public Punto getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(Punto coordenadas) {
        this.coordenadas = coordenadas;
    }
}
