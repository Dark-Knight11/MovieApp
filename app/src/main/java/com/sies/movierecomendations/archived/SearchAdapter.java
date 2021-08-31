//package com.sies.movierecomendations.Search.ui;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.sies.movierecomendations.MovieDetails.ui.MovieDetails;
//import com.sies.movierecomendations.R;
//import com.sies.movierecomendations.network.MoviesList;
//
//public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
//
//    Context context;
//    MoviesList res;
//    int i;
//
//    public SearchAdapter(MoviesList res) {
//        this.res = res;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        context = parent.getContext();
//        View view = LayoutInflater.from(context).inflate(R.layout.search_view_item, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        if(res.getResults().get(position).getMedia_type().equals("tv")) {
//            i = 2;
//            holder.text.setText(res.getResults().get(position).getName());
//        }
//        else {
//            i = 1;
//            holder.text.setText(res.getResults().get(position).getTitle());
//        }
//
//        holder.card.setOnClickListener(v -> {
//            Intent intent = new Intent(context, MovieDetails.class);
//            if (res.getResults().get(position).getMedia_type().equals("movie")) {
//                intent.putExtra("title", res.getResults().get(position).getOriginal_title());
//                intent.putExtra("release-date", res.getResults().get(position).getRelease_date());
//            }
//            else {
//                intent.putExtra("title", res.getResults().get(position).getOriginal_name());
//                intent.putExtra("release-date", res.getResults().get(position).getFirst_air_date());
//            }
//            intent.putExtra("backdrop-path", res.getResults().get(position).getBackdrop_path());
//            intent.putExtra("rating", res.getResults().get(position).getVote_average());
//            intent.putExtra("id", res.getResults().get(position).getId());
//            intent.putExtra("overview", res.getResults().get(position).getOverview());
//            intent.putExtra("flag", i);
//            context.startActivity(intent);
//        });
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return res.getResults().size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView text;
//        RelativeLayout card;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            text = itemView.findViewById(R.id.text);
//            card = itemView.findViewById(R.id.card);
//        }
//    }
//
//}
