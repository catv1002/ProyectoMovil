package com.catv.proyectomovil.Modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jmim on 23/11/2017.
 */

public class Ubicacion {

    private int codigo;
    private String latitud;
    private String longitud;
    private static List<Ubicacion> lista_ubicaciones = new ArrayList<Ubicacion>();

    public Ubicacion() {

        lista_ubicaciones.add(new Ubicacion("3.383427914071563", "-76.52943167835474"));
        lista_ubicaciones.add(new Ubicacion("3.3954603413268027", "-76.53099440038204"));
        lista_ubicaciones.add(new Ubicacion("3.426530559373309", "-76.54773641377687"));
        lista_ubicaciones.add(new Ubicacion("3.4183088552843515", "-76.54369298368694"));
        lista_ubicaciones.add(new Ubicacion("3.47623913287551", "-76.52662172913551"));

    }

    public Ubicacion(String latitud, String longitud) {

        this.latitud = latitud;
        this.longitud = longitud;

    }


    public Ubicacion(int codigo, String latitud, String longitud) {
        this.codigo = codigo;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public static List<Ubicacion> getLista_ubicaciones() {
        return lista_ubicaciones;
    }

    public static void setLista_ubicaciones(List<Ubicacion> lista_ubicaciones) {
        Ubicacion.lista_ubicaciones = lista_ubicaciones;
    }


    public int Generar_Codigo (){

        int codigo_generado = (int) (Math.random()*100000 + 1);

        this.codigo = codigo_generado;

        return this.codigo;

    }

}
