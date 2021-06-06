package com.example.usermanagement.API;

import com.example.usermanagement.Model.DeleteResponse;
import com.example.usermanagement.Model.FetchUserResponse;
import com.example.usermanagement.Model.LoginResponse;
import com.example.usermanagement.Model.RegisterResponse;
import com.example.usermanagement.Model.UpdatePasswordResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api
{
    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterResponse> register(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponse> login(
             @Field("email") String email,
            @Field("password") String password
    );

    @GET("fetchusers.php")
    Call<FetchUserResponse> FetchAllUsers();

    @FormUrlEncoded
    @POST("updateuser.php")
    Call<LoginResponse> updateUserAccount(
            @Field("id") int userid,
            @Field("username") String userName,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("updatepassword.php")
    Call<UpdatePasswordResponse> UpdateUserPassword(
            @Field("email") String email,
            @Field("current") String CurrentPassword,
            @Field("new") String NewPassword
    );


    @FormUrlEncoded
    @POST("deleteaccount.php")
    Call<DeleteResponse> DeleteUserAccount
            (
                @Field("id") int userId
            );






}
