package com.catv.proyectomovil.Mapa;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.catv.proyectomovil.Constantes.Cliente;
import com.catv.proyectomovil.Constantes.Ubicacion;
import com.catv.proyectomovil.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Activamos el permiso por el puerto 1000
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            // Si el servicio esta disponible llamamos al metodo  locationStart
            locationStart();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng myUbica = new LatLng(Ubicacion.latitud, Ubicacion.longitud);

        Insert();


//        Cliente cliente = new Cliente();
//        Geolocalizacion<com.catv.proyectomovil.Modelo.Cliente> geolocalizacion = new Geolocalizacion<>();
//        geolocalizacion.setLatitud_origen(Ubicacion.latitud);
//        geolocalizacion.setLongitud_origen(Ubicacion.longitud);
//        geolocalizacion.setLista_ubicaciones(cliente.getLista_clientes());
//
//
//        for (int i = 0; i < geolocalizacion.lista_ubicaciones_seleccionadas().size(); i++) {
//
//            googleMap.addMarker(new MarkerOptions()
//                    .position(geolocalizacion.lista_ubicaciones_seleccionadas().get(i)));
//
//        }

        CameraPosition cameraposition = CameraPosition.builder()
                .target(myUbica)
                .zoom(10)
                .build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraposition));
        GoogleMap mMap = googleMap;
        mMap.setOnMapClickListener(this);


    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) { // Metodo para validar el código (1000)
        if (requestCode == 1000) { // si el código del permiso es 1000
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // se activa el permiso
                locationStart(); // se llama al metodo locationStart()
                return;
            }
        }
    }

    //Evento de clic sobre el mapa cuando un cliente no se encuentre en la ubicacion actual del usuario
    //y el usuario quiera agregarlo manualmente
    @Override
    public void onMapClick(LatLng point) {

        Log.d(TAG, "onMapClick: latitud" + Ubicacion.latitudTouch + ", longitud" + Ubicacion.longitudTouch);
        Ubicacion.latitudTouch = point.latitude;
        Ubicacion.longitudTouch = point.longitude;

        Log.d(TAG, "onMapClick: " + point.latitude);

        //Estas variables son las que deben llevarse al fragment para llenar los edittext
        //y la modificacion manual de latitud longitus este completa

    }


    private void locationStart() { // Metodo para obtener el servicio de localización por GPS o Internet
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); // Activación del servicio de Localización
        LocalizacionGPS Local = new LocalizacionGPS(); // Instancia de la clase Localización
//        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER); // Activación de la API de GPS
//        if (!gpsEnabled) { // Si el GPS esta desactivado
//            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS); // Se llama una intención para activar el gps
//            startActivity(settingsIntent); // se activa la intención
//        }
        // Si los permisos ACCES_FINE_LOCATION Y ACCESS_CORE_LOCATION no están activos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Activamos el permiso con el código 1000
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local); // Obtenemos la localización por medio de la red de datos
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local); // Obtenemos la localización por medio del GPS

    }


    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
////                    direccion.setText("Mi direccion es: \n"
////                            + DirCalle.getAddressLine(0));
//
//                    String dir = "Mi direccion es: \n"
//                            + DirCalle.getAddressLine(0);
//                    System.out.println(dir);

                    Cliente.direccion = DirCalle.getAddressLine(0);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onMapLongClick(LatLng latLng) {

        Log.d(TAG, "onMapLongClick: " + latLng);

    }


    public void Insert(){

//*********************
        int codigo = 67890;
        int codigo_ubicacion /*Lo asigno con un random desde la clase ubicacion al atributo codigo_ubicacion de la clase cliente*/;
        String nombre = "vlad";
        String telefono = "55555";
/*        Las tres variables siguientes son obtenidas mediante la clase api_localizacion*/
        String direccion = "asdsad"/*Cliente.direccion*/;
        double latitud_ubicacion = 1/*Ubicacion.latitud*/;
        double longitud_ubicacion = 2/*Ubicacion.longitud*/;

        com.catv.proyectomovil.Modelo.Cliente cliente = new  com.catv.proyectomovil.Modelo.Cliente(codigo, nombre, telefono, direccion);
        cliente.setLatitud_ubicacion(latitud_ubicacion);
        cliente.setLongitud_ubicacion(longitud_ubicacion);

        //System.out.println(cliente);
        cliente.Insertar();

        try {
            cliente.acceso_masivo_insertar_modificar_eliminar();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//*****************
    }
}
