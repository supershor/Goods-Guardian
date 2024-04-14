package com.om_tat_sat.goodsguardian.RecyclerAdapters;

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
import com.om_tat_sat.goodsguardian.data_holders.Category_holder;

import java.util.ArrayList;

public class Recycler_Adapter_Category_shower extends RecyclerView.Adapter<Recycler_Adapter_Category_shower.ViewHolder> {
    ArrayList<Category_holder>arrayList;
    Context context;
    public final RecyclerviewInterface recyclerviewInterface;
    public Recycler_Adapter_Category_shower(Context context, ArrayList<Category_holder>arrayList, RecyclerviewInterface recyclerviewInterface){
        this.arrayList=arrayList;
        this.context=context;
        this.recyclerviewInterface=recyclerviewInterface;
        Log.e( "Recycler_Adapter_Category_shower: ","Reach1");
        Log.e( "Recycler_Adapter_Category_shower: ",context.toString());
    }
    @NonNull
    @Override
    public Recycler_Adapter_Category_shower.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e( "Recycler_Adapter_Category_shower: ","Reach2");

        View view= LayoutInflater.from(context).inflate(R.layout.recycler_view_element_layout_category_shower,parent,false);
        ViewHolder viewHolder=new ViewHolder(view,recyclerviewInterface);
        Log.e( "Recycler_Adapter_Category_shower: ","Reach2");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Recycler_Adapter_Category_shower.ViewHolder holder, int position) {
        Log.e( "Recycler_Adapter_Category_shower: ","Reach3");
        holder.Name.setText(arrayList.get(position).getName());
        Log.e( "Recycler_Adapter_Category_shower: ",arrayList.get(position).getName()+"");
        holder.quantity.setText(arrayList.get(position).getQuantity());
        Log.e( "Recycler_Adapter_Category_shower: ",arrayList.get(position).getQuantity()+"");
        Log.e( "Recycler_Adapter_Category_shower: ","Reach3");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;
        TextView quantity;
        AppCompatButton bin;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView,RecyclerviewInterface recyclerviewInterface) {
            super(itemView);
            imageView=itemView.findViewById(R.id.category_imageview_setter_at_main_category_page);
            Name=itemView.findViewById(R.id.category_name_setter_at_main_category_page);
            quantity=itemView.findViewById(R.id.category_quantity_setter_at_main_category_page);
            bin=itemView.findViewById(R.id.category_delete_button_at_main_category_page);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerviewInterface!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            recyclerviewInterface.onclick(position,1);
                        }
                    }
                }
            });
            bin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerviewInterface!=null){
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            recyclerviewInterface.onclick(position,2);
                        }
                    }
                }
            });
        }
    }
}
