package com.om_tat_sat.goodsguardian;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.om_tat_sat.goodsguardian.RecyclerAdapters.ViewPagerAdapter;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerviewInterface{
    TabLayout tabLayout;
    ViewPager2 viewPager2;
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
        Toast.makeText(this,position+"->"+index, Toast.LENGTH_SHORT).show();
    }
}