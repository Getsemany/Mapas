package com.example.mapas;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //JSONObject jso;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;
    Double latInicial,longInicial,latFinal,longFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    private void webServiceObtenerRuta(String latitudInicial, String longitudInicial, String latitudFinal, String longitudFinal) {
        final List<List<HashMap<String, String>>> routes = new ArrayList<>();
        String url="https://maps.googleapis.com/maps/api/directions/json?origin="+latitudInicial+","+longitudInicial
                +"&destination="+latitudFinal+","+longitudFinal+"&key="+"AIzaSyC69im735dRrG4LtHyxduqr6bifbDSPOBY";

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    jso = new JSONObject(response);
                    trazarRuta(jso);
                    Log.i("jsonRuta: ",""+response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
    }
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
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
    GoogleMap map;
    Boolean posicionActual=true;
    JSONObject jso;
    Double longitudOri, latitudOri;

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        map.setMyLocationEnabled(true);
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {


                //20.0282742,-100.7256648

                if (posicionActual){
                    latitudOri = location.getLatitude();
                    longitudOri = location.getLongitude();
                    posicionActual=false;

                    LatLng miPosicion = new LatLng(latitudOri,longitudOri);

                    map.addMarker(new MarkerOptions().position(miPosicion).title("Aquí estoy"));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(latitudOri, longitudOri))
                            .zoom(14)
                            .bearing(30)
                            .build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                    String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+latitudOri+","+longitudOri+"&destination="+Utilidades.coordenadas.getLatitudFinal()+","+Utilidades.coordenadas.getLongitudFinal()+"&key=AIzaSyC69im735dRrG4LtHyxduqr6bifbDSPOBY";

                    RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                jso = new JSONObject(response);
                                trazarRuta(jso);
                                Log.i("jsonRuta: ",""+response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    queue.add(stringRequest);
                }

            }
        });


    }

    private void trazarRuta(JSONObject jso) {

        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try{

            jRoutes = jso.getJSONArray("routes");
            for (int i=0; i<jRoutes.length(); i++){
                jLegs = ((JSONObject)(jRoutes.get(i))).getJSONArray("legs");

                for (int j = 0; j<jLegs.length(); j++){

                    jSteps = ((JSONObject)jLegs.get(j)).getJSONArray("steps");

                    for (int k = 0; k<jSteps.length(); k++){

                        String polyline = ""+((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        Log.i("end", ""+polyline);
                        List<LatLng> list = decodePoly(polyline);
                        map.addPolyline(new PolylineOptions().addAll(list).color(Color.GRAY).width(5));

                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}