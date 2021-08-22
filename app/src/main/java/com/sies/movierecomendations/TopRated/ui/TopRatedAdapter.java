package com.sies.movierecomendations.TopRated.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sies.movierecomendations.MovieDetails.ui.MovieDetails;
import com.sies.movierecomendations.network.MoviesList;
import com.sies.movierecomendations.R;

public class TopRatedAdapter extends RecyclerView.Adapter<TopRatedAdapter.ViewHolder> {

    MoviesList res;
    int i;
    private Context context;
    public TopRatedAdapter (MoviesList res, int i) {
        this.res = res;
        this.i = i;
    }

    @NonNull
    @Override
    public TopRatedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_rated_card, parent, false);
        context = parent.getContext();
        return new TopRatedAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TopRatedAdapter.ViewHolder holder, int position) {
        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + res.getResults().get(position).getPoster_path()).into(holder.poster);
        if(i==1) {
            holder.title.setText(res.getResults().get(position).getTitle());
            holder.date.setText(res.getResults().get(position).getRelease_date());
        }
        else {
            holder.title.setText(res.getResults().get(position).getName());
            holder.date.setText(res.getResults().get(position).getFirst_air_date());
        }
        holder.votes.setText(Float.toString(res.getResults().get(position).getVote_average()));
        holder.rating.setRating(res.getResults().get(position).getVote_average()/2);

        if(position<9) holder.rank.setText("0" + (position + 1));
        else holder.rank.setText(Integer.toString(position + 1));
        holder.top_rated_card.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetails.class);
            if (i==1) {
                intent.putExtra("title", res.getResults().get(position).getTitle());
                intent.putExtra("release-date", res.getResults().get(position).getRelease_date());
            }
            else {
                intent.putExtra("title", res.getResults().get(position).getName());
                intent.putExtra("release-date", res.getResults().get(position).getFirst_air_date());
            }
            intent.putExtra("backdrop-path", res.getResults().get(position).getBackdrop_path());
            intent.putExtra("rating", res.getResults().get(position).getVote_average());
            intent.putExtra("id", res.getResults().get(position).getId());
            intent.putExtra("overview", res.getResults().get(position).getOverview());
            intent.putExtra("flag", i);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return res.getResults().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView title, votes, date, rank;
        private final ImageView poster;
        private final RatingBar rating;
        private final ConstraintLayout top_rated_card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            rating = itemView.findViewById(R.id.rating);
            votes = itemView.findViewById(R.id.votes);
            poster = itemView.findViewById(R.id.poster);
            date = itemView.findViewById(R.id.release);
            rank = itemView.findViewById(R.id.rank);
            top_rated_card = itemView.findViewById(R.id.top_rated_card);
        }
    }
}