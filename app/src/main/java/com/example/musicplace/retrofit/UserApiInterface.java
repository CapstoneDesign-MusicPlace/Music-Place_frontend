package com.example.musicplace.retrofit;

import com.example.musicplace.playlist.dto.ResponsePLDto;
import com.example.musicplace.sign.dto.SignInSaveDto;
import com.example.musicplace.youtubeMusicPlayer.youtubeDto.YoutubeVidioDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApiInterface {
    @POST("sign_in")
    Call<SignInSaveDto> saveMember(@Body SignInSaveDto data);

    @GET("sign_in/{member_id}/sameid")
    Call<Boolean> SignInCheckSameId(@Path("member_id") String member_id);

    @GET("youtube/{keyword}")
    Call<List<YoutubeVidioDto>> youtubeSearch(@Path("keyword") String keyword);

    @POST("playList/{member_id}")
    Call<Long> PLSave(@Path("member_id") String member_id);

    @PATCH("playList/{pl_id}")
    void PLUpdate(@Path("pl_id") String pl_id);

    @DELETE("playList/{pl_id}")
    void PLDelete(@Path("pl_id") String pl_id);

    @GET("playList/{member_id}")
    Call<List<ResponsePLDto>> PLFindAll(@Path("member_id") String member_id);

    @GET("playList/public")
    Call<List<ResponsePLDto>> PLFindPublic();

}
