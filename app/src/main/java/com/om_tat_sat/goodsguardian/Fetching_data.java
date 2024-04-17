package com.om_tat_sat.goodsguardian;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.om_tat_sat.goodsguardian.SqlHelper.Category_MyDbHandler;
import com.om_tat_sat.goodsguardian.SqlHelper.MyDbHandler;
import com.om_tat_sat.goodsguardian.model.Category_holder;
import com.om_tat_sat.goodsguardian.model.Items_holder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.security.auth.login.LoginException;

public class Fetching_data extends AppCompatActivity {

    Intent intent;
    int i=0;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Category_MyDbHandler categoryMyDbHandler;
    MyDbHandler myDbHandler;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fetching_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //initializing
        intent=getIntent();
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(Fetching_data.this, Loading_Page.class));
            finishAffinity();
        }
        categoryMyDbHandler=new Category_MyDbHandler(Fetching_data.this);
        myDbHandler=new MyDbHandler(Fetching_data.this);
        firebaseDatabase=FirebaseDatabase.getInstance("https://goods-guardian-216f8-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference=firebaseDatabase.getReference().child("USER DATA").child(firebaseAuth.getCurrentUser().getUid()).child("Items");


        //setting condition to follow which code on arrival
        if (intent.getIntExtra("upload_or_download",1)==1){
            upload_data();
        }else if (intent.getIntExtra("upload_or_download",1)==2){
            download_data();
        }else if (intent.getIntExtra("upload_or_download",1)==3){
            upload_data_and_logout();
        }
    }
    public void upload_data_and_logout(){
        upload_data();
        startActivity(new Intent(Fetching_data.this, Loading_Page.class));
        finishAffinity();
    }
    public void upload_data(){
        List<Items_holder>list=myDbHandler.get_all_items_in_sorted_form_without_category(1);
        databaseReference.setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.e( "Upload started----------------------","started");
                            Toast.makeText(Fetching_data.this, "Upload started", Toast.LENGTH_SHORT).show();
                        }else {
                            Log.e( "Upload started----------------------", Objects.requireNonNull(task.getException()).toString());
                            Toast.makeText(Fetching_data.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        i=0;
        for (Items_holder itemsHolder:list){
            String name= itemsHolder.getName()+itemsHolder.getExpiry_date()+itemsHolder.getQuantity()+itemsHolder.getCategory()+itemsHolder.getDescription();
            HashMap<String,String>hashMap=new HashMap<>();
            hashMap.put("Name",itemsHolder.getName());
            hashMap.put("Quantity",itemsHolder.getQuantity()+"");
            hashMap.put("Expiry_date",itemsHolder.getExpiry_date());
            hashMap.put("Description",itemsHolder.getDescription());
            hashMap.put("Category",itemsHolder.getCategory());
            databaseReference.child(name).child("name").setValue(hashMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                i++;
                                if (i==list.size()-1){
                                    Toast.makeText(Fetching_data.this,i+" Items Uploaded successful ", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Fetching_data.this,MainActivity.class));
                                    finishAffinity();
                                }
                            }else {
                                Log.e("ERROR -------------------------------------",task.getException().toString());
                                Toast.makeText(Fetching_data.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    public void download_data(){
        Toast.makeText(this, "download", Toast.LENGTH_SHORT).show();
    }
}