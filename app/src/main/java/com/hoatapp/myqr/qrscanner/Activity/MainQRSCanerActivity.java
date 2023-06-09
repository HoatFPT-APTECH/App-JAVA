package com.hoatapp.myqr.qrscanner.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.Result;
import com.hoatapp.myqr.R;
import com.hoatapp.myqr.qrscanner.Entity.History;
import com.hoatapp.myqr.qrscanner.SQLite.ORM.HistoryORM;

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainQRSCanerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    // Init ui elements
//    @BindView(R.id.lightButton)
    ImageView flashImageView;

    //Variables
    Intent i;
    HistoryORM h = new HistoryORM();
    private ZXingScannerView mScannerView;
    private boolean flashState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_qr_scan);
        ButterKnife.bind(this);
        ActivityCompat.requestPermissions(MainQRSCanerActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();

    }

    @Override
    public void handleResult(final Result rawResult) {

        // adding result to history
        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        History history = new History();
        history.setContext(rawResult.getText());
        history.setDate(mydate);
        h.add(getApplicationContext(), history);

        // show custom alert dialog
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);

        View v = dialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);

        TextView text = (TextView) dialog.findViewById(R.id.someText);
        text.setText(rawResult.getText());
        ImageView img = (ImageView) dialog.findViewById(R.id.imgOfDialog);
        img.setImageResource(R.drawable.ic_done_gr);

        Button webSearch = (Button) dialog.findViewById(R.id.searchButton);
        Button copy = (Button) dialog.findViewById(R.id.copyButton);
        Button share = (Button) dialog.findViewById(R.id.shareButton);
        webSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                if(Patterns.WEB_URL.matcher(rawResult.getText()).matches()) {
                    url = rawResult.getText();
                }else {
                    url = "http://www.google.com/#q=" + rawResult.getText();
                }
                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                dialog.dismiss();
                mScannerView.resumeCameraPreview(MainQRSCanerActivity.this);
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", rawResult.getText());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(getApplicationContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                mScannerView.resumeCameraPreview(MainQRSCanerActivity.this);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = rawResult.getText();
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share "));

                dialog.dismiss();
                mScannerView.resumeCameraPreview(MainQRSCanerActivity.this);
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mScannerView.resumeCameraPreview(MainQRSCanerActivity.this);
            }
        });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(MainQRSCanerActivity.this, "Permission denied to camera", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @OnClick
    void mainActivityOnClickEvents(View v) {
        if(v.getId()==R.id.historyButton){
            i = new Intent(this, HistoryActivity.class);
            startActivity(i);
        }
        else if( v.getId()==R.id.lightButton){
            if(flashState==false) {
                v.setBackgroundResource(R.drawable.ic_flash_off_foreground);
                Toast.makeText(getApplicationContext(), "Flashlight turned on", Toast.LENGTH_SHORT).show();
                mScannerView.setFlash(true);
                flashState = true;
            }else if(flashState) {
                v.setBackgroundResource(R.drawable.ic_flash_on_foreground);
                Toast.makeText(getApplicationContext(), "Flashlight turned off", Toast.LENGTH_SHORT).show();
                mScannerView.setFlash(false);
                flashState = false;
            }
        }

    }
}