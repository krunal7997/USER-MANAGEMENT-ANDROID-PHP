package com.example.usermanagement.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.usermanagement.R;
import com.example.usermanagement.SharedPrefManager;

public class Dashboard extends Fragment
{
    TextView txt_name,txt_email;
    SharedPrefManager sharedPrefManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard,container,false);
        txt_name = view.findViewById(R.id.txt_name);
        txt_email = view.findViewById(R.id.txt_email);

        sharedPrefManager = new SharedPrefManager(getActivity());

        String username="Hey !"+sharedPrefManager.getUser().getUsername();
        txt_name.setText(username);
        txt_email.setText(sharedPrefManager.getUser().getEmail());
        return view;


    }
}