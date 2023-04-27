package com.hoatapp.myqr.ui.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hoatapp.myqr.R;

public class Text_To_QR_Activity extends AppCompatActivity {
    EditText inputtxt;
    Button btngen;
    public static final int REQUEST_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_qr);
        ActionBar actionBar= getSupportActionBar();
        if(actionBar!=null){
           actionBar.setTitle(" Text To QR");
        }
        inputtxt=findViewById(R.id.inputText);
        btngen=findViewById(R.id.button);
        btngen.setVisibility(View.INVISIBLE);
    }
    protected  void onResume(){
        super.onResume();
        inputtxt.addTextChangedListener(new TextWatcher() {
            String str;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length()==0)btngen.setVisibility(View.INVISIBLE);
                else btngen.setVisibility(View.VISIBLE);
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
      btngen.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String strInput= inputtxt.getText().toString();
              Intent qr= new Intent();
              qr.setClass(v.getContext(), QR_Image_Activity.class);
              qr.putExtra("text",strInput);
              startActivityForResult(qr,REQUEST_CODE);
          }
      });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==REQUEST_CODE){
            if(resultCode== Activity.RESULT_OK){
                inputtxt.setText(data.getStringExtra("oldText"));
            }
        }
    }
}