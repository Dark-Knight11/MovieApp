package com.sies.movierecomendations.Home.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sies.movierecomendations.MovieDetails.ui.MovieDetails;
import com.sies.movierecomendations.R;
import com.sies.movierecomendations.network.Results;

import java.util.List;

public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.ViewHolder> {

    private Context context;
    List<Results> res;
    private final String[] data = {"1st string", "2nd string", "3rd string", "4th string", "5th string", "6th string", "7th string", "8th string", "9th string", "10th string", "11th string"};

    public PopularMoviesAdapter(List<Results> res) { this.res = res; }

    @NonNull
    @Override
    public PopularMoviesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.popular_and_trending_movies_card, parent, false);
        return new PopularMoviesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularMoviesAdapter.ViewHolder holder, int position) {
        Glide.with(context).load("https://image.tmdb.org/t/p/w500"+ res.get(position).getPoster_path()).into(holder.poster);
//        holder.movieName.setText(res.getResults().get(position).getTitle());
        holder.card.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetails.class);
            intent.putExtra("backdrop-path", res.get(position).getBackdrop_path());
            intent.putExtra("title", res.get(position).getTitle());
            intent.putExtra("release-date", res.get(position).getRelease_date());
            intent.putExtra("rating", res.get(position).getVote_average());
            intent.putExtra("overview", res.get(position).getOverview());
            intent.putExtra("id", res.get(position).getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return res.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView poster;
        private final RelativeLayout card;
        @SuppressLint("NewApi")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            card = itemView.findViewById(R.id.movieCard);
            poster.setClipToOutline(true);
        }
    }
}
