package com.om_tat_sat.goodsguardian.RecyclerAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.om_tat_sat.goodsguardian.R;
import com.om_tat_sat.goodsguardian.RecyclerviewInterface;
import com.om_tat_sat.goodsguardian.Utils;
import com.om_tat_sat.goodsguardian.model.Items_holder;

import java.util.ArrayList;

public class Item_recycler extends RecyclerView.Adapter<Item_recycler.ViewHolder>{
    ArrayList<Items_holder>arrayList;
    private final RecyclerviewInterface recyclerviewInterface;
    Context context;
    public String curr_date="";

    public Item_recycler(ArrayList<Items_holder> arrayList, Context context, RecyclerviewInterface recyclerviewInterface,String curr_date) {
        this.arrayList = arrayList;
        this.curr_date=curr_date;
        this.recyclerviewInterface=recyclerviewInterface;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.recycler_view_element_layout_item_shower,null);
        ViewHolder viewHolder=new ViewHolder(view,recyclerviewInterface);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Name.setText(arrayList.get(position).getName()+"");
        holder.Desc.setText("Desc. : "+arrayList.get(position).getDescription()+"");
        holder.Quantity.setText("Quan. : "+arrayList.get(position).getQuantity()+"");
        String arr[]=arrayList.get(position).getExpiry_date().split("_");
        int month=Integer.parseInt(arr[1])+1;
        holder.Expiry_date.setText("Exp. : "+Integer.parseInt(arr[0])+"/"+month+"/"+Integer.parseInt(arr[2]));
        holder.Expiring_in.setText(calculate_date_left(position));
        holder.imageView.setImageBitmap(Utils.getImage(arrayList.get(position).getImage()));
    }
    public String calculate_date_left(int position){
        String exp_date[]=arrayList.get(position).getExpiry_date().split("_");
        String date[]=curr_date.split("-");
        Log.e( "calculate_date_left:-------------------",arrayList.get(position).getExpiry_date());
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
        String exp[]=arrayList.get(position).getExpiry_date().split("_");
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
            for (int i = 1; i <=exp_month; i++) {
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
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        TextView Desc;
        TextView Quantity;
        TextView Expiry_date;
        TextView Expiring_in;
        AppCompatButton delete;
        AppCompatButton edit;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView,RecyclerviewInterface recyclerviewInterface) {
            super(itemView);
            Name=itemView.findViewById(R.id.item_name_setter_at_main_item_page);
            Desc=itemView.findViewById(R.id.item_description_setter_at_main_item_page);
            Quantity=itemView.findViewById(R.id.item_quantity_setter_at_main_item_page);
            Expiry_date=itemView.findViewById(R.id.item_expiry_date_setter_at_main_item_page);
            Expiring_in=itemView.findViewById(R.id.expiring_in_setter_item_page);
            delete=itemView.findViewById(R.id.delete_item_page);
            edit=itemView.findViewById(R.id.edit_item_page);
            imageView=itemView.findViewById(R.id.item_imageview_setter_at_main_item_page);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerviewInterface!=null){
                        int position=getAdapterPosition();
                        if ((position!=RecyclerView.NO_POSITION)){
                            recyclerviewInterface.onclick(position,4);
                        }
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerviewInterface!=null){
                        int position=getAdapterPosition();
                        if ((position!=RecyclerView.NO_POSITION)){
                            recyclerviewInterface.onclick(position,2);
                        }
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerviewInterface!=null){
                        int position=getAdapterPosition();
                        if ((position!=RecyclerView.NO_POSITION)){
                            recyclerviewInterface.onclick(position,1);
                        }
                    }
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerviewInterface!=null){
                        int position=getAdapterPosition();
                        if ((position!=RecyclerView.NO_POSITION)){
                            recyclerviewInterface.onclick(position,3);
                        }
                    }
                }
            });
        }
    }
}
