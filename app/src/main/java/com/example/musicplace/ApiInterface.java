package com.example.musicplace;

import com.example.musicplace.dto.SignInSaveDto;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {
    @POST("sign-in")
    Call<SignInSaveDto> saveMember(@Body HashMap<String, Object> data);

    @GET("sign-in/{member_id}/sameid")
    Call<Boolean> SignInCheckSameId(@Path("member_id")String member_id);
}
