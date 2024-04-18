package com.om_tat_sat.goodsguardian;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.om_tat_sat.goodsguardian.RecyclerAdapters.Item_recycler_for_expiring_list;
import com.om_tat_sat.goodsguardian.SqlHelper.Category_MyDbHandler;
import com.om_tat_sat.goodsguardian.SqlHelper.MyDbHandler;
import com.om_tat_sat.goodsguardian.SqlParameters.Parameters;
import com.om_tat_sat.goodsguardian.model.Items_holder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Expiring_frag extends Fragment implements RecyclerviewInterface_for_expiring{
    AppCompatButton expiring_today;
    RecyclerView expiring_today_recycler;
    AppCompatButton expiring_tomorrow;
    RecyclerView expiring_tomorrow_recycler;
    AppCompatButton expires_in_a_week_7d;
    RecyclerView expires_in_a_week_7d_recycler;
    AppCompatButton expires_in_2_week_14d;
    RecyclerView expires_in_2_week_14d_recycler;
    AppCompatButton expire_in_a_month_30d;
    RecyclerView expire_in_a_month_30d_recycler;
    AppCompatButton good_product_30d;
    RecyclerView good_product_30d_recycler;
    ArrayList<Items_holder> static_arr=new ArrayList<>();
    ArrayList<Items_holder> one_day=new ArrayList<>();
    ArrayList<Items_holder> two_day=new ArrayList<>();
    ArrayList<Items_holder> week=new ArrayList<>();
    ArrayList<Items_holder> two_week=new ArrayList<>();
    ArrayList<Items_holder> month=new ArrayList<>();
    ArrayList<Items_holder> more_then_a_month=new ArrayList<>();
    MyDbHandler myDbHandler;
    String curr_date="";
    Boolean arr_recycler[]={false,false,false,false,false,false};

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        expiring_today=view.findViewById(R.id.expiring_today);
        expiring_today_recycler=view.findViewById(R.id.expiring_today_recycler);
        expiring_tomorrow=view.findViewById(R.id.expiring_tomorrow);
        expiring_tomorrow_recycler=view.findViewById(R.id.expiring_tomorrow_recycler);
        expires_in_a_week_7d=view.findViewById(R.id.expiring_in_a_week);
        expires_in_a_week_7d_recycler=view.findViewById(R.id.expiring_in_a_week_recycler);
        expires_in_2_week_14d=view.findViewById(R.id.expiring_in_two_week);
        expires_in_2_week_14d_recycler=view.findViewById(R.id.expiring_in_two_week_recycler);
        expire_in_a_month_30d=view.findViewById(R.id.expiring_in_a_month);
        expire_in_a_month_30d_recycler=view.findViewById(R.id.expiring_in_a_month_recycler);
        good_product_30d=view.findViewById(R.id.expiring_after_a_month);
        good_product_30d_recycler=view.findViewById(R.id.expiring_after_a_month_recycler);
        myDbHandler=new MyDbHandler(getContext());
        static_arr=new ArrayList<>();
        one_day=new ArrayList<>();
        two_day=new ArrayList<>();
        week=new ArrayList<>();
        two_week=new ArrayList<>();
        month=new ArrayList<>();
        more_then_a_month=new ArrayList<>();
        //refreshing
        refresh();

        //onclick
        expiring_today.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (arr_recycler[0]){
                    expiring_today_recycler.setVisibility(View.GONE);
                }else{
                    expiring_today_recycler.setVisibility(View.VISIBLE);
                }
                arr_recycler[0]=!arr_recycler[0];
            }
        });
        expiring_tomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arr_recycler[1]){
                    expiring_tomorrow_recycler.setVisibility(View.GONE);
                }else{
                    expiring_tomorrow_recycler.setVisibility(View.VISIBLE);
                }
                arr_recycler[1]=!arr_recycler[1];
            }
        });
        expires_in_a_week_7d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arr_recycler[2]){
                    expires_in_a_week_7d_recycler.setVisibility(View.GONE);
                }else{
                    expires_in_a_week_7d_recycler.setVisibility(View.VISIBLE);
                }
                arr_recycler[2]=!arr_recycler[2];
            }
        });
        expires_in_2_week_14d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arr_recycler[3]){
                    expires_in_2_week_14d_recycler.setVisibility(View.GONE);
                }else{
                    expires_in_2_week_14d_recycler.setVisibility(View.VISIBLE);
                }
                arr_recycler[3]=!arr_recycler[3];
            }
        });
        expire_in_a_month_30d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arr_recycler[4]){
                    expire_in_a_month_30d_recycler.setVisibility(View.GONE);
                }else{
                    expire_in_a_month_30d_recycler.setVisibility(View.VISIBLE);
                }
                arr_recycler[4]=!arr_recycler[4];
            }
        });
        good_product_30d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arr_recycler[5]){
                    good_product_30d_recycler.setVisibility(View.GONE);
                }else{
                    good_product_30d_recycler.setVisibility(View.VISIBLE);
                }
                arr_recycler[5]=!arr_recycler[5];
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
    public void refresh(){
        static_arr.clear();
        one_day.clear();
        two_day.clear();
        week.clear();
        two_week.clear();
        month.clear();
        more_then_a_month.clear();
        curr_date= new SimpleDateFormat("dd-MM-YYYY",Locale.getDefault()).format(Calendar.getInstance().getTime());
        List<Items_holder>itemsHolders=myDbHandler.get_all_items_in_sorted_form_without_category(1);
        static_arr.addAll(itemsHolders);
        for (int i = 0; i < static_arr.size(); i++) {
            int total=Integer.parseInt(calculate_date_left(i).replace("d",""));
            if (total>=30){
                more_then_a_month.add(static_arr.get(i));
            }else{
                if(total>14){
                    month.add(static_arr.get(i));
                }
                if (total<=14 && total>7){
                    two_week.add(static_arr.get(i));

                }if (total<=7 && total>2){
                    week.add(static_arr.get(i));

                }if (total==2){
                    two_day.add(static_arr.get(i));

                }if (total == 1){
                    one_day.add(static_arr.get(i));
                }
            }
        }
        Item_recycler_for_expiring_list itemRecycler1=new Item_recycler_for_expiring_list(one_day,getContext(),this::onclick,curr_date,2,1);
        Item_recycler_for_expiring_list itemRecycler2=new Item_recycler_for_expiring_list(two_day,getContext(),this::onclick,curr_date,2,2);
        Item_recycler_for_expiring_list itemRecycler3=new Item_recycler_for_expiring_list(week,getContext(),this::onclick,curr_date,2,3);
        Item_recycler_for_expiring_list itemRecycler4=new Item_recycler_for_expiring_list(two_week,getContext(),this::onclick,curr_date,2,4);
        Item_recycler_for_expiring_list itemRecycler5=new Item_recycler_for_expiring_list(month,getContext(),this::onclick,curr_date,2,5);
        Item_recycler_for_expiring_list itemRecycler6=new Item_recycler_for_expiring_list(more_then_a_month,getContext(),this::onclick,curr_date,2,6);
        expiring_today_recycler.setAdapter(itemRecycler1);
        expiring_tomorrow_recycler.setAdapter(itemRecycler2);
        expires_in_a_week_7d_recycler.setAdapter(itemRecycler3);
        expires_in_2_week_14d_recycler.setAdapter(itemRecycler4);
        expire_in_a_month_30d_recycler.setAdapter(itemRecycler5);
        good_product_30d_recycler.setAdapter(itemRecycler6);
    }

    @Override
    public void onResume() {
        refresh();
        super.onResume();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_expiring_frag,container,false);
        expiring_today_recycler=view.findViewById(R.id.expiring_today_recycler);
        expiring_tomorrow_recycler=view.findViewById(R.id.expiring_tomorrow_recycler);
        expires_in_a_week_7d_recycler=view.findViewById(R.id.expiring_in_a_week_recycler);
        expires_in_2_week_14d_recycler=view.findViewById(R.id.expiring_in_two_week_recycler);
        expire_in_a_month_30d_recycler=view.findViewById(R.id.expiring_in_a_month_recycler);
        good_product_30d_recycler=view.findViewById(R.id.expiring_after_a_month_recycler);
        expiring_today_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        expiring_tomorrow_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        expires_in_a_week_7d_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        expires_in_2_week_14d_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        expire_in_a_month_30d_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        good_product_30d_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
    @Override
    public void onclick(int position, int index, int arr_index) {
        ArrayList<Items_holder>arrayList=new ArrayList<>();
        if (arr_index==1){
            arrayList.addAll(one_day);
        }else if (arr_index==2){
            arrayList.addAll(two_day);
        }else if (arr_index==3){
            arrayList.addAll(week);
        }else if (arr_index==4){
            arrayList.addAll(two_week);
        }else if (arr_index==5){
            arrayList.addAll(month);
        }else if (arr_index==6){
            arrayList.addAll(more_then_a_month);
        }
        if (index==2){
            AlertDialog.Builder alert =new AlertDialog.Builder(getContext());
            alert.setTitle("Delete"+arrayList.get(position).getName())
                    .setMessage("This will delete "+arrayList.get(position).getName()+" and all its items.")
                    .setCancelable(false)
                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myDbHandler.delete(Parameters.KEY_NAME+"='"+arrayList.get(position).getName()+"' AND "+Parameters.KEY_CATEGORY+"='"+arrayList.get(position).getCategory()+"' AND "+Parameters.KEY_DESCRIPTION+"='"+arrayList.get(position).getDescription()+"' AND "+Parameters.KEY_EXPIRY_DATE+"='"+arrayList.get(position).getExpiry_date()+"' AND "+Parameters.KEY_QUANTITY+"='"+arrayList.get(position).getQuantity()+"'");
                            Category_MyDbHandler categoryMyDbHandler=new Category_MyDbHandler(getContext());
                            categoryMyDbHandler.decrease(arrayList.get(position).getCategory(),Parameters.KEY_NAME+"='"+arrayList.get(position).getCategory()+"'");
                            Toast.makeText(getContext(), arrayList.get(position).getName()+" delete successful", Toast.LENGTH_SHORT).show();
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
            intent1.putExtra("name",arrayList.get(position).getName());
            intent1.putExtra("description",arrayList.get(position).getDescription());
            intent1.putExtra("quantity",arrayList.get(position).getQuantity());
            intent1.putExtra("expiry_date",arrayList.get(position).getExpiry_date());
            intent1.putExtra("img_uri",arrayList.get(position).getImage());
            intent1.putExtra("category",arrayList.get(position).getCategory());
            startActivity(intent1);
        }
        else if (index==4){
            Intent intent1=new Intent(getContext(), Big_picture_view.class);
            intent1.putExtra("img_uri",arrayList.get(position).getImage());
            startActivity(intent1);
        }
    }
}