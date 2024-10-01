package com.example.musicplace.playlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplace.R;
import com.example.musicplace.playlist.dto.CommentSaveDto;

import java.util.ArrayList;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CommentSaveDto> commentItems;
    private OnItemClickListener onItemClickListener;

    // 어댑터 생성자
    public CommentRecyclerAdapter(Context context, ArrayList<CommentSaveDto> commentItems) {
        this.context = context;
        this.commentItems = commentItems;
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
        // CommentSaveDto에서 데이터 가져오기
        CommentSaveDto commentItem = commentItems.get(position);

        // nameTextView 및 commentTextView에 텍스트 설정
        holder.nameTextView.setText(commentItem.getNickName());
        holder.commentTextView.setText(commentItem.getComment());

        // 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentItems.size();
    }

    // ViewHolder 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView commentTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
        }
    }

    // 어댑터 데이터 갱신 메서드
    public void setCommentItems(ArrayList<CommentSaveDto> commentItems) {
        this.commentItems = commentItems;
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
