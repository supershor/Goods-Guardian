package com.om_tat_sat.goodsguardian;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Big_picture_view extends AppCompatActivity {

    Intent intent;
    ImageView imageView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_big_picture_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setStatusBarColor(ContextCompat.getColor(Big_picture_view.this,R.color.black));
        intent=getIntent();
        imageView=findViewById(R.id.full_page_image_viewer);
        if (intent.getByteArrayExtra("img_uri")==null||Utils.getImage(intent.getByteArrayExtra("img_uri"))==null){
            Toast.makeText(Big_picture_view.this, "Unable to set Imageview", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            imageView.setImageBitmap(Utils.getImage(intent.getByteArrayExtra("img_uri")));
        }
    }
}