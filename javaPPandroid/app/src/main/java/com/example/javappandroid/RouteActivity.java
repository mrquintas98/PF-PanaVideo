package com.example.javappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RouteActivity extends DrawerBaseActivity {

    ListView listView;

    ArrayList<String> pointArrayList;
    ArrayAdapter<String> pointArrayAdapter;
    JSONArray routes = null;
    serverRequest getServerRequest = new serverRequest();
    ActivityRouteBinding activityRouteBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRouteBinding = ActivityRouteBinding.inflate(getLayoutInflater());
        allocateActivityTitle("Map");
        setContentView(activityRouteBinding.getRoot());

        listView=findViewById(R.id.list);

        pointArrayList = new ArrayList<>();
        pointArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,pointArrayList);
        listView.setAdapter(pointArrayAdapter);

        try {
            routes = getServerRequest.execute("http://10.0.2.2:3000/points/points").get();
            Log.i("SIZE", " -> " + routes.length());

            JSONObject aux = new JSONObject(routes.get(2).toString());
            Log.i("OBJECT", aux.toString());


            for (int i = 0; i<routes.length();i++){
                String routeName = routes.getJSONObject(i).getString("name");
                pointArrayList.add(routeName);

                    pointArrayAdapter.notifyDataSetChanged();

            }

        } catch (ExecutionException | JSONException | InterruptedException e) {
            e.printStackTrace();
        }

         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 Toast.makeText(RouteActivity.this, " " + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
             }
         });

    }
}