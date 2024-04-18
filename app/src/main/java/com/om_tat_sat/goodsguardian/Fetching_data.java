package com.om_tat_sat.goodsguardian;

import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.om_tat_sat.goodsguardian.SqlHelper.Category_MyDbHandler;
import com.om_tat_sat.goodsguardian.SqlHelper.MyDbHandler;
import com.om_tat_sat.goodsguardian.SqlParameters.Parameters;
import com.om_tat_sat.goodsguardian.model.Category_holder;
import com.om_tat_sat.goodsguardian.model.Items_holder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.security.auth.login.LoginException;

public class Fetching_data extends AppCompatActivity {

    Intent intent;
    int total;
    Boolean ongoing=true;
    Boolean should_run=false;
    int i=0;
    int j=0;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Category_MyDbHandler categoryMyDbHandler;
    MyDbHandler myDbHandler;
    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
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
        firebaseStorage=FirebaseStorage.getInstance("gs://goods-guardian-216f8.appspot.com");
        storageReference=firebaseStorage.getReference().child("USER DATA").child(firebaseAuth.getCurrentUser().getUid()).child("Items");
        firebaseDatabase=FirebaseDatabase.getInstance("https://goods-guardian-216f8-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference=firebaseDatabase.getReference().child("USER DATA").child(firebaseAuth.getCurrentUser().getUid()).child("Items");


        //setting condition to follow which code on arrival
        if (intent.getIntExtra("upload_or_download",1)==1){
            ongoing=true;
            should_run=true;
            if (ongoing&&should_run){
                upload_data();
            }
            should_run=false;
        }else if (intent.getIntExtra("upload_or_download",1)==2){
            ongoing=false;
            if (!ongoing){
                databaseReference.child("Total Count").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue()!=null){
                            total=Integer.parseInt(snapshot.getValue()+"");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        total=0;
                        Toast.makeText(Fetching_data.this,error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            download_data();
        }else if (intent.getIntExtra("upload_or_download",1)==3){
            ongoing=true;
            should_run=true;
            if (ongoing&&should_run){
                upload_data_and_logout();
            }
            should_run=false;
        }
    }
    public void upload_data_and_logout(){
        if (ongoing&&should_run){
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
            databaseReference.child("Total Count").setValue(myDbHandler.total_items());
            i=0;
            j=0;
            if (list.isEmpty()){
                Toast.makeText(this, "Empty Nothing To Upload", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Fetching_data.this,Loading_Page.class));
                firebaseAuth.signOut();
                finishAffinity();
            }
            for (Items_holder itemsHolder:list){
                String name= itemsHolder.getName()+itemsHolder.getExpiry_date()+itemsHolder.getQuantity()+itemsHolder.getCategory()+itemsHolder.getDescription();
                HashMap<String,String>hashMap=new HashMap<>();
                hashMap.put("Name",itemsHolder.getName());
                hashMap.put("Quantity",itemsHolder.getQuantity()+"");
                hashMap.put("Expiry_date",itemsHolder.getExpiry_date());
                hashMap.put("Description",itemsHolder.getDescription());
                hashMap.put("Category",itemsHolder.getCategory());
                databaseReference.child(name).setValue(hashMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Log.e( "hit------------------- ",i+"");
                                    i++;
                                    storageReference.child(name).putBytes(itemsHolder.getImage())
                                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                    if (task.isSuccessful()){
                                                        j++;
                                                        Log.e( "Image Upload started----------------------","started");
                                                        Log.e( "Image Upload started----------------------",i+"==i");
                                                        Log.e( "Image Upload started----------------------",j+"==j");
                                                        Log.e( "Image Upload started----------------------",list.size()+"list.size()");
                                                        if (i==list.size()&&j==list.size()){
                                                            Log.e( "Image Upload started----------------------","i");
                                                            Toast.makeText(Fetching_data.this,i+" Items Uploaded successful ", Toast.LENGTH_SHORT).show();
                                                            Toast.makeText(Fetching_data.this,"Log out done", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(Fetching_data.this, Loading_Page.class));
                                                            firebaseAuth.signOut();
                                                            finishAffinity();
                                                        }
                                                    }else {
                                                        Toast.makeText(Fetching_data.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }else {
                                    Log.e("ERROR -------------------------------------",task.getException().toString());
                                    Toast.makeText(Fetching_data.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }
    public void upload_data(){
        if (ongoing&&should_run){
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
            databaseReference.child("Total Count").setValue(myDbHandler.total_items());
            i=0;
            j=0;
            if (list.isEmpty()){
                Toast.makeText(this, "Empty Nothing To Upload", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Fetching_data.this,MainActivity.class));
                finishAffinity();
            }
            for (Items_holder itemsHolder:list){
                String name= itemsHolder.getName()+itemsHolder.getExpiry_date()+itemsHolder.getQuantity()+itemsHolder.getCategory()+itemsHolder.getDescription();
                HashMap<String,String>hashMap=new HashMap<>();
                hashMap.put("Name",itemsHolder.getName());
                hashMap.put("Quantity",itemsHolder.getQuantity()+"");
                hashMap.put("Expiry_date",itemsHolder.getExpiry_date());
                hashMap.put("Description",itemsHolder.getDescription());
                hashMap.put("Category",itemsHolder.getCategory());
                databaseReference.child(name).setValue(hashMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Log.e( "hit------------------- ",i+"");
                                    i++;
                                    storageReference.child(name).putBytes(itemsHolder.getImage())
                                            .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                    if (task.isSuccessful()){
                                                        j++;
                                                        Log.e( "Image Upload started----------------------","started");
                                                        Log.e( "Image Upload started----------------------",i+"==i");
                                                        Log.e( "Image Upload started----------------------",j+"==j");
                                                        Log.e( "Image Upload started----------------------",list.size()+"list.size()");
                                                        if (i==list.size()&&j==list.size()){
                                                            Log.e( "Image Upload started----------------------","i");
                                                            Toast.makeText(Fetching_data.this,i+" Items Uploaded successful ", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(Fetching_data.this, MainActivity.class));
                                                            finishAffinity();
                                                        }
                                                    }else {
                                                        Toast.makeText(Fetching_data.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }else {
                                    Log.e("ERROR -------------------------------------",task.getException().toString());
                                    Toast.makeText(Fetching_data.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                Log.e( "end------------------- ",i+"");
                            }
                        });
            }
        }
    }
    public void download_data(){
        if (!ongoing){
            i=0;
            HashMap<String,Integer>hashMap=new HashMap<String, Integer>();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!ongoing){
                        Log.e( "onDataChange:0000000000000000000","snapshot id null");
                        Log.e( "onDataChange:0000000000000000000",snapshot.toString());
                        Log.e( "onDataChange:0000000000000000000","="+snapshot.getValue()+"-");
                        if (total==0){
                            Toast.makeText(Fetching_data.this, "Nothing To Download", Toast.LENGTH_SHORT).show();
                            Log.e("Item download null", "Data Download Complete item found null");
                            startActivity(new Intent(Fetching_data.this,MainActivity.class));
                            finishAffinity();
                        }
                        if (snapshot.getValue()==null||snapshot.getValue().equals("")|| snapshot.getValue().toString().isEmpty()){
                            Toast.makeText(Fetching_data.this, "Data Download Complete", Toast.LENGTH_SHORT).show();
                            Log.e("Item download null", "Data Download Complete item found null");
                            startActivity(new Intent(Fetching_data.this,MainActivity.class));
                            finishAffinity();
                        }else{
                            Log.e( "onDataChange:0000000000000000000",snapshot.toString());
                            for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                                if (!(dataSnapshot.getKey()+"").equals("Total Count")){
                                    storageReference.child(dataSnapshot.getKey()+"").getBytes(1024*1024*100).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            Log.e( "onSuccess: item add-------------",dataSnapshot.toString());
                                            Items_holder itemsHolder=new Items_holder(dataSnapshot.child("Name").getValue().toString(),dataSnapshot.child("Description").getValue().toString(),Integer.parseInt(dataSnapshot.child("Quantity").getValue()+""),dataSnapshot.child("Category").getValue().toString(),dataSnapshot.child("Expiry_date").getValue().toString());
                                            itemsHolder.setImage(bytes);
                                            String query="SELECT * FROM "+ Parameters.Table_Name+" WHERE "+Parameters.KEY_NAME+"='"+itemsHolder.getName()+"'"+" AND "+Parameters.KEY_CATEGORY+"='"+itemsHolder.getCategory()+"' AND "+ Parameters.KEY_QUANTITY+"='"+itemsHolder.getQuantity()+"'AND "+Parameters.KEY_EXPIRY_DATE+"='"+itemsHolder.getExpiry_date()+"'";
                                            myDbHandler.addItems_if_does_not_exists(itemsHolder,bytes,query);
                                            hashMap.put(dataSnapshot.child("Category").getValue().toString(),1+hashMap.getOrDefault(dataSnapshot.child("Category").getValue().toString(),0));
                                            i++;
                                            if (i==total){
                                                Log.e( "onDataChange: -------------------","Running done1");
                                                for (Map.Entry<String,Integer> entry:hashMap.entrySet()){
                                                    categoryMyDbHandler.addItems_if_not_exists(new Category_holder(entry.getKey(),entry.getValue()),"SELECT * FROM "+Parameters.CATEGORY_Table_Name+" WHERE "+Parameters.KEY_NAME+"='"+entry.getKey()+"'");
                                                }
                                                Log.e( "onDataChange: -------------------","Running done2");
                                                Log.e( "Download Complete----------------------","i");
                                                Toast.makeText(Fetching_data.this,i+" Items Download successful ", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Fetching_data.this, MainActivity.class));
                                                finishAffinity();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e( "Image download failed",e.toString());
                                            Toast.makeText(Fetching_data.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e( "Data download cancel ",error.toString());
                    Toast.makeText(Fetching_data.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            Toast.makeText(this, "download", Toast.LENGTH_SHORT).show();
        }
    }

}