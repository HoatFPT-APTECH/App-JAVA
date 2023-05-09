package com.hoatapp.myqr.ui.home;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoatapp.myqr.R;
import com.hoatapp.myqr.Service.GenQR;

public class MyQr_Image_Activity extends AppCompatActivity {

    public static final  int RESULT_OK=1;

    private TextView txtshow;
    private Button btnShare;
    private Button btnSave;
    private  Button btnCreate;
    private ImageView imgQR;
    private Bitmap btpQr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr_image);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("Generated QR code ");
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE},1);
        // lay cac id cua tp activty
        txtshow=findViewById(R.id.textView2);
        btnSave=findViewById(R.id.btnSave);
        btnShare= findViewById(R.id.btnShare);
        btnCreate=findViewById(R.id.btncreate);
        imgQR=findViewById(R.id.imageView);
        // lay du lieu tu activity input send
        Intent oldIntent=getIntent();
        GenQR genQR= new GenQR();
        String strGen=String.valueOf(oldIntent.getStringExtra("text"));
        // set img qr
        Bitmap img=genQR.generateQRCode(strGen,300,300);
        btpQr=img;
        imgQR.setImageBitmap(img);

        //set txt
        txtshow.setText(strGen);



    }
    protected  void onResume(){
        super.onResume();
       btnCreate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent data=getIntent();
               String strName=String.valueOf(data.getStringExtra("name"));

               String strAddress=String.valueOf(data.getStringExtra("address"));
               String  strFacebook=String.valueOf(data.getStringExtra("facebook"));
               String strEmail=String.valueOf(data.getStringExtra("email"));
               String strPhone=String.valueOf(data.getStringExtra("phone"));
            Intent qrcard= new Intent();
            qrcard.setClass(v.getContext(),Card_QR_Activity.class);
               qrcard.setClass(MyQr_Image_Activity.this,Card_QR_Activity.class);
               qrcard.putExtra("name",strName);
               qrcard.putExtra("phone",strPhone);
               qrcard.putExtra("address",strAddress);
               qrcard.putExtra("facebook",strFacebook);
               qrcard.putExtra("email",strEmail);
               Bundle bundle = new Bundle();
               bundle.putParcelable("qrImage", btpQr);
               qrcard.putExtras(bundle);

               startActivity(qrcard);
           }
       });
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GenQR genQR= new GenQR();
                genQR.shareBitmap(btpQr,MyQr_Image_Activity.this);

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenQR genQR= new GenQR();
                if( genQR.saveImageToGallery(btpQr)) Toast.makeText(MyQr_Image_Activity.this,"Save Image is successfully",Toast.LENGTH_SHORT).show();
                else Toast.makeText(MyQr_Image_Activity.this,"Error",Toast.LENGTH_SHORT).show();

            }
        });
    }
    protected void onPause(){
        super.onPause();
        setResultData();
    }
    private void setResultData(){
        Intent data=getIntent();
        String strName=String.valueOf(data.getStringExtra("name"));

        String strAddress=String.valueOf(data.getStringExtra("email"));
        String  strFacebook=String.valueOf(data.getStringExtra("facebook"));
        String strEmail=String.valueOf(data.getStringExtra("email"));
        String strPhone=String.valueOf(data.getStringExtra("phone"));

        Intent myqr= new Intent();

        myqr.putExtra("name",strName);
        myqr.putExtra("phone",strPhone);
        myqr.putExtra("address",strAddress);
        myqr.putExtra("facebook",strFacebook);
        myqr.putExtra("email",strEmail);
        setResult(RESULT_OK,myqr);
    }
}