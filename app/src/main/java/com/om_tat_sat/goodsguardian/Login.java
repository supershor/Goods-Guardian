package com.om_tat_sat.goodsguardian;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity {
    EditText email;
    EditText password;
    AppCompatButton forgot_password;
    AppCompatButton login;
    AppCompatButton sign_up;
    FirebaseAuth firebaseAuth;
    String issue;
    Intent main_page;
    Intent signup_page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //setting status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(Login.this,R.color.black));

        //setting intents
        main_page=new Intent(Login.this,MainActivity.class);
        signup_page=new Intent(Login.this, Sign_Up.class);

        //checking is the user is signed in or not
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            startActivity(main_page);
            finishAffinity();
        }

        //initializing elements
        email=findViewById(R.id.email_information_login_page);
        password=findViewById(R.id.password_information_login_page);
        forgot_password=findViewById(R.id.forgot_password_login_page);
        login=findViewById(R.id.login_at_login_page);
        sign_up=findViewById(R.id.sign_up_at_login_page);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_fields()){
                    Toast.makeText(Login.this,issue, Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                startActivity(main_page);
                                finishAffinity();
                            }else {
                                Toast.makeText(Login.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(signup_page);
                finish();
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText=new EditText(Login.this);
                final AlertDialog.Builder alertDialog=new AlertDialog.Builder(Login.this);
                alertDialog.setView(editText);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Reset Password").setMessage("Enter your email to get reset link");
                alertDialog.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText() == null || editText.getText().toString().isEmpty() || !editText.getText().toString().contains("@") || editText.getText().toString().contains(" ")) {
                            Toast.makeText(Login.this, "Invalid email address.", Toast.LENGTH_SHORT).show();
                        } else {
                            firebaseAuth.sendPasswordResetEmail(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login.this, "Reset link sent.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Login.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }
    public boolean check_fields(){
        if (email.getText()==null||password.getText()==null||email.getText().toString().isEmpty()||password.getText().toString().isEmpty()){
            issue="Enter all fields";
            return true;
        }else if (!email.getText().toString().contains("@")){
            issue="Invalid email";
            return true;
        }else if (email.getText().toString().contains(" ")){
            issue="Email cant contains spaces";
            return true;
        }
        return false;
    }
}