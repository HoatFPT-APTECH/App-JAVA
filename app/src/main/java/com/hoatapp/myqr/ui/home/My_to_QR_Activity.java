package com.hoatapp.myqr.ui.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hoatapp.myqr.R;

public class My_to_QR_Activity extends AppCompatActivity {

    Button btngen;
    EditText edtName;
    EditText edtAddress;
    EditText edtPhone;
    EditText edtLinkFacebook;
    EditText edtLinkEmail;
    public static final int REQUEST_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_to_qr);
        ActionBar actionBar= getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(" My card to QR");
        }
        btngen=findViewById(R.id.button);
        edtName=findViewById(R.id.editTextName);
        edtAddress=findViewById(R.id.editTextAddress);
        edtPhone=findViewById(R.id.editTextPhone);
        edtLinkFacebook=findViewById(R.id.editTextFacebook);
        edtLinkEmail=findViewById(R.id.editTextEmail);
        btngen.setVisibility(View.INVISIBLE);
    }
    protected  void onResume(){
        super.onResume();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (allEditTextsAreFilled()) {
                    btngen.setVisibility(View.VISIBLE);
                } else {
                    btngen.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        edtName.addTextChangedListener(textWatcher);
        edtAddress.addTextChangedListener(textWatcher);
        edtPhone.addTextChangedListener(textWatcher);
        edtLinkFacebook.addTextChangedListener(textWatcher);
        edtLinkEmail.addTextChangedListener(textWatcher);

        btngen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strInput= "Name: "+edtName.getText()+" | "
                        +"Phone: "+edtPhone.getText().toString()+" | "
                        +"Address: "+edtAddress.getText()+" | "
                        +"Facebook: "+edtLinkFacebook.getText()+" | "
                        +"Email: "+edtLinkEmail.getText();
                Intent qr= new Intent();

                qr.setClass(v.getContext(), MyQr_Image_Activity.class);
                qr.putExtra("text",strInput);
                qr.putExtra("name",edtName.getText().toString());
                qr.putExtra("phone",edtPhone.getText().toString());
                qr.putExtra("address",edtAddress.getText().toString());
                qr.putExtra("facebook",edtLinkFacebook.getText().toString());
                qr.putExtra("email",edtLinkEmail.getText().toString());

                startActivityForResult(qr,REQUEST_CODE);
            }
        });
    }
    private boolean allEditTextsAreFilled() {
        return !edtName.getText().toString().isEmpty()
                && !edtAddress.getText().toString().isEmpty()
                && !edtPhone.getText().toString().isEmpty()
                && !edtLinkFacebook.getText().toString().isEmpty()
                && !edtLinkEmail.getText().toString().isEmpty();
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==REQUEST_CODE){
            if(resultCode== Activity.RESULT_OK){
            edtName.setText(data.getStringExtra("name"));
            edtLinkEmail.setText(data.getStringExtra("email"));
            edtPhone.setText(data.getStringExtra("phone"));
            edtLinkEmail.setText(data.getStringExtra("email"));
            edtLinkFacebook.setText(data.getStringExtra("facebook"));
            }
        }
    }
}