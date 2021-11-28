package com.example.newsproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<Articles> mExampleList = new ArrayList<Articles>() ;
    private OnItemClickListener mlistener;
    private Context context;

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_card, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mlistener);
        context = parent.getContext();
        return evh;
    }

    public ExampleAdapter(ArrayList<Articles> exampleList){
        mExampleList = exampleList;

    }

    @Override
    public void onBindViewHolder( ExampleAdapter.ExampleViewHolder holder, int position) {
        holder.newsDescription.setText(mExampleList.get(position).getDescription());
        holder.newsTitle.setText(mExampleList.get(position).getTitle());
        Glide.with(context)
                .load(mExampleList.get(position).getUrlToImage())
                .placeholder(R.drawable.download)
                .error(R.drawable.error)
                .into(holder.newsImage);
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        TextView newsTitle, newsDescription;
        ImageView newsImage;
        ImageButton share, delete;

        public ExampleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsDescription = itemView.findViewById(R.id.newsDescription);
            newsImage = itemView.findViewById(R.id.newsImage);
            share = itemView.findViewById(R.id.share);
            delete = itemView.findViewById(R.id.delete);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onItemDelete(position);
                        }
                    }
                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        int position = getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            listener.onItemShare(position);
                        }
                    }
                }
            });





        }
    }



    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemDelete(int position);
        void onItemShare(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

}
