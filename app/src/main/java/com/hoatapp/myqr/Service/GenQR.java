package com.hoatapp.myqr.Service;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hoatapp.myqr.ui.home.QR_Image_Activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class GenQR extends AppCompatActivity {

    public GenQR(){

    }
    public Bitmap generateQRCode(String textToEncode, int width, int height) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(textToEncode, BarcodeFormat.QR_CODE, width, height);
            int matrixWidth = bitMatrix.getWidth();

            // Create a bitmap to hold the QR code
            Bitmap bitmap = Bitmap.createBitmap(matrixWidth, matrixWidth, Bitmap.Config.RGB_565);

            // Fill the bitmap with the QR code matrix
            for (int x = 0; x < matrixWidth; x++) {
                for (int y = 0; y < matrixWidth; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
    public Boolean saveImageToGallery(Bitmap bitmap) {

        File root=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
         Date date=new Date();


        String fileName = "myqr_" +date  + ".jpg";
        File file = new File(root,fileName);
        try {
            file.createNewFile();
            FileOutputStream ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
            ostream.close();
           Log.i("Success save image:","Success"+root);
           return true;
        } catch (Exception e) {
            e.printStackTrace();

            Log.d("Error Save Image: ",e.getMessage());
            Log.d("Error Save Image: ","In: "+file.getPath());
            return false;
        }
    }
    public void shareBitmap(Bitmap bitmap, Context context) {
        try {

            File cachePath = new File(context.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        File imagePath = new File(context.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri imageUri = FileProvider.getUriForFile(context, "com.hoatapp.myqr.fileprovider", newFile);

        if (imageUri != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(intent, "Chia sẻ ảnh"));
            Log.i("Share Image", "Success");
        }
    }





}