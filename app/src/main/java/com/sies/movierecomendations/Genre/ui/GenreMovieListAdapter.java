package com.sies.movierecomendations.Genre.ui;

import android.annotation.SuppressLint;
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
import com.sies.movierecomendations.network.MoviesList;
import com.sies.movierecomendations.MovieDetails.ui.MovieDetails;
import com.sies.movierecomendations.R;

public class GenreMovieListAdapter extends RecyclerView.Adapter<GenreMovieListAdapter.ViewHolder> {

//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    DatabaseReference mDatabase = database.getReference();
//    FirebaseUser person = FirebaseAuth.getInstance().getCurrentUser();

    MoviesList res;
//    int count = 0, fav = 0;
    private Context context;
    public GenreMovieListAdapter(MoviesList res) {
        this.res = res;
    }

    @NonNull
    @Override
    public GenreMovieListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_movies_card, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GenreMovieListAdapter.ViewHolder holder, int position) {
        Glide.with(context).load("https://image.tmdb.org/t/p/w500"+ res.getResults().get(position).getBackdrop_path()).into(holder.poster);
        holder.title.setText(res.getResults().get(position).getTitle());
        holder.frame.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetails.class);
            intent.putExtra("backdrop-path", res.getResults().get(position).getBackdrop_path());
            intent.putExtra("title", res.getResults().get(position).getTitle());
            intent.putExtra("release-date", res.getResults().get(position).getRelease_date());
            intent.putExtra("rating", res.getResults().get(position).getVote_average());
            intent.putExtra("overview", res.getResults().get(position).getOverview());
            intent.putExtra("id", res.getResults().get(position).getId());
            context.startActivity(intent);
        });
        holder.date.setText("Release Date: " + res.getResults().get(position).getRelease_date());
//        holder.star.setOnClickListener(v -> {
//            int pos;
//            pos = position;
//            Toast.makeText(context, Integer.toString(pos), Toast.LENGTH_SHORT).show();
//            if(fav%2==0) {
//                holder.star.setImageResource(R.drawable.ic_baseline_star_rate_24);
//                Map<String, Object> childUpdates = new HashMap<>();
//                childUpdates.put("id", res.getResults().get(position).getTitle());
//                mDatabase.child("Users")
//                        .child(person.getUid())
//                        .child("favourites")
//                        .updateChildren(childUpdates);
//            }
//            else
//                holder.star.setImageResource(R.drawable.ic_baseline_star_outline_24);
//            fav++;
//        });
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("id", res.getResults().get(position).getId());
//        mDatabase.child("Users").child(person.getUid()).child("favourites").updateChildren(childUpdates);
    }

    @Override
    public int getItemCount() {
        return res.getResults().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView poster;
        private final TextView title;
        private final TextView date;
        private final ConstraintLayout frame;
        /* private final ImageView star; */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.releaseDate);
            frame = itemView.findViewById(R.id.frame);
            /* star = itemView.findViewById(R.id.star); */
        }

    }
}
