package com.sies.movierecomendations.GenreRecycler;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sies.movierecomendations.Fragments.GenreFragment;
import com.sies.movierecomendations.Fragments.PopularMovies;
import com.sies.movierecomendations.Fragments.ProfileFragment;
import com.sies.movierecomendations.R;

public class MainActivity extends AppCompatActivity {

    // ARCHIVE
//    RecyclerView movies;
//    GenreList res;
//    String API_KEY = BuildConfig.API_KEY;
//
//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl("https://api.themoviedb.org/3/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
//    MovieDbAPI movieDbAPI = retrofit.create(MovieDbAPI.class);

    @SuppressLint({"ResourceAsColor", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        View pop = findViewById(R.id.popular);
//        View genre = findViewById(R.id.genre);
//        View profile = findViewById(R.id.profile);

        FragmentManager ft = getSupportFragmentManager();
//        ft.beginTransaction().add(R.id.fl_wrapper, new PopularMovies(), "Home").commit();

        final androidx.fragment.app.Fragment genre = new GenreFragment();
        final androidx.fragment.app.Fragment popularMovies = new PopularMovies();
        final androidx.fragment.app.Fragment profile = new ProfileFragment();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setItemIconTintList(null);
//        bottomNavigationView.setItemTextColor(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.genre:
                    fragment = genre;
                    break;
                case R.id.popular:
                    fragment = popularMovies;
                    break;
                case R.id.profile:
                    fragment = profile;
                    break;
                default:
                    fragment = popularMovies;
                    break;
            }
            ft.beginTransaction().replace(R.id.fl_wrapper, fragment).commit();
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.popular);



//        pop.setOnClickListener(v -> ft.beginTransaction().replace(R.id.fl_wrapper, new PopularMovies(), "Home").commit());
//        genre.setOnClickListener(v -> ft.beginTransaction().replace(R.id.fl_wrapper, new GenreFragment(), "Genre").commit());
//        profile.setOnClickListener(v -> ft.beginTransaction().replace(R.id.fl_wrapper, new ProfileFragment(), "Profile").commit());

    }
}


// ARCHIVE

/* if (!networkWhere()) {
        TextView _404 = findViewById(R.id.noInternet);
        _404.setText("404\nPlease Connect to Internet");
        _404.setVisibility(View.VISIBLE);

        }

        Button profile = findViewById(R.id.profile);
        Button pop = findViewById(R.id.pop);

        pop.setOnClickListener(v -> {
        startActivity(new Intent(MainActivity.this, PopularMovies.class));
        });


        profile.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Profile.class)));

        movies = findViewById(R.id.movies);
        movies.setLayoutManager(new LinearLayoutManager(MainActivity.this));


        movieDbAPI.getGenre(API_KEY).enqueue(new Callback<GenreList>() {
@Override
public void onResponse(Call<GenreList> call, Response<GenreList> response) {
        res = response.body();
        Log.i("onResponse: ", String.valueOf(response.code()));
        movies.setAdapter(new genreAdapter(res));
        }

@Override
public void onFailure(Call<GenreList> call, Throwable t) {
        Log.i("onFailure: ", t.getMessage());
        }
        });
        }

private boolean networkWhere() {

        boolean have_WIFI = false;
        boolean have_MobileData = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for(NetworkInfo info: networkInfos) {
        if (info.getTypeName().equalsIgnoreCase("WIFI"))
        if (info.isConnected())
        have_WIFI = true;
        if (info.getTypeName().equalsIgnoreCase("MOBILE"))
        if (info.isConnected())
        have_MobileData = true;

        }
        return have_MobileData || have_WIFI;
        }

*/