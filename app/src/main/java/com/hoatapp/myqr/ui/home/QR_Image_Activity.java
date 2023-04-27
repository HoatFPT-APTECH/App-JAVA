package com.hoatapp.myqr.ui.home;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoatapp.myqr.R;
import com.hoatapp.myqr.Service.GenQR;
public class QR_Image_Activity extends AppCompatActivity {
   public static final  int RESULT_OK=1;

   private TextView txtshow;
   private Button btnSave;
   private ImageView imgQR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_image);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setTitle("Generated QR code ");
        // lay cac id cua tp activty
        txtshow=findViewById(R.id.textView2);
        btnSave=findViewById(R.id.button2);
        imgQR=findViewById(R.id.imageView);
        // lay du lieu tu activity input send
        Intent intent=getIntent();
        GenQR genQR= new GenQR();
        String strGen=intent.getStringExtra("text");
        // set img qr
        Bitmap img=genQR.generateQRCode(strGen,300,300);
        imgQR.setImageBitmap(img);
        //set txt
        txtshow.setText(strGen);

    }
    protected  void onResume(){
        super.onResume();
    }
    protected void onPause(){
        super.onPause();
        setResultData();
    }
    private void setResultData(){
      Intent intent= new Intent();
      intent.putExtra("olgText",txtshow.getText().toString());
      setResult(RESULT_OK,intent);
    }
}