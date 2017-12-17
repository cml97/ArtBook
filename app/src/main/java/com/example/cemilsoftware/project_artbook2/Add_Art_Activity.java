package com.example.cemilsoftware.project_artbook2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Add_Art_Activity extends AppCompatActivity {
    ImageView imageView;
    EditText textOfName, textOfDescription;
    Bitmap selectedImage;

    public static final int CODE_FOR_SELECT_IMAGE = 1;
    public static final int CODE_FOR_ACCESS_IMAGE = 2;
    static SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__art_);

        getElementById();


    }
    private void getElementById(){
        imageView = (ImageView) findViewById(R.id.imageView);
        textOfName = (EditText) findViewById(R.id.editTextOfName);
        textOfDescription = (EditText) findViewById(R.id.editTextOfDesc);

    }
    //save art button
    public void addArt(View view){
        String artName = textOfName.getText().toString();
        String desc = textOfDescription.getText().toString();
        byte [] array = createArrayFromBitmap(selectedImage);

        try{
            database = this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS Arts (name VARCHAR,desc VARCHAR,image BLOB)");
            //This is the piece of code that you insert to database
            String sqlString = "INSERT INTO Arts (name,desc,image) VALUES (?,?,?)";
            SQLiteStatement statement = database.compileStatement(sqlString);

            statement.bindString(1,artName);
            statement.bindString(2,desc);
            statement.bindBlob(3,array);

            statement.execute();
            Toast.makeText(getApplicationContext(),"Resim başarıyla eklendi.",Toast.LENGTH_SHORT).show();
            returnMainActivity();

        }catch (Exception e){
            e.printStackTrace();
        }



    }
    //imageView click
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void chooseImage(View view){

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},CODE_FOR_ACCESS_IMAGE);
        }
        else{
            startGalleryApp();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_FOR_ACCESS_IMAGE){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startGalleryApp();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_FOR_SELECT_IMAGE && resultCode == RESULT_OK && data!=null ){
            Uri uri = data.getData();
            try {
                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                imageView.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);

    }
    //starts the process that you take photo from gallery
    private void startGalleryApp(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CODE_FOR_SELECT_IMAGE);
    }
    //creates a byte array from a bitmap
    private byte[] createArrayFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,50,stream);
        byte[] array = stream.toByteArray();
        return array;
    }

    //returns main activity after adding image to database
    private void returnMainActivity(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }

}
