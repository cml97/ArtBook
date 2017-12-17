package com.example.cemilsoftware.project_artbook2;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class List_Art_Activity extends AppCompatActivity {
    ListView listview;

    static ArrayList<String> artNames;
    static ArrayList<String> artDescriptions;
    static ArrayList<Bitmap> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__art_);

        getElementByID();

        artNames = new ArrayList<>();
        artDescriptions = new ArrayList<>();
        images = new ArrayList<>();

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),R.layout.listview_layout,artNames);
        listview.setAdapter(adapter);

        try{
            Add_Art_Activity.database = this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);
            Add_Art_Activity.database.execSQL("CREATE TABLE IF NOT EXISTS Arts (name VARCHAR,desc VARCHAR,image BLOB)");

            Cursor cursor = Add_Art_Activity.database.rawQuery("SELECT * FROM Arts",null);
            int nameIx = cursor.getColumnIndex("name");
            int descIx = cursor.getColumnIndex("desc");
            int imageIx = cursor.getColumnIndex("image");


            cursor.moveToFirst();

            while (cursor != null){

                artNames.add(cursor.getString(nameIx));
                artDescriptions.add(cursor.getString(descIx));

                byte[] array = cursor.getBlob(imageIx);
                Bitmap bitmap = BitmapFactory.decodeByteArray(array,0,array.length);
                images.add(bitmap);
                cursor.moveToNext();

                adapter.notifyDataSetChanged();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),ShowActivity.class);
                intent.putExtra("position",position);

                startActivity(intent);
            }
        });

    }
    private void getElementByID(){
        listview = (ListView) findViewById(R.id.listView);
    }
}
