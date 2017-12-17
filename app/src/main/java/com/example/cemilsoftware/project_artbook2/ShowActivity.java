package com.example.cemilsoftware.project_artbook2;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShowActivity extends AppCompatActivity {
    TextView nameText,descText;
    ImageView imageView;
    int position;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        getElementById();

        intent = getIntent();
        position = intent.getIntExtra("position",-1);

        imageView.setImageBitmap(List_Art_Activity.images.get(position));
        nameText.setText(List_Art_Activity.artNames.get(position));
        descText.setText(List_Art_Activity.artDescriptions.get(position));

    }
    public void getElementById(){
        nameText = (TextView) findViewById(R.id.textView2);
        descText = (TextView) findViewById(R.id.textView3);
        imageView = (ImageView) findViewById(R.id.imageView2);
    }
    public void deleteArt(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Silme");
        builder.setMessage("Bu gönderiyi silmek istediğinizden emin misiniz ? ");
        builder.setCancelable(false);
        builder.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                position = intent.getIntExtra("position",-1);
                deleteFromSQL();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Başarıyla silindi",Toast.LENGTH_LONG).show();

            }
        });
        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    public void deleteFromSQL(){
        intent = getIntent();
        position = intent.getIntExtra("position",-1);
        System.out.println("Position is = " + position);
        System.out.println("Size is = " + List_Art_Activity.artNames.size());
        System.out.println(List_Art_Activity.artNames.toString());
        try{

            SQLiteDatabase database = this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS Arts (name VARCHAR,desc VARCHAR,image BLOB)");
            database.execSQL("DELETE FROM Arts WHERE name = '" + List_Art_Activity.artNames.get(position) + "'");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
