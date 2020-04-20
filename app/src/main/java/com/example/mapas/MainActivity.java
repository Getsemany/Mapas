package com.example.mapas;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    EditText txtLatInicio,txtLongInicio,txtLatFinal,txtLongFinal;

    JsonObjectRequest jsonObjectRequest;
    RequestQueue request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtLatInicio= (EditText) findViewById(R.id.txtLatIni);
        txtLongInicio= (EditText) findViewById(R.id.txtLongIni);
        txtLatFinal= (EditText) findViewById(R.id.txtLatFin);
        txtLongFinal= (EditText) findViewById(R.id.txtLongFin);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilidades.coordenadas.setLatitudInicial(Double.valueOf(txtLatInicio.getText().toString()));
                Utilidades.coordenadas.setLongitudInicial(Double.valueOf(txtLongInicio.getText().toString()));
                Utilidades.coordenadas.setLatitudFinal(Double.valueOf(txtLatFinal.getText().toString()));
                Utilidades.coordenadas.setLongitudFinal(Double.valueOf(txtLongFinal.getText().toString()));


                Intent miIntent=new Intent(MainActivity.this, MapsActivity.class);
                startActivity(miIntent);

            }
        });
    }



    public void onClick(View view) {

        if (view.getId()==R.id.btnObtenerCoordenadas){
            FusedLocationProviderClient fusedLocationClient;
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                txtLatInicio.setText(String.valueOf(location.getLatitude()));
                                txtLongInicio.setText(location.getLongitude()+"");
                                // Toast.makeText(MainActivity.this, msj,
                                //       Toast.LENGTH_LONG).show();
                                //   Log.i("MiUbi", msj);
                            }else {Log.i("MiUbi", "Sin ubicaciòn ");}

                        }
                    });
            txtLatFinal.setText("20.1394133"); txtLongFinal.setText("-101.1529094");


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

