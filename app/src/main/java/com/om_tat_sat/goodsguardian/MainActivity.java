package com.om_tat_sat.goodsguardian;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.om_tat_sat.goodsguardian.RecyclerAdapters.ViewPagerAdapter;

import java.net.URI;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerviewInterface{
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FirebaseAuth firebaseAuth;
    ViewPagerAdapter viewPagerAdapter;
    Toolbar toolbar;
    AppCompatButton add_items;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //setting status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.black));

        //initializing
        add_items=findViewById(R.id.add_items);
        firebaseAuth=FirebaseAuth.getInstance();

        //checking if user is signed in or nor
        if (firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(MainActivity.this, Loading_Page.class));
            finishAffinity();
        }

        //tool bar setup
        toolbar=findViewById(R.id.toolsbar);
        toolbar.setTitle("Goods Guardian");
        setSupportActionBar(toolbar);

        //on click listener
        add_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Add_new_item_page_information_gathering.class));
            }
        });

        //tool bar setup
        toolbar=findViewById(R.id.toolsbar);
        toolbar.setTitle("Goods Guardian");
        setSupportActionBar(toolbar);
        //Tab layout setup
        tabLayout=findViewById(R.id.tabLayout);
        viewPager2=findViewById(R.id.viewpager);
        viewPagerAdapter=new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(tabLayout.getTabAt(position)).select();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onclick(int position, int index) {
    }
    public void notifications(){
        Toast.makeText(this, "This service will be added soon.", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "I sincerely apologize for the inconvenience happened.", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logout){
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);
            builder.setTitle("Logout")
                    .setMessage("Are you sure you want to Logout ?\nWe recommend uploading data before Logout.")
                    .setPositiveButton("Save data", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(MainActivity.this,Fetching_data.class);
                            intent.putExtra("upload_or_download",3);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                            .setNeutralButton("Don't Save Data", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    firebaseAuth.signOut();
                                    startActivity(new Intent(MainActivity.this, Loading_Page.class));
                                    finishAffinity();
                                }
                            });
            builder.show();
        }
        else if (item.getItemId()==R.id.report_error){
            Intent intent=new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"supershor.cp@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"Report error on Goods Guardian.");
            startActivity(intent);
        }else if (item.getItemId()==R.id.contact_owner){
            Intent intent=new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(MailTo.MAILTO_SCHEME));
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"supershor.cp@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"Contact owner of Goods Guardian.");
            startActivity(intent);
        }else if(item.getItemId()==R.id.upload){
            Intent intent=new Intent(MainActivity.this,Fetching_data.class);
            intent.putExtra("upload_or_download",1);
            startActivity(intent);
        }else if(item.getItemId()==R.id.download){
            Intent intent=new Intent(MainActivity.this,Fetching_data.class);
            intent.putExtra("upload_or_download",2);
            startActivity(intent);
        }else if (item.getItemId()==R.id.notifications){
            notifications();
        }
        return super.onOptionsItemSelected(item);
    }
}