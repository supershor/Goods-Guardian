package com.om_tat_sat.goodsguardian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Sign_Up extends AppCompatActivity {
    EditText email;
    EditText name;
    CheckBox checkBox;
    EditText password;
    EditText confirm_password;
    AppCompatButton save;
    AppCompatButton have_an_account;
    Intent main_page;
    Intent login;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String issue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //setting status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(Sign_Up.this,R.color.black));

        //setting intents
        main_page=new Intent(Sign_Up.this,MainActivity.class);
        login=new Intent(Sign_Up.this, Login.class);

        //checking is the user is signed in or not
        firebaseAuth= FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            startActivity(main_page);
            finishAffinity();
        }

        //initializing elements
        name=findViewById(R.id.name_information_sign_up_page);
        email=findViewById(R.id.email_information_sign_up_page);
        password=findViewById(R.id.password_information_sign_up_page);
        confirm_password=findViewById(R.id.confirm_password_information_sign_up_page);
        checkBox=findViewById(R.id.checkbox_agree_for_terms_and_condition_at_sign_up_page);

        //FireBase initializing
        firebaseDatabase=FirebaseDatabase.getInstance("https://goods-guardian-216f8-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference=firebaseDatabase.getReference("USER DATA");
        //setting on click listener
        save=findViewById(R.id.save_account_creation_information);
        have_an_account=findViewById(R.id.already_have_an_account_sign_up_page);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_fields()){
                    Toast.makeText(Sign_Up.this,issue, Toast.LENGTH_SHORT).show();
                }else{
                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("Personal Information").child("Name").setValue(name.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            startActivity(main_page);
                                            finishAffinity();
                                        }else{
                                            Toast.makeText(Sign_Up.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else {
                                Toast.makeText(Sign_Up.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        have_an_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(login);
                finish();
            }
        });
    }
    public boolean check_fields(){
        //checking all input fields for valid input
        if (name.getText()==null||confirm_password.getText()==null||email.getText()==null||password.getText()==null|| name.getText().toString().isEmpty() ||email.getText().toString().isEmpty()||password.getText().toString().isEmpty()||confirm_password.getText().toString().isEmpty()){
            issue="Enter all fields";
            return true;
        }else if(password.getText().length()<=8){
            issue="Password must be greater than 8 characters";
            return true;
        }else if(email.getText().length()<=0){
            issue="Enter valid email";
            return true;
        }else if(name.getText().length()<=1){
            issue="Name must be greater than 1 characters";
            return true;
        }else if(confirm_password.getText().length()<=8){
            issue="Password must be greater than 8 characters";
            return true;
        }else if(!checkBox.isChecked()){
            issue="Please agree to terms and conditions";
            return true;
        } else if (password.getText().toString().contains(" ")) {
            issue="Password can not contain spaces";
            return true;
        }
        else if (!password.getText().toString().equals(confirm_password.getText().toString())){
            issue="Both passwords must be same";
            return true;
        } else if (!email.getText().toString().contains("@")){
            issue="Invalid email";
            return true;
        }else if (email.getText().toString().contains(" ")){
            issue="Email cant contains spaces";
            return true;
        }
        return false;
    }
}