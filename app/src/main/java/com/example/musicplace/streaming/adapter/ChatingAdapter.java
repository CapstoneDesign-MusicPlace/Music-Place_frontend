package com.example.musicplace.streaming.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplace.R;
import com.example.musicplace.streaming.dto.ReqestChatDto;
import com.example.musicplace.streaming.dto.ResponseChatDto;


import java.util.ArrayList;

public class ChatingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<ResponseChatDto> myDataList;

    public ChatingAdapter(ArrayList<ResponseChatDto> dataList) {
        this.myDataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == ViewType.CENTER_JOIN) {
            view = inflater.inflate(R.layout.center_join, parent, false);
            return new CenterViewHolder(view);
        } else if (viewType == ViewType.LEFT_CHAT) {
            view = inflater.inflate(R.layout.left_chat, parent, false);
            return new LeftViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.right_chat, parent, false);
            return new RightViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ResponseChatDto chatData = myDataList.get(position);

        if (viewHolder instanceof CenterViewHolder) {
            ((CenterViewHolder) viewHolder).content.setText(chatData.getMessage());
        } else if (viewHolder instanceof LeftViewHolder) {
            ((LeftViewHolder) viewHolder).name.setText(chatData.getUserNickname());
            ((LeftViewHolder) viewHolder).content.setText(chatData.getMessage());
        } else if (viewHolder instanceof RightViewHolder) {
            ((RightViewHolder) viewHolder).content.setText(chatData.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return myDataList.get(position).getViewType();
    }

    public class CenterViewHolder extends RecyclerView.ViewHolder {
        TextView content;

        CenterViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView name;

        LeftViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            name = itemView.findViewById(R.id.name);
        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder {
        TextView content;

        RightViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }
}
