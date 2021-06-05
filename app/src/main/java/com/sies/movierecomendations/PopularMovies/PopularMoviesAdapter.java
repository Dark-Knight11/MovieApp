package com.sies.movierecomendations.PopularMovies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sies.movierecomendations.MoviesApi.MoviesList;
import com.sies.movierecomendations.R;

public class PopularMoviesAdapter extends RecyclerView.Adapter<PopularMoviesAdapter.ViewHolder> {

    private Context context;
    MoviesList res;

    public PopularMoviesAdapter(MoviesList res) { this.res = res; }

    @NonNull
    @Override
    public PopularMoviesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.popular_movies_list, parent, false);
        return new PopularMoviesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularMoviesAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load("https://image.tmdb.org/t/p/w500"+ res.getResults().get(position).getPoster_path()).into(holder.poster);
        holder.movieName.setText(res.getResults().get(position).getTitle());
        holder.card.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieScreen.class);
            intent.putExtra("backdrop-path", res.getResults().get(position).getBackdrop_path());
            intent.putExtra("title", res.getResults().get(position).getTitle());
            intent.putExtra("release-date", res.getResults().get(position).getRelease_date());
            intent.putExtra("rating", res.getResults().get(position).getVote_average());
            intent.putExtra("overview", res.getResults().get(position).getOverview());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return res.getResults().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView poster;
        private final TextView movieName;
        private final ConstraintLayout card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.poster);
            movieName = itemView.findViewById(R.id.movieName);
            card = itemView.findViewById(R.id.movieCard);
        }
    }
}
