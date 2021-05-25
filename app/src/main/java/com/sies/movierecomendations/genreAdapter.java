package com.sies.movierecomendations;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class genreAdapter extends RecyclerView.Adapter<genreAdapter.ViewHolder> {

    GenreList res;
    private Context context;

    public genreAdapter(GenreList res) { this.res = res; }

    @NonNull
    @Override
    public genreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_genre, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.genre.setText(res.getGenres().get(position).getName());

        holder.genre.setOnClickListener(v -> {
            Intent intent = new Intent(context, Movies.class);
            intent.putExtra("genreId", res.getGenres().get(position).getId());
            Toast.makeText(context, "sent", Toast.LENGTH_SHORT).show();
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return res.getGenres().size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView genre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            genre = itemView.findViewById(R.id.genre);
        }
    }

}

