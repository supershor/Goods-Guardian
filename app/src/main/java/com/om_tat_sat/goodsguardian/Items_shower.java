package com.om_tat_sat.goodsguardian;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.om_tat_sat.goodsguardian.RecyclerAdapters.Item_recycler;
import com.om_tat_sat.goodsguardian.SqlHelper.Category_MyDbHandler;
import com.om_tat_sat.goodsguardian.SqlHelper.MyDbHandler;
import com.om_tat_sat.goodsguardian.SqlParameters.Parameters;
import com.om_tat_sat.goodsguardian.model.Items_holder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Items_shower extends AppCompatActivity implements RecyclerviewInterface{
    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<Items_holder> arrayList;
    Intent intent;
    MyDbHandler myDbHandler;
    Integer category_total_itmes;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_items_shower);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //receiving intents
        intent=getIntent();
        category_total_itmes=intent.getIntExtra("category_total_itmes",0);

        //setting status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(Items_shower.this,R.color.black));

        //initializing
        myDbHandler=new MyDbHandler(Items_shower.this);
        recyclerView=findViewById(R.id.recycler);
        arrayList=new ArrayList<>();
        refresh();


        //tool bar setup
        toolbar=findViewById(R.id.toolsbar);
        toolbar.setTitle(intent.getStringExtra("Category_name"));
        setSupportActionBar(toolbar);
    }
    @Override
    public void onclick(int position, int index) {
        if (index==2){
            AlertDialog.Builder alert =new AlertDialog.Builder(Items_shower.this);
            alert.setTitle("Delete"+arrayList.get(position).getName())
                    .setMessage("This will delete "+arrayList.get(position).getName()+" and all its items.")
                    .setCancelable(false)
                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myDbHandler.delete(Parameters.KEY_NAME+"='"+arrayList.get(position).getName()+"' AND "+Parameters.KEY_CATEGORY+"='"+arrayList.get(position).getCategory()+"' AND "+Parameters.KEY_DESCRIPTION+"='"+arrayList.get(position).getDescription()+"' AND "+Parameters.KEY_EXPIRY_DATE+"='"+arrayList.get(position).getExpiry_date()+"' AND "+Parameters.KEY_QUANTITY+"='"+arrayList.get(position).getQuantity()+"'");
                            Category_MyDbHandler categoryMyDbHandler=new Category_MyDbHandler(Items_shower.this);
                            categoryMyDbHandler.decrease(intent.getStringExtra("Category_name"),category_total_itmes,Parameters.KEY_NAME+"='"+intent.getStringExtra("Category_name")+"'");
                            category_total_itmes--;
                            Toast.makeText(Items_shower.this, arrayList.get(position).getName()+" delete successful", Toast.LENGTH_SHORT).show();
                            refresh();
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alert.show();
        } else if (index==3) {
            Intent intent1=new Intent(Items_shower.this, Add_new_item_page_information_gathering.class);
            intent1.putExtra("save_or_change",1);
            intent1.putExtra("name",arrayList.get(position).getName());
            intent1.putExtra("description",arrayList.get(position).getDescription());
            intent1.putExtra("quantity",arrayList.get(position).getQuantity());
            intent1.putExtra("expiry_date",arrayList.get(position).getExpiry_date());
            intent1.putExtra("img_uri",arrayList.get(position).getImage());
            intent1.putExtra("category",arrayList.get(position).getCategory());
            startActivity(intent1);
        }else if (index==4){
            Intent intent1=new Intent(Items_shower.this, Big_picture_view.class);
            intent1.putExtra("img_uri",arrayList.get(position).getImage());
            startActivity(intent1);
        }
        Toast.makeText(Items_shower.this, position+"->"+index, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        refresh();
        super.onResume();
    }

    public void refresh(){
        arrayList.clear();
        List<Items_holder> list=myDbHandler.get_all_items_in_sorted_form_for_category(intent.getStringExtra("Category_name"),1);
        for (Items_holder itemsShower:list){
            arrayList.add(itemsShower);
        }
        Item_recycler itemRecycler=new Item_recycler(arrayList,Items_shower.this,this::onclick,(new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault())).format(Calendar.getInstance().getTime()),1);
        recyclerView.setLayoutManager(new LinearLayoutManager(Items_shower.this));
        recyclerView.setAdapter(itemRecycler);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        new MenuInflater(Items_shower.this).inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}