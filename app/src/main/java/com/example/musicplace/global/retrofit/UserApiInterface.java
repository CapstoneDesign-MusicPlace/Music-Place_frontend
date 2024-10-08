package com.example.musicplace.global.retrofit;

import com.example.musicplace.follow.dto.FollowResponseDto;
import com.example.musicplace.follow.dto.FollowSaveDto;
import com.example.musicplace.playlist.dto.CommentSaveDto;
import com.example.musicplace.playlist.dto.MusicSaveDto;
import com.example.musicplace.playlist.dto.PLSaveDto;
import com.example.musicplace.playlist.dto.PLUpdateDto;
import com.example.musicplace.playlist.dto.ResponseCommentDto;
import com.example.musicplace.playlist.dto.ResponseMusicDto;
import com.example.musicplace.playlist.dto.ResponsePLDto;
import com.example.musicplace.profile.dto.SignInGetUserDataDto;
import com.example.musicplace.profile.dto.SignInUpdateDto;
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
    @POST("sign_in/save")
    Call<SignInSaveDto> saveMember(@Body SignInSaveDto data);

    // 자체 로그인
    @POST("auth/login")
    Call<LoginResponseDto> login(@Body LoginRequestDto loginRequestDto);

    // 로그아웃
    @POST("auth/logout")
    Call<Void> logout();

    // 동일 아이디 확인
    @GET("sign_in/{member_id}/sameid")
    Call<Boolean> SignInCheckSameId(@Path("member_id") String member_id);

    // 유저 정보 조회
    @GET("sign_in/getuser")
    Call<SignInGetUserDataDto> SignInGetUserData();

    // 유저 정보 삭제
    @DELETE("sign_in/delete")
    Call<Void> SignInDelete();

    // 유저 정보 업데이트
    @PATCH("sign_in/update")
    Call<Void> SignInUpdate(@Body SignInUpdateDto signInUpdateDto);





    // 유튜브 검색
    @GET("youtube/{keyword}")
    Call<List<YoutubeVidioDto>> youtubeSearch(@Path("keyword") String keyword);

    // 유트브 이번주 베스트 음악
    @GET("youtube/playlist")
    Call<List<YoutubeVidioDto>> youtubeGetPlaylist();




    // 플리 등록
    @POST("playList")
    Call<Long> PLSave(@Body PLSaveDto plSaveDto);

    // 플리 수정
    @PATCH("playList/{pl_id}")
    Call<Void> PLUpdate(@Path("pl_id") Long pl_id, @Body PLUpdateDto plUpdateDto);

    // 플리 삭제
    @DELETE("playList/{pl_id}")
    Call<Void> PLDelete(@Path("pl_id") Long pl_id);

    // 모든 플리 조회
    @GET("playList")
    Call<List<ResponsePLDto>> PLFindAll();

    // 공개 플리 조회
    @GET("playList/public")
    Call<List<ResponsePLDto>> PLFindPublic();

    // 플리 개수 조회
    @GET("playList/count")
    Call<Long> PLCount();

    // 다른 사용자 플리 개수 조회
    @GET("playList/otherCount/{otherMemberId}")
    Call<Long> otherPLCount(@Path("otherMemberId") String otherMemberId);

    // 다른 사용자의 공개 플리 조회
    @GET("playList/other/{otherMemberId}")
    Call<List<ResponsePLDto>> getOtherUserPL(@Path("otherMemberId") String otherMemberId);



    // 플리 - 노래 저장
    @POST("playList/music/{PLId}")
    Call<Long> MusicSave(@Path("PLId") Long PLId, @Body MusicSaveDto musicSaveDto);

    // 플리 - 노래 삭제
    @DELETE("playList/music/{PLId}/{MusicId}")
    Call<Boolean> MusicDelete(@Path("PLId") Long PLId, @Path("MusicId") Long MusicId);

    // 플리 - 노래 목록 조회
    @GET("playList/music/{PLId}")
    Call<List<ResponseMusicDto>> MusicFindAll(@Path("PLId") Long PLId);




    // 플리 - 댓글 저장
    @POST("playList/comment/{PLId}")
    Call<Long> CommentSave(@Path("PLId") Long PLId, @Body CommentSaveDto commentSaveDto);

    // 플리 - 댓글 삭제

    // 플리 - 댓글 조회
    @GET("playList/comment/{PLId}")
    Call<List<ResponseCommentDto>> CommentFindAll(@Path("PLId") Long PLId);




    // 팔로우 저장
    @POST("follow")
    Call<Long> FollowSave(@Body FollowSaveDto followSaveDto);

    // 팔로우 삭제
    @DELETE("follow/{follow_id}")
    Call<Long> FollowDelete(@Path("follow_id") Long follow_id, @Body FollowSaveDto followSaveDto);

    // 팔로우 조회
    @GET("follow")
    Call<List<FollowResponseDto>> FollowFindAll();

    // 팔로우 개수 조회
    @GET("follow/count")
    Call<Long> FollowCount();

    // 다른 사용자 팔로우 개수 조회
    @GET("follow/otherCount/{otherMemberId}")
    Call<Long> otherFollowCount(@Path("otherMemberId") String otherMemberId);


}
