package com.hoatapp.myqr.ui.scan;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.hoatapp.myqr.R;
import com.hoatapp.myqr.qrscanner.Activity.HistoryActivity;
import com.hoatapp.myqr.qrscanner.Entity.History;
import com.hoatapp.myqr.qrscanner.SQLite.ORM.HistoryORM;

import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import com.hoatapp.myqr.qrscanner.Activity.MainQRSCanerActivity;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class ScanFragment extends Fragment  implements ActivityCompat.OnRequestPermissionsResultCallback, ZXingScannerView.ResultHandler {
    ImageView flashImageView;
    Button btnHistory;
    Button btnFlash;
    //Variables
    Intent i;
    HistoryORM h = new HistoryORM();
    private ZXingScannerView mScannerView;
    private boolean flashState = false;

    private MainQRSCanerActivity mainQRSCanerActivity;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScanFragment() {
        // Required empty public constructor
        this.mainQRSCanerActivity= new MainQRSCanerActivity();

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanFragment newInstance(String param1, String param2) {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
          View view=inflater.inflate(R.layout.fragment_scan,container,false);
        ButterKnife.bind(this,view);


        ViewGroup contentFrame = (ViewGroup) view.findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(requireContext());
      // set mscanner
        mScannerView.setLaserColor(ContextCompat.getColor(requireContext(),R.color.secondColor));
        mScannerView.setAutoFocus(true);
        mScannerView.setBorderColor(ContextCompat.getColor(requireContext(),R.color.secondColor));

        // gan mscannerview cho frame
        contentFrame.addView(mScannerView);
           // gan du lieu cho btn flash and history
        btnFlash= view.findViewById(R.id.lightButton);
        btnHistory=view.findViewById(R.id.historyButton);
        return view;

    }
// qr scan
void mainActivityOnClickEvents(View v) {
    if(v.getId()==R.id.historyButton){
        i = new Intent(requireContext(), HistoryActivity.class);
        startActivity(i);
    }
    else if( v.getId()==R.id.lightButton){
        if(flashState==false) {
            v.setBackgroundResource(R.drawable.ic_flash_off);
            Toast.makeText(requireContext(), "Flashlight turned on", Toast.LENGTH_SHORT).show();
            mScannerView.setFlash(true);
            flashState = true;
        }else if(flashState) {
            v.setBackgroundResource(R.drawable.ic_flash_on);
            Toast.makeText(requireContext(), "Flashlight turned off", Toast.LENGTH_SHORT).show();
            mScannerView.setFlash(false);
            flashState = false;
        }
    }

}
@Override
public void onResume() {
    super.onResume();
    mScannerView.setResultHandler(ScanFragment.this);
    mScannerView.startCamera();

    btnHistory.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         mainActivityOnClickEvents(v);
        }
    });
    btnFlash.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mainActivityOnClickEvents(v);
        }
    });

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
        h.add(requireContext(), history);

        // show custom alert dialog
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView text = dialog.findViewById(R.id.someText);
        text.setText(rawResult.getText());
        ImageView img = dialog.findViewById(R.id.imgOfDialog);
        img.setImageResource(R.drawable.ic_done_gr);

        Button webSearch = dialog.findViewById(R.id.searchButton);
        Button copy = dialog.findViewById(R.id.copyButton);
        Button share = dialog.findViewById(R.id.shareButton);
        webSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                if (Patterns.WEB_URL.matcher(rawResult.getText()).matches()) {
                    url = rawResult.getText();
                } else {
                    url = "http://www.google.com/#q=" + rawResult.getText();
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                dialog.dismiss();
                mScannerView.resumeCameraPreview(ScanFragment.this);
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", rawResult.getText());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
                mScannerView.resumeCameraPreview(ScanFragment.this);
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
                mScannerView.resumeCameraPreview(ScanFragment.this);
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mScannerView.resumeCameraPreview(ScanFragment.this);
            }
        });
        dialog.show();
    }




}