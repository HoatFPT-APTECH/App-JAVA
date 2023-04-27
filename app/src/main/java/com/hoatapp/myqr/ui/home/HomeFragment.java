package com.hoatapp.myqr.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.hoatapp.myqr.R;
import com.hoatapp.myqr.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    public Button genText;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       //lay du lieu tu layout
       genText= root.findViewById(R.id.gen_text);
        return root;
    }
    public void onResume(){
        super.onResume();
        genText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent textbdn= new Intent();
                textbdn.setClass(v.getContext(),Text_To_QR_Activity.class);
                startActivity(textbdn);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}