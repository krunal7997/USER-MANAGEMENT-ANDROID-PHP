package com.example.usermanagement.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usermanagement.API.RetrofitClient;
import com.example.usermanagement.Model.LoginResponse;
import com.example.usermanagement.Model.UpdatePasswordResponse;
import com.example.usermanagement.R;
import com.example.usermanagement.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class profile extends Fragment
{
    EditText edt_name,edt_email;
    EditText edt_currentpassword,edt_newpassword;
    Button btn_UpdateAccount;
    Button btn_UpdatePassword;
    SharedPrefManager sharedPrefManager;
    int userId;
    String userEmail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        //for update Account
        edt_name = view.findViewById(R.id.edt_name);
        edt_email = view.findViewById(R.id.edt_email);
        btn_UpdateAccount = view.findViewById(R.id.btn_UpdateAccount);

        edt_currentpassword = view.findViewById(R.id.edt_currentpassword);
        edt_newpassword = view.findViewById(R.id.edt_newpassword);
        btn_UpdatePassword = view.findViewById(R.id.btn_UpdatePassword);

        sharedPrefManager = new SharedPrefManager(getActivity());
        userId = sharedPrefManager.getUser().getId();
        userEmail = sharedPrefManager.getUser().getEmail();

        btn_UpdateAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                updateUserAccount();
            }
        });

        btn_UpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserPassword();
            }
        });

        return view;
    }

    private void UpdateUserPassword()
    {
        String currentpass = edt_currentpassword.getText().toString().trim();
        String newpass = edt_newpassword.getText().toString().trim();

        if (currentpass.isEmpty())
        {
            edt_currentpassword.setError("Enter Current Password");
            edt_currentpassword.requestFocus();
            return;
        }
        if (currentpass.length()<8)
        {
            edt_currentpassword.setError("Enter 8 digit Password");
            edt_currentpassword.requestFocus();
            return;
        }
        if (newpass.isEmpty())
        {
            edt_newpassword.setError("Enter Current Password");
            edt_newpassword.requestFocus();
            return;
        }
        if (newpass.length()<8)
        {
            edt_newpassword.setError("Enter 8 digit Password");
            edt_newpassword.requestFocus();
            return;
        }

        Call<UpdatePasswordResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .UpdateUserPassword(userEmail,currentpass,newpass);

        call.enqueue(new Callback<UpdatePasswordResponse>() {
            @Override
            public void onResponse(Call<UpdatePasswordResponse> call, Response<UpdatePasswordResponse> response) {

                UpdatePasswordResponse updatePasswordResponse = response.body();

                if (response.isSuccessful())
                {
                    if(updatePasswordResponse.getError().equals("200"))
                    {
                        Toast.makeText(getContext(),updatePasswordResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdatePasswordResponse> call, Throwable t)
            {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateUserAccount()
    {

        String username=edt_name.getText().toString().trim();
        String email=edt_email.getText().toString().trim();


        if(username.isEmpty()){
            edt_name.setError("Please enter user name");
            edt_name.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edt_email.requestFocus();
            edt_email.setError("Please enter correct email");
            return;
        }

        Call<LoginResponse> call= RetrofitClient
                .getInstance()
                .getApi()
                .updateUserAccount(userId,username,email);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {


                LoginResponse updateReponse=response.body();
                if(response.isSuccessful()){

                    if(updateReponse.getError().equals("200")){

                        sharedPrefManager.SaveUser(updateReponse.getUser());
                        Toast.makeText(getActivity(), updateReponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getActivity(), updateReponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                else{
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

}