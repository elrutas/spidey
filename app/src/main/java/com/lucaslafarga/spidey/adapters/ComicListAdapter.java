package com.lucaslafarga.spidey.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lucaslafarga.spidey.ui.MainActivity;
import com.lucaslafarga.spidey.R;
import com.lucaslafarga.spidey.models.Comic;

import java.util.ArrayList;
import java.util.List;

public class ComicListAdapter extends RecyclerView.Adapter<ComicListAdapter.ViewHolder> {
    private final List<Comic> comicList = new ArrayList<>();
    private Context mContext;
    private MainActivityInterface activityInterface;

    public ComicListAdapter(MainActivity activity) {
        this.activityInterface = activity;
    }

    public void addComics(List<Comic> list) {
        comicList.addAll(list);
        notifyItemInserted(comicList.size() - list.size());
    }

    @Override
    public ComicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View itemView = inflater.inflate(R.layout.list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Comic comic = comicList.get(holder.getAdapterPosition());
        holder.comicTitle.setText(comic.title);
        Glide.with(mContext)
                .load(comic.thumbnail.path + "." + comic.thumbnail.extension)
                .centerCrop()
                .into(holder.comicThumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityInterface.itemClicked(comicList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public interface MainActivityInterface {
        void itemClicked(Comic comic);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView comicThumbnail;
        public TextView comicTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            comicThumbnail = (ImageView) itemView.findViewById(R.id.comic_thumbnail);
            comicTitle = (TextView) itemView.findViewById(R.id.comic_title);
        }
    }
}
