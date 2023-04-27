package com.hoatapp.myqr.Service;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class GenQR extends AppCompatActivity {

//    private ImageView qrCodeImageView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        String textToEncode = "Hello, QR code!";
//
//        // Generate the QR code
//        Bitmap qrCodeBitmap = generateQRCode(textToEncode, 500, 500);
//
//        // Set the generated QR code bitmap to the ImageView
//        qrCodeImageView.setImageBitmap(qrCodeBitmap);
//    }
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
}