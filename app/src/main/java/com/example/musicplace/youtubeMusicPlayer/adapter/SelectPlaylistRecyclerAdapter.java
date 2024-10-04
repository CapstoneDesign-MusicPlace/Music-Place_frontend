package com.example.musicplace.youtubeMusicPlayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplace.R;
import com.example.musicplace.youtubeMusicPlayer.youtubeDto.LoadPlaylistDto;

import java.util.ArrayList;

public class SelectPlaylistRecyclerAdapter extends RecyclerView.Adapter<SelectPlaylistRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<LoadPlaylistDto> playlistItems;
    private OnItemClickListener onItemClickListener;
    private int selectedPosition = -1;  // 선택된 아이템의 인덱스를 추적하는 변수

    public SelectPlaylistRecyclerAdapter(Context context, ArrayList<LoadPlaylistDto> playlistItems) {
        this.context = context;
        this.playlistItems = playlistItems;
    }

    // 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;
        TextView titleTextView;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 아이템 레이아웃을 inflate
        View view = LayoutInflater.from(context).inflate(R.layout.item_check_youtube, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 데이터 설정
        LoadPlaylistDto playlistItem = playlistItems.get(position);
        holder.titleTextView.setText(playlistItem.getPlaylistTitle());

        // 이미지 URL 로드 (Glide 사용)
        Glide.with(context)
                .load(playlistItem.getImageUrl())
                .placeholder(R.drawable.playlistimg) // 기본 이미지 설정
                .into(holder.thumbnailImageView);

        // 체크박스 상태 설정 (선택된 아이템만 체크)
        holder.checkBox.setChecked(position == selectedPosition);

        // 체크박스 클릭 리스너 설정
        holder.checkBox.setOnClickListener(v -> {
            // 선택된 포지션 업데이트
            if (selectedPosition != position) {
                selectedPosition = position;
                notifyDataSetChanged();  // 전체 아이템을 다시 그림 (UI 갱신)

                if (onItemClickListener != null) {
                    onItemClickListener.onItemCheck(position, true);
                }
            } else {
                // 이미 선택된 아이템 클릭 시 체크 해제
                selectedPosition = -1;
                notifyDataSetChanged();

                if (onItemClickListener != null) {
                    onItemClickListener.onItemCheck(position, false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlistItems.size();
    }

    // 어댑터 데이터 갱신 메서드
    public void setPlaylistItems(ArrayList<LoadPlaylistDto> playlistItems) {
        this.playlistItems = playlistItems;
        notifyDataSetChanged();  // 데이터 변경 후 UI 갱신
    }

    // 아이템 체크 상태를 리스너로 전달하는 메서드
    public interface OnItemClickListener {
        void onItemCheck(int position, boolean isChecked);
    }

    // 아이템 클릭 리스너 설정 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // 선택된 플레이리스트 ID를 반환하는 메서드 추가
    public Long getSelectedPlaylistId() {
        if (selectedPosition != -1) {
            return playlistItems.get(selectedPosition).getPlaylistId();
        }
        return null;  // 아무것도 선택되지 않은 경우
    }
}
