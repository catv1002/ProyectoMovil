package com.catv.proyectomovil.Constantes;

/**
 * Created by CATV on 24/11/17.
 */

public class Conexion {

    public static String IP = "10.10.39.184";

    public static String CLIENTE_INSERT = "http://" + Conexion.IP + ":8080/android/proyecto_final/clientes/masive_insert_update_delete.php";
    public static String CLIENTE_SELECTALL = "http://" + Conexion.IP + ":8080/android/proyecto_final/clientes/select_all.php";

    public static String PRODUCTO_INSERT = "    http://" + Conexion.IP + ":8080/android/proyecto_final/productos/masive_insert_update_delete.php";
    public static String PRODUCTO_SELECTALL = "http://" + Conexion.IP + ":8080/android/proyecto_final/productos/select_all.php";

}
