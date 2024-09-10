package com.example.musicplace.main;

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
import com.example.musicplace.dto.youtub.YoutubeItem;

import java.util.ArrayList;

// YoutubeRecyclerAdapter.java


public class YoutubeRecyclerAdapter extends RecyclerView.Adapter<YoutubeRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<YoutubeItem> youtubeItems;
    private OnItemClickListener onItemClickListener;

    public YoutubeRecyclerAdapter(Context context, ArrayList<YoutubeItem> youtubeItems) {
        this.context = context;
        this.youtubeItems = youtubeItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 아이템 레이아웃을 inflate
        View view = LayoutInflater.from(context).inflate(R.layout.item_youtube, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 데이터 설정
        YoutubeItem youtubeItem = youtubeItems.get(position);
        holder.titleTextView.setText(youtubeItem.getVidioTitle());

        // 이미지 URL 로드 (Glide 사용)
        Glide.with(context)
                .load(youtubeItem.getImageUrl())
                .into(holder.thumbnailImageView);

        // 클릭 리스너 설정
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return youtubeItems.size();
    }

    // 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;
        TextView titleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }

    // 어댑터 데이터 갱신 메서드
    public void setYoutubeItems(ArrayList<YoutubeItem> youtubeItems) {
        this.youtubeItems = youtubeItems;
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