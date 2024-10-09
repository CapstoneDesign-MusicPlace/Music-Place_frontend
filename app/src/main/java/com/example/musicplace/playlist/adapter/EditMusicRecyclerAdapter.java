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
import com.example.musicplace.playlist.dto.EditMusicDto;

import java.util.ArrayList;
import java.util.List;

public class EditMusicRecyclerAdapter extends RecyclerView.Adapter<EditMusicRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<EditMusicDto> editMusicItems;
    private OnItemClickListener onItemClickListener;
    private List<Long> selectedMusicIds = new ArrayList<>(); // 선택된 music_id를 저장할 리스트

    public EditMusicRecyclerAdapter(Context context, ArrayList<EditMusicDto> editMusicItems) {
        this.context = context;
        this.editMusicItems = editMusicItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_check_youtube, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EditMusicDto editMusicItem = editMusicItems.get(position);
        holder.titleTextView.setText(editMusicItem.getVidioTitle());

        // Glide를 사용하여 이미지 로드
        Glide.with(context)
                .load(editMusicItem.getImageUrl())
                .placeholder(R.drawable.playlistimg) // 기본 이미지 설정
                .into(holder.thumbnailImageView);

        // 체크박스 상태 설정
        holder.checkBox.setChecked(selectedMusicIds.contains(editMusicItem.getMusic_id()));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedMusicIds.add(editMusicItem.getMusic_id()); // 체크되면 리스트에 추가
            } else {
                selectedMusicIds.remove(editMusicItem.getMusic_id()); // 체크 해제되면 리스트에서 제거
            }
            if (onItemClickListener != null) {
                onItemClickListener.onItemCheck(position, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return editMusicItems.size();
    }

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

    public List<Long> getSelectedMusicIds() {
        return selectedMusicIds; // 선택된 music_id 리스트를 반환
    }

    public void setEditMusicItems(ArrayList<EditMusicDto> editMusicItems) {
        this.editMusicItems = editMusicItems;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemCheck(int position, boolean isChecked);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
