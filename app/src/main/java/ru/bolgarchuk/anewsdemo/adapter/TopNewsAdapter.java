package ru.bolgarchuk.anewsdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ru.bolgarchuk.anewsdemo.R;
import ru.bolgarchuk.anewsdemo.model.TopNews;

public class TopNewsAdapter extends RecyclerView.Adapter<TopNewsAdapter.TopNewsHolder> {
    List<TopNews> mNews;
    private Context mContext;

    public TopNewsAdapter(List<TopNews> aNews, final Context context) {
        mNews = aNews;
        mContext = context;
    }

    @Override
    public TopNewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.top_news_row, parent, false);

        return new TopNewsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TopNewsHolder holder, int position) {
        TopNews topNews = mNews.get(position);
        holder.title.setText(topNews.getTitle());

        Glide.with(mContext)
                .load("http://img.anews.com/media/" + topNews.getImg())
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.grey_stub)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        final int count;
        if (mNews == null) {
            count = 0;
        } else {
            count = mNews.size();
        }

        return count;
    }

    public void addItems(final List<TopNews> aNews) {
        if (mNews == null) {
            mNews = new ArrayList<>();
        } else {
            mNews.addAll(aNews);
        }
    }

    public class TopNewsHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public TopNewsHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.top_news_title);
            image = (ImageView) view.findViewById(R.id.top_news_img);
        }
    }
}
