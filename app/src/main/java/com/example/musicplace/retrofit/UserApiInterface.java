package com.example.musicplace.retrofit;

import com.example.musicplace.playlist.dto.ResponsePLDto;
import com.example.musicplace.sign.dto.LoginRequestDto;
import com.example.musicplace.sign.dto.LoginResponseDto;
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

    @POST("/auth/google")
    Call<LoginResponseDto> getOAuth2Jwt(@Body String idToken);


    // 회원가입
    @POST("sign_in")
    Call<SignInSaveDto> saveMember(@Body SignInSaveDto data);

    // 자체 로그인
    @POST("auth/login")
    Call<LoginResponseDto> login(@Body LoginRequestDto loginRequestDto);

    // 로그아웃
    @POST("auth/logout")
    Call<Void> logout(@Body SignInSaveDto data);

    // 동일 아이디 확인
    @GET("sign_in/{member_id}/sameid")
    Call<Boolean> SignInCheckSameId(@Path("member_id") String member_id);

    // 유튜브 검색
    @GET("youtube/{keyword}")
    Call<List<YoutubeVidioDto>> youtubeSearch(@Path("keyword") String keyword);

    // 플리 등록
    @POST("playList")
    Call<Long> PLSave();

    // 플리 수정
    @PATCH("playList/{pl_id}")
    void PLUpdate(@Path("pl_id") String pl_id);

    // 플리 삭제
    @DELETE("playList/{pl_id}")
    void PLDelete(@Path("pl_id") String pl_id);

    // 모든 플리 조회
    @GET("playList")
    Call<List<ResponsePLDto>> PLFindAll();

    // 공개 플리 조회
    @GET("playList/public")
    Call<List<ResponsePLDto>> PLFindPublic();

}
