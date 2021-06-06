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
import com.example.usermanagement.Model.LoginResponse;
import com.example.usermanagement.Model.RegisterResponse;
import com.example.usermanagement.R;
import com.example.usermanagement.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity
{
    EditText edt_email,edt_password;
    Button btn_login;
    TextView txt_signup;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //hide actionbar
        getSupportActionBar().hide();

        //hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Allocatememory();
        setEvents();
    }

    private void setEvents()
    {
        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UserLogin();
            }
        });

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent signup = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(signup);
            }
        });
    }

    private void UserLogin()
    {
        String email = edt_email.getText().toString();
        String password = edt_password.getText().toString();

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


        Call<LoginResponse> call = RetrofitClient
                .getInstance().getApi().login(email,password);
        call.enqueue(new Callback<LoginResponse>()
        {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
            {
                LoginResponse loginResponse = response.body();
                if(response.isSuccessful())
                {
                    if (loginResponse.getError().equals("200"))
                    {
                        sharedPrefManager.SaveUser(loginResponse.getUser());
                        Intent login = new Intent(getApplicationContext(),HomeActivity.class);
                        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(login);
                        Toast.makeText(getApplicationContext(),loginResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),loginResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),loginResponse.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t)
            {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (sharedPrefManager.isLoggedIn())
        {
            Intent login = new Intent(getApplicationContext(),HomeActivity.class);
            login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(login);
        }
    }

    private void Allocatememory()
    {
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
        txt_signup = findViewById(R.id.txt_signup);
    }
}