package com.example.musicplace.retrofit;

import com.example.musicplace.dto.SignInSaveDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApiInterface {
    @POST("sign_in")
    Call<SignInSaveDto> saveMember(@Body SignInSaveDto data);

    @GET("sign_in/{member_id}/sameid")
    Call<Boolean> SignInCheckSameId(@Path("member_id")String member_id);
}
