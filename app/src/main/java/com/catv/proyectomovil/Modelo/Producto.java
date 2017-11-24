package com.catv.proyectomovil.Modelo;

import android.os.AsyncTask;

import com.catv.proyectomovil.Constantes.Conexion;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by jmim on 27/10/2017.
 */

public class Producto {

    private int codigo;
    private String nombre;
    private double precio_min;
    private double precio_max;
    private int cantidad;
    private int bodega;
    public static List<Producto> lista_productos = new ArrayList<Producto>();

    public Producto() {
    }

    public Producto(int codigo, String nombre, double precio_min, double precio_max, int cantidad, int bodega) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio_min = precio_min;
        this.precio_max = precio_max;
        this.cantidad = cantidad;
        this.bodega = bodega;
    }


    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio_min() {
        return precio_min;
    }

    public void setPrecio_min(double precio_min) {
        this.precio_min = precio_min;
    }

    public double getPrecio_max() {
        return precio_max;
    }

    public void setPrecio_max(double precio_max) {
        this.precio_max = precio_max;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getBodega() {
        return bodega;
    }

    public void setBodega(int bodega) {
        this.bodega = bodega;
    }

    public static List<Producto> getLista_productos() {
        return lista_productos;
    }

    public static void setLista_productos(List<Producto> lista_productos) {
        Producto.lista_productos = lista_productos;
    }


    public String Insert_Update_Productos() throws JSONException {

        List<NameValuePair> namevaluepairs;
        String request = "";
        HttpClient httpclient;
        HttpPost httppost;
        httpclient = new DefaultHttpClient();
        httppost = new HttpPost(Conexion.PRODUCTO_INSERT);


        namevaluepairs = new ArrayList<NameValuePair>(2);
        namevaluepairs.add(new BasicNameValuePair("json_productos", this.Convertir_ListaProductos_JsonObject().toString()));
        namevaluepairs.add(new BasicNameValuePair("json_productos_eliminados", this.Convertir_ListaProductoseEliminados_JsonObject().toString()));

        try {

            httppost.setEntity(new UrlEncodedFormEntity(namevaluepairs));

            ResponseHandler<String> responsehandler = new BasicResponseHandler();
            request = httpclient.execute(httppost, responsehandler);

            System.out.println("/*-+/*-+/*-+/*-+/*-+" + request);

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return request;

    }

    public AsyncTask<String, String, String> acceso_Insert_Update_Productos() throws ExecutionException, InterruptedException {
        Insert_Update_Productos Insert_Update_Productos = new Insert_Update_Productos();
        return Insert_Update_Productos.execute();
    }


    class Insert_Update_Productos extends AsyncTask<String, String, String> {

        Insert_Update_Productos() {

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Insert_Update_Productos();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;


        }
    }


    private String Consultar() {

        InputStream is = null;

        try {

            URL url = new URL(Conexion.PRODUCTO_SELECTALL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.connect();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                is = conn.getInputStream();
                return convertStreamToString(is);

            }


        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            try {

                if (is != null) {

                    is.close();

                }

            } catch (IOException e) {

                e.printStackTrace();

            }
        }

        return null;


    }


    private String convertStreamToString(InputStream is) throws IOException {

        if (is != null) {

            StringBuilder sb = new StringBuilder();
            String line;

            try {

                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                while ((line = reader.readLine()) != null) {

                    sb.append(line).append("\n");

                }

            } finally {

                is.close();

            }

            return sb.toString();

        } else {

            return "";

        }


    }


    private boolean filtrarDatos() {

        lista_productos = new ArrayList<Producto>();

        String data = Consultar();


        if (!data.equalsIgnoreCase("") || !data.equalsIgnoreCase("0")) {

            JSONObject json;

            try {

                json = new JSONObject(data);
                JSONArray jsonArray = json.optJSONArray("productos");

                for (int i = 0; i < jsonArray.length(); i++) {

                    Producto producto = new Producto();
                    JSONObject jsonArrayChild = jsonArray.getJSONObject(i);

                    producto.setCodigo(Integer.parseInt(jsonArrayChild.optString("codigo")));
                    producto.setNombre(jsonArrayChild.optString("nombre"));
                    producto.setPrecio_min(Double.parseDouble(jsonArrayChild.optString("precio_min")));
                    producto.setPrecio_max(Double.parseDouble(jsonArrayChild.optString("precio_max")));
                    producto.setCantidad(Integer.parseInt(jsonArrayChild.optString("cantidad")));
                    producto.setBodega(Integer.parseInt(jsonArrayChild.optString("bodega")));
                    lista_productos.add(producto);


                }

            } catch (JSONException e) {

                e.printStackTrace();

            }

            return true;

        }

        return false;


    }


    public AsyncTask<String, String, String> acceso_consultar() throws ExecutionException, InterruptedException {
        Consultar consultar = new Consultar();
        return consultar.execute();
    }


    class Consultar extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {

            filtrarDatos();

            return null;


        }
    }

    public boolean Insertar() {

        if (this.getLista_productos().add(this)) {

            System.out.println("----------" + this.getLista_productos().toString());

            return true;

        }

        return false;

    }


    public boolean Modificar() {


        for (int i = 0; i < this.getLista_productos().size(); i++) {

            if (this.getLista_productos().get(i).getCodigo() == this.codigo) {

                this.getLista_productos().get(i).setCodigo(this.codigo);
                this.getLista_productos().get(i).setNombre(this.nombre);
                this.getLista_productos().get(i).setPrecio_min(this.precio_min);
                this.getLista_productos().get(i).setPrecio_max(this.precio_max);
                this.getLista_productos().get(i).setCantidad(this.cantidad);
                this.getLista_productos().get(i).setBodega(this.bodega);

                System.out.println("----------" + this.getLista_productos().toString());

                return true;
            }


        }

        return false;

    }


    public boolean Eliminar(int codigo) {

        for (int i = 0; i < this.getLista_productos().size(); i++) {

            if (this.getLista_productos().get(i).getCodigo() == codigo) {

                this.getLista_productos().remove(this.getLista_productos().get(i));


                System.out.println("----------" + this.getLista_productos().toString());

                return true;

            }

        }

        return false;

    }


    public JSONObject Convertir_ListaProductos_JsonObject() throws JSONException {

        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();

        for (int i = 0; i < this.getLista_productos().size(); i++) {
            JSONObject jgroup = new JSONObject();
            jgroup.put("codigo", this.getLista_productos().get(i).getCodigo());
            jgroup.put("nombre", this.getLista_productos().get(i).getNombre());
            jgroup.put("precio_min", this.getLista_productos().get(i).getPrecio_min());
            jgroup.put("precio_max", this.getLista_productos().get(i).getPrecio_max());
            jgroup.put("cantidad", this.getLista_productos().get(i).getCantidad());
            jgroup.put("bodega", this.getLista_productos().get(i).getBodega());

            System.out.println("objeto" + jgroup);
            ja.put(jgroup);
            System.out.println("array_objetos" + ja);
        }

        jo.put("Productos", ja);
        System.out.println("json" + jo);

        return jo;


    }


    public JSONObject Convertir_ListaProductoseEliminados_JsonObject() throws JSONException {

        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();

        for (int i = 0; i < com.catv.proyectomovil.Constantes.Producto.lista_productos_eliminados.size(); i++) {
            JSONObject jgroup = new JSONObject();

            jgroup.put("codigo", com.catv.proyectomovil.Constantes.Producto.lista_productos_eliminados.get(i));

            ja.put(jgroup);

        }

        jo.put("Productos", ja);

        System.out.println(jo);

        return jo;


    }


    @Override
    public String toString() {
        return "Producto{" +
                "codigo=" + codigo +
                ", nombre='" + nombre + '\'' +
                ", precio_min=" + precio_min +
                ", precio_max=" + precio_max +
                ", cantidad=" + cantidad +
                ", bodega=" + bodega +
                '}';
    }


}
