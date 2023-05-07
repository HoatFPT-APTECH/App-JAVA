package com.hoatapp.myqr.ui.home;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.hoatapp.myqr.R;
import com.hoatapp.myqr.Service.GenQR;

import android.Manifest;
import android.widget.Toast;

import java.util.Date;

public class QR_Image_Activity extends AppCompatActivity{
   public static final  int RESULT_OK=1;

   private TextView txtshow;
   private  Button btnShare;
   private Button btnSave;
   private ImageView imgQR;
   private Bitmap btpQr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_image);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("Generated QR code ");
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE},1);
        // lay cac id cua tp activty
        txtshow=findViewById(R.id.textView2);
        btnSave=findViewById(R.id.btnSave);
        btnShare= findViewById(R.id.btnShare);
        imgQR=findViewById(R.id.imageView);
        // lay du lieu tu activity input send
        Intent intent=getIntent();
        GenQR genQR= new GenQR();
        String strGen=intent.getStringExtra("text");
        // set img qr
        Bitmap img=genQR.generateQRCode(strGen,300,300);
        btpQr=img;
        imgQR.setImageBitmap(img);

        //set txt
        txtshow.setText(strGen);


    }
    protected  void onResume(){
        super.onResume();

      btnShare.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              GenQR genQR= new GenQR();
              genQR.shareBitmap(btpQr,QR_Image_Activity.this);

          }
      });
      btnSave.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
               GenQR genQR= new GenQR();
             if( genQR.saveImageToGallery(btpQr))Toast.makeText(QR_Image_Activity.this,"Save Image is successfully",Toast.LENGTH_SHORT).show();
             else Toast.makeText(QR_Image_Activity.this,"Error",Toast.LENGTH_SHORT).show();

          }
      });
    }
    protected void onPause(){
        super.onPause();
        setResultData();
    }
    private void setResultData(){
      Intent intent= new Intent();
      intent.putExtra("oldText",txtshow.getText().toString());
      setResult(RESULT_OK,intent);
    }
}