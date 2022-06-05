package com.example.javappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class LoginActivity extends AppCompatActivity {

    EditText user,pass;
    Button btn;
    Handler handler = new Handler();
    Runnable runnable;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (EditText) findViewById(R.id.login_email);
        pass = (EditText) findViewById(R.id.login_pass);
        btn = (Button) findViewById(R.id.login_button);


    }

    public void onButtonClick (View view){
        String u = user.getText().toString();
        String p = pass.getText().toString();

        Log.i("USER",u);
        Log.i("PASS",p);

        try {
            Map<String, String> postData = new HashMap<>();
            postData.put("email", u);
            postData.put("password", p);

            Post task = new Post(postData);
            task.execute("https://prototype-p.herokuapp.com/users/login");



            TimeUnit.SECONDS.sleep(2);
            if(task.status == 200){
                changeActivity();
            }







        } catch (Exception e){
            e.printStackTrace();
            Log.i("ERRO","Utilizador não existe");
        }


    }

    public void changeActivity(){
        startActivity(new Intent(this,Dashboard.class));
        overridePendingTransition(0,0);
    }
}