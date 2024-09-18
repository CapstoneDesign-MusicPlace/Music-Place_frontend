package com.example.musicplace.playlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplace.R;
import com.example.musicplace.playlist.dto.PlaylistDto;


import java.util.ArrayList;

public class PlaylistRecyclerAdapter extends RecyclerView.Adapter<PlaylistRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PlaylistDto> playlistItems;
    private OnItemClickListener onItemClickListener;

    // 어댑터 생성자
    public PlaylistRecyclerAdapter(Context context, ArrayList<PlaylistDto> playlistItems) {
        this.context = context;
        this.playlistItems = playlistItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_playlist.xml 레이아웃을 뷰홀더에 연결
        View view = LayoutInflater.from(context).inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // PlaylistDto에서 데이터 가져오기
        PlaylistDto playlistItem = playlistItems.get(position);

        // titleTextView 및 nameTextView에 텍스트 설정
        holder.titleTextView.setText(playlistItem.getPlaylistTitle());
        holder.nameTextView.setText(playlistItem.getCreatorName());

        // 이미지 로드 (URL을 사용하는 경우 Glide 사용)
        if (playlistItem.getImageUrl() != null && !playlistItem.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(playlistItem.getImageUrl())
                    .into(holder.thumbnailImageView);
        } else {
            // 기본 이미지 설정
            holder.thumbnailImageView.setImageResource(R.drawable.playlistimg);
        }

        // 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlistItems.size();
    }

    // ViewHolder 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;
        TextView titleTextView;
        TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }

    // 어댑터 데이터 갱신 메서드
    public void setPlaylistItems(ArrayList<PlaylistDto> playlistItems) {
        this.playlistItems = playlistItems;
        notifyDataSetChanged();
    }

    // 아이템 클릭 리스너 인터페이스
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // 아이템 클릭 리스너 설정 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}