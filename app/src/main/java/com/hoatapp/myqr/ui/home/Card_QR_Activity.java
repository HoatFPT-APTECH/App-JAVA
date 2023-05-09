package com.hoatapp.myqr.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.hoatapp.myqr.R;
import com.hoatapp.myqr.Service.GenQR;

public class Card_QR_Activity extends AppCompatActivity {

    TextView edtName;
    TextView  edtAddress;
    TextView  edtPhone;
    TextView  edtLinkFacebook;
    TextView  edtLinkEmail;
    TextView txtName;
    TextView txtPhone;
    TextView txtEmail;

    ImageView qrImage,qrImageBe;
    CardView cardViewQR;
    Button btnSave;
    Button btnShare;
    Bitmap btpCardQrFe,btpCardQrBe;
    String strName,strAddress,strFacebook,strEmail,strPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_qr);

        edtName=findViewById(R.id.editTextName);
        edtAddress=findViewById(R.id.editTextAddress);
        edtPhone=findViewById(R.id.editTextPhone);
        edtLinkFacebook=findViewById(R.id.editTextFacebook);
        edtLinkEmail=findViewById(R.id.editTextEmail);
        btnSave=findViewById(R.id.btnSave);
        btnShare= findViewById(R.id.btnShare);

        qrImage=findViewById(R.id.qrImage);
        txtEmail=findViewById(R.id.txtEmail);
        txtName=findViewById(R.id.txtName);
        txtPhone=findViewById(R.id.txtPHone);
        qrImageBe=findViewById(R.id.qrcardBe);

       // lay du lieu cap nhat cho truong card
        Intent data=getIntent();
        String strName=String.valueOf(data.getStringExtra("name").toString());

        String strAddress=String.valueOf(data.getStringExtra("address"));
        String  strFacebook=String.valueOf(data.getStringExtra("facebook"));
        String strEmail=String.valueOf(data.getStringExtra("email"));
        String strPhone=String.valueOf(data.getStringExtra("phone"));
                        //set up du lieu cho activity
        btpCardQrFe=data.getExtras().getParcelable("qrImage");

        edtName.setText(strName);
        txtName.setText(strName);
        edtLinkEmail.setText(strEmail);
        txtEmail.setText("Email: "+strEmail);
        edtPhone.setText(strPhone);
        txtPhone.setText("Phone number: "+strPhone);
        edtAddress.setText(strAddress);
        edtLinkFacebook.setText(strFacebook);

        qrImage.setImageBitmap(btpCardQrFe);
        // lay du lieu de luu card
        // lay du lieu the tu mat truoc
        cardViewQR=(CardView) findViewById(R.id.card_view);
        try{
            cardViewQR.setDrawingCacheEnabled(true);
            btpCardQrFe =  loadBitmapFromView(cardViewQR);
        }catch (Exception ex){

          Log.d("Lỗi hàm convert cardview",ex.getMessage());
        }
    }

    protected  void onResume(){
        super.onResume();

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GenQR genQR= new GenQR();
                genQR.shareBitmap(btpCardQrFe,Card_QR_Activity.this);

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenQR genQR= new GenQR();
                if( genQR.saveImageToGallery(btpCardQrFe)) Toast.makeText(Card_QR_Activity.this,"Save Image is successfully",Toast.LENGTH_SHORT).show();
                else Toast.makeText(Card_QR_Activity.this,"Error",Toast.LENGTH_SHORT).show();

            }
        });

    }

    public Bitmap loadBitmapFromView(View v) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);

        return returnedBitmap;

    }
}