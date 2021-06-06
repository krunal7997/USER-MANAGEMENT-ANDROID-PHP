package com.example.usermanagement.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usermanagement.API.RetrofitClient;
import com.example.usermanagement.Model.RegisterResponse;
import com.example.usermanagement.R;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity
{
    EditText edt_name,edt_email,edt_password;
    Button btn_register;
    TextView txt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        //hide actionbar
        getSupportActionBar().hide();

        //hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Allocatememory();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) 
            {
                registeruser();
            }
        });

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent login = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(login);
                Toast.makeText(getApplicationContext(),"LOGIN",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void registeruser()
    {
        String username = edt_name.getText().toString();
        String email = edt_email.getText().toString();
        String password = edt_password.getText().toString();

        if(username.isEmpty())
        {
            edt_name.requestFocus();
            edt_name.setError("Please Enter Your Name");
        }
        if(email.isEmpty())
        {
            edt_email.requestFocus();
            edt_email.setError("Please Enter Your Email address");
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            edt_email.requestFocus();
            edt_email.setError("Please Enter Your Correct Email address");
            return;
        }
        if(password.isEmpty())
        {
            edt_password.requestFocus();
            edt_password.setError("Please Enter Your Password");
        }
        if(password.length()<8)
        {
            edt_password.requestFocus();
            edt_password.setError("Please Enter Your Password");
            return;
        }

        Call<RegisterResponse> call = RetrofitClient
                .getInstance().getApi().register(username,email,password);
        call.enqueue(new Callback<RegisterResponse>()
        {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response)
            {
                RegisterResponse registerResponse = response.body();
                if(response.isSuccessful())
                {
                    Intent login = new Intent(getApplicationContext(),LoginActivity.class);
                    login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(login);
                    finish();
                    Toast.makeText(getApplicationContext(),registerResponse.getMessage(),Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),registerResponse.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t)
            {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void Allocatememory()
    {
        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        btn_register = findViewById(R.id.btn_register);
        txt_login = findViewById(R.id.txt_login);
    }
}