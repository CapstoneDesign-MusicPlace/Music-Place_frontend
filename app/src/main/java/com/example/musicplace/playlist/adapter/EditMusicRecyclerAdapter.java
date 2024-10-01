package com.example.musicplace.playlist.adapter;

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
import com.example.musicplace.youtubeMusicPlayer.youtubeDto.YoutubeItem;

import java.util.ArrayList;

public class EditMusicRecyclerAdapter extends RecyclerView.Adapter<EditMusicRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<YoutubeItem> youtubeItems;
    private OnItemClickListener onItemClickListener;

    public EditMusicRecyclerAdapter(Context context, ArrayList<YoutubeItem> youtubeItems) {
        this.context = context;
        this.youtubeItems = youtubeItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 아이템 레이아웃을 inflate
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkyoutube, parent, false);
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
                .placeholder(R.drawable.playlistimg)
                .into(holder.thumbnailImageView);

        // 체크박스 클릭 리스너 설정
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemCheck(position, isChecked);
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
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    // 어댑터 데이터 갱신 메서드
    public void setYoutubeItems(ArrayList<YoutubeItem> youtubeItems) {
        this.youtubeItems = youtubeItems;
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
}
