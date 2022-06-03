package com.example.javappandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.javappandroid.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.collections.MarkerManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import kotlin.Unit;


public class MapsActivity extends DrawerBaseActivity implements OnMapReadyCallback {

    ActivityMapsBinding activityMapsBinding;

    serverRequest getServerRequest;
    JSONArray points = null;
    JSONArray rPoints = null;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final int LOCATION_PERMISSION_CODE = 101;
    private double GEOFENCE_RADIUS = 100.00;
    FusedLocationProviderClient mFusedLocationClient;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 5000;
    MediaPlayer mp;
    List <Point> pointList = new ArrayList<>();
    List <Point> rPointList = new ArrayList<>();
    private double markerLat;
    private double markerLon;
    private int cont = 0;
    public static boolean route = RouteActivity.isRoute;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMapsBinding = ActivityMapsBinding.inflate(getLayoutInflater());
        allocateActivityTitle("Map");
        setContentView(activityMapsBinding.getRoot());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);





        if(isLocationPermissionGranted()){

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }
        else{
            requestLocationPermission();
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
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(route){
            cont = 0;
            try {
                getServerRequest = new serverRequest();
                Log.i("request", " -> " + "REQUEST");
                rPoints =  getServerRequest.execute("http://10.0.2.2:3000/points/"+RouteActivity.routeIdAll+"/route").get();
                Log.i("SIZE", " -> " + rPoints.length());

                JSONObject aux = new JSONObject(rPoints.get(2).toString());
                Log.i("OBJECT", aux.toString());


                for (int i = 0; i<rPoints.length();i++){
                    Point point = new Point(rPoints.getJSONObject(i).getInt("id"),
                            rPoints.getJSONObject(i).getDouble("longi"),
                            rPoints.getJSONObject(i).getDouble("lati"),
                            rPoints.getJSONObject(i).getString("name"),
                            "NA",
                            "NA",
                            rPoints.getJSONObject(i).getString("pathfile"));
                    rPointList.add(point);

                    Log.i("ROTA", rPointList.get(i).getLongi()+" "+ rPointList.get(i).getLati());


                }

                nextRoutePoint();

            } catch (ExecutionException | JSONException | InterruptedException e) {
                e.printStackTrace();
            }

        }else {
            try {
                getServerRequest = new serverRequest();
                points = getServerRequest.execute("http://10.0.2.2:3000/points/points").get();
                Log.i("SIZE", " -> " + points.length());

                JSONObject aux = new JSONObject(points.get(2).toString());
                Log.i("OBJECT", aux.toString());


                for (int i = 0; i<points.length();i++){
                    Point point = new Point(points.getJSONObject(i).getInt("id"),
                            points.getJSONObject(i).getDouble("longi"),
                            points.getJSONObject(i).getDouble("lati"),
                            points.getJSONObject(i).getString("name"),
                            points.getJSONObject(i).getString("description"),
                            points.getJSONObject(i).getString("build"),
                            "NA");
                    pointList.add(point);

                    markerLon = pointList.get(i).getLongi();
                    markerLat = pointList.get(i).getLati();

                    LatLng marker = new LatLng(markerLon,markerLat);
                    mMap.addMarker(new MarkerOptions().position(marker).title(pointList.get(i).getName()));

                    Log.i("marker", pointList.get(i).getLati() + " " + pointList.get(i).getLongi() + " " + pointList.get(i).getName());


                }

            } catch (ExecutionException | JSONException | InterruptedException e) {
                e.printStackTrace();
            }

        }






        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                ==PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                        ==PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);

        }


    }

    private boolean isLocationPermissionGranted(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                ==PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                        ==PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else{
            return false;
        }
    }

    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(this,new String []{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        ActivityCompat.requestPermissions(this,new String []{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given

            mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();

                        checkForGeoFenceEntry(lat,lon,markerLon ,markerLat,GEOFENCE_RADIUS);
                    }
                    else{
                        Log.i("ERRO", "erro na obtenção da localização");
                    }
                }
            });



        }

        private void checkForGeoFenceEntry (double userLat, double userLon, double geofenceLat, double geofenceLon, double radius){
            LatLng userLocation = new LatLng(userLat,userLon);
            LatLng geofenceLocation = new LatLng(geofenceLat,geofenceLon);

            double distanceInMeters = SphericalUtil.computeDistanceBetween(userLocation, geofenceLocation);

            if (distanceInMeters < radius) {
                // User is inside the Geo-fence

                Log.i("GEOFENCE", "Utilizador dentro");

                int resId = getResources().getIdentifier(rPointList.get(cont).getPath(),"raw",getPackageName());
                System.out.println("RESID "+ resId);
                MediaPlayer mp;
                mp = MediaPlayer.create(this,resId);
                mp.start();


                if (cont<rPointList.size()){
                    cont++;
                    nextRoutePoint();
                    Log.i("PROXIMO DENTRO", "" + cont);
                }
                else{
                    route = false;
                    cont = 0;
                    mp.release();
                    handler.removeCallbacks(runnable);
                    Log.i("ROTA", "rota terminada");
                    cont = 0;


                }

            }
        }



        private void nextRoutePoint () {
         handler.removeCallbacks(runnable);
            mMap.clear();
            Log.i("SIZE", " -> " + rPointList.size());
            markerLon = rPointList.get(cont).getLongi();
            markerLat = rPointList.get(cont).getLati();
            Log.i("ROTA", rPointList.get(cont).getLongi()+" "+ rPointList.get(cont).getLati());

            LatLng marker = new LatLng(markerLon,markerLat);
            mMap.addMarker(new MarkerOptions().position(marker).title(rPointList.get(cont).getName()));

            mMap.addCircle( new CircleOptions()
                    .center(marker)
                    .radius(GEOFENCE_RADIUS)
                    .strokeColor(Color.parseColor("#990EE540"))
                    .fillColor(Color.parseColor("#9065EF85")));

            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));


                handler.postDelayed(runnable = new Runnable() {
                    public void run() {
                        handler.postDelayed(runnable, delay);
                        getLastLocation();
                        Log.i("CORRER", "correr");
                    }
                }, delay);

        }

    @Override
    protected void onResume() {


        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
    }


}

