package com.example.musicplace.streaming.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplace.R;
import com.example.musicplace.streaming.dto.RoomDto;

import java.util.ArrayList;

public class ChatRoomRecyclerAdapter extends RecyclerView.Adapter<ChatRoomRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<RoomDto> chatRooms;
    private OnItemClickListener onItemClickListener;

    public ChatRoomRecyclerAdapter(Context context, ArrayList<RoomDto> chatRooms) {
        this.context = context;
        this.chatRooms = chatRooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_streaming_room, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomDto room = chatRooms.get(position);

        // RoomDto의 데이터 설정

        holder.titleTextView.setText(room.getRoomTitle());
        holder.commentTextView.setText(room.getRoomComment());

        // 클릭 리스너 설정
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    // ViewHolder 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView commentTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
        }
    }

    // 어댑터 데이터 갱신 메서드
    public void setChatRooms(ArrayList<RoomDto> chatRooms) {
        this.chatRooms = chatRooms;
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
