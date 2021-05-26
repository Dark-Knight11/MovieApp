package com.sies.movierecomendations.MoviesRecycler;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sies.movierecomendations.MoviesApi.MoviesList;
import com.sies.movierecomendations.R;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    MoviesList res;
    int count = 0;
    public MovieListAdapter (MoviesList res) {
        this.res = res;
    }

    @NonNull
    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load("https://image.tmdb.org/t/p/w500"+ res.getResults().get(position).getBackdrop_path()).into(holder.poster);
        holder.title.setText(res.getResults().get(position).getTitle());
        holder.desc.setText(res.getResults().get(position).getOverview());
        holder.frame.setOnClickListener(v -> {
            if(count%2==0) holder.desc.setVisibility(View.VISIBLE);
            else holder.desc.setVisibility(View.GONE);
            count++;
        });
        holder.date.setText("Release Date: " + res.getResults().get(position).getRelease_date());
    }

    @Override
    public int getItemCount() {
        return res.getResults().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView poster;
        private TextView title;
        private TextView desc;
        private TextView date;
        private RelativeLayout frame;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.releaseDate);
            frame = itemView.findViewById(R.id.frame);
        }

    }
}
