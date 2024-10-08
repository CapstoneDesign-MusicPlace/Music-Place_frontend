package com.example.musicplace.playlist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplace.R;
import com.example.musicplace.playlist.dto.ResponseCommentDto;

import java.util.ArrayList;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ResponseCommentDto> commentItems;

    public CommentRecyclerAdapter(Context context, ArrayList<ResponseCommentDto> commentItems) {
        this.context = context;
        this.commentItems = commentItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_comment 레이아웃을 inflate하여 ViewHolder 생성
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // commentItems 리스트에서 해당 position의 데이터를 가져와서 ViewHolder에 설정
        ResponseCommentDto commentItem = commentItems.get(position);
        holder.nameTextView.setText(commentItem.getNickName());
        holder.commentTextView.setText(commentItem.getUserComment());
    }

    @Override
    public int getItemCount() {
        return commentItems.size();
    }

    // ViewHolder 클래스: item_comment.xml의 TextView들을 참조
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
    public void setCommentItems(ArrayList<ResponseCommentDto> commentItems) {
        this.commentItems = commentItems;
        notifyDataSetChanged();  // 데이터 변경 후 UI 갱신
    }

    public void addComment(ResponseCommentDto comment) {
        this.commentItems.add(comment); // 댓글 목록에 추가
        notifyItemInserted(commentItems.size() - 1); // 새로운 아이템 추가를 알림
    }
}
