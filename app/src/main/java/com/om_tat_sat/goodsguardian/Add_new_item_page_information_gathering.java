package com.om_tat_sat.goodsguardian;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.om_tat_sat.goodsguardian.Image_compressers.Image_reducer;
import com.om_tat_sat.goodsguardian.SqlHelper.Category_MyDbHandler;
import com.om_tat_sat.goodsguardian.SqlHelper.MyDbHandler;
import com.om_tat_sat.goodsguardian.SqlParameters.Parameters;
import com.om_tat_sat.goodsguardian.model.Category_holder;
import com.om_tat_sat.goodsguardian.model.Items_holder;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.security.auth.login.LoginException;

public class Add_new_item_page_information_gathering extends AppCompatActivity {
    EditText item_name;
    EditText item_description;
    EditText quantity;
    DatePicker datePicker;
    ImageView imageView;

    androidx.appcompat.widget.AppCompatSpinner spinner;
    AppCompatButton take_photo;
    Uri uri;
    String issue;
    AppCompatButton save;
    AppCompatButton change;
    AppCompatButton choose_photo;
    LinearLayout layout;TextView textView;
    FirebaseAuth firebaseAuth;
    String expiry_date;
    String description;
    String name;
    int quan;
    Intent intent;
    Category_MyDbHandler Category_MyDbHandler;
    ArrayList<String> categories_for_storing_items;
    @SuppressLint({"SetTextI18n", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_item_page_information_gathering);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //setting status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(Add_new_item_page_information_gathering.this,R.color.cream));

        //checking is the user is signed in or not
        firebaseAuth=FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(Add_new_item_page_information_gathering.this,Loading_Page.class));
            finishAffinity();
        }


        //initializing elements
        textView=findViewById(R.id.spinner_shower_tv);
        layout=findViewById(R.id.spinner_shower_ll);
        item_name=findViewById(R.id.item_name_at_add_items);
        imageView=findViewById(R.id.image_setter);
        item_description=findViewById(R.id.item_description_at_add_items);
        quantity=findViewById(R.id.item_quantity_at_add_items);
        datePicker=findViewById(R.id.item_expiry_date_at_add_items);
        take_photo=findViewById(R.id.item_take_photo_at_add_items);
        take_photo.setVisibility(View.GONE);
        save=findViewById(R.id.save_item_details);
        change=findViewById(R.id.change_item_details);
        choose_photo=findViewById(R.id.item_take_choose_at_add_items);
        spinner=findViewById(R.id.spinner);

        //if its added to change then hiding save button and if its added to save then hiding change button
        intent=getIntent();
        if (intent.getIntExtra("save_or_change",0)==0){
            change.setVisibility(View.GONE);
        }else{
            save.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
            name=intent.getStringExtra("name");
            description=intent.getStringExtra("description");
            if (intent.getByteArrayExtra("img_uri")==null||Utils.getImage(intent.getByteArrayExtra("img_uri"))==null){
                Toast.makeText(this, "Unable to set Imageview", Toast.LENGTH_SHORT).show();
            }else {
                imageView.setImageBitmap(Utils.getImage(intent.getByteArrayExtra("img_uri")));
            }
            expiry_date=intent.getStringExtra("expiry_date");
            String[] arr=expiry_date.split("_");
            datePicker.updateDate(Integer.parseInt(arr[2]),Integer.parseInt(arr[1]),Integer.parseInt(arr[0]));
            quan=intent.getIntExtra("quantity",0);
            item_name.setText(name, TextView.BufferType.EDITABLE);
            item_description.setText(description);
            quantity.setText(quan+"");
        }
        //Sql
        MyDbHandler myDbHandler=new MyDbHandler(Add_new_item_page_information_gathering.this);
        Category_MyDbHandler=new Category_MyDbHandler(Add_new_item_page_information_gathering.this);
        //setting up spinner for category
        categories_for_storing_items=new ArrayList<>();
        add_elements_in_categories_arr();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getBaseContext(), R.layout.spinner_black_text_element_layout,categories_for_storing_items);
        arrayAdapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(31);

        //setting on click listeners
        choose_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenImageChooser();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_fields()){
                    Toast.makeText(Add_new_item_page_information_gathering.this,issue, Toast.LENGTH_SHORT).show();
                }else{
                    String query="SELECT * FROM "+ Parameters.Table_Name+" WHERE "+Parameters.KEY_NAME+"='"+item_name.getText().toString()+"'"+" AND "+Parameters.KEY_CATEGORY+"='"+spinner.getSelectedItem().toString()+"' AND "+Parameters.KEY_QUANTITY+"='"+quantity.getText().toString()+"' AND "+Parameters.KEY_EXPIRY_DATE+"='"+expiry_date+"'";
                    if (myDbHandler.check_already_exists_or_not(query)){
                        Toast.makeText(Add_new_item_page_information_gathering.this, "Item with already exist.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(Add_new_item_page_information_gathering.this, "Try changing name, category, quantity or expiry date.", Toast.LENGTH_SHORT).show();
                        List<Items_holder> itemsList=myDbHandler.get_all_items_in_sorted_form_without_category(1);
                        for (Items_holder items:itemsList) {
                            Log.e("ans dbms main --------->>>>",items.getName()+"-"+items.getDescription()+"-"+items.getCategory()+"-"+items.getQuantity()+"-"+items.getExpiry_date()+"-"+items.getImage());
                        }
                    }else{
                        if (getImg()){
                            Toast.makeText(Add_new_item_page_information_gathering.this, "Please choose a image.", Toast.LENGTH_SHORT).show();
                        }else{
                            myDbHandler.addItems(new Items_holder(item_name.getText().toString(),item_description.getText().toString(),Integer.parseInt(quantity.getText().toString()),spinner.getSelectedItem().toString(),datePicker.getDayOfMonth()+"_"+datePicker.getMonth()+"_"+datePicker.getYear(),getUri()),getUri());
                            Toast.makeText(Add_new_item_page_information_gathering.this, "Add successful", Toast.LENGTH_SHORT).show();
                            if (Category_MyDbHandler.check_already_exists_or_not("SELECT * FROM "+Parameters.CATEGORY_Table_Name +" WHERE "+Parameters.KEY_NAME+"='"+spinner.getSelectedItem()+"'")){
                                Log.e( "onClick:---------------", "already exists");
                            }else {
                                Log.e( "onClick:-------------", "adding exists");
                                Category_MyDbHandler.addItems(new Category_holder(spinner.getSelectedItem()+"",1));
                            }
                            List<Category_holder>categoryHolders= Category_MyDbHandler.get_all_items_in_sorted_form_without_category(1);
                            for (Category_holder categoryMyDbHandler:categoryHolders) {
                                Log.e("ans dbms category --------->>>>",categoryMyDbHandler.getName()+"-"+categoryMyDbHandler.getQuantity());
                            }
                            startActivity(new Intent(Add_new_item_page_information_gathering.this,MainActivity.class));
                            finishAffinity();
                        }

                    }

                }
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_fields()){
                    Toast.makeText(Add_new_item_page_information_gathering.this,issue, Toast.LENGTH_SHORT).show();
                }else{
                    if (uri==null){
                        myDbHandler.update(Parameters.KEY_NAME+"='"+intent.getStringExtra("name")+"' AND "+Parameters.KEY_EXPIRY_DATE+"='"+intent.getStringExtra("expiry_date")+"' AND "+Parameters.KEY_QUANTITY+"='"+intent.getIntExtra("quantity",0)+"' AND "+Parameters.KEY_DESCRIPTION+"='"+intent.getStringExtra("description")+"' AND "+Parameters.KEY_CATEGORY+"='"+intent.getStringExtra("category")+"'",new Items_holder(item_name.getText().toString(),item_description.getText().toString(),Integer.parseInt(String.valueOf(quantity.getText())),intent.getStringExtra("category"),datePicker.getDayOfMonth()+"_"+datePicker.getMonth()+"_"+datePicker.getYear(),intent.getByteArrayExtra("img_uri")));
                    }else {
                        myDbHandler.update(Parameters.KEY_NAME+"='"+intent.getStringExtra("name")+"' AND "+Parameters.KEY_EXPIRY_DATE+"='"+intent.getStringExtra("expiry_date")+"' AND "+Parameters.KEY_QUANTITY+"='"+intent.getIntExtra("quantity",0)+"' AND "+Parameters.KEY_DESCRIPTION+"='"+intent.getStringExtra("description")+"' AND "+Parameters.KEY_CATEGORY+"='"+intent.getStringExtra("category")+"'",new Items_holder(item_name.getText().toString(),item_description.getText().toString(),Integer.parseInt(String.valueOf(quantity.getText())),intent.getStringExtra("category"),datePicker.getDayOfMonth()+"_"+datePicker.getMonth()+"_"+datePicker.getYear(),getUri()));
                    }
                    startActivity(new Intent(Add_new_item_page_information_gathering.this,MainActivity.class));
                    finishAffinity();
                }
            }
        });
    }
    public void OpenImageChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }
    public boolean getImg(){
        if (uri==null){
            OpenImageChooser();
            return true;
        }else{
            return false;
        }
    }
    public byte[] getUri(){
        if (uri==null){
            OpenImageChooser();
        }else{
            try {
                InputStream inputStream=getContentResolver().openInputStream(uri);
                byte[] inputData=Utils.getBytes(inputStream);
                return inputData;
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode==RESULT_OK){
            if (requestCode==100){
                if (data!=null){
                    Toast.makeText(this, "Image selection performed", Toast.LENGTH_SHORT).show();
                    Uri ImageSetter=data.getData();
                    uri=ImageSetter;
                    Bitmap bitmap=Image_reducer.reduceBitmapSize(Utils.getImage(getUri()),1024*1024);
                    imageView.setImageBitmap(bitmap);
                    uri=getImageUri(Add_new_item_page_information_gathering.this,bitmap);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public boolean check_fields(){
        issue="";
        if (item_name.getText()==null||item_name.getText().toString().isEmpty()) {
            issue = "Please enter item name";
            return true;
        } else if (item_description.getText()==null||item_description.getText().toString().isEmpty()) {
            issue="Please enter item description";
            return true;
        }else if (quantity.getText()==null||quantity.getText().toString().isEmpty()){
            issue="Please enter item quantity";
            return true;
        } else if (item_name.getText().toString().contains(".")) {
            issue="Item name cannot contain ->  .  <- in it";return true;
        }else if (item_name.getText().toString().contains("#")) {
            issue="Item name cannot contain ->  #  <- in it";return true;
        }else if (item_name.getText().toString().contains("$")) {
            issue="Item name cannot contain ->  $  <- in it";return true;
        }else if (item_name.getText().toString().contains("[")) {
            issue="Item name cannot contain ->  [  <- in it";return true;
        }else if (item_name.getText().toString().contains("]")) {
            issue="Item name cannot contain ->  ]  <- in it";return true;
        }else if (item_description.getText().toString().contains(".")) {
            issue="Item description cannot contain ->  .  <- in it";return true;
        }else if (item_description.getText().toString().contains("#")) {
            issue="Item description cannot contain ->  #  <- in it";return true;
        }else if (item_description.getText().toString().contains("$")) {
            issue="Item description cannot contain ->  $  <- in it";return true;
        }else if (item_description.getText().toString().contains("[")) {
            issue="Item description cannot contain ->  [  <- in it";return true;
        }else if (item_description.getText().toString().contains("]")) {
            issue="Item description cannot contain ->  ]  <- in it";return true;
        }
        return false;
    }
    public void add_elements_in_categories_arr(){
        categories_for_storing_items.clear();
        categories_for_storing_items.add("Alcohol");
        categories_for_storing_items.add("Bakery");
        categories_for_storing_items.add("Barbecue");
        categories_for_storing_items.add("Bread");
        categories_for_storing_items.add("Canned Food");
        categories_for_storing_items.add("Cereal");
        categories_for_storing_items.add("Chocolate");
        categories_for_storing_items.add("Cookies");
        categories_for_storing_items.add("Cosmetics");
        categories_for_storing_items.add("Dairy Products");
        categories_for_storing_items.add("Dessert");
        categories_for_storing_items.add("Drink");
        categories_for_storing_items.add("Eggs");
        categories_for_storing_items.add("Fridge");
        categories_for_storing_items.add("Fruit");
        categories_for_storing_items.add("Grain");
        categories_for_storing_items.add("Grocery");
        categories_for_storing_items.add("Honey");
        categories_for_storing_items.add("Ice_cream");
        categories_for_storing_items.add("Liquor");
        categories_for_storing_items.add("Meat");
        categories_for_storing_items.add("Medicine");
        categories_for_storing_items.add("Noodle");
        categories_for_storing_items.add("Nuts");
        categories_for_storing_items.add("Oil");
        categories_for_storing_items.add("Pasta");
        categories_for_storing_items.add("Pizza");
        categories_for_storing_items.add("Seafood");
        categories_for_storing_items.add("Shopping Bag");
        categories_for_storing_items.add("Snack");
        categories_for_storing_items.add("Vegetable");
        categories_for_storing_items.add("Other");
    }
}