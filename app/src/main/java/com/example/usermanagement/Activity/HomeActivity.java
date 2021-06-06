package com.example.usermanagement.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.usermanagement.API.RetrofitClient;
import com.example.usermanagement.Fragment.Dashboard;
import com.example.usermanagement.Fragment.Users;
import com.example.usermanagement.Fragment.profile;
import com.example.usermanagement.Model.DeleteResponse;
import com.example.usermanagement.R;
import com.example.usermanagement.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener
{
    BottomNavigationView bottomnav;
    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        bottomnav = findViewById(R.id.bottomnav);
        bottomnav.setOnNavigationItemSelectedListener(this);
        LoadFragment(new Dashboard());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        Fragment fragment = null;
        switch (item.getItemId())
        {
            case R.id.dashboard:
                fragment = new Dashboard();
                break;

            case R.id.profile:
                fragment = new profile();
                break;

            case R.id.users:
                fragment = new Users();
                break;
        }

        if(fragment!=null)
        {
            LoadFragment(fragment);
        }
        return true;
    }

    void LoadFragment(Fragment fragment)
    {
        //to attach fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout,fragment).commit();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.setting_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.logout:
                LogoutUser();
                break;

            case R.id.delete:
                DeleteAccount();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void DeleteAccount()
    {
        Call<DeleteResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .DeleteUserAccount(sharedPrefManager.getUser().getId());

        call.enqueue(new Callback<DeleteResponse>()
        {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response)
            {
                DeleteResponse deleteResponse = response.body();
                if (response.isSuccessful())
                {
                    if (deleteResponse.getError().equals("200"))
                    {
                        LogoutUser();
                        Toast.makeText(HomeActivity.this,deleteResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(HomeActivity.this,deleteResponse.getError(),Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(HomeActivity.this,"Failed...",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t)
            {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void LogoutUser()
    {
        sharedPrefManager.Logout();
        Intent i = new Intent(HomeActivity.this,LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        Toast.makeText(getApplicationContext(),"You Have Been Logged Out",Toast.LENGTH_SHORT).show();
    }
}