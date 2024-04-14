package com.om_tat_sat.goodsguardian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class Loading_Page extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    AppCompatButton sign_up;
    AppCompatButton login;
    Intent sign_up_page;
    Intent main_page;
    Intent login_page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loading_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //setting status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(Loading_Page.this,R.color.black));

        //setting up intents
        sign_up_page=new Intent(Loading_Page.this, Sign_Up.class);
        login_page=new Intent(Loading_Page.this, Login.class);
        main_page=new Intent(Loading_Page.this,MainActivity.class);

        //checking is the user is signed in or not
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            startActivity(main_page);
            finishAffinity();
        }

        //setting up buttons and click listener on them to jump to required layout
        sign_up=findViewById(R.id.loading_page_sign_up);
        login=findViewById(R.id.loading_page_login);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(sign_up_page);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(login_page);
            }
        });
    }
}