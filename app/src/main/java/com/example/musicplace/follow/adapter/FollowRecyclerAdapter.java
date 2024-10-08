package com.example.musicplace.follow.adapter;

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
import com.example.musicplace.follow.dto.FollowResponseDto;

import java.util.ArrayList;

public class FollowRecyclerAdapter extends RecyclerView.Adapter<FollowRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FollowResponseDto> followItems;
    private OnItemClickListener onItemClickListener;

    // 어댑터 생성자
    public FollowRecyclerAdapter(Context context, ArrayList<FollowResponseDto> followItems) {
        this.context = context;
        this.followItems = followItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // follow_item.xml 레이아웃을 뷰홀더에 연결
        View view = LayoutInflater.from(context).inflate(R.layout.item_follow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // FollowResponseDto에서 데이터 가져오기
        FollowResponseDto followItem = followItems.get(position);

        // nickname을 nameTextView에 설정
        holder.nameTextView.setText(followItem.getNickname());

        // 프로필 이미지 로드 (URL을 사용하는 경우 Glide 사용)
        if (followItem.getProfile_img_url() != null && !followItem.getProfile_img_url().isEmpty()) {
            Glide.with(context)
                    .load(followItem.getProfile_img_url())
                    .into(holder.profileImageView);
        } else {
            // 기본 이미지 설정
            holder.profileImageView.setImageResource(android.R.drawable.ic_menu_gallery);
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
        return followItems.size();
    }

    // ViewHolder 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }

    // 어댑터 데이터 갱신 메서드
    public void setFollowItems(ArrayList<FollowResponseDto> followItems) {
        this.followItems = followItems;
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
