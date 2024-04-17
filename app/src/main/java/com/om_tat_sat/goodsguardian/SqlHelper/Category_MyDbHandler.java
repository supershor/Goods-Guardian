package com.om_tat_sat.goodsguardian.SqlHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


import com.om_tat_sat.goodsguardian.SqlParameters.Parameters;
import com.om_tat_sat.goodsguardian.model.Category_holder;

import java.util.ArrayList;
import java.util.List;

public class Category_MyDbHandler extends SQLiteOpenHelper {
    public Category_MyDbHandler(@Nullable Context context) {
        super(context, Parameters.CATEGORY_Database_Name, null, Parameters.Database_Version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create="CREATE TABLE "+Parameters.CATEGORY_Table_Name+"("+Parameters.KEY_NAME+" Text,"+Parameters.KEY_QUANTITY+" Integer)";
        db.execSQL(create);
    }
    public void decrease(String name, int quantity,String where){
        if (quantity-1==0){
            SQLiteDatabase database=getWritableDatabase();
            database.delete(Parameters.CATEGORY_Table_Name,where,null);
            Log.e( "delete:-------------------","delete Successful");
        }else {
            SQLiteDatabase database=getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(Parameters.KEY_NAME,name);
            contentValues.put(Parameters.KEY_QUANTITY,quantity-1);
            database.update(Parameters.CATEGORY_Table_Name,contentValues,Parameters.KEY_NAME+"='"+name+"' AND "+Parameters.KEY_QUANTITY+"='"+quantity+"'",null);
        }
    }
    public void decrease(String name,String where){
        int quantity=0;
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+Parameters.CATEGORY_Table_Name+" WHERE "+Parameters.KEY_NAME+"='"+name+"'",null);
        if (cursor.moveToFirst()){
            quantity=cursor.getInt(1);
        }
        if (quantity-1==0){
            SQLiteDatabase database=getWritableDatabase();
            database.delete(Parameters.CATEGORY_Table_Name,where,null);
            Log.e( "delete:-------------------","delete Successful");
        }else {
            SQLiteDatabase database=getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(Parameters.KEY_NAME,name);
            contentValues.put(Parameters.KEY_QUANTITY,quantity-1);
            database.update(Parameters.CATEGORY_Table_Name,contentValues,Parameters.KEY_NAME+"='"+name+"' AND "+Parameters.KEY_QUANTITY+"='"+quantity+"'",null);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addItems(Category_holder categoryHolder){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Parameters.KEY_NAME,categoryHolder.getName());
        contentValues.put(Parameters.KEY_QUANTITY,categoryHolder.getQuantity());
        db.insert(Parameters.CATEGORY_Table_Name,null,contentValues);
        db.close();
        Log.e("ans Items_holder addes------","sucessfull");
    }
    public void addItems_if_not_exists(Category_holder categoryHolder,String query){
        if (only_check(query)){
            update_only(categoryHolder.getName(),categoryHolder.getQuantity());
        }else {
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(Parameters.KEY_NAME,categoryHolder.getName());
            contentValues.put(Parameters.KEY_QUANTITY,categoryHolder.getQuantity());
            db.insert(Parameters.CATEGORY_Table_Name,null,contentValues);
            db.close();
            Log.e("ans Items_holder addes------","sucessfull");
        }
    }
    public void update_only(String name, int quantity){
        SQLiteDatabase database=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Parameters.KEY_NAME,name);
        contentValues.put(Parameters.KEY_QUANTITY,quantity);
        database.update(Parameters.CATEGORY_Table_Name,contentValues,Parameters.KEY_NAME+"='"+name+"'",null);
    }
    public boolean only_check(String query){
        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        Log.e("check_already_exists_or_not:---",cursor.toString());
        Log.e("check_already_exists_or_not:---", String.valueOf(Boolean.valueOf(cursor==null)));
        if (cursor.moveToFirst()){
            return true;
        }
        return false;
    }
    public List<Category_holder> get_all_items_in_sorted_form_without_category(int code){
        List<Category_holder>categoryHolders=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        String select="SELECT * FROM "+Parameters.CATEGORY_Table_Name;
        if (code==1){
            select+=" ORDER BY "+Parameters.KEY_NAME+" ASC";
        }else if(code==2){
            select+=" ORDER BY "+Parameters.KEY_NAME+" DESC";
        }else if(code==3){
            select+=" ORDER BY "+Parameters.KEY_NAME+" ASC";
        }else if(code==4){
            select+=" ORDER BY "+Parameters.KEY_NAME+" DESC";
        }else if(code==5){
            select+=" ORDER BY "+Parameters.KEY_QUANTITY+" ASC";
        }else if(code==6){
            select+=" ORDER BY "+ Parameters.KEY_QUANTITY+" DESC";
        }
        Cursor cursor=db.rawQuery(select,null);
        if (cursor.moveToFirst()){
            do{
                Category_holder categoryHolder=new Category_holder();
                categoryHolder.setName(cursor.getString(0));
                categoryHolder.setQuantity(Integer.parseInt(cursor.getString(1)));
                categoryHolders.add(categoryHolder);
            }while (cursor.moveToNext());

        }
        for (Category_holder Items_holder:categoryHolders) {
            Log.e("ans dbms","-"+Items_holder.getName()+"-"+Items_holder.getQuantity());
        }
        return categoryHolders;
    }
    public void update(String name, int quantity){
        SQLiteDatabase database=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Parameters.KEY_NAME,name);
        contentValues.put(Parameters.KEY_QUANTITY,quantity+1);
        database.update(Parameters.CATEGORY_Table_Name,contentValues,Parameters.KEY_NAME+"='"+name+"' AND "+Parameters.KEY_QUANTITY+"='"+quantity+"'",null);
    }
    public void delete(String where){
        SQLiteDatabase database=getWritableDatabase();
        database.delete(Parameters.CATEGORY_Table_Name,where,null);
        Log.e( "delete:-------------------","delete Successful");
    }
    public boolean check_already_exists_or_not(String query){
        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        Log.e("check_already_exists_or_not:---",cursor.toString());
        Log.e("check_already_exists_or_not:---", String.valueOf(Boolean.valueOf(cursor==null)));
        if (cursor.moveToFirst()){
            update(cursor.getString(0),Integer.parseInt(cursor.getString(1)));
            return true;
        }
        return false;
    }
}
