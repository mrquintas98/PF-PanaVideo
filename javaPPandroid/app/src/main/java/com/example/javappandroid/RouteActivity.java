package com.example.javappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.javappandroid.databinding.ActivityMapsBinding;
import com.example.javappandroid.databinding.ActivityRouteBinding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RouteActivity extends DrawerBaseActivity {

    ListView listView;

    ArrayList<String> pointArrayList;
    ArrayAdapter<String> pointArrayAdapter;
    JSONArray routes = null;
    serverRequest getServerRequest = new serverRequest();
    ActivityRouteBinding activityRouteBinding;
    public static int routeIdAll;
    public static boolean isRoute = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRouteBinding = ActivityRouteBinding.inflate(getLayoutInflater());
        allocateActivityTitle("Map");
        setContentView(activityRouteBinding.getRoot());

        listView=findViewById(R.id.list);
        List<Route> routeList = new ArrayList<>();
        pointArrayList = new ArrayList<>();
        pointArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,pointArrayList);
        listView.setAdapter(pointArrayAdapter);


        try {
            routes = getServerRequest.execute("http://10.0.2.2:3000/points/routes").get();
            Log.i("SIZE", " -> " + routes.length());

            JSONObject aux = new JSONObject(routes.get(2).toString());
            Log.i("OBJECT", aux.toString());


            for (int i = 0; i<routes.length();i++){
                String routeName = routes.getJSONObject(i).getString("name");
                int routeId = routes.getJSONObject(i).getInt("id");
                Route route = new Route (routeId,routeName);
                routeList.add(route);
                pointArrayList.add(routeName);


                    pointArrayAdapter.notifyDataSetChanged();

            }

        } catch (ExecutionException | JSONException | InterruptedException e) {
            e.printStackTrace();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 Log.i("ITEM",adapterView.getItemAtPosition(i).toString() );
                 String routeName = adapterView.getItemAtPosition(i).toString();
                 for (int j = 0; j<routeList.size(); j++){
                     if(routeName == routeList.get(j).getName()){
                         routeIdAll = routeList.get(j).getId();
                         System.out.println(routeIdAll);
                         isRoute = true;
                         Intent intent = new Intent (RouteActivity.this,MapsActivity.class);
                         startActivity(intent);
                         overridePendingTransition(0,0);
                     }
                 }
             }
         });

    }
}