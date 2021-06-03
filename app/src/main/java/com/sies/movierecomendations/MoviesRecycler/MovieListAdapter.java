package com.sies.movierecomendations.MoviesRecycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sies.movierecomendations.MoviesApi.MoviesList;
import com.sies.movierecomendations.R;

import java.util.HashMap;
import java.util.Map;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabase = database.getReference();
    FirebaseUser person = FirebaseAuth.getInstance().getCurrentUser();

    MoviesList res;
    int count = 0, fav = 0;
    private Context context;
    public MovieListAdapter (MoviesList res) {
        this.res = res;
    }

    @NonNull
    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
        context = parent.getContext();
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
        holder.star.setOnClickListener(v -> {
            int pos;
            pos = position;
            Toast.makeText(context, Integer.toString(pos), Toast.LENGTH_SHORT).show();
            if(fav%2==0) {
                holder.star.setImageResource(R.drawable.ic_baseline_star_rate_24);
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("id", res.getResults().get(position).getTitle());
                mDatabase.child("Users").child(person.getUid()).child("favourites").updateChildren(childUpdates);
            }
            else holder.star.setImageResource(R.drawable.ic_baseline_star_outline_24);
            fav++;
        });
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("id", res.getResults().get(position).getId());
//        mDatabase.child("Users").child(person.getUid()).child("favourites").updateChildren(childUpdates);
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
        private ConstraintLayout frame;
        private ImageView star;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.releaseDate);
            frame = itemView.findViewById(R.id.frame);
            star = itemView.findViewById(R.id.star);
        }

    }
}
