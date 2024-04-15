package com.om_tat_sat.goodsguardian.RecyclerAdapters;

import android.content.Context;
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
import com.om_tat_sat.goodsguardian.model.Items_holder;

import java.util.ArrayList;

public class Item_recycler extends RecyclerView.Adapter<Item_recycler.ViewHolder>{
    ArrayList<Items_holder>arrayList;
    private final RecyclerviewInterface recyclerviewInterface;
    Context context;

    public Item_recycler(ArrayList<Items_holder> arrayList, Context context, RecyclerviewInterface recyclerviewInterface) {
        this.arrayList = arrayList;
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Name.setText(arrayList.get(position).getName()+"");
        holder.Desc.setText("Desc. : "+arrayList.get(position).getDescription()+"");
        holder.Quantity.setText("Quan. : "+arrayList.get(position).getQuantity()+"");
        holder.Expiry_date.setText("Exp. : "+arrayList.get(position).getExpiry_date()+"");
        holder.Expiring_in.setText("+30d");
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
