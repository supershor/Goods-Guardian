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
import com.om_tat_sat.goodsguardian.model.Category_holder;

import java.util.ArrayList;
import java.util.Arrays;

public class recycler extends RecyclerView.Adapter<recycler.ViewHolder> {
    ArrayList<Category_holder>arr;
    private final RecyclerviewInterface recyclerviewInterface;
    ArrayList<Integer>resource_arr;
    Context context;
    public void set(){
        resource_arr.clear();
        resource_arr.add(R.drawable.alcohol);
        resource_arr.add(R.drawable.bakery);
        resource_arr.add(R.drawable.barbecue);
        resource_arr.add(R.drawable.bread);
        resource_arr.add(R.drawable.canned_food);
        resource_arr.add(R.drawable.cereal);
        resource_arr.add(R.drawable.chocolate);
        resource_arr.add(R.drawable.cookies);
        resource_arr.add(R.drawable.cosmetics);
        resource_arr.add(R.drawable.dairy_products);
        resource_arr.add(R.drawable.dessert);
        resource_arr.add(R.drawable.drink);
        resource_arr.add(R.drawable.eggs);
        resource_arr.add(R.drawable.fridge);
        resource_arr.add(R.drawable.fruit);
        resource_arr.add(R.drawable.grain);
        resource_arr.add(R.drawable.grocery);
        resource_arr.add(R.drawable.honey);
        resource_arr.add(R.drawable.ice_cream);
        resource_arr.add(R.drawable.liquor);
        resource_arr.add(R.drawable.meat);
        resource_arr.add(R.drawable.medicine);
        resource_arr.add(R.drawable.noodle);
        resource_arr.add(R.drawable.nuts);
        resource_arr.add(R.drawable.oil);
        resource_arr.add(R.drawable.pasta);
        resource_arr.add(R.drawable.pizza);
        resource_arr.add(R.drawable.seafood);
        resource_arr.add(R.drawable.shopping_bag);
        resource_arr.add(R.drawable.snack);
        resource_arr.add(R.drawable.vegetable);
        resource_arr.add(R.drawable.other);
    }
    public recycler(Context context, ArrayList<Category_holder>arr,RecyclerviewInterface recyclerviewInterface){
        this.arr=arr;
        this.recyclerviewInterface=recyclerviewInterface;
        this.context=context;
        resource_arr=new ArrayList<>();
        set();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_view_element_layout_category_shower,null);
        ViewHolder viewHolder=new ViewHolder(view,recyclerviewInterface);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e( "onBindViewHolder:------------",arr.get(position).getName());
        Log.e( "onBindViewHolder:------------",arr.get(position).getQuantity()+"");
        Log.e( "onBindViewHolder:------------", String.valueOf(Boolean.valueOf(holder.name==null)));
        holder.name.setText(arr.get(position).getName());
        holder.quantity.setText("x"+arr.get(position).getQuantity());
        holder.imageView.setImageResource((int)image_extracter(arr.get(position).getName()));
    }

    private int image_extracter(String name){
        if (name.equals("Alcohol")){
            return resource_arr.get(0);
        }if (name.equals("Bakery")){
            return resource_arr.get(1);
        }if (name.equals("Barbecue")){
            return resource_arr.get(2);
        }if (name.equals("Bread")){
            return resource_arr.get(3);
        }if (name.equals("Canned Food")){
            return resource_arr.get(4);
        }if (name.equals("Cereal")){
            return resource_arr.get(5);
        }if (name.equals("Chocolate")){
            return resource_arr.get(6);
        }if (name.equals("Cookies")){
            return resource_arr.get(7);
        }if (name.equals("Cosmetics")){
            return resource_arr.get(8);
        }if (name.equals("Dairy Products")){
            return resource_arr.get(9);
        }if (name.equals("Dessert")){
            return resource_arr.get(10);
        }if (name.equals("Drink")){
            return resource_arr.get(11);
        }if (name.equals("Eggs")){
            return resource_arr.get(12);
        }if (name.equals("Fridge")){
            return resource_arr.get(13);
        }if (name.equals("Fruit")){
            return resource_arr.get(14);
        }if (name.equals("Grain")){
            return resource_arr.get(15);
        }if (name.equals("Grocery")){
            return resource_arr.get(16);
        }if (name.equals("Honey")){
            return resource_arr.get(17);
        }if (name.equals("Ice_cream")){
            return resource_arr.get(18);
        }if (name.equals("Liquor")){
            return resource_arr.get(19);
        }if (name.equals("Meat")){
            return resource_arr.get(20);
        }if (name.equals("Medicine")){
            return resource_arr.get(21);
        }if (name.equals("Noodle")){
            return resource_arr.get(22);
        }if (name.equals("Nuts")){
            return resource_arr.get(23);
        }if (name.equals("Oil")){
            return resource_arr.get(24);
        }if (name.equals("Pasta")){
            return resource_arr.get(25);
        }if (name.equals("Pizza")){
            return resource_arr.get(26);
        }if (name.equals("Seafood")){
            return resource_arr.get(27);
        }if (name.equals("Shopping Bag")){
            return resource_arr.get(28);
        }if (name.equals("Snack")){
            return resource_arr.get(29);
        }if (name.equals("Vegetable")){
            return resource_arr.get(30);
        }if (name.equals("Other")){
            return resource_arr.get(31);
        }
        return resource_arr.get(31);
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imageView;
        AppCompatButton appCompatButton;
        TextView quantity;
        public ViewHolder(@NonNull View itemView,RecyclerviewInterface recyclerviewInterface) {
            super(itemView);
            imageView=itemView.findViewById(R.id.category_imageview_setter_at_main_category_page);
            appCompatButton=itemView.findViewById(R.id.category_delete_button_at_main_category_page);
            name=itemView.findViewById(R.id.category_name_setter_at_main_category_page);
            quantity=itemView.findViewById(R.id.category_quantity_setter_at_main_category_page);
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
            appCompatButton.setOnClickListener(new View.OnClickListener() {
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
