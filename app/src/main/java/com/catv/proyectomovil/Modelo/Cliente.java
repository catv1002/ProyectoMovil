package com.catv.proyectomovil.Modelo;

import android.os.AsyncTask;


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

import com.catv.proyectomovil.Constantes.Conexion;

import static com.catv.proyectomovil.Constantes.Conexion.CLIENTE_INSERT;

/**
 * Created by jmim on 22/11/2017.
 */

public class Cliente {

    private int codigo;
    private int codigo_ubicacion = new Ubicacion().Generar_Codigo();
    private String nombre;
    private String telefono;
    private String direccion;
    private static List<Cliente> lista_clientes = new ArrayList<Cliente>();
    //parametros de ubicacion
    private double latitud_ubicacion;
    private double longitud_ubicacion;

    public Cliente() {


    }

    public Cliente(int codigo, String nombre, String telefono, String direccion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;

    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo_ubicacion() {
        return codigo_ubicacion;
    }

    public void setCodigo_ubicacion(int codigo_ubicacion) {
        this.codigo_ubicacion = codigo_ubicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitud_ubicacion() {
        return latitud_ubicacion;
    }

    public void setLatitud_ubicacion(double latitud_ubicacion) {
        this.latitud_ubicacion = latitud_ubicacion;
    }

    public double getLongitud_ubicacion() {
        return longitud_ubicacion;
    }

    public void setLongitud_ubicacion(double longitud_ubicacion) {
        this.longitud_ubicacion = longitud_ubicacion;
    }

    public static List<Cliente> getLista_clientes() {
        return lista_clientes;
    }

    public static void setLista_clientes(List<Cliente> lista_clientes) {
        Cliente.lista_clientes = lista_clientes;
    }


    public String Masivo_Insertar_Modificar_Eliminar() throws JSONException {

        List<NameValuePair> namevaluepairs;
        String request = "";
        HttpClient httpclient;
        HttpPost httppost;
        httpclient = new DefaultHttpClient();
        httppost = new HttpPost(CLIENTE_INSERT);


        namevaluepairs = new ArrayList<NameValuePair>(1);
        namevaluepairs.add(new BasicNameValuePair("json_clientes", this.Convertir_ListaProductos_JsonObject().toString()));


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

    public AsyncTask<String, String, String> acceso_masivo_insertar_modificar_eliminar() throws ExecutionException, InterruptedException {
        Masivo_Insertar_Modificar_Eliminar masivo_insertar_modificar_eliminar = new Masivo_Insertar_Modificar_Eliminar();
        return masivo_insertar_modificar_eliminar.execute();
    }


    class Masivo_Insertar_Modificar_Eliminar extends AsyncTask<String, String, String> {

        Masivo_Insertar_Modificar_Eliminar() {

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Masivo_Insertar_Modificar_Eliminar();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;


        }
    }


    private String Consultar() {

        InputStream is = null;

        try {

            URL url = new URL(Conexion.CLIENTE_SELECTALL);
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

        //lista_clientes = new ArrayList<Cliente>();

        String data = Consultar();


        if (!data.equalsIgnoreCase("") && !data.equalsIgnoreCase("0")) {

            JSONObject json;

            try {

                json = new JSONObject(data);
                JSONArray jsonArray = json.optJSONArray("clientes");

                for (int i = 0; i < jsonArray.length(); i++) {

                    Cliente cliente = new Cliente();
                    JSONObject jsonArrayChild = jsonArray.getJSONObject(i);

                    cliente.setCodigo(Integer.parseInt(jsonArrayChild.optString("codigo")));
                    cliente.setCodigo_ubicacion(Integer.parseInt(jsonArrayChild.optString("codigo_ubicacion")));
                    cliente.setNombre(jsonArrayChild.optString("nombre"));
                    cliente.setTelefono(jsonArrayChild.optString("telefono"));
                    cliente.setDireccion(jsonArrayChild.optString("direccion"));
                    cliente.setLatitud_ubicacion(Double.parseDouble(jsonArrayChild.optString("latitud_ubicacion")));
                    cliente.setLongitud_ubicacion(Double.parseDouble(jsonArrayChild.optString("longitud_ubicacion")));
                    lista_clientes.add(cliente);

                }

            } catch (JSONException e) {

                e.printStackTrace();

            }

            System.out.println("8888888888888888888" + lista_clientes);

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

        if (this.getLista_clientes().add(this)) {

            System.out.println("----------" + this.getLista_clientes().toString());

            return true;

        }

        return false;

    }


    public boolean Modificar() {


        for (int i = 0; i < this.getLista_clientes().size(); i++) {

            if (this.getLista_clientes().get(i).getCodigo() == this.codigo) {

                this.getLista_clientes().get(i).setCodigo(this.codigo);
                this.getLista_clientes().get(i).setCodigo_ubicacion(this.codigo_ubicacion);
                this.getLista_clientes().get(i).setNombre(this.nombre);
                this.getLista_clientes().get(i).setTelefono(this.telefono);
                this.getLista_clientes().get(i).setDireccion(this.direccion);


                System.out.println("----------" + this.getLista_clientes().toString());

                return true;
            }


        }

        return false;

    }


    public boolean Eliminar(int codigo) {

        for (int i = 0; i < this.getLista_clientes().size(); i++) {

            if (this.getLista_clientes().get(i).getCodigo() == codigo) {

                this.getLista_clientes().remove(this.getLista_clientes().get(i));


                System.out.println("----------" + this.getLista_clientes().toString());

                return true;

            }

        }

        return false;

    }


    public JSONObject Convertir_ListaProductos_JsonObject() throws JSONException {

        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();

        for (int i = 0; i < this.getLista_clientes().size(); i++) {
            JSONObject jgroup = new JSONObject();
            jgroup.put("codigo", this.getLista_clientes().get(i).getCodigo());
            jgroup.put("codigo_ubicacion", this.getLista_clientes().get(i).getCodigo_ubicacion());
            jgroup.put("nombre", this.getLista_clientes().get(i).getNombre());
            jgroup.put("telefono", this.getLista_clientes().get(i).getTelefono());
            jgroup.put("direccion", this.getLista_clientes().get(i).getDireccion());
            jgroup.put("latitud", this.getLista_clientes().get(i).getLatitud_ubicacion());
            jgroup.put("longitud", this.getLista_clientes().get(i).getLongitud_ubicacion());

            System.out.println("objeto" + jgroup);
            ja.put(jgroup);
            System.out.println("array_objetos" + ja);
        }

        jo.put("Clientes", ja);
        System.out.println("json" + jo);

        return jo;

    }


    @Override
    public String toString() {
        return "Cliente{" +
                "codigo=" + codigo +
                ", codigo_ubicacion=" + codigo_ubicacion +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", latitud_ubicacion=" + latitud_ubicacion +
                ", longitud_ubicacion=" + longitud_ubicacion +
                '}';
    }
}
