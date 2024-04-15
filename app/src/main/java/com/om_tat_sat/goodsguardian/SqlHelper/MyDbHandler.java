package com.om_tat_sat.goodsguardian.SqlHelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


import com.om_tat_sat.goodsguardian.SqlParameters.Parameters;
import com.om_tat_sat.goodsguardian.model.Items_holder;

import java.util.ArrayList;
import java.util.List;

public class MyDbHandler extends SQLiteOpenHelper {
    public MyDbHandler(@Nullable Context context) {
        super(context, Parameters.Database_Name, null, Parameters.Database_Version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create="CREATE TABLE "+Parameters.Table_Name+"("+Parameters.KEY_ID+"Integer Primary Key,"+
                Parameters.KEY_NAME+" Text,"+Parameters.KEY_DESCRIPTION+" Text,"+Parameters.KEY_QUANTITY+" Integer,"
                +Parameters.KEY_CATEGORY+" Text,"+Parameters.KEY_EXPIRY_DATE+" Text,"+Parameters.KEY_Image+" Text"+")";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addItems(Items_holder Items_holder){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Parameters.KEY_NAME,Items_holder.getName());
        contentValues.put(Parameters.KEY_DESCRIPTION,Items_holder.getDescription());
        contentValues.put(Parameters.KEY_QUANTITY,Items_holder.getQuantity());
        contentValues.put(Parameters.KEY_CATEGORY,Items_holder.getCategory());
        contentValues.put(Parameters.KEY_EXPIRY_DATE,Items_holder.getExpiry_date());
        contentValues.put(Parameters.KEY_Image,Items_holder.getImage());
        db.insert(Parameters.Table_Name,null,contentValues);
        db.close();
        Log.e("ans Items_holder addes------","sucessfull");
    }
    public void delete(String where){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(Parameters.Table_Name,where,null);
        Log.e( "delete:-------------------","delete Successful");
    }
    public List<Items_holder> get_all_items_in_sorted_form_for_category(String Category,int code){
        List<Items_holder>item_list=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        String select="SELECT * FROM "+Parameters.Table_Name+" WHERE "+Parameters.KEY_CATEGORY+"='"+Category+"'";
        if (code==1){
            select+=" ORDER BY "+Parameters.KEY_NAME+" ASC";
        }else if(code==2){
            select+=" ORDER BY "+Parameters.KEY_NAME+" DESC";
        }else if(code==3){
            select+=" ORDER BY "+Parameters.KEY_EXPIRY_DATE+" ASC";
        }else if(code==4){
            select+=" ORDER BY "+Parameters.KEY_EXPIRY_DATE+" DESC";
        }else if(code==5){
            select+=" ORDER BY "+Parameters.KEY_QUANTITY+" ASC";
        }else if(code==6){
            select+=" ORDER BY "+Parameters.KEY_QUANTITY+" DESC";
        }
        Cursor cursor=db.rawQuery(select,null);
        if (cursor.moveToFirst()){
            do{
                Items_holder Items_holder=new Items_holder();
                Items_holder.setName(cursor.getString(1));
                Items_holder.setDescription(cursor.getString(2));
                Items_holder.setQuantity(Integer.parseInt(cursor.getString(3)));
                Items_holder.setCategory(cursor.getString(4));
                Items_holder.setExpiry_date(cursor.getString(5));
                Items_holder.setImage(cursor.getString(6));
                item_list.add(Items_holder);
            }while (cursor.moveToNext());

        }
        return item_list;
    }
    public List<Items_holder> get_all_items_in_sorted_form_without_category(int code){
        List<Items_holder>item_list=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        String select="SELECT * FROM "+Parameters.Table_Name;
        if (code==1){
            select+=" ORDER BY "+Parameters.KEY_NAME+" ASC";
        }else if(code==2){
            select+=" ORDER BY "+Parameters.KEY_NAME+" DESC";
        }else if(code==3){
            select+=" ORDER BY "+Parameters.KEY_EXPIRY_DATE+" ASC";
        }else if(code==4){
            select+=" ORDER BY "+Parameters.KEY_EXPIRY_DATE+" DESC";
        }else if(code==5){
            select+=" ORDER BY "+Parameters.KEY_QUANTITY+" ASC";
        }else if(code==6){
            select+=" ORDER BY "+ Parameters.KEY_QUANTITY+" DESC";
        }
        Cursor cursor=db.rawQuery(select,null);
        if (cursor.moveToFirst()){
            do{
                Items_holder Items_holder=new Items_holder();
                Items_holder.setName(cursor.getString(1));
                Items_holder.setDescription(cursor.getString(2));
                Items_holder.setQuantity(Integer.parseInt(cursor.getString(3)));
                Items_holder.setCategory(cursor.getString(4));
                Items_holder.setExpiry_date(cursor.getString(5));
                Items_holder.setImage(cursor.getString(6));
                item_list.add(Items_holder);
            }while (cursor.moveToNext());

        }
        for (Items_holder Items_holder:item_list) {
            Log.e("ans dbms","-"+Items_holder.getName()+"-"+Items_holder.getDescription()+"-"+Items_holder.getCategory()+"-"+Items_holder.getQuantity()+"-"+Items_holder.getExpiry_date()+"-"+Items_holder.getImage());
        }
        return item_list;
    }
    public boolean check_already_exists_or_not(String query){
        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        Log.e("check_already_exists_or_not:---",cursor.toString());
        Log.e("check_already_exists_or_not:---", String.valueOf(Boolean.valueOf(cursor==null)));
        if (cursor.moveToFirst()){
            return  true;
        }
        return false;
    }
}
