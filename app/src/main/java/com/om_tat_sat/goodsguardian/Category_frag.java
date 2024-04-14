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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.om_tat_sat.goodsguardian.RecyclerAdapters.recycler;
import com.om_tat_sat.goodsguardian.SqlHelper.Category_MyDbHandler;
import com.om_tat_sat.goodsguardian.SqlParameters.Parameters;
import com.om_tat_sat.goodsguardian.model.Category_holder;

import java.util.ArrayList;
import java.util.List;

public class Category_frag extends Fragment implements RecyclerviewInterface{
    RecyclerView recyclerView2;
    ArrayList<Category_holder> categoryHolders;
    Category_MyDbHandler categoryMyDbHandler;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryHolders=new ArrayList<>();
        categoryMyDbHandler=new Category_MyDbHandler(getContext());
        List<com.om_tat_sat.goodsguardian.model.Category_holder> holders=categoryMyDbHandler.get_all_items_in_sorted_form_without_category(1);
        for (com.om_tat_sat.goodsguardian.model.Category_holder categoryHolder:holders){
            categoryHolders.add(categoryHolder);
        }
        recyclerView2=view.findViewById(R.id.recyclerview);
        recycler recycler=new recycler(getContext(),categoryHolders,this::onclick);
        recyclerView2.setAdapter(recycler);
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

    @Override
    public void onclick(int position, int index) {
        if (index==1){

        }else {
            AlertDialog.Builder alert =new AlertDialog.Builder(getContext());
            alert.setTitle("DELETE "+categoryHolders.get(position).getName()+"?")
                    .setMessage("This will delete "+categoryHolders.get(position).getName()+" and all its items.")
                    .setCancelable(false)
                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            categoryMyDbHandler.delete(Parameters.KEY_NAME+"='"+categoryHolders.get(position).getName()+"'");
                            Toast.makeText(getContext(), categoryHolders.get(position).getName()+" delete successful", Toast.LENGTH_SHORT).show();
                            relaunch();
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
    }

    private void relaunch() {
        categoryHolders.clear();
        List<com.om_tat_sat.goodsguardian.model.Category_holder> holders=categoryMyDbHandler.get_all_items_in_sorted_form_without_category(1);
        for (com.om_tat_sat.goodsguardian.model.Category_holder categoryHolder:holders){
            categoryHolders.add(categoryHolder);
        }
        recycler recycler=new recycler(getContext(),categoryHolders,this::onclick);
        recyclerView2.setAdapter(recycler);
    }
}