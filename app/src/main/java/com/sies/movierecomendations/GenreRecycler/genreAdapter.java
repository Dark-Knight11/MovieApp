package com.sies.movierecomendations.GenreRecycler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sies.movierecomendations.GenreApi.GenreList;
import com.sies.movierecomendations.MoviesRecycler.Movies;
import com.sies.movierecomendations.R;

public class genreAdapter extends RecyclerView.Adapter<genreAdapter.ViewHolder> {

    GenreList res;
    private Context context;

    public genreAdapter(GenreList res) { this.res = res; }

    @NonNull
    @Override
    public genreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.activity_genre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.genre.setText(res.getGenres().get(position).getName());

        holder.genre.setOnClickListener(v -> {

            // Passing data to fragment
//            AppCompatActivity activity = (AppCompatActivity) v.getContext();
//            MoviesCardFragment moviesCardFragment = new MoviesCardFragment();
//
//            FragmentManager at = activity.getSupportFragmentManager();
//            Bundle result = new Bundle();
//            result.putInt("genreId", res.getGenres().get(position).getId());
//
//            at.setFragmentResult("requestKey", result);
//            at.beginTransaction().replace(R.id.genre, moviesCardFragment).addToBackStack(null).commit();


            Intent intent = new Intent(context, Movies.class);
            intent.putExtra("genreId", res.getGenres().get(position).getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return res.getGenres().size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView genre;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            genre = itemView.findViewById(R.id.genre);
        }
    }

}

