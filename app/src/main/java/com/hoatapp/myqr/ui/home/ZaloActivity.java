package com.hoatapp.myqr.ui.home;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hoatapp.myqr.R;

public class ZaloActivity extends AppCompatActivity {

    EditText edtNameWebsite;
    EditText edtLinkWebsite;

    Button btngen;
    public static final int REQUEST_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zalo);
        ActionBar actionBar= getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle("Zalo to QR");
        }
        edtNameWebsite=findViewById(R.id.edtNameWebstie);
        edtLinkWebsite=findViewById(R.id.edtLinkwebsite);

        btngen=findViewById(R.id.button);

        btngen.setVisibility(View.INVISIBLE);
    }
    protected  void onResume(){
        super.onResume();
        TextWatcher textWatcher= new TextWatcher() {
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
        };
        edtNameWebsite.addTextChangedListener(textWatcher);
        edtLinkWebsite.addTextChangedListener(textWatcher);
        btngen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strInput= edtNameWebsite.getText().toString() +"| " +edtLinkWebsite.getText().toString() ;
                Intent qr= new Intent();
                qr.setClass(v.getContext(), QR_Image_Activity.class);
                qr.putExtra("text",strInput);
                startActivity(qr);
            }
        });
    }
}