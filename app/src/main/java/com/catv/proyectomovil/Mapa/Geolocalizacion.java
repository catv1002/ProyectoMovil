package com.catv.proyectomovil.Mapa;

import com.catv.proyectomovil.Modelo.Cliente;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmim on 23/11/2017.
 */

public class Geolocalizacion <T extends Cliente> {


    private double latitud_origen;
    private double longitud_origen;
    private List<T> lista_ubicaciones = new ArrayList<T>();

    public Geolocalizacion(){

    }

    public Geolocalizacion(float latitud_origen, float longitud_origen, List<T> lista_ubicaciones) {
        this.latitud_origen = latitud_origen;
        this.longitud_origen = longitud_origen;
        this.lista_ubicaciones = lista_ubicaciones;
    }

    public double getLatitud_origen() {
        return latitud_origen;
    }

    public void setLatitud_origen(double latitud_origen) {
        this.latitud_origen = latitud_origen;
    }

    public double getLongitud_origen() {
        return longitud_origen;
    }

    public void setLongitud_origen(double longitud_origen) {
        this.longitud_origen = longitud_origen;
    }

    public List<T> getLista_ubicaciones() {
        return lista_ubicaciones;
    }

    public void setLista_ubicaciones(List<T> lista_ubicaciones) {
        this.lista_ubicaciones = lista_ubicaciones;
    }

    public ArrayList<LatLng> lista_ubicaciones_seleccionadas() {
        ArrayList<LatLng> lista_ubicaciones_seleccionadas = new ArrayList<LatLng>();
        double distancia = 0;
        double earthRadius = 6371000; //metros

        for (int i = 0; i < lista_ubicaciones.size(); i++) {

            double dLat = Math.toRadians(((float)latitud_origen) - ((float)lista_ubicaciones.get(i).getLatitud_ubicacion()));
            double dLng = Math.toRadians(((float)longitud_origen) - ((float)lista_ubicaciones.get(i).getLongitud_ubicacion()));
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.cos(Math.toRadians(lista_ubicaciones.get(i).getLatitud_ubicacion())) * Math.cos(Math.toRadians(latitud_origen)) *
                            Math.sin(dLng/2) * Math.sin(dLng/2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            distancia = (earthRadius * c); //metros

            //System.out.println(",,,,,,,,,,,,,,,,,,,"+(float)distancia);

            if (distancia <= 5000){

               //lista_ubicaciones_seleccionadas.add(lista_ubicaciones.get(i).getLatitud_ubicacion(), lista_ubicaciones.get(i).getLongitud_ubicacion());

                lista_ubicaciones_seleccionadas.add(new LatLng(lista_ubicaciones.get(i).getLatitud_ubicacion(), lista_ubicaciones.get(i).getLongitud_ubicacion()));

            }

        }


     return lista_ubicaciones_seleccionadas;

    }


}
