package com.example.cemilsoftware.project_artbook2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void addArt(View view){
        Intent intent = new Intent(getApplicationContext(),Add_Art_Activity.class);
        startActivity(intent);
    }
    public void listArt(View view){
        Intent intent = new Intent(getApplicationContext(),List_Art_Activity.class);
        startActivity(intent);
    }


}
