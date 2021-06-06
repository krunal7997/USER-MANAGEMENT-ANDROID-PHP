package com.example.usermanagement.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.usermanagement.API.RetrofitClient;
import com.example.usermanagement.Adapter.UserAdapter;
import com.example.usermanagement.Model.FetchUserResponse;
import com.example.usermanagement.Model.User;
import com.example.usermanagement.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Users extends Fragment
{
    RecyclerView rv_recycler;
    List<User> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_recycler = view.findViewById(R.id.rv_recycler);
        rv_recycler.setHasFixedSize(true);
        rv_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        //retrofit

        Call<FetchUserResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .FetchAllUsers();
        call.enqueue(new Callback<FetchUserResponse>() {
            @Override
            public void onResponse(Call<FetchUserResponse> call, Response<FetchUserResponse> response)
            {
                if(response.isSuccessful())
                {
                    userList = response.body().getUserList();
                    rv_recycler.setAdapter(new UserAdapter(userList, getActivity()));
                }
                else
                {
                    Toast.makeText(getActivity(),response.body().getError(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FetchUserResponse> call, Throwable t)
            {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
