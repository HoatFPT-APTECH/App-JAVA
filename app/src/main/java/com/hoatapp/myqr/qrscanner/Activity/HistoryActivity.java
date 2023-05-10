package com.hoatapp.myqr.qrscanner.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hoatapp.myqr.R;
import com.hoatapp.myqr.qrscanner.Adapter.HistoryAdapter;
import com.hoatapp.myqr.qrscanner.Entity.History;
import com.hoatapp.myqr.qrscanner.SQLite.ORM.HistoryORM;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;



public class HistoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    // Init ui elements
//    @BindView(R.id.historySwipeRefreshLayout)
    SwipeRefreshLayout historySwipeRefreshLayout;
//    @BindView(R.id.historyRecyclerView)
    RecyclerView historyRecyclerView;

    // Variables
    HistoryORM h = new HistoryORM();
    List<History> historyList;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
       ButterKnife.bind(this);
         historySwipeRefreshLayout=findViewById(R.id.historySwipeRefreshLayout);
         historyRecyclerView=findViewById(R.id.historyRecyclerView);
        historySwipeRefreshLayout.setOnRefreshListener(this);
        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        historyRecyclerView.setLayoutManager(layoutManager);
        getData();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Отменяем анимацию обновления
                getData();
                historySwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void getData() {
        historyList = h.getAll(getApplicationContext());
        adapter = new HistoryAdapter(historyList);
        historyRecyclerView.setAdapter(adapter);
    }

    @OnClick()
   public void historyOnClickEvents(View v) {
        if(v.getId()==R.id.backButton)finish();
        else if (v.getId()==R.id.clearButton) {
            h.clearAll(getApplicationContext());
            Toast.makeText(getApplicationContext(), "The history is cleared, please refresh this page!", Toast.LENGTH_LONG).show();
        }
    }

}
