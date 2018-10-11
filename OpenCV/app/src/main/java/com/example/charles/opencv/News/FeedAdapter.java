package com.example.charles.opencv.News;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.charles.opencv.News.Model.RSS;
import com.example.charles.opencv.R;

/***
 * Recycler view feed holder
 */
class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public TextView txtTitle, txtPubDate, txtContent;
    private ItemClickListener itemClickListener;

    public FeedViewHolder(View itemView) {
        super(itemView);

        txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        txtPubDate = (TextView) itemView.findViewById(R.id.txtPubDate);
        txtContent = (TextView) itemView.findViewById(R.id.txtContent);

        //Set Event
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(), false);

    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), true);
        return true;
    }
}

/**
 * Recycler view feed adapter
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {

    private RSS rss;
    private Context mContext;
    private LayoutInflater inflater;

    public FeedAdapter(RSS rss, Context mContext) {
        this.rss = rss;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.news_row, parent, false);
        return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {

        holder.txtTitle.setText(rss.getItems().get(position).getTitle());
        holder.txtPubDate.setText(rss.getItems().get(position).getPubDate());
        holder.txtContent.setText(Html.fromHtml(rss.getItems().get(position).getContent()));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(rss.getItems().get(position).getLink()));
                    mContext.startActivity(browserIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rss.items.size();
    }
}