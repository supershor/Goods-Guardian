package com.om_tat_sat.goodsguardian;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.om_tat_sat.goodsguardian.RecyclerAdapters.Item_recycler;
import com.om_tat_sat.goodsguardian.SqlHelper.Category_MyDbHandler;
import com.om_tat_sat.goodsguardian.SqlHelper.MyDbHandler;
import com.om_tat_sat.goodsguardian.SqlParameters.Parameters;
import com.om_tat_sat.goodsguardian.model.Items_holder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.SimpleFormatter;

public class Expired_frag extends Fragment implements RecyclerviewInterface{
    MyDbHandler myDbHandler;
    RecyclerView recyclerview;
    String curr_date;
    ArrayList<Items_holder>static_arr=new ArrayList<>();
    ArrayList<Items_holder>final_arr=new ArrayList<>();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        myDbHandler=new MyDbHandler(getContext());
        recyclerview=view.findViewById(R.id.recyclerview);
        refresh();
        super.onViewCreated(view, savedInstanceState);
    }
    public void refresh(){
        static_arr.clear();
        final_arr.clear();
        curr_date= new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault()).format(Calendar.getInstance().getTime());
        List<Items_holder> list=myDbHandler.get_all_items_in_sorted_form_without_category(1);
        for (Items_holder itemsHolder:list){
            static_arr.add(itemsHolder);
        }
        for (int i = 0; i < static_arr.size(); i++) {
            if (Integer.parseInt(calculate_date_left(i).replace("d",""))==0){
                final_arr.add(static_arr.get(i));
            }
        }
        Item_recycler itemRecycler=new Item_recycler(final_arr,getContext(),this::onclick,curr_date,2);
        recyclerview.setAdapter(itemRecycler);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_category_frag, container, false);
        RecyclerView recyclerView1=view.findViewById(R.id.recyclerview);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
    public String calculate_date_left(int position){
        String exp_date[]=static_arr.get(position).getExpiry_date().split("_");
        String date[]=curr_date.split("-");
        Log.e( "calculate_date_left:-------------------",static_arr.get(position).getExpiry_date());
        Log.e( "calculate_date_left:-------------------",curr_date);
        if (Integer.parseInt(date[2])>Integer.parseInt(exp_date[2])){
            Log.e( "calculate_date_left: -----------","1");
            return "0d";
        } else if (Integer.parseInt(date[2])==Integer.parseInt(exp_date[2])) {
            if ((Integer.parseInt(date[1]))>(Integer.parseInt(exp_date[1])+1)){
                Log.e( "calculate_date_left: -----------",(Integer.parseInt(date[1]))+""+(Integer.parseInt(exp_date[1])+1));
                Log.e( "calculate_date_left: -----------","2");
                return "0d";
            } else if ((Integer.parseInt(date[1]))==(Integer.parseInt(exp_date[1])+1)) {
                if (Integer.parseInt(date[0])>=Integer.parseInt(exp_date[0])){
                    Log.e( "calculate_date_left: -----------","3");
                    return "0d";
                }else {
                    Log.e( "calculate_date_left: -----------","4->"+(Integer.parseInt(exp_date[0])-Integer.parseInt(date[0]))+"d");
                    return Integer.parseInt(exp_date[0])-Integer.parseInt(date[0])+"d";
                }
            }else{
                int i=calculate(position);
                Log.e( "calculate_date_left: -----------","5->"+i);
                return i+"d";
            }
        }else{
            int i=calculate(position);
            Log.e( "calculate_date_left: -----------","6->"+i);
            return i+"d";
        }
    }
    public int calculate(int position){
        String exp[]=static_arr.get(position).getExpiry_date().split("_");
        String date[]=curr_date.split("-");
        int curr_year=Integer.parseInt(date[2]);
        int curr_month=(Integer.parseInt(date[1]));
        int curr_date=Integer.parseInt(date[0]);
        int exp_year=Integer.parseInt(exp[2]);
        int exp_month=Integer.parseInt(exp[1])+1;
        int exp_date=Integer.parseInt(exp[0]);
        int total=0;
        Log.e( "calculate: >>>>>>>>>>>>>>>>>>>>>>>","1");
        if (curr_year==exp_year){
            for (int i = curr_month; i<=exp_month; i++) {
                if (i==curr_month){
                    total+=day_in_month(i,curr_year)-curr_date;
                }else if (i==exp_month){
                    total+=exp_date;
                }
                else {
                    total+=day_in_month(i,curr_year);
                }
            }
            Log.e( "calculate: >>>>>>>>>>>>>>>>>>>>>>>","2");
            return total;
        }
        for (int i=curr_year;i<exp_year;i++){
            for (int j = curr_month; j <=12; j++) {
                if (j==curr_month){
                    total+=day_in_month(j,i)-curr_date;
                }else {
                    total+=day_in_month(j,i);
                }
            }
            curr_year++;
        }
        if (curr_year==exp_year){
            for (int i = 1; i<=exp_month; i++) {
                if (i==exp_month){
                    total+=(day_in_month(i,curr_year)-exp_date);
                }else {
                    total+=day_in_month(i,curr_year);
                }
            }
            Log.e( "calculate: >>>>>>>>>>>>>>>>>>>>>>>","4");
            return total;
        }
        Log.e( "calculate: >>>>>>>>>>>>>>>>>>>>>>>","5");
        return 0;
    }
    public int day_in_month(int month,int year){
        if (year%400==0||year%100!=0&&year%4==0&&month==2){
            return 30;
        }
        switch (month){
            case 1:return 31;
            case 2:return 29;
            case 3:return 31;
            case 4:return 30;
            case 5:return 31;
            case 6:return 30;
            case 7:return 31;
            case 8:return 31;
            case 9:return 30;
            case 10:return 31;
            case 11:return 30;
            case 12:return 31;
        }
        return 0;
    }

    @Override
    public void onclick(int position, int index) {
        if (index==2){
            AlertDialog.Builder alert =new AlertDialog.Builder(getContext());
            alert.setTitle("Delete"+final_arr.get(position).getName())
                    .setMessage("This will delete "+final_arr.get(position).getName()+" and all its items.")
                    .setCancelable(false)
                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myDbHandler.delete(Parameters.KEY_NAME+"='"+final_arr.get(position).getName()+"' AND "+Parameters.KEY_CATEGORY+"='"+final_arr.get(position).getCategory()+"' AND "+Parameters.KEY_DESCRIPTION+"='"+final_arr.get(position).getDescription()+"' AND "+Parameters.KEY_EXPIRY_DATE+"='"+final_arr.get(position).getExpiry_date()+"' AND "+Parameters.KEY_QUANTITY+"='"+final_arr.get(position).getQuantity()+"'");
                            Category_MyDbHandler categoryMyDbHandler=new Category_MyDbHandler(getContext());
                            categoryMyDbHandler.decrease(final_arr.get(position).getCategory(),Parameters.KEY_NAME+"='"+final_arr.get(position).getCategory()+"'");
                            Toast.makeText(getContext(), final_arr.get(position).getName()+" delete successful", Toast.LENGTH_SHORT).show();
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
        }
        else if (index==3) {
            Intent intent1=new Intent(getContext(), Add_new_item_page_information_gathering.class);
            intent1.putExtra("save_or_change",1);
            intent1.putExtra("name",final_arr.get(position).getName());
            intent1.putExtra("description",final_arr.get(position).getDescription());
            intent1.putExtra("quantity",final_arr.get(position).getQuantity());
            intent1.putExtra("expiry_date",final_arr.get(position).getExpiry_date());
            intent1.putExtra("img_uri",final_arr.get(position).getImage());
            intent1.putExtra("category",final_arr.get(position).getCategory());
            startActivity(intent1);
        }
        else if (index==4){
            Intent intent1=new Intent(getContext(), Big_picture_view.class);
            intent1.putExtra("img_uri",final_arr.get(position).getImage());
            startActivity(intent1);
        }
        Toast.makeText(getContext(), position+"->"+index, Toast.LENGTH_SHORT).show();
    }
}