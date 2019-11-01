package com.example.cafateriaadminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddUpdateMenuItem extends AppCompatActivity {

    public static String EXTRA_MENU_ITEM_ID="MENU_ITEM_ID";
    public static String EXTRA_MENU_ITEM_NAME="MENU_ITEM_NAME";
    public static String EXTRA_MENU_ITEM_Categories="MENU_ITEM_Categories";
    public static String EXTRA_MENU_ITEM_Price="MENU_ITEM_PRICE";

    private EditText etItemName,etItemPrice,etItemCategory;
    private Button addUpdateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_menu_item);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        etItemName=findViewById(R.id.et_itemName);
        etItemCategory=findViewById(R.id.et_itemCategory);
        etItemPrice=findViewById(R.id.et_itemPrice);

        addUpdateBtn=findViewById(R.id.add_update_btn);

        final Intent intent=getIntent();
        if(intent.hasExtra(EXTRA_MENU_ITEM_ID)){
            etItemName.setText(intent.getStringExtra(EXTRA_MENU_ITEM_NAME));
            etItemCategory.setText(intent.getStringExtra(EXTRA_MENU_ITEM_Categories));
            etItemPrice.setText(String.valueOf(intent.getDoubleExtra(EXTRA_MENU_ITEM_Price,0.0)));
            addUpdateBtn.setText(getString(R.string.update));
        }

        addUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName=etItemName.getText().toString();
                String categories=etItemCategory.getText().toString();
                String price=etItemPrice.getText().toString();

                if(itemName.equals("") || categories.equals("")|| price.equals("")){
                    Toast.makeText(getApplicationContext(),"Fields Are Empty",Toast.LENGTH_SHORT).show();
                }
                Intent data=new Intent();
                data.putExtra(EXTRA_MENU_ITEM_NAME,itemName);
                data.putExtra(EXTRA_MENU_ITEM_Categories,categories);
                data.putExtra(EXTRA_MENU_ITEM_Price,Double.parseDouble(price));

                String id=intent.getStringExtra(EXTRA_MENU_ITEM_ID);
                if(id!=null){
                    data.putExtra(EXTRA_MENU_ITEM_ID,id);
                }

                setResult(RESULT_OK,data);
                finish();
            }
        });




    }
}
